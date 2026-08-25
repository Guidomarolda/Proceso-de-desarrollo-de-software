package Veterinaria;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Dueño {
    private String id;
    private String nombre;
    private String apellido;
    private String telefono;
    private String correo;
    private List<Mascota> mascotas;

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = validarTexto(nombre, "nombre");
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = validarTexto(apellido, "apellido");
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = validarTexto(telefono, "telefono");
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = validarTexto(correo, "correo");
    }

    public Mascota getMascota() {
        if (mascotas.isEmpty()) {
            return null;
        }
        return mascotas.get(0);
    }

    public void setMascota(Mascota mascota) {
        mascotas.clear();
        if (mascota != null) {
            agregarMascota(mascota);
        }
    }

    public Dueño(String nombre, String apellido, String telefono, String correo, Mascota mascota) {
        this(UUID.randomUUID().toString(), nombre, apellido, telefono, correo, mascota);
    }

    public Dueño(String id, String nombre, String apellido, String telefono, String correo) {
        this(id, nombre, apellido, telefono, correo, null);
    }

    public Dueño(String id, String nombre, String apellido, String telefono, String correo, Mascota mascota) {
        this.id = validarTexto(id, "id");
        this.mascotas = new ArrayList<>();
        setNombre(nombre);
        setApellido(apellido);
        setTelefono(telefono);
        setCorreo(correo);
        if (mascota != null) {
            agregarMascota(mascota);
        }
    }

    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    public void agregarMascota(Mascota mascota) {
        if (mascota == null) {
            throw new IllegalArgumentException("La mascota no puede ser nula.");
        }
        if (!mascotas.contains(mascota)) {
            mascotas.add(mascota);
        }
    }

    public List<Mascota> getMascotas() {
        return Collections.unmodifiableList(mascotas);
    }

    private String validarTexto(String valor, String campo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException("El campo " + campo + " es obligatorio.");
        }
        return valor.trim();
    }

    @Override
    public String toString() {
        return getNombreCompleto() + " - " + telefono;
    }
}
