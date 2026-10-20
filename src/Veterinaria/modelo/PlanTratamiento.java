package Veterinaria.modelo;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Patron COMPOSITE (Composite):
 * Representa un plan de tratamiento compuesto por multiples sub-tratamientos (hojas u otros planes).
 * Permite tratar a un conjunto de tratamientos de manera uniforme como si fuera un unico Tratamiento.
 */
public class PlanTratamiento extends Tratamiento {
    private final List<Tratamiento> subTratamientos;

    public PlanTratamiento(String nombreTratamiento, String fechaInicio, Estado estado) {
        this(UUID.randomUUID().toString(), nombreTratamiento, fechaInicio, estado, new ArrayList<>());
    }

    public PlanTratamiento(String nombreTratamiento, String fechaInicio, Estado estado, List<Tratamiento> subTratamientos) {
        this(UUID.randomUUID().toString(), nombreTratamiento, fechaInicio, estado, subTratamientos);
    }

    public PlanTratamiento(String id, String nombreTratamiento, String fechaInicio, Estado estado, List<Tratamiento> subTratamientos) {
        super(id, nombreTratamiento, fechaInicio, estado);
        this.subTratamientos = new ArrayList<>();
        if (subTratamientos != null) {
            for (Tratamiento sub : subTratamientos) {
                agregarTratamiento(sub);
            }
        }
    }

    public void agregarTratamiento(Tratamiento tratamiento) {
        if (tratamiento == null) {
            throw new IllegalArgumentException("El sub-tratamiento del plan no puede ser nulo.");
        }
        if (tratamiento == this) {
            throw new IllegalArgumentException("Un plan no puede contenerse a si mismo.");
        }
        this.subTratamientos.add(tratamiento);
    }

    public void removerTratamiento(Tratamiento tratamiento) {
        this.subTratamientos.remove(tratamiento);
    }

    public List<Tratamiento> getSubTratamientos() {
        return Collections.unmodifiableList(subTratamientos);
    }

    /**
     * Operacion compuesta: suma recursivamente el costo fijo de todos los tratamientos hijos.
     */
    @Override
    public Double costoFijo() {
        double total = 0.0;
        for (Tratamiento sub : subTratamientos) {
            total += sub.costoFijo();
        }
        return total;
    }

    /**
     * Operacion compuesta: requiere atencion urgente si cualquiera de sus componentes hijos la requiere.
     */
    @Override
    public Boolean requiereAtencionUrgente() {
        for (Tratamiento sub : subTratamientos) {
            if (Boolean.TRUE.equals(sub.requiereAtencionUrgente())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getTipoRegistro() {
        return "PLAN_COMPUESTO";
    }

    @Override
    public String getDatoExtra1() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < subTratamientos.size(); i++) {
            Tratamiento t = subTratamientos.get(i);
            if (i > 0) sb.append("|");
            String item = String.join("~",
                    encode(t.getId()),
                    encode(t.getTipoRegistro()),
                    encode(t.getNombreTratamiento()),
                    encode(t.getFechaInicio()),
                    encode(t.getEstado().name()),
                    encode(t.getDatoExtra1()),
                    encode(t.getDatoExtra2()));
            sb.append(item);
        }
        return sb.toString();
    }

    @Override
    public String getDatoExtra2() {
        return String.valueOf(subTratamientos.size());
    }

    @Override
    public String getDescripcionExtra() {
        StringBuilder sb = new StringBuilder();
        sb.append("Plan Compuesto (").append(subTratamientos.size()).append(" etapas):");
        for (Tratamiento sub : subTratamientos) {
            sb.append("\n  - ").append(sub.getNombreTratamiento())
              .append(" [").append(sub.getTipoRegistro()).append("]: $").append(sub.costoFijo());
        }
        return sb.toString();
    }

    private static String encode(String valor) {
        String seguro = valor == null ? "" : valor;
        return Base64.getUrlEncoder().withoutPadding().encodeToString(seguro.getBytes(StandardCharsets.UTF_8));
    }
}
