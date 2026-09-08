package Veterinaria.controladores;

import Veterinaria.Cirujia;
import Veterinaria.Estado;
import Veterinaria.Mascota;
import Veterinaria.NivelComplejidad;
import Veterinaria.Tratamiento;
import Veterinaria.TratamientoCurativo;
import Veterinaria.TratamientoPreventivo;
import Veterinaria.servicios.ClinicaVeterinariaService;

import java.util.List;

/**
 * Controlador GRASP especializado en solicitudes y ciclo de vida de Tratamientos.
 */
public class TratamientoController {
    private final ClinicaVeterinariaService service;

    public TratamientoController(ClinicaVeterinariaService service) {
        if (service == null) {
            throw new IllegalArgumentException("El servicio de clinica no puede ser nulo.");
        }
        this.service = service;
    }

    public Tratamiento registrarPreventivo(String mascotaId, String nombre, String fechaInicio, Estado estado, int frecuencia) {
        Tratamiento tratamiento = new TratamientoPreventivo(nombre, fechaInicio, estado, frecuencia);
        agregarTratamientoAMascota(mascotaId, tratamiento);
        return tratamiento;
    }

    public Tratamiento registrarCurativo(String mascotaId, String nombre, String fechaInicio, Estado estado, int duracion, String diagnostico) {
        Tratamiento tratamiento = new TratamientoCurativo(nombre, fechaInicio, estado, duracion, diagnostico);
        agregarTratamientoAMascota(mascotaId, tratamiento);
        return tratamiento;
    }

    public Tratamiento registrarCirujia(String mascotaId, String nombre, String fechaInicio, Estado estado, String tipoCirujia, NivelComplejidad nivelComplejidad) {
        Tratamiento tratamiento = new Cirujia(nombre, fechaInicio, estado, tipoCirujia, nivelComplejidad);
        agregarTratamientoAMascota(mascotaId, tratamiento);
        return tratamiento;
    }

    public List<Tratamiento> getTratamientos() {
        return service.getTratamientos();
    }

    private void agregarTratamientoAMascota(String mascotaId, Tratamiento tratamiento) {
        Mascota mascota = service.buscarMascotaPorId(mascotaId);
        mascota.agregarTratamiento(tratamiento);
        service.guardarDatos();
    }
}