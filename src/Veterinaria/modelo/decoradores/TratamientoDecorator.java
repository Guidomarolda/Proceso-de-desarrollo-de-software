package Veterinaria.modelo.decoradores;

import Veterinaria.modelo.Estado;
import Veterinaria.modelo.Tratamiento;

/**
 * Patron DECORATOR (Base Decorator):
 * Envuelve un objeto Tratamiento (simple o compuesto) para agregar responsabilidades,
 * costos o comportamientos dinamicamente sin modificar su estructura original.
 */
public abstract class TratamientoDecorator extends Tratamiento {
    protected final Tratamiento tratamientoEnvuelto;

    protected TratamientoDecorator(Tratamiento tratamientoEnvuelto) {
        super(validarEnvuelto(tratamientoEnvuelto).getId(),
                tratamientoEnvuelto.getNombreTratamiento(),
                tratamientoEnvuelto.getFechaInicio(),
                tratamientoEnvuelto.getEstado());
        this.tratamientoEnvuelto = tratamientoEnvuelto;
    }

    private static Tratamiento validarEnvuelto(Tratamiento tratamiento) {
        if (tratamiento == null) {
            throw new IllegalArgumentException("El tratamiento a decorar no puede ser nulo.");
        }
        return tratamiento;
    }

    public Tratamiento getTratamientoEnvuelto() {
        return tratamientoEnvuelto;
    }

    @Override
    public String getId() {
        return tratamientoEnvuelto.getId();
    }

    @Override
    public String getNombreTratamiento() {
        return tratamientoEnvuelto.getNombreTratamiento();
    }

    @Override
    public String getFechaInicio() {
        return tratamientoEnvuelto.getFechaInicio();
    }

    @Override
    public Estado getEstado() {
        return tratamientoEnvuelto.getEstado();
    }

    @Override
    public void setEstado(Estado estado) {
        if (tratamientoEnvuelto != null) {
            tratamientoEnvuelto.setEstado(estado);
        }
        super.setEstado(estado);
    }

    @Override
    public Double costoFijo() {
        return tratamientoEnvuelto.costoFijo();
    }

    @Override
    public Boolean requiereAtencionUrgente() {
        return tratamientoEnvuelto.requiereAtencionUrgente();
    }

    @Override
    public String getDatoExtra1() {
        return tratamientoEnvuelto.getDatoExtra1();
    }

    @Override
    public String getDatoExtra2() {
        return tratamientoEnvuelto.getDatoExtra2();
    }

    @Override
    public String getDescripcionExtra() {
        return tratamientoEnvuelto.getDescripcionExtra();
    }

    protected abstract String getEtiquetaDecorador();

    @Override
    public String toString() {
        return tratamientoEnvuelto.toString() + " + " + getEtiquetaDecorador();
    }
}
