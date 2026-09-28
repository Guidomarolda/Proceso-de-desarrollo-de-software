package Veterinaria.servicios;

import Veterinaria.Dueño;
import Veterinaria.Mascota;
import Veterinaria.Tratamiento;
import java.util.List;
import java.util.Map;

public interface ConsultaService {
    Double calcularCostoTotal(Mascota mascota, Tratamiento tratamiento);
    int compararMascotas(Mascota primera, Mascota segunda);
    List<Tratamiento> tratamientosQueRequierenSeguimiento();
    Map<Mascota, Integer> resumenTratamientosPorMascota(Dueño dueno);
    List<Dueño> getDuenos();
    List<Mascota> getMascotas();
    List<Tratamiento> getTratamientos();
}