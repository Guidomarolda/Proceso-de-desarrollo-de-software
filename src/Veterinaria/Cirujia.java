package Veterinaria;

public class Cirujia extends  Tratamiento{
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
        this(java.util.UUID.randomUUID().toString(), nombreTratamiento, fechaInicio, estado, tipoCirujia, nivelComplejidad);
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
// en este metodo se ven todaslas cirujias segun su nivel de complejidad
    @Override
    public Double costoFijo() {
        Double costoBase = 3000.0;
        Double costoTotal = 0.0;
        if(nivelComplejidad == NivelComplejidad.media){
            costoTotal = costoBase + 1500.0;
            return costoTotal;

        } else if (nivelComplejidad == NivelComplejidad.alta) {
            costoTotal = costoBase + 3000.0;
            return costoTotal;
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
}
