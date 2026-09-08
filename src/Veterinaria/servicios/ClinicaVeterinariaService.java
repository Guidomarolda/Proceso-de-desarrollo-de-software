package Veterinaria.servicios;

import Veterinaria.Dueño;
import Veterinaria.Mascota;
import Veterinaria.NivelCuidado;
import Veterinaria.NivelComplejidad;
import Veterinaria.Tipo;
import Veterinaria.Tratamiento;
import Veterinaria.Estado;
import Veterinaria.controladores.DuenoController;
import Veterinaria.controladores.MascotaController;
import Veterinaria.controladores.TratamientoController;
import Veterinaria.persistencia.ArchivoClinicaRepository;
import Veterinaria.persistencia.DatosClinica;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Servicio central y fachada de la clinica.
 * Coordina los controladores especializados GRASP (DuenoController, MascotaController, TratamientoController)
 * y el servicio de consultas (ConsultaVeterinariaService), evitando ser una God Class.
 */
public class ClinicaVeterinariaService {
    private final ArchivoClinicaRepository repository;
    private final List<Dueño> duenos;
    private final ConsultaVeterinariaService consultaService;
    private final DuenoController duenoController;
    private final MascotaController mascotaController;
    private final TratamientoController tratamientoController;

    public ClinicaVeterinariaService(ArchivoClinicaRepository repository) {
        this.repository = repository;
        this.duenos = new ArrayList<>();
        this.consultaService = new ConsultaVeterinariaService(this);
        this.duenoController = new DuenoController(this);
        this.mascotaController = new MascotaController(this);
        this.tratamientoController = new TratamientoController(this);
    }

    public void cargarDatos() {
        duenos.clear();
        duenos.addAll(repository.cargar().getDuenos());
    }

    public void guardarDatos() {
        repository.guardar(new DatosClinica(duenos));
    }

    public DuenoController getDuenoController() {
        return duenoController;
    }

    public MascotaController getMascotaController() {
        return mascotaController;
    }

    public TratamientoController getTratamientoController() {
        return tratamientoController;
    }

    public ConsultaVeterinariaService getConsultaService() {
        return consultaService;
    }

    public List<Dueño> getDuenosListModificable() {
        return duenos;
    }

    // Delegacion a controladores para compatibilidad
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

    public List<Dueño> getDuenos() {
        return Collections.unmodifiableList(duenos);
    }

    public List<Mascota> getMascotas() {
        List<Mascota> mascotas = new ArrayList<>();
        for (Dueño dueno : duenos) {
            mascotas.addAll(dueno.getMascotas());
        }
        return Collections.unmodifiableList(mascotas);
    }

    public List<Tratamiento> getTratamientos() {
        List<Tratamiento> tratamientos = new ArrayList<>();
        for (Mascota mascota : getMascotas()) {
            tratamientos.addAll(mascota.getTratamientos());
        }
        return Collections.unmodifiableList(tratamientos);
    }

    public Double calcularCostoTotal(Mascota mascota, Tratamiento tratamiento) {
        return consultaService.calcularCostoTotal(mascota, tratamiento);
    }

    public int compararMascotas(Mascota primera, Mascota segunda) {
        return consultaService.compararMascotas(primera, segunda);
    }

    public List<Tratamiento> tratamientosQueRequierenSeguimiento() {
        return consultaService.tratamientosQueRequierenSeguimiento();
    }

    public Map<Mascota, Integer> resumenTratamientosPorMascota(Dueño dueno) {
        return consultaService.resumenTratamientosPorMascota(dueno);
    }

    public Dueño buscarDuenoPorId(String id) {
        for (Dueño dueno : duenos) {
            if (dueno.getId().equals(id)) {
                return dueno;
            }
        }
        throw new IllegalArgumentException("No existe un dueño con el id indicado.");
    }

    public Mascota buscarMascotaPorId(String id) {
        for (Mascota mascota : getMascotas()) {
            if (mascota.getId().equals(id)) {
                return mascota;
            }
        }
        throw new IllegalArgumentException("No existe una mascota con el id indicado.");
    }
}