package Veterinaria.modelo.decoradores;

import Veterinaria.modelo.Tratamiento;

/**
 * Decorador concreto que agrega el recargo por atencion en Guardia de Urgencia
 * y marca el tratamiento como de seguimiento urgente obligatorio.
 */
public class UrgenciaGuardiaDecorator extends TratamientoDecorator {
    private static final double RECARGO_GUARDIA = 800.0;

    public UrgenciaGuardiaDecorator(Tratamiento tratamientoEnvuelto) {
        super(tratamientoEnvuelto);
    }

    @Override
    public Double costoFijo() {
        return super.costoFijo() + RECARGO_GUARDIA;
    }

    @Override
    public Boolean requiereAtencionUrgente() {
        return true;
    }

    @Override
    public String getTipoRegistro() {
        return "URGENCIA+" + tratamientoEnvuelto.getTipoRegistro();
    }

    @Override
    public String getDescripcionExtra() {
        return super.getDescripcionExtra() + "\n[Decorador] Guardia de Urgencia: +$" + RECARGO_GUARDIA;
    }

    @Override
    protected String getEtiquetaDecorador() {
        return "Guardia Urgencia";
    }
}
