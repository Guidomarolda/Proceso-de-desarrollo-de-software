package Veterinaria.controladores;

import Veterinaria.Estado;
import Veterinaria.Mascota;
import Veterinaria.NivelComplejidad;
import Veterinaria.Tratamiento;
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
        return service.registrarPreventivo(mascotaId, nombre, fechaInicio, estado, frecuencia);
    }

    public Tratamiento registrarCurativo(String mascotaId, String nombre, String fechaInicio, Estado estado, int duracion, String diagnostico) {
        return service.registrarCurativo(mascotaId, nombre, fechaInicio, estado, duracion, diagnostico);
    }

    public Tratamiento registrarCirujia(String mascotaId, String nombre, String fechaInicio, Estado estado, String tipoCirujia, NivelComplejidad nivelComplejidad) {
        return service.registrarCirujia(mascotaId, nombre, fechaInicio, estado, tipoCirujia, nivelComplejidad);
    }

    public List<Mascota> getMascotas() {
        return service.getMascotas();
    }

    public List<Tratamiento> getTratamientos() {
        return service.getTratamientos();
    }

}
