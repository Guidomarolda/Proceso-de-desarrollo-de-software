package Veterinaria.modelo.decoradores;

import Veterinaria.modelo.Tratamiento;

/**
 * Decorador concreto que agrega internacion hospitalaria por una cantidad de dias determinada.
 * Si la internacion supera los 3 dias, el tratamiento pasa a requerir seguimiento urgente.
 */
public class InternacionDecorator extends TratamientoDecorator {
    private static final double COSTO_POR_DIA = 400.0;
    private final int diasInternacion;

    public InternacionDecorator(Tratamiento tratamientoEnvuelto, int diasInternacion) {
        super(tratamientoEnvuelto);
        if (diasInternacion <= 0) {
            throw new IllegalArgumentException("Los dias de internacion deben ser mayores a cero.");
        }
        this.diasInternacion = diasInternacion;
    }

    public int getDiasInternacion() {
        return diasInternacion;
    }

    public double calcularRecargoInternacion() {
        return diasInternacion * COSTO_POR_DIA;
    }

    @Override
    public Double costoFijo() {
        return super.costoFijo() + calcularRecargoInternacion();
    }

    @Override
    public Boolean requiereAtencionUrgente() {
        return super.requiereAtencionUrgente() || diasInternacion > 3;
    }

    @Override
    public String getTipoRegistro() {
        return "INTERNACION:" + diasInternacion + "+" + tratamientoEnvuelto.getTipoRegistro();
    }

    @Override
    public String getDescripcionExtra() {
        return super.getDescripcionExtra()
                + "\n[Decorador] Internacion (" + diasInternacion + " dias): +$" + calcularRecargoInternacion();
    }

    @Override
    protected String getEtiquetaDecorador() {
        return "Internacion (" + diasInternacion + "d)";
    }
}
