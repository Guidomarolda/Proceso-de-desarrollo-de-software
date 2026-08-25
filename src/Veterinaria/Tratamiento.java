package Veterinaria;

import java.util.UUID;

public abstract class Tratamiento {
    private String id;
    private String nombreTratamiento;
    private String fechaInicio;
    private Estado estado;

    public Tratamiento(String nombreTratamiento, int fechaInicio, Estado estado) {
        this(nombreTratamiento, String.valueOf(fechaInicio), estado);
    }

    public Tratamiento(String nombreTratamiento, String fechaInicio, Estado estado) {
        this(UUID.randomUUID().toString(), nombreTratamiento, fechaInicio, estado);
    }

    public Tratamiento(String id, String nombreTratamiento, String fechaInicio, Estado estado) {
        this.id = validarTexto(id, "id");
        setNombreTratamiento(nombreTratamiento);
        setFechaInicio(fechaInicio);
        setEstado(estado);
    }

    public String getId() {
        return id;
    }

    public String getNombreTratamiento() {
        return nombreTratamiento;
    }

    public void setNombreTratamiento(String nombreTratamiento) {
        this.nombreTratamiento = validarTexto(nombreTratamiento, "nombre del tratamiento");
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = validarTexto(fechaInicio, "fecha de inicio");
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        if (estado == null) {
            throw new IllegalArgumentException("El estado es obligatorio.");
        }
        this.estado = estado;
    }

    public abstract Double costoFijo();
    //de tipo boolean debido a que es un aviso simplemente con 2 respuestas posibles
    public abstract Boolean requiereAtencionUrgente();

    protected String validarTexto(String valor, String campo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException("El campo " + campo + " es obligatorio.");
        }
        return valor.trim();
    }

    @Override
    public String toString() {
        return nombreTratamiento + " (" + getClass().getSimpleName() + " - " + estado + ")";
    }
}
