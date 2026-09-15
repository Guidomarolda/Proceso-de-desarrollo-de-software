package Veterinaria.servicios;

import Veterinaria.Dueño;
import Veterinaria.ClinicaVeterinaria;
import Veterinaria.Mascota;
import Veterinaria.Tratamiento;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Servicio especializado en consultas, reportes y calculos analiticos.
 * Consulta el modelo compartido y delega los calculos a sus expertos.
 */
public class ConsultaVeterinariaService {
    private final ClinicaVeterinaria clinicaService;

    public ConsultaVeterinariaService(ClinicaVeterinaria clinicaService) {
        if (clinicaService == null) {
            throw new IllegalArgumentException("La clinica no puede ser nula.");
        }
        this.clinicaService = clinicaService;
    }

    public Double calcularCostoTotal(Mascota mascota, Tratamiento tratamiento) {
        validarNoNulo(mascota, "mascota");
        validarNoNulo(tratamiento, "tratamiento");
        return mascota.costoBaseAtencion() + tratamiento.costoFijo();
    }

    public int compararMascotas(Mascota primera, Mascota segunda) {
        validarNoNulo(primera, "primera mascota");
        validarNoNulo(segunda, "segunda mascota");
        return primera.compareTo(segunda);
    }

    public List<Tratamiento> tratamientosQueRequierenSeguimiento() {
        List<Tratamiento> tratamientos = new ArrayList<>();
        for (Tratamiento tratamiento : clinicaService.getTratamientos()) {
            if (tratamiento.requiereAtencionUrgente()) {
                tratamientos.add(tratamiento);
            }
        }
        return Collections.unmodifiableList(tratamientos);
    }

    public Map<Mascota, Integer> resumenTratamientosPorMascota(Dueño dueno) {
        validarNoNulo(dueno, "dueno");
        return dueno.obtenerResumenTratamientos();
    }

    public List<Dueño> getDuenos() { return clinicaService.getDuenos(); }
    public List<Mascota> getMascotas() { return clinicaService.getMascotas(); }
    public List<Tratamiento> getTratamientos() { return clinicaService.getTratamientos(); }

    private void validarNoNulo(Object valor, String campo) {
        if (valor == null) {
            throw new IllegalArgumentException("Debe seleccionar " + campo + ".");
        }
    }
}
