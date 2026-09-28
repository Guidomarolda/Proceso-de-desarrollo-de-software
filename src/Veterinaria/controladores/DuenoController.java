package Veterinaria.controladores;

import Veterinaria.Dueño;
import Veterinaria.servicios.DuenoService;

import java.util.List;

/**
 * Controlador GRASP especializado en las solicitudes y eventos sobre Dueños.
 */
public class DuenoController {
    private final DuenoService service;

    public DuenoController(DuenoService service) {
        if (service == null) {
            throw new IllegalArgumentException("El servicio de dueños no puede ser nulo.");
        }
        this.service = service;
    }

    public Dueño registrarDueno(String nombre, String apellido, String telefono, String correo) {
        return service.registrarDueno(nombre, apellido, telefono, correo);
    }

    public List<Dueño> getDuenos() {
        return service.getDuenos();
    }

    public Dueño buscarDuenoPorId(String id) {
        return service.buscarDuenoPorId(id);
    }
}
