package Veterinaria.servicios;

import Veterinaria.modelo.Estado;
import Veterinaria.modelo.Mascota;
import Veterinaria.modelo.NivelComplejidad;
import Veterinaria.modelo.Tratamiento;
import java.util.List;

public interface TratamientoService {
    Tratamiento registrarPreventivo(String mascotaId, String nombre, String fechaInicio, Estado estado, int frecuencia);
    Tratamiento registrarCurativo(String mascotaId, String nombre, String fechaInicio, Estado estado, int duracion, String diagnostico);
    Tratamiento registrarCirujia(String mascotaId, String nombre, String fechaInicio, Estado estado, String tipoCirujia, NivelComplejidad nivelComplejidad);
    Tratamiento registrarPlanCompuesto(String mascotaId, String nombre, String fechaInicio, Estado estado, List<Tratamiento> subTratamientos);
    Tratamiento registrarTratamientoPersonalizado(String mascotaId, Tratamiento tratamiento);
    List<Mascota> getMascotas();
    List<Tratamiento> getTratamientos();
}
