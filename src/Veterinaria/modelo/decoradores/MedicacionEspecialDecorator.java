package Veterinaria.modelo.decoradores;

import Veterinaria.modelo.Tratamiento;

/**
 * Decorador concreto que agrega el costo de insumos y medicacion especial/importada al tratamiento.
 */
public class MedicacionEspecialDecorator extends TratamientoDecorator {
    private static final double RECARGO_MEDICACION = 500.0;

    public MedicacionEspecialDecorator(Tratamiento tratamientoEnvuelto) {
        super(tratamientoEnvuelto);
    }

    @Override
    public Double costoFijo() {
        return super.costoFijo() + RECARGO_MEDICACION;
    }

    @Override
    public String getTipoRegistro() {
        return "MEDICACION+" + tratamientoEnvuelto.getTipoRegistro();
    }

    @Override
    public String getDescripcionExtra() {
        return super.getDescripcionExtra() + "\n[Decorador] Medicacion Especial: +$" + RECARGO_MEDICACION;
    }

    @Override
    protected String getEtiquetaDecorador() {
        return "Medicacion Especial";
    }
}
