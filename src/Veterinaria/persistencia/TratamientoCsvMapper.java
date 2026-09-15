package Veterinaria.persistencia;

import Veterinaria.*;
import java.util.List;

/** Traduce tratamientos al formato CSV, conservando los datos de cada subtipo. */
final class TratamientoCsvMapper {
    Tratamiento desdeCampos(List<String> campos) {
        return crearTratamiento(campos);
    }

    String aLinea(String mascotaId, Tratamiento tratamiento) {
        return CsvUtils.linea(tratamiento.getId(), mascotaId, tipoTratamiento(tratamiento),
                tratamiento.getNombreTratamiento(), tratamiento.getFechaInicio(),
                tratamiento.getEstado().name(), extraTratamiento1(tratamiento), extraTratamiento2(tratamiento));
    }

    private Tratamiento crearTratamiento(List<String> campos) {
        String id = campos.get(0);
        String tipo = campos.get(2);
        String nombre = campos.get(3);
        String fechaInicio = campos.get(4);
        Estado estado = Estado.valueOf(campos.get(5));
        String extra1 = campos.get(6);
        String extra2 = campos.get(7);
        if ("PREVENTIVO".equals(tipo)) {
            return new TratamientoPreventivo(id, nombre, fechaInicio, estado, Integer.parseInt(extra1));
        }
        if ("CURATIVO".equals(tipo)) {
            return new TratamientoCurativo(id, nombre, fechaInicio, estado, Integer.parseInt(extra1), extra2);
        }
        if ("CIRUJIA".equals(tipo)) {
            return new Cirujia(id, nombre, fechaInicio, estado, extra1, NivelComplejidad.valueOf(extra2));
        }
        throw new IllegalArgumentException("Tipo de tratamiento desconocido: " + tipo);
    }

    private String tipoTratamiento(Tratamiento tratamiento) {
        if (tratamiento instanceof TratamientoPreventivo) return "PREVENTIVO";
        if (tratamiento instanceof TratamientoCurativo) return "CURATIVO";
        if (tratamiento instanceof Cirujia) return "CIRUJIA";
        throw new IllegalArgumentException("Tipo de tratamiento no soportado.");
    }

    private String extraTratamiento1(Tratamiento tratamiento) {
        if (tratamiento instanceof TratamientoPreventivo) return String.valueOf(((TratamientoPreventivo) tratamiento).getFrecuenciaRecomendada());
        if (tratamiento instanceof TratamientoCurativo) return String.valueOf(((TratamientoCurativo) tratamiento).getDuracion());
        if (tratamiento instanceof Cirujia) return ((Cirujia) tratamiento).getTipoCirujia();
        return "";
    }

    private String extraTratamiento2(Tratamiento tratamiento) {
        if (tratamiento instanceof TratamientoCurativo) return ((TratamientoCurativo) tratamiento).getDiagnostico();
        if (tratamiento instanceof Cirujia) return ((Cirujia) tratamiento).getNivelComplejidad().name();
        return "";
    }
}
