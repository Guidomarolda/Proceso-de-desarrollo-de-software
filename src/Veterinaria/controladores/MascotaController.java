package Veterinaria.controladores;

import Veterinaria.Dueño;
import Veterinaria.Mascota;
import Veterinaria.NivelCuidado;
import Veterinaria.Tipo;
import Veterinaria.servicios.ClinicaVeterinariaService;

import java.util.List;

/**
 * Controlador GRASP especializado en las solicitudes y operaciones sobre Mascotas.
 * Delega el registro y la asociacion al servicio de aplicacion.
 */
public class MascotaController {
    private final ClinicaVeterinariaService service;

    public MascotaController(ClinicaVeterinariaService service) {
        if (service == null) {
            throw new IllegalArgumentException("El servicio de clinica no puede ser nulo.");
        }
        this.service = service;
    }

    public Mascota registrarPerro(String duenoId, String nombre, int edad, Double peso, String raza) {
        return service.registrarPerro(duenoId, nombre, edad, peso, raza);
    }

    public Mascota registrarGato(String duenoId, String nombre, int edad, Double peso, Boolean vacunado) {
        return service.registrarGato(duenoId, nombre, edad, peso, vacunado);
    }

    public Mascota registrarAve(String duenoId, String nombre, int edad, Double peso, Tipo tipo) {
        return service.registrarAve(duenoId, nombre, edad, peso, tipo);
    }

    public Mascota registrarExotico(String duenoId, String nombre, int edad, Double peso, NivelCuidado nivelCuidado) {
        return service.registrarExotico(duenoId, nombre, edad, peso, nivelCuidado);
    }

    public List<Dueño> getDuenos() {
        return service.getDuenos();
    }

    public List<Mascota> getMascotas() {
        return service.getMascotas();
    }

    public Mascota buscarMascotaPorId(String id) {
        return service.buscarMascotaPorId(id);
    }


}
