package Veterinaria.servicios;

import Veterinaria.Dueño;
import Veterinaria.Mascota;
import Veterinaria.Tratamiento;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Servicio especializado en consultas, reportes y calculos analiticos.
 * Resuelve el problema de baja cohesion (God Class) en ClinicaVeterinariaService.
 */
public class ConsultaVeterinariaService {
    private final ClinicaVeterinariaService clinicaService;

    public ConsultaVeterinariaService(ClinicaVeterinariaService clinicaService) {
        if (clinicaService == null) {
            throw new IllegalArgumentException("El servicio de clinica no puede ser nulo.");
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
        Map<Mascota, Integer> resumen = new LinkedHashMap<>();
        for (Mascota mascota : dueno.getMascotas()) {
            resumen.put(mascota, mascota.getTratamientos().size());
        }
        return resumen;
    }

    private void validarNoNulo(Object valor, String campo) {
        if (valor == null) {
            throw new IllegalArgumentException("Debe seleccionar " + campo + ".");
        }
    }
}