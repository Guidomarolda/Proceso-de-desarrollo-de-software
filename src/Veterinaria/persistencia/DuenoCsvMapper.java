package Veterinaria.persistencia;

import Veterinaria.modelo.Dueño;
import java.util.List;

/** Conversion CSV de los datos del dueno. */
final class DuenoCsvMapper {
    Dueño desdeCampos(List<String> campos) {
        return new Dueño(campos.get(0), campos.get(1), campos.get(2), campos.get(3), campos.get(4));
    }

    String aLinea(Dueño dueno) {
        return CsvUtils.linea(dueno.getId(), dueno.getNombre(), dueno.getApellido(),
                dueno.getTelefono(), dueno.getCorreo());
    }
}
