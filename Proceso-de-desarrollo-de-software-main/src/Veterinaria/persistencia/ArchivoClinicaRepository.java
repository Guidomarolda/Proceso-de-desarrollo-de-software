package Veterinaria.persistencia;
//god class
import Veterinaria.Ave;
import Veterinaria.Cirujia;
import Veterinaria.Dueño;
import Veterinaria.Estado;
import Veterinaria.Exoticos;
import Veterinaria.Gato;
import Veterinaria.Mascota;
import Veterinaria.NivelComplejidad;
import Veterinaria.NivelCuidado;
import Veterinaria.Perro;
import Veterinaria.Tipo;
import Veterinaria.Tratamiento;
import Veterinaria.TratamientoCurativo;
import Veterinaria.TratamientoPreventivo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ArchivoClinicaRepository {
    private final Path directorio;
    private final Path duenosArchivo;
    private final Path mascotasArchivo;
    private final Path tratamientosArchivo;

    public ArchivoClinicaRepository() {
        this(Paths.get("data"));
    }

    public ArchivoClinicaRepository(Path directorio) {
        this.directorio = directorio;
        this.duenosArchivo = directorio.resolve("duenos.csv");
        this.mascotasArchivo = directorio.resolve("mascotas.csv");
        this.tratamientosArchivo = directorio.resolve("tratamientos.csv");
    }

    public DatosClinica cargar() {
        try {
            Map<String, Dueño> duenosPorId = cargarDuenos();
            Map<String, Mascota> mascotasPorId = cargarMascotas(duenosPorId);
            cargarTratamientos(mascotasPorId);
            return new DatosClinica(new ArrayList<>(duenosPorId.values()));
        } catch (IOException | RuntimeException e) {
            throw new PersistenciaException("No se pudieron cargar los datos de la clinica.", e);
        }
    }

    public void guardar(DatosClinica datos) {
        try {
            Files.createDirectories(directorio);
            guardarDuenos(datos);
            guardarMascotas(datos);
            guardarTratamientos(datos);
        } catch (IOException e) {
            throw new PersistenciaException("No se pudieron guardar los datos de la clinica.", e);
        }
    }

    private Map<String, Dueño> cargarDuenos() throws IOException {
        Map<String, Dueño> duenos = new LinkedHashMap<>();
        if (!Files.exists(duenosArchivo)) {
            return duenos;
        }
        List<String> lineas = Files.readAllLines(duenosArchivo, StandardCharsets.UTF_8);
        for (int i = 1; i < lineas.size(); i++) {
            List<String> campos = CsvUtils.parsear(lineas.get(i));
            if (campos.size() >= 5) {
                Dueño dueno = new Dueño(campos.get(0), campos.get(1), campos.get(2), campos.get(3), campos.get(4));
                duenos.put(dueno.getId(), dueno);
            }
        }
        return duenos;
    }

    private Map<String, Mascota> cargarMascotas(Map<String, Dueño> duenosPorId) throws IOException {
        Map<String, Mascota> mascotas = new LinkedHashMap<>();
        if (!Files.exists(mascotasArchivo)) {
            return mascotas;
        }
        List<String> lineas = Files.readAllLines(mascotasArchivo, StandardCharsets.UTF_8);
        for (int i = 1; i < lineas.size(); i++) {
            List<String> campos = CsvUtils.parsear(lineas.get(i));
            if (campos.size() >= 7) {
                String id = campos.get(0);
                String duenoId = campos.get(1);
                String tipo = campos.get(2);
                String nombre = campos.get(3);
                int edad = Integer.parseInt(campos.get(4));
                Double peso = Double.parseDouble(campos.get(5));
                String extra = campos.get(6);
                Mascota mascota = crearMascota(id, tipo, nombre, edad, peso, extra);
                Dueño dueno = duenosPorId.get(duenoId);
                if (dueno == null) {
                    throw new IllegalStateException("Mascota sin dueño valido: " + nombre);
                }
                dueno.agregarMascota(mascota);
                mascotas.put(mascota.getId(), mascota);
            }
        }
        return mascotas;
    }

    private void cargarTratamientos(Map<String, Mascota> mascotasPorId) throws IOException {
        if (!Files.exists(tratamientosArchivo)) {
            return;
        }
        List<String> lineas = Files.readAllLines(tratamientosArchivo, StandardCharsets.UTF_8);
        for (int i = 1; i < lineas.size(); i++) {
            List<String> campos = CsvUtils.parsear(lineas.get(i));
            if (campos.size() >= 8) {
                String mascotaId = campos.get(1);
                Tratamiento tratamiento = crearTratamiento(campos);
                Mascota mascota = mascotasPorId.get(mascotaId);
                if (mascota == null) {
                    throw new IllegalStateException("Tratamiento sin mascota valida: " + tratamiento.getNombreTratamiento());
                }
                mascota.agregarTratamiento(tratamiento);
            }
        }
    }

    private Mascota crearMascota(String id, String tipo, String nombre, int edad, Double peso, String extra) {
        if ("PERRO".equals(tipo)) {
            return new Perro(id, peso, edad, nombre, extra);
        }
        if ("GATO".equals(tipo)) {
            return new Gato(id, peso, edad, nombre, Boolean.parseBoolean(extra));
        }
        if ("AVE".equals(tipo)) {
            return new Ave(id, peso, edad, nombre, Tipo.valueOf(extra));
        }
        if ("EXOTICO".equals(tipo)) {
            return new Exoticos(id, peso, edad, nombre, NivelCuidado.valueOf(extra));
        }
        throw new IllegalArgumentException("Tipo de mascota desconocido: " + tipo);
    }

    private Tratamiento crearTratamiento(List<String> campos) {
        String id = campos.get(0);
        String tipo = campos.get(2);
        String nombre = campos.get(3);
        String fechaInicio = campos.get(4);
        Estado estado = Estado.valueOf(campos.get(5));
        String extra1 = campos.get(6);
        String extra2 = campos.get(7);
        if ("PREVENTIVO".equals(tipo)) {
            return new TratamientoPreventivo(id, nombre, fechaInicio, estado, Integer.parseInt(extra1));
        }
        if ("CURATIVO".equals(tipo)) {
            return new TratamientoCurativo(id, nombre, fechaInicio, estado, Integer.parseInt(extra1), extra2);
        }
        if ("CIRUJIA".equals(tipo)) {
            return new Cirujia(id, nombre, fechaInicio, estado, extra1, NivelComplejidad.valueOf(extra2));
        }
        throw new IllegalArgumentException("Tipo de tratamiento desconocido: " + tipo);
    }

    private void guardarDuenos(DatosClinica datos) throws IOException {
        List<String> lineas = new ArrayList<>();
        lineas.add("id;nombre;apellido;telefono;correo");
        for (Dueño dueno : datos.getDuenos()) {
            lineas.add(CsvUtils.linea(dueno.getId(), dueno.getNombre(), dueno.getApellido(), dueno.getTelefono(), dueno.getCorreo()));
        }
        Files.write(duenosArchivo, lineas, StandardCharsets.UTF_8);
    }

    private void guardarMascotas(DatosClinica datos) throws IOException {
        List<String> lineas = new ArrayList<>();
        lineas.add("id;duenoId;tipo;nombre;edad;peso;extra");
        for (Dueño dueno : datos.getDuenos()) {
            for (Mascota mascota : dueno.getMascotas()) {
                lineas.add(CsvUtils.linea(mascota.getId(), dueno.getId(), tipoMascota(mascota), mascota.getNombre(),
                        String.valueOf(mascota.getEdad()), String.valueOf(mascota.getPeso()), extraMascota(mascota)));
            }
        }
        Files.write(mascotasArchivo, lineas, StandardCharsets.UTF_8);
    }

    private void guardarTratamientos(DatosClinica datos) throws IOException {
        List<String> lineas = new ArrayList<>();
        lineas.add("id;mascotaId;tipo;nombre;fechaInicio;estado;extra1;extra2");
        for (Dueño dueno : datos.getDuenos()) {
            for (Mascota mascota : dueno.getMascotas()) {
                for (Tratamiento tratamiento : mascota.getTratamientos()) {
                    lineas.add(CsvUtils.linea(tratamiento.getId(), mascota.getId(), tipoTratamiento(tratamiento),
                            tratamiento.getNombreTratamiento(), tratamiento.getFechaInicio(),
                            tratamiento.getEstado().name(), extraTratamiento1(tratamiento), extraTratamiento2(tratamiento)));
                }
            }
        }
        Files.write(tratamientosArchivo, lineas, StandardCharsets.UTF_8);
    }

    private String tipoMascota(Mascota mascota) {
        if (mascota instanceof Perro) return "PERRO";
        if (mascota instanceof Gato) return "GATO";
        if (mascota instanceof Ave) return "AVE";
        if (mascota instanceof Exoticos) return "EXOTICO";
        throw new IllegalArgumentException("Tipo de mascota no soportado.");
    }

    private String extraMascota(Mascota mascota) {
        if (mascota instanceof Perro) return ((Perro) mascota).getRaza();
        if (mascota instanceof Gato) return String.valueOf(((Gato) mascota).getEstaVacunado());
        if (mascota instanceof Ave) return ((Ave) mascota).getTipo().name();
        if (mascota instanceof Exoticos) return ((Exoticos) mascota).getNivelCuidado().name();
        return "";
    }

    private String tipoTratamiento(Tratamiento tratamiento) {
        if (tratamiento instanceof TratamientoPreventivo) return "PREVENTIVO";
        if (tratamiento instanceof TratamientoCurativo) return "CURATIVO";
        if (tratamiento instanceof Cirujia) return "CIRUJIA";
        throw new IllegalArgumentException("Tipo de tratamiento no soportado.");
    }

    private String extraTratamiento1(Tratamiento tratamiento) {
        if (tratamiento instanceof TratamientoPreventivo) return String.valueOf(((TratamientoPreventivo) tratamiento).getFrecuenciaRecomendada());
        if (tratamiento instanceof TratamientoCurativo) return String.valueOf(((TratamientoCurativo) tratamiento).getDuracion());
        if (tratamiento instanceof Cirujia) return ((Cirujia) tratamiento).getTipoCirujia();
        return "";
    }

    private String extraTratamiento2(Tratamiento tratamiento) {
        if (tratamiento instanceof TratamientoCurativo) return ((TratamientoCurativo) tratamiento).getDiagnostico();
        if (tratamiento instanceof Cirujia) return ((Cirujia) tratamiento).getNivelComplejidad().name();
        return "";
    }
}
