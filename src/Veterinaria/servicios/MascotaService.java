package Veterinaria.servicios;

import Veterinaria.Dueño;
import Veterinaria.Mascota;
import Veterinaria.NivelCuidado;
import Veterinaria.Tipo;
import java.util.List;

public interface MascotaService {
    Mascota registrarPerro(String duenoId, String nombre, int edad, Double peso, String raza);
    Mascota registrarGato(String duenoId, String nombre, int edad, Double peso, Boolean vacunado);
    Mascota registrarAve(String duenoId, String nombre, int edad, Double peso, Tipo tipo);
    Mascota registrarExotico(String duenoId, String nombre, int edad, Double peso, NivelCuidado nivelCuidado);
    List<Dueño> getDuenos();
    List<Mascota> getMascotas();
    Mascota buscarMascotaPorId(String id);
}