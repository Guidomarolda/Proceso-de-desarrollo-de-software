package Veterinaria;

import java.util.UUID;

/**
 * Builder para construir objetos Mascota de forma más legible.
 */
public class MascotaBuilder {
    private String id;
    private String nombre;
    private int edad;
    private Double peso;
    private Tratamiento tratamiento;

    public MascotaBuilder() {
        this.id = UUID.randomUUID().toString();
    }

    public MascotaBuilder conId(String id) {
        this.id = id;
        return this;
    }

    public MascotaBuilder conNombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public MascotaBuilder conEdad(int edad) {
        this.edad = edad;
        return this;
    }

    public MascotaBuilder conPeso(Double peso) {
        this.peso = peso;
        return this;
    }

    public MascotaBuilder conTratamiento(Tratamiento tratamiento) {
        this.tratamiento = tratamiento;
        return this;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public int getEdad() {
        return edad;
    }

    public Double getPeso() {
        return peso;
    }

    public Tratamiento getTratamiento() {
        return tratamiento;
    }
}
