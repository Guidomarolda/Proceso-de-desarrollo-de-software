package Veterinaria.controladores;

import Veterinaria.Dueño;
import Veterinaria.Mascota;
import Veterinaria.MascotaFactory;
import Veterinaria.NivelCuidado;
import Veterinaria.Tipo;
import Veterinaria.servicios.ClinicaVeterinariaService;

import java.util.List;

/**
 * Controlador GRASP especializado en las solicitudes y operaciones sobre Mascotas.
 * Coordina la creacion mediante MascotaFactory y la asociacion con sus dueños.
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
        Mascota mascota = MascotaFactory.crearPerro(nombre, edad, peso, null, raza);
        agregarMascotaADueno(duenoId, mascota);
        return mascota;
    }

    public Mascota registrarGato(String duenoId, String nombre, int edad, Double peso, Boolean vacunado) {
        Mascota mascota = MascotaFactory.crearGato(nombre, edad, peso, null, vacunado);
        agregarMascotaADueno(duenoId, mascota);
        return mascota;
    }

    public Mascota registrarAve(String duenoId, String nombre, int edad, Double peso, Tipo tipo) {
        Mascota mascota = MascotaFactory.crearAve(nombre, edad, peso, null, tipo);
        agregarMascotaADueno(duenoId, mascota);
        return mascota;
    }

    public Mascota registrarExotico(String duenoId, String nombre, int edad, Double peso, NivelCuidado nivelCuidado) {
        Mascota mascota = MascotaFactory.crearExotico(nombre, edad, peso, null, nivelCuidado);
        agregarMascotaADueno(duenoId, mascota);
        return mascota;
    }

    public List<Mascota> getMascotas() {
        return service.getMascotas();
    }

    public Mascota buscarMascotaPorId(String id) {
        return service.buscarMascotaPorId(id);
    }

    public int compararMascotas(Mascota primera, Mascota segunda) {
        return service.getConsultaService().compararMascotas(primera, segunda);
    }

    private void agregarMascotaADueno(String duenoId, Mascota mascota) {
        Dueño dueno = service.buscarDuenoPorId(duenoId);
        dueno.agregarMascota(mascota);
        service.guardarDatos();
    }
}