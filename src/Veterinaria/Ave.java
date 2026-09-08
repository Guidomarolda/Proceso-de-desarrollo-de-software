package Veterinaria;

import java.util.UUID;

public class Ave extends Mascota {
private  Tipo tipo;

    public Ave(Tratamiento tratamiento, Double peso, int edad, String nombre, Tipo tipo) {
        this(UUID.randomUUID().toString(), tratamiento, peso, edad, nombre, tipo);
    }

    public Ave(String id, Double peso, int edad, String nombre, Tipo tipo) {
        this(id, null, peso, edad, nombre, tipo);
    }

    private Ave(String id, Tratamiento tratamiento, Double peso, int edad, String nombre, Tipo tipo) {
        super(id, tratamiento, peso, edad, nombre);
        setTipo(tipo);
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de ave es obligatorio.");
        }
        this.tipo = tipo;
    }

    @Override
    public Double costoBaseAtencion() {
        Double costoBase = 300.0 + getEdad()*20;
        return  costoBase;
    }

    @Override
    public Boolean esAptaParaProcedimientoEspecial(Mascota mascota) {
        if(mascota.getEdad()<10 && mascota.getPeso()<30){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int compareTo(Mascota o) {
        return Double.compare(this.costoBaseAtencion(), o.costoBaseAtencion());
    }
}
