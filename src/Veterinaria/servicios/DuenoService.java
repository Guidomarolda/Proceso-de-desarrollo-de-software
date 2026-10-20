package Veterinaria.servicios;

import Veterinaria.modelo.Dueño;
import java.util.List;

public interface DuenoService {
    Dueño registrarDueno(String nombre, String apellido, String telefono, String correo);
    List<Dueño> getDuenos();
    Dueño buscarDuenoPorId(String id);
}
