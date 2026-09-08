package Veterinaria;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Long Parameter List actualizado 
 */
public abstract class Mascota implements Comparable<Mascota> {
    private String id;
    private String nombre;
    private int edad;
    private Double peso;
    private List<Tratamiento> tratamientos;

    public Mascota(Tratamiento tratamiento, Double peso, int edad, String nombre) {
        this(UUID.randomUUID().toString(), tratamiento, peso, edad, nombre);
    }

    public Mascota(String id, Double peso, int edad, String nombre) {
        this(id, null, peso, edad, nombre);
    }

    protected Mascota(String id, Tratamiento tratamiento, Double peso, int edad, String nombre) {
        this.id = validarTexto(id, "id");
        this.tratamientos = new ArrayList<>();
        setPeso(peso);
        setEdad(edad);
        setNombre(nombre);
        if (tratamiento != null) {
            agregarTratamiento(tratamiento);
        }
    }

    /**
     * Constructor que acepta un MascotaBuilder (soluciona Long Parameter List)
     */
    protected Mascota(MascotaBuilder builder) {
        this.id = validarTexto(builder.getId(), "id");
        this.tratamientos = new ArrayList<>();
        setPeso(builder.getPeso());
        setEdad(builder.getEdad());
        setNombre(builder.getNombre());
        if (builder.getTratamiento() != null) {
            agregarTratamiento(builder.getTratamiento());
        }
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = validarTexto(nombre, "nombre");
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        if (edad < 0) {
            throw new IllegalArgumentException("La edad no puede ser negativa.");
        }
        this.edad = edad;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        if (peso == null || peso <= 0) {
            throw new IllegalArgumentException("El peso debe ser mayor a cero.");
        }
        this.peso = peso;
    }

    public Tratamiento getTratamiento() {
        if (tratamientos.isEmpty()) {
            return null;
        }
        return tratamientos.get(tratamientos.size() - 1);
    }

    public void setTratamiento(Tratamiento tratamiento) {
        tratamientos.clear();
        if (tratamiento != null) {
            agregarTratamiento(tratamiento);
        }
    }

    public void agregarTratamiento(Tratamiento tratamiento) {
        if (tratamiento == null) {
            throw new IllegalArgumentException("El tratamiento no puede ser nulo.");
        }
        if (!tratamientos.contains(tratamiento)) {
            tratamientos.add(tratamiento);
        }
    }

    public List<Tratamiento> getTratamientos() {
        return Collections.unmodifiableList(tratamientos);
    }

    public abstract  Double costoBaseAtencion();

    public Boolean esAptaParaProcedimientoEspecial() {
        return edad < 10 && peso < 30;
    }

    @Override
    public int compareTo(Mascota o) {
        return Double.compare(this.costoBaseAtencion(), o.costoBaseAtencion());
    }

    protected String validarTexto(String valor, String campo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException("El campo " + campo + " es obligatorio.");
        }
        return valor.trim();
    }

    @Override
    public String toString() {
        return nombre + " (" + getClass().getSimpleName() + ")";
    }

}
