package Veterinaria;

public class TratamientoCurativo extends Tratamiento{
    private String diagnostico;
    private Integer duracion;

    public TratamientoCurativo(String nombreTratamiento, int fechaInicio, Estado estado, Integer duracion, String diagnostico) {
        this(nombreTratamiento, String.valueOf(fechaInicio), estado, duracion, diagnostico);
    }

    public TratamientoCurativo(String id, String nombreTratamiento, String fechaInicio, Estado estado, Integer duracion, String diagnostico) {
        super(id, nombreTratamiento, fechaInicio, estado);
        setDuracion(duracion);
        setDiagnostico(diagnostico);
    }

    public TratamientoCurativo(String nombreTratamiento, String fechaInicio, Estado estado, Integer duracion, String diagnostico) {
        this(java.util.UUID.randomUUID().toString(), nombreTratamiento, fechaInicio, estado, duracion, diagnostico);
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = validarTexto(diagnostico, "diagnostico");
    }

    public Integer getDuracion() {
        return duracion;
    }

    public void setDuracion(Integer duracion) {
        if (duracion == null || duracion <= 0) {
            throw new IllegalArgumentException("La duracion debe ser mayor a cero.");
        }
        this.duracion = duracion;
    }

    @Override
    public Double costoFijo() {
        Double costoFijo = 1000.0 + this.duracion *50.0;
        return costoFijo;
    }

    @Override
    public Boolean requiereAtencionUrgente() {
        if(this.duracion>30){
            return true;
        } else {
            return false;
        }
    }


}
