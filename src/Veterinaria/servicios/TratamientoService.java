package Veterinaria.servicios;

import Veterinaria.Estado;
import Veterinaria.Mascota;
import Veterinaria.NivelComplejidad;
import Veterinaria.Tratamiento;
import java.util.List;

public interface TratamientoService {
    Tratamiento registrarPreventivo(String mascotaId, String nombre, String fechaInicio, Estado estado, int frecuencia);
    Tratamiento registrarCurativo(String mascotaId, String nombre, String fechaInicio, Estado estado, int duracion, String diagnostico);
    Tratamiento registrarCirujia(String mascotaId, String nombre, String fechaInicio, Estado estado, String tipoCirujia, NivelComplejidad nivelComplejidad);
    List<Mascota> getMascotas();
    List<Tratamiento> getTratamientos();
}