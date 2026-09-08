package Veterinaria;

public class TratamientoPreventivo extends  Tratamiento{
    private Integer frecuenciaRecomendada;

    public TratamientoPreventivo(String nombreTratamiento, int fechaInicio, Estado estado, Integer frecuenciaRecomendada) {
        this(nombreTratamiento, String.valueOf(fechaInicio), estado, frecuenciaRecomendada);
    }

    public TratamientoPreventivo(String id, String nombreTratamiento, String fechaInicio, Estado estado, Integer frecuenciaRecomendada) {
        super(id, nombreTratamiento, fechaInicio, estado);
        setFrecuenciaRecomendada(frecuenciaRecomendada);
    }

    public TratamientoPreventivo(String nombreTratamiento, String fechaInicio, Estado estado, Integer frecuenciaRecomendada) {
        this(java.util.UUID.randomUUID().toString(), nombreTratamiento, fechaInicio, estado, frecuenciaRecomendada);
    }

    public Integer getFrecuenciaRecomendada() {
        return frecuenciaRecomendada;
    }

    public void setFrecuenciaRecomendada(Integer frecuenciaRecomendada) {
        if (frecuenciaRecomendada == null || frecuenciaRecomendada <= 0) {
            throw new IllegalArgumentException("La frecuencia recomendada debe ser mayor a cero.");
        }
        this.frecuenciaRecomendada = frecuenciaRecomendada;
    }

    @Override
    public Double costoFijo() {
        Double costoFijo = 600.0;
        return  costoFijo;
    }

    @Override
    public Boolean requiereAtencionUrgente() {
        if(this.frecuenciaRecomendada<3){
            return true;
        } else {
            return false;
        }
    }
}
