package Veterinaria.persistencia;

import Veterinaria.modelo.*;
import Veterinaria.modelo.decoradores.InternacionDecorator;
import Veterinaria.modelo.decoradores.MedicacionEspecialDecorator;
import Veterinaria.modelo.decoradores.UrgenciaGuardiaDecorator;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/** Traduce tratamientos (simples, compuestos y decorados) al formato CSV. */
final class TratamientoCsvMapper {
    Tratamiento desdeCampos(List<String> campos) {
        return crearTratamiento(campos.get(0), campos.get(2), campos.get(3),
                campos.get(4), Estado.valueOf(campos.get(5)), campos.get(6), campos.get(7));
    }

    String aLinea(String mascotaId, Tratamiento tratamiento) {
        return CsvUtils.linea(tratamiento.getId(), mascotaId, tratamiento.getTipoRegistro(),
                tratamiento.getNombreTratamiento(), tratamiento.getFechaInicio(),
                tratamiento.getEstado().name(), tratamiento.getDatoExtra1(), tratamiento.getDatoExtra2());
    }

    private Tratamiento crearTratamiento(String id, String tipo, String nombre,
                                         String fechaInicio, Estado estado, String extra1, String extra2) {
        if (tipo.startsWith("URGENCIA+")) {
            String subTipo = tipo.substring("URGENCIA+".length());
            Tratamiento base = crearTratamiento(id, subTipo, nombre, fechaInicio, estado, extra1, extra2);
            return new UrgenciaGuardiaDecorator(base);
        }
        if (tipo.startsWith("MEDICACION+")) {
            String subTipo = tipo.substring("MEDICACION+".length());
            Tratamiento base = crearTratamiento(id, subTipo, nombre, fechaInicio, estado, extra1, extra2);
            return new MedicacionEspecialDecorator(base);
        }
        if (tipo.startsWith("INTERNACION:")) {
            int idxMas = tipo.indexOf('+');
            int dias = Integer.parseInt(tipo.substring("INTERNACION:".length(), idxMas));
            String subTipo = tipo.substring(idxMas + 1);
            Tratamiento base = crearTratamiento(id, subTipo, nombre, fechaInicio, estado, extra1, extra2);
            return new InternacionDecorator(base, dias);
        }
        if ("PLAN_COMPUESTO".equals(tipo)) {
            List<Tratamiento> hijos = new ArrayList<>();
            if (extra1 != null && !extra1.isEmpty()) {
                String[] partesHijos = extra1.split("\\|");
                for (String parte : partesHijos) {
                    String[] atributos = parte.split("~", -1);
                    if (atributos.length >= 7) {
                        Tratamiento hijo = crearTratamiento(
                                decode(atributos[0]),
                                decode(atributos[1]),
                                decode(atributos[2]),
                                decode(atributos[3]),
                                Estado.valueOf(decode(atributos[4])),
                                decode(atributos[5]),
                                decode(atributos[6]));
                        hijos.add(hijo);
                    }
                }
            }
            return new PlanTratamiento(id, nombre, fechaInicio, estado, hijos);
        }
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

    private static String decode(String valor) {
        if (valor == null || valor.isEmpty()) return "";
        return new String(Base64.getUrlDecoder().decode(valor), StandardCharsets.UTF_8);
    }
}
