package Veterinaria;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/** Experto en los registros de la clinica y creador de sus dueños. */
public class ClinicaVeterinaria {
    private final List<Dueño> duenos = new ArrayList<>();

    public Dueño registrarDueno(String nombre, String apellido, String telefono, String correo) {
        Dueño dueno = new Dueño(nombre, apellido, telefono, correo, (Mascota) null);
        duenos.add(dueno);
        return dueno;
    }

    public void reemplazarDuenos(List<Dueño> nuevosDuenos) {
        List<Dueño> copia = new ArrayList<>(Objects.requireNonNull(nuevosDuenos));
        for (Dueño dueno : copia) {
            Objects.requireNonNull(dueno, "El dueño no puede ser nulo.");
        }
        duenos.clear();
        duenos.addAll(copia);
    }

    public List<Dueño> getDuenos() {
        return Collections.unmodifiableList(duenos);
    }

    public List<Mascota> getMascotas() {
        List<Mascota> mascotas = new ArrayList<>();
        for (Dueño dueno : duenos) {
            mascotas.addAll(dueno.getMascotas());
        }
        return Collections.unmodifiableList(mascotas);
    }

    public List<Tratamiento> getTratamientos() {
        List<Tratamiento> tratamientos = new ArrayList<>();
        for (Mascota mascota : getMascotas()) {
            tratamientos.addAll(mascota.getTratamientos());
        }
        return Collections.unmodifiableList(tratamientos);
    }

    public Dueño buscarDuenoPorId(String id) {
        for (Dueño dueno : duenos) {
            if (dueno.getId().equals(id)) return dueno;
        }
        throw new IllegalArgumentException("No existe un dueño con el id indicado.");
    }

    public Mascota buscarMascotaPorId(String id) {
        for (Mascota mascota : getMascotas()) {
            if (mascota.getId().equals(id)) return mascota;
        }
        throw new IllegalArgumentException("No existe una mascota con el id indicado.");
    }
}
