package Veterinaria.persistencia;

import Veterinaria.modelo.Dueño;
import Veterinaria.modelo.Mascota;
import Veterinaria.modelo.Tratamiento;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Coordina los archivos y la reconstruccion de relaciones; delega la conversion a mappers. */
public class ArchivoClinicaRepository implements ClinicaRepository {
    private final DuenoCsvMapper duenoMapper = new DuenoCsvMapper();
    private final MascotaCsvMapper mascotaMapper = new MascotaCsvMapper();
    private final TratamientoCsvMapper tratamientoMapper = new TratamientoCsvMapper();
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

    @Override
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

    @Override
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
                Dueño dueno = duenoMapper.desdeCampos(campos);
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
                String duenoId = campos.get(1);
                Mascota mascota = mascotaMapper.desdeCampos(campos);
                Dueño dueno = duenosPorId.get(duenoId);
                if (dueno == null) {
                    throw new IllegalStateException("Mascota sin dueno valido: " + mascota.getNombre());
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
                Tratamiento tratamiento = tratamientoMapper.desdeCampos(campos);
                Mascota mascota = mascotasPorId.get(mascotaId);
                if (mascota == null) {
                    throw new IllegalStateException("Tratamiento sin mascota valida: " + tratamiento.getNombreTratamiento());
                }
                mascota.agregarTratamiento(tratamiento);
            }
        }
    }

    private void guardarDuenos(DatosClinica datos) throws IOException {
        List<String> lineas = new ArrayList<>();
        lineas.add("id;nombre;apellido;telefono;correo");
        for (Dueño dueno : datos.getDuenos()) {
            lineas.add(duenoMapper.aLinea(dueno));
        }
        Files.write(duenosArchivo, lineas, StandardCharsets.UTF_8);
    }

    private void guardarMascotas(DatosClinica datos) throws IOException {
        List<String> lineas = new ArrayList<>();
        lineas.add("id;duenoId;tipo;nombre;edad;peso;extra");
        for (Dueño dueno : datos.getDuenos()) {
            for (Mascota mascota : dueno.getMascotas()) {
                lineas.add(mascotaMapper.aLinea(dueno.getId(), mascota));
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
                    lineas.add(tratamientoMapper.aLinea(mascota.getId(), tratamiento));
                }
            }
        }
        Files.write(tratamientosArchivo, lineas, StandardCharsets.UTF_8);
    }
}
