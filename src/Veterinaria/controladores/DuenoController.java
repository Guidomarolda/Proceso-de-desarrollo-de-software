package Veterinaria.controladores;

import Veterinaria.Dueño;
import Veterinaria.Mascota;
import Veterinaria.servicios.ClinicaVeterinariaService;

import java.util.List;

/**
 * Controlador GRASP especializado en las solicitudes y eventos sobre Dueños.
 */
public class DuenoController {
    private final ClinicaVeterinariaService service;

    public DuenoController(ClinicaVeterinariaService service) {
        if (service == null) {
            throw new IllegalArgumentException("El servicio de clinica no puede ser nulo.");
        }
        this.service = service;
    }

    public Dueño registrarDueno(String nombre, String apellido, String telefono, String correo) {
        Dueño dueno = new Dueño(nombre, apellido, telefono, correo, (Mascota) null);
        service.getDuenosListModificable().add(dueno);
        service.guardarDatos();
        return dueno;
    }

    public List<Dueño> getDuenos() {
        return service.getDuenos();
    }

    public Dueño buscarDuenoPorId(String id) {
        return service.buscarDuenoPorId(id);
    }
}