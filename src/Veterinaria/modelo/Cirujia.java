package Veterinaria.modelo;

import java.util.UUID;

public class Cirujia extends Tratamiento {
    private String tipoCirujia;
    private NivelComplejidad nivelComplejidad;

    public Cirujia(String nombreTratamiento, int fechaInicio, Estado estado, String tipoCirujia, NivelComplejidad nivelComplejidad) {
        this(nombreTratamiento, String.valueOf(fechaInicio), estado, tipoCirujia, nivelComplejidad);
    }

    public Cirujia(String id, String nombreTratamiento, String fechaInicio, Estado estado, String tipoCirujia, NivelComplejidad nivelComplejidad) {
        super(id, nombreTratamiento, fechaInicio, estado);
        validarEstadoCirujia(estado);
        setTipoCirujia(tipoCirujia);
        setNivelComplejidad(nivelComplejidad);
    }

    public Cirujia(String nombreTratamiento, String fechaInicio, Estado estado, String tipoCirujia, NivelComplejidad nivelComplejidad) {
        this(UUID.randomUUID().toString(), nombreTratamiento, fechaInicio, estado, tipoCirujia, nivelComplejidad);
    }

    public String getTipoCirujia() {
        return tipoCirujia;
    }

    public void setTipoCirujia(String tipoCirujia) {
        this.tipoCirujia = validarTexto(tipoCirujia, "tipo de cirujia");
    }

    public NivelComplejidad getNivelComplejidad() {
        return nivelComplejidad;
    }

    public void setNivelComplejidad(NivelComplejidad nivelComplejidad) {
        if (nivelComplejidad == null) {
            throw new IllegalArgumentException("El nivel de complejidad es obligatorio.");
        }
        this.nivelComplejidad = nivelComplejidad;
    }

    @Override
    public Double costoFijo() {
        Double costoBase = 3000.0;
        if (nivelComplejidad == NivelComplejidad.media) {
            return costoBase + 1500.0;
        } else if (nivelComplejidad == NivelComplejidad.alta) {
            return costoBase + 3000.0;
        } else {
            return costoBase;
        }
    }

    @Override
    public Boolean requiereAtencionUrgente() {
        return getEstado() == Estado.encurso;
    }

    @Override
    public void setEstado(Estado estado) {
        validarEstadoCirujia(estado);
        super.setEstado(estado);
    }

    private void validarEstadoCirujia(Estado estado) {
        if (estado == Estado.encurso) {
            throw new CirujiaEnCursoException("El tratamiento no puede ser una cirujia en curso.");
        }
    }

    @Override
    public String getTipoRegistro() {
        return "CIRUJIA";
    }

    @Override
    public String getDatoExtra1() {
        return tipoCirujia;
    }

    @Override
    public String getDatoExtra2() {
        return nivelComplejidad.name();
    }

    @Override
    public String getDescripcionExtra() {
        return "Tipo cirujia: " + tipoCirujia + "\nComplejidad: " + nivelComplejidad;
    }
}
