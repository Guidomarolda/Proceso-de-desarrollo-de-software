package Veterinaria.servicios;

import Veterinaria.modelo.ClinicaVeterinaria;
import Veterinaria.modelo.Dueño;
import Veterinaria.modelo.Mascota;
import Veterinaria.modelo.Tratamiento;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Servicio especializado en consultas, reportes y calculos analiticos.
 * Consulta el modelo compartido y delega los calculos a sus expertos.
 */
public class ConsultaVeterinariaService implements ConsultaService {
    private final ClinicaVeterinaria clinicaService;

    public ConsultaVeterinariaService(ClinicaVeterinaria clinicaService) {
        if (clinicaService == null) {
            throw new IllegalArgumentException("La clinica no puede ser nula.");
        }
        this.clinicaService = clinicaService;
    }

    @Override
    public Double calcularCostoTotal(Mascota mascota, Tratamiento tratamiento) {
        validarNoNulo(mascota, "mascota");
        validarNoNulo(tratamiento, "tratamiento");
        return mascota.costoBaseAtencion() + tratamiento.costoFijo();
    }

    @Override
    public int compararMascotas(Mascota primera, Mascota segunda) {
        validarNoNulo(primera, "primera mascota");
        validarNoNulo(segunda, "segunda mascota");
        return primera.compareTo(segunda);
    }

    @Override
    public List<Tratamiento> tratamientosQueRequierenSeguimiento() {
        List<Tratamiento> tratamientos = new ArrayList<>();
        for (Tratamiento tratamiento : clinicaService.getTratamientos()) {
            if (tratamiento.requiereAtencionUrgente()) {
                tratamientos.add(tratamiento);
            }
        }
        return Collections.unmodifiableList(tratamientos);
    }

    @Override
    public Map<Mascota, Integer> resumenTratamientosPorMascota(Dueño dueno) {
        validarNoNulo(dueno, "dueno");
        return dueno.obtenerResumenTratamientos();
    }

    @Override
    public List<Dueño> getDuenos() { return clinicaService.getDuenos(); }
    @Override
    public List<Mascota> getMascotas() { return clinicaService.getMascotas(); }
    @Override
    public List<Tratamiento> getTratamientos() { return clinicaService.getTratamientos(); }

    private void validarNoNulo(Object valor, String campo) {
        if (valor == null) {
            throw new IllegalArgumentException("Debe seleccionar " + campo + ".");
        }
    }
}
