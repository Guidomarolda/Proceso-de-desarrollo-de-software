package Veterinaria.controladores;

import Veterinaria.modelo.Dueño;
import Veterinaria.modelo.Mascota;
import Veterinaria.modelo.NivelCuidado;
import Veterinaria.modelo.Tipo;
import Veterinaria.servicios.MascotaService;

import java.util.List;

/**
 * Controlador MVC/GRASP especializado en las solicitudes y operaciones sobre Mascotas.
 */
public class MascotaController {
    private final MascotaService service;

    public MascotaController(MascotaService service) {
        if (service == null) {
            throw new IllegalArgumentException("El servicio de mascotas no puede ser nulo.");
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

    public void actualizarImagenMascota(String mascotaId, String rutaImagen) {
        service.actualizarImagenMascota(mascotaId, rutaImagen);
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

    public String obtenerDetalle(Mascota mascota) {
        if (mascota == null) {
            return "";
        }
        return mascota.obtenerDetalle();
    }

    public boolean verificarAptitud(Mascota mascota) {
        if (mascota == null) {
            throw new IllegalArgumentException("Debe seleccionar una mascota de la lista.");
        }
        return mascota.esAptaParaProcedimientoEspecial();
    }
}
