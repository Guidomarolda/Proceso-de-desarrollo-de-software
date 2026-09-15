package Veterinaria;

/** Centraliza la construccion de las distintas clases de tratamiento. */
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
}
