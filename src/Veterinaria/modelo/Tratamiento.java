package Veterinaria.modelo;

import java.util.UUID;

/**
 * Componente base abstracto para los tratamientos veterinarios.
 * Actua como Component en los patrones Composite y Decorator.
 */
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

    public abstract Boolean requiereAtencionUrgente();

    public abstract String getTipoRegistro();

    public abstract String getDatoExtra1();

    public abstract String getDatoExtra2();

    /**
     * Devuelve la informacion especifica del tratamiento sin requerir instanceof en la Vista.
     */
    public abstract String getDescripcionExtra();

    /**
     * Construye el resumen completo del tratamiento de manera polimorfica.
     */
    public String obtenerDetalle() {
        StringBuilder builder = new StringBuilder();
        builder.append("Nombre: ").append(getNombreTratamiento()).append("\n");
        builder.append("Fecha inicio: ").append(getFechaInicio()).append("\n");
        builder.append("Estado: ").append(getEstado()).append("\n");
        builder.append("Costo: ").append(costoFijo()).append("\n");
        builder.append("Requiere seguimiento: ").append(requiereAtencionUrgente() ? "Si" : "No").append("\n");
        String extra = getDescripcionExtra();
        if (extra != null && !extra.isEmpty()) {
            builder.append(extra).append("\n");
        }
        return builder.toString();
    }

    protected String validarTexto(String valor, String campo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException("El campo " + campo + " es obligatorio.");
        }
        return valor.trim();
    }

    @Override
    public String toString() {
        return getNombreTratamiento() + " (" + getClass().getSimpleName() + " - " + getEstado() + ")";
    }
}
