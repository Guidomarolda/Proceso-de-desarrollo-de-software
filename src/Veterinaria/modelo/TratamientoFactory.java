package Veterinaria.modelo;

import Veterinaria.modelo.decoradores.InternacionDecorator;
import Veterinaria.modelo.decoradores.MedicacionEspecialDecorator;
import Veterinaria.modelo.decoradores.UrgenciaGuardiaDecorator;

import java.util.List;

/** Centraliza la construccion de las distintas clases de tratamiento, planes compuestos y decoradores. */
public final class TratamientoFactory {
    private TratamientoFactory() { }

    public static Tratamiento crearPreventivo(String nombre, String fecha, Estado estado, int frecuencia) {
        return new TratamientoPreventivo(nombre, fecha, estado, frecuencia);
    }

    public static Tratamiento crearCurativo(String nombre, String fecha, Estado estado, int duracion, String diagnostico) {
        return new TratamientoCurativo(nombre, fecha, estado, duracion, diagnostico);
    }

    public static Tratamiento crearCirujia(String nombre, String fecha, Estado estado, String tipo, NivelComplejidad nivel) {
        return new Cirujia(nombre, fecha, estado, tipo, nivel);
    }

    public static PlanTratamiento crearPlanCompuesto(String nombre, String fecha, Estado estado, List<Tratamiento> subTratamientos) {
        return new PlanTratamiento(nombre, fecha, estado, subTratamientos);
    }

    public static Tratamiento decorarTratamiento(Tratamiento base, boolean urgenciaGuardia, boolean medicacionEspecial, int diasInternacion) {
        Tratamiento resultado = base;
        if (urgenciaGuardia) {
            resultado = new UrgenciaGuardiaDecorator(resultado);
        }
        if (medicacionEspecial) {
            resultado = new MedicacionEspecialDecorator(resultado);
        }
        if (diasInternacion > 0) {
            resultado = new InternacionDecorator(resultado, diasInternacion);
        }
        return resultado;
    }
}
