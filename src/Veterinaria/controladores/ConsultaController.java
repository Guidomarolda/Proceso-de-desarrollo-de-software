package Veterinaria.controladores;

import Veterinaria.modelo.Dueño;
import Veterinaria.modelo.Mascota;
import Veterinaria.modelo.Tratamiento;
import Veterinaria.servicios.ConsultaService;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/** Recibe los eventos de consulta y comparacion de la interfaz. */
public class ConsultaController {
    private final ConsultaService service;

    public ConsultaController(ConsultaService service) {
        this.service = Objects.requireNonNull(service, "El servicio de consultas es obligatorio.");
    }

    public Double calcularCostoTotal(Mascota mascota, Tratamiento tratamiento) {
        return service.calcularCostoTotal(mascota, tratamiento);
    }

    public int compararMascotas(Mascota primera, Mascota segunda) {
        return service.compararMascotas(primera, segunda);
    }

    public Map<Mascota, Integer> resumenTratamientosPorMascota(Dueño dueno) {
        return service.resumenTratamientosPorMascota(dueno);
    }

    public List<Tratamiento> tratamientosQueRequierenSeguimiento() {
        return service.tratamientosQueRequierenSeguimiento();
    }

    public List<Dueño> getDuenos() { return service.getDuenos(); }
    public List<Mascota> getMascotas() { return service.getMascotas(); }
    public List<Tratamiento> getTratamientos() { return service.getTratamientos(); }
}
