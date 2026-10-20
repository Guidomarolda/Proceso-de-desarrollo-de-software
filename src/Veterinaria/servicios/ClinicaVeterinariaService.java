package Veterinaria.servicios;

import Veterinaria.modelo.*;
import Veterinaria.persistencia.ClinicaRepository;
import Veterinaria.persistencia.DatosClinica;
import java.util.List;
import java.util.Objects;

/** Coordina los registros y su persistencia; delega las reglas a los expertos del dominio. */
public class ClinicaVeterinariaService implements DuenoService, MascotaService, TratamientoService {
    private final ClinicaRepository repository;
    private final ClinicaVeterinaria clinica;

    public ClinicaVeterinariaService(ClinicaRepository repository, ClinicaVeterinaria clinica) {
        this.repository = Objects.requireNonNull(repository, "El repositorio es obligatorio.");
        this.clinica = Objects.requireNonNull(clinica, "La clinica es obligatoria.");
    }

    public void cargarDatos() {
        clinica.reemplazarDuenos(repository.cargar().getDuenos());
    }

    public void guardarDatos() {
        repository.guardar(new DatosClinica(clinica.getDuenos()));
    }

    @Override
    public Dueño registrarDueno(String nombre, String apellido, String telefono, String correo) {
        Dueño dueno = clinica.registrarDueno(nombre, apellido, telefono, correo);
        guardarDatos();
        return dueno;
    }

    @Override
    public Mascota registrarPerro(String duenoId, String nombre, int edad, Double peso, String raza) {
        Mascota mascota = MascotaFactory.crearPerro(nombre, edad, peso, null, raza);
        agregarMascotaADueno(duenoId, mascota);
        return mascota;
    }

    @Override
    public Mascota registrarGato(String duenoId, String nombre, int edad, Double peso, Boolean vacunado) {
        Mascota mascota = MascotaFactory.crearGato(nombre, edad, peso, null, vacunado);
        agregarMascotaADueno(duenoId, mascota);
        return mascota;
    }

    @Override
    public Mascota registrarAve(String duenoId, String nombre, int edad, Double peso, Tipo tipo) {
        Mascota mascota = MascotaFactory.crearAve(nombre, edad, peso, null, tipo);
        agregarMascotaADueno(duenoId, mascota);
        return mascota;
    }

    @Override
    public Mascota registrarExotico(String duenoId, String nombre, int edad, Double peso, NivelCuidado nivelCuidado) {
        Mascota mascota = MascotaFactory.crearExotico(nombre, edad, peso, null, nivelCuidado);
        agregarMascotaADueno(duenoId, mascota);
        return mascota;
    }

    @Override
    public Tratamiento registrarPreventivo(String mascotaId, String nombre, String fechaInicio, Estado estado, int frecuencia) {
        Tratamiento tratamiento = TratamientoFactory.crearPreventivo(nombre, fechaInicio, estado, frecuencia);
        agregarTratamientoAMascota(mascotaId, tratamiento);
        return tratamiento;
    }

    @Override
    public Tratamiento registrarCurativo(String mascotaId, String nombre, String fechaInicio, Estado estado, int duracion, String diagnostico) {
        Tratamiento tratamiento = TratamientoFactory.crearCurativo(nombre, fechaInicio, estado, duracion, diagnostico);
        agregarTratamientoAMascota(mascotaId, tratamiento);
        return tratamiento;
    }

    @Override
    public Tratamiento registrarCirujia(String mascotaId, String nombre, String fechaInicio, Estado estado, String tipoCirujia, NivelComplejidad nivelComplejidad) {
        Tratamiento tratamiento = TratamientoFactory.crearCirujia(nombre, fechaInicio, estado, tipoCirujia, nivelComplejidad);
        agregarTratamientoAMascota(mascotaId, tratamiento);
        return tratamiento;
    }

    @Override
    public Tratamiento registrarPlanCompuesto(String mascotaId, String nombre, String fechaInicio, Estado estado, List<Tratamiento> subTratamientos) {
        Tratamiento plan = TratamientoFactory.crearPlanCompuesto(nombre, fechaInicio, estado, subTratamientos);
        agregarTratamientoAMascota(mascotaId, plan);
        return plan;
    }

    @Override
    public Tratamiento registrarTratamientoPersonalizado(String mascotaId, Tratamiento tratamiento) {
        agregarTratamientoAMascota(mascotaId, tratamiento);
        return tratamiento;
    }

    private void agregarMascotaADueno(String duenoId, Mascota mascota) {
        clinica.buscarDuenoPorId(duenoId).agregarMascota(mascota);
        guardarDatos();
    }

    private void agregarTratamientoAMascota(String mascotaId, Tratamiento tratamiento) {
        clinica.buscarMascotaPorId(mascotaId).agregarTratamiento(tratamiento);
        guardarDatos();
    }

    @Override
    public List<Dueño> getDuenos() { return clinica.getDuenos(); }
    @Override
    public List<Mascota> getMascotas() { return clinica.getMascotas(); }
    @Override
    public List<Tratamiento> getTratamientos() { return clinica.getTratamientos(); }
    @Override
    public Dueño buscarDuenoPorId(String id) { return clinica.buscarDuenoPorId(id); }
    @Override
    public Mascota buscarMascotaPorId(String id) { return clinica.buscarMascotaPorId(id); }
}
