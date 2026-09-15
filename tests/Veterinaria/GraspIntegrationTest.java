package Veterinaria;

import Veterinaria.controladores.*;
import Veterinaria.gui.*;
import Veterinaria.persistencia.*;
import Veterinaria.servicios.*;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.swing.SwingUtilities;

/** Pruebas de integracion sin dependencias externas. Usa archivos temporales. */
public class GraspIntegrationTest {
    public static void main(String[] args) throws Exception {
        Path directorio = Files.createTempDirectory("veterinaria-grasp-");
        try {
            ClinicaVeterinaria clinica = new ClinicaVeterinaria();
            ClinicaRepository repository = new ArchivoClinicaRepository(directorio);
            ClinicaVeterinariaService service = new ClinicaVeterinariaService(repository, clinica);
            DuenoController duenos = new DuenoController(service);
            MascotaController mascotas = new MascotaController(service);
            TratamientoController tratamientos = new TratamientoController(service);
            ConsultaController consultas = new ConsultaController(new ConsultaVeterinariaService(clinica));
            service.cargarDatos();
            comprobar(duenos.getDuenos().isEmpty(), "Inicio sin archivos");
            Dueño dueno = duenos.registrarDueno("Ana; Maria", "Perez", "123", "ana@example.com");
            Mascota perro = mascotas.registrarPerro(dueno.getId(), "Fido", 2, 12.0, "Mestizo");
            Mascota gato = mascotas.registrarGato(dueno.getId(), "Michi", 3, 4.0, false);
            mascotas.registrarAve(dueno.getId(), "Pio", 1, 0.5, Tipo.values()[0]);
            mascotas.registrarExotico(dueno.getId(), "Rex", 1, 2.0, NivelCuidado.values()[0]);
            Tratamiento preventivo = tratamientos.registrarPreventivo(perro.getId(), "Vacuna", "2026-09-15", Estado.encurso, 2);
            tratamientos.registrarCurativo(gato.getId(), "Terapia", "2026-09-15", Estado.encurso, 40, "Diagnostico; con separador");
            tratamientos.registrarCirujia(perro.getId(), "Operacion", "2026-09-15", Estado.completado, "General", NivelComplejidad.media);
            comprobar(consultas.calcularCostoTotal(perro, preventivo) == 1200.0, "Costo total");
            comprobar(consultas.compararMascotas(perro, gato) < 0, "Comparacion polimorfica");
            comprobar(consultas.resumenTratamientosPorMascota(dueno).get(perro) == 2, "Resumen experto");
            comprobar(consultas.tratamientosQueRequierenSeguimiento().size() == 2, "Seguimiento");
            esperarError(UnsupportedOperationException.class, () -> duenos.getDuenos().clear());
            esperarError(UnsupportedOperationException.class, () -> perro.getTratamientos().clear());
            esperarError(UnsupportedOperationException.class, () -> dueno.obtenerResumenTratamientos().clear());
            esperarError(IllegalArgumentException.class, () -> mascotas.registrarPerro("inexistente", "Otro", 1, 2.0, "Raza"));
            esperarError(RuntimeException.class, () -> tratamientos.registrarCirujia(perro.getId(), "Invalida", "2026-09-15", Estado.encurso, "General", NivelComplejidad.baja));
            comprobar(tratamientos.getTratamientos().size() == 3, "Registro invalido no modifica el modelo");
            String idPerro = perro.getId();
            String idTratamiento = preventivo.getId();
            service.cargarDatos();
            comprobar(consultas.getMascotas().size() == 4, "Consultas ven el modelo recargado");
            comprobar(duenos.getDuenos().get(0).getNombre().equals("Ana; Maria"), "Escape CSV");
            comprobar(mascotas.buscarMascotaPorId(idPerro) instanceof Perro, "Tipo e ID de mascota");
            comprobar(mascotas.buscarMascotaPorId(idPerro).cantidadTratamientos() == 2, "Relaciones recargadas");
            comprobar(tratamientos.getTratamientos().stream().anyMatch(t -> t.getId().equals(idTratamiento)), "ID tratamiento");
            comprobar(mascotas.getMascotas().stream().anyMatch(m -> m instanceof Gato && !((Gato) m).getEstaVacunado()), "Datos de gato");
            comprobar(mascotas.getMascotas().stream().anyMatch(m -> m instanceof Ave), "Tipo ave");
            comprobar(mascotas.getMascotas().stream().anyMatch(m -> m instanceof Exoticos), "Tipo exotico");
            comprobar(tratamientos.getTratamientos().stream().anyMatch(t -> t instanceof TratamientoCurativo && ((TratamientoCurativo) t).getDiagnostico().equals("Diagnostico; con separador")), "Datos curativos");
            comprobar(tratamientos.getTratamientos().stream().anyMatch(t -> t instanceof Cirujia && t.costoFijo() == 4500.0), "Datos cirugia");
            SwingUtilities.invokeAndWait(() -> {
                new DuenosPanel(duenos, () -> {}).refreshData();
                new MascotasPanel(mascotas, consultas, () -> {}).refreshData();
                new TratamientosPanel(tratamientos, () -> {}).refreshData();
                new ConsultasPanel(consultas).refreshData();
            });
            ClinicaVeterinaria conservada = new ClinicaVeterinaria();
            conservada.registrarDueno("Existente", "Perez", "123", "correo");
            ClinicaRepository fallido = new ClinicaRepository() {
                public DatosClinica cargar() { throw new IllegalStateException("Fallo de lectura"); }
                public void guardar(DatosClinica datos) { }
            };
            esperarError(IllegalStateException.class, () -> new ClinicaVeterinariaService(fallido, conservada).cargarDatos());
            comprobar(conservada.getDuenos().size() == 1, "Carga fallida conserva el estado");
            System.out.println("OK: registros, expertos, validaciones, CSV, recarga y paneles Swing.");
        } finally {
            Files.deleteIfExists(directorio.resolve("duenos.csv"));
            Files.deleteIfExists(directorio.resolve("mascotas.csv"));
            Files.deleteIfExists(directorio.resolve("tratamientos.csv"));
            Files.deleteIfExists(directorio);
        }
    }

    private static void comprobar(boolean condicion, String mensaje) {
        if (!condicion) throw new AssertionError(mensaje);
    }

    private static void esperarError(Class<? extends RuntimeException> tipo, Runnable accion) {
        try {
            accion.run();
        } catch (RuntimeException e) {
            if (tipo.isInstance(e)) return;
            throw e;
        }
        throw new AssertionError("Se esperaba " + tipo.getSimpleName());
    }
}
