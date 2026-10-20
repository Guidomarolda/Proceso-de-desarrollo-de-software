package Veterinaria.facade;

import Veterinaria.controladores.ConsultaController;
import Veterinaria.controladores.DuenoController;
import Veterinaria.controladores.MascotaController;
import Veterinaria.controladores.TratamientoController;
import Veterinaria.modelo.*;
import Veterinaria.persistencia.ArchivoClinicaRepository;
import Veterinaria.persistencia.ClinicaRepository;
import Veterinaria.servicios.ClinicaVeterinariaService;
import Veterinaria.servicios.ConsultaVeterinariaService;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Patron FACADE (Fachada):
 * Proporciona una interfaz unificada y simplificada a todo el subsistema de la clinica veterinaria
 * (Modelo, Persistencia, Servicios y Controladores), ocultando la complejidad de su inicializacion
 * y coordinacion interna.
 */
public class ClinicaVeterinariaFacade {
    private final ClinicaVeterinaria clinica;
    private final ClinicaVeterinariaService clinicaService;
    private final ConsultaVeterinariaService consultaService;
    private final DuenoController duenoController;
    private final MascotaController mascotaController;
    private final TratamientoController tratamientoController;
    private final ConsultaController consultaController;

    public ClinicaVeterinariaFacade() {
        this(new ArchivoClinicaRepository());
    }

    public ClinicaVeterinariaFacade(ClinicaRepository repository) {
        Objects.requireNonNull(repository, "El repositorio es obligatorio.");
        this.clinica = new ClinicaVeterinaria();
        this.clinicaService = new ClinicaVeterinariaService(repository, clinica);
        this.consultaService = new ConsultaVeterinariaService(clinica);
        this.duenoController = new DuenoController(clinicaService);
        this.mascotaController = new MascotaController(clinicaService);
        this.tratamientoController = new TratamientoController(clinicaService);
        this.consultaController = new ConsultaController(consultaService);
    }

    public void inicializarSistema() {
        clinicaService.cargarDatos();
    }

    public void guardarCambios() {
        clinicaService.guardarDatos();
    }

    // --- Operaciones simplificadas del Subsistema ---

    public Dueño registrarDueno(String nombre, String apellido, String telefono, String correo) {
        return duenoController.registrarDueno(nombre, apellido, telefono, correo);
    }

    public Mascota registrarPerro(String duenoId, String nombre, int edad, Double peso, String raza) {
        return mascotaController.registrarPerro(duenoId, nombre, edad, peso, raza);
    }

    public Mascota registrarGato(String duenoId, String nombre, int edad, Double peso, Boolean vacunado) {
        return mascotaController.registrarGato(duenoId, nombre, edad, peso, vacunado);
    }

    public Mascota registrarAve(String duenoId, String nombre, int edad, Double peso, Tipo tipo) {
        return mascotaController.registrarAve(duenoId, nombre, edad, peso, tipo);
    }

    public Mascota registrarExotico(String duenoId, String nombre, int edad, Double peso, NivelCuidado nivelCuidado) {
        return mascotaController.registrarExotico(duenoId, nombre, edad, peso, nivelCuidado);
    }

    public Tratamiento registrarPreventivo(String mascotaId, String nombre, String fechaInicio, Estado estado, int frecuencia) {
        return tratamientoController.registrarPreventivo(mascotaId, nombre, fechaInicio, estado, frecuencia);
    }

    public Tratamiento registrarCurativo(String mascotaId, String nombre, String fechaInicio, Estado estado, int duracion, String diagnostico) {
        return tratamientoController.registrarCurativo(mascotaId, nombre, fechaInicio, estado, duracion, diagnostico);
    }

    public Tratamiento registrarCirujia(String mascotaId, String nombre, String fechaInicio, Estado estado, String tipoCirujia, NivelComplejidad nivelComplejidad) {
        return tratamientoController.registrarCirujia(mascotaId, nombre, fechaInicio, estado, tipoCirujia, nivelComplejidad);
    }

    public Tratamiento registrarPlanCompuesto(String mascotaId, String nombre, String fechaInicio, Estado estado, List<Tratamiento> subTratamientos) {
        return tratamientoController.registrarPlanCompuesto(mascotaId, nombre, fechaInicio, estado, subTratamientos);
    }

    public Tratamiento registrarTratamientoDecorado(String mascotaId, Tratamiento base,
                                                    boolean urgenciaGuardia, boolean medicacionEspecial, int diasInternacion) {
        return tratamientoController.registrarTratamientoDecorado(mascotaId, base, urgenciaGuardia, medicacionEspecial, diasInternacion);
    }

    public Double calcularCostoTotal(Mascota mascota, Tratamiento tratamiento) {
        return consultaController.calcularCostoTotal(mascota, tratamiento);
    }

    public int compararMascotas(Mascota primera, Mascota segunda) {
        return consultaController.compararMascotas(primera, segunda);
    }

    public List<Tratamiento> tratamientosQueRequierenSeguimiento() {
        return consultaController.tratamientosQueRequierenSeguimiento();
    }

    public Map<Mascota, Integer> resumenTratamientosPorMascota(Dueño dueno) {
        return consultaController.resumenTratamientosPorMascota(dueno);
    }

    public List<Dueño> getDuenos() { return duenoController.getDuenos(); }
    public List<Mascota> getMascotas() { return mascotaController.getMascotas(); }
    public List<Tratamiento> getTratamientos() { return tratamientoController.getTratamientos(); }

    // --- Acceso a Controladores MVC ---

    public DuenoController getDuenoController() { return duenoController; }
    public MascotaController getMascotaController() { return mascotaController; }
    public TratamientoController getTratamientoController() { return tratamientoController; }
    public ConsultaController getConsultaController() { return consultaController; }
}
