package Veterinaria.persistencia;

import Veterinaria.*;

/** Traduce mascotas al formato CSV y reconstituye sus identificadores. */
final class MascotaCsvMapper {
    Mascota desdeCampos(java.util.List<String> campos) {
        return crearMascota(campos.get(0), campos.get(2), campos.get(3),
                Integer.parseInt(campos.get(4)), Double.parseDouble(campos.get(5)), campos.get(6));
    }

    String aLinea(String duenoId, Mascota mascota) {
        return CsvUtils.linea(mascota.getId(), duenoId, tipoMascota(mascota), mascota.getNombre(),
                String.valueOf(mascota.getEdad()), String.valueOf(mascota.getPeso()), extraMascota(mascota));
    }

    private Mascota crearMascota(String id, String tipo, String nombre, int edad, Double peso, String extra) {
        if ("PERRO".equals(tipo)) {
            return new Perro(id, peso, edad, nombre, extra);
        }
        if ("GATO".equals(tipo)) {
            return new Gato(id, peso, edad, nombre, Boolean.parseBoolean(extra));
        }
        if ("AVE".equals(tipo)) {
            return new Ave(id, peso, edad, nombre, Tipo.valueOf(extra));
        }
        if ("EXOTICO".equals(tipo)) {
            return new Exoticos(id, peso, edad, nombre, NivelCuidado.valueOf(extra));
        }
        throw new IllegalArgumentException("Tipo de mascota desconocido: " + tipo);
    }

    private String tipoMascota(Mascota mascota) {
        if (mascota instanceof Perro) return "PERRO";
        if (mascota instanceof Gato) return "GATO";
        if (mascota instanceof Ave) return "AVE";
        if (mascota instanceof Exoticos) return "EXOTICO";
        throw new IllegalArgumentException("Tipo de mascota no soportado.");
    }

    private String extraMascota(Mascota mascota) {
        if (mascota instanceof Perro) return ((Perro) mascota).getRaza();
        if (mascota instanceof Gato) return String.valueOf(((Gato) mascota).getEstaVacunado());
        if (mascota instanceof Ave) return ((Ave) mascota).getTipo().name();
        if (mascota instanceof Exoticos) return ((Exoticos) mascota).getNivelCuidado().name();
        return "";
    }
}
