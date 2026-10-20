package Veterinaria.controladores;

import Veterinaria.modelo.Estado;
import Veterinaria.modelo.Mascota;
import Veterinaria.modelo.NivelComplejidad;
import Veterinaria.modelo.Tratamiento;
import Veterinaria.modelo.TratamientoFactory;
import Veterinaria.servicios.TratamientoService;

import java.util.List;

/**
 * Controlador MVC/GRASP especializado en solicitudes y ciclo de vida de Tratamientos,
 * incluyendo Planes Compuestos (Composite) y Adicionales Dinamicos (Decorator).
 */
public class TratamientoController {
    private final TratamientoService service;

    public TratamientoController(TratamientoService service) {
        if (service == null) {
            throw new IllegalArgumentException("El servicio de tratamientos no puede ser nulo.");
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

    public Tratamiento registrarPlanCompuesto(String mascotaId, String nombre, String fechaInicio, Estado estado, List<Tratamiento> subTratamientos) {
        if (subTratamientos == null || subTratamientos.isEmpty()) {
            throw new IllegalArgumentException("Debe seleccionar al menos un sub-tratamiento para conformar el Plan Compuesto.");
        }
        return service.registrarPlanCompuesto(mascotaId, nombre, fechaInicio, estado, subTratamientos);
    }

    public Tratamiento registrarTratamientoDecorado(String mascotaId, Tratamiento base,
                                                    boolean urgenciaGuardia, boolean medicacionEspecial, int diasInternacion) {
        Tratamiento decorado = TratamientoFactory.decorarTratamiento(base, urgenciaGuardia, medicacionEspecial, diasInternacion);
        return service.registrarTratamientoPersonalizado(mascotaId, decorado);
    }

    public List<Mascota> getMascotas() {
        return service.getMascotas();
    }

    public List<Tratamiento> getTratamientos() {
        return service.getTratamientos();
    }

    public String obtenerDetalle(Tratamiento tratamiento) {
        if (tratamiento == null) {
            return "";
        }
        return tratamiento.obtenerDetalle();
    }
}
