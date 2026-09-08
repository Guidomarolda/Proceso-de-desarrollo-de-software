package Veterinaria;

import java.util.UUID;

public class Gato extends  Mascota {
    private Boolean estaVacunado;

    public Gato(Tratamiento tratamiento, Double peso, int edad, String nombre, Boolean estaVacunado) {
        this(UUID.randomUUID().toString(), tratamiento, peso, edad, nombre, estaVacunado);
    }

    public Gato(String id, Double peso, int edad, String nombre, Boolean estaVacunado) {
        this(id, null, peso, edad, nombre, estaVacunado);
    }

    private Gato(String id, Tratamiento tratamiento, Double peso, int edad, String nombre, Boolean estaVacunado) {
        super(id, tratamiento, peso, edad, nombre);
        setEstaVacunado(estaVacunado);
    }

    public Boolean getEstaVacunado() {
        return estaVacunado;
    }

    public void setEstaVacunado(Boolean estaVacunado) {
        if (estaVacunado == null) {
            throw new IllegalArgumentException("Debe indicar si el gato esta vacunado.");
        }
        this.estaVacunado = estaVacunado;
    }

    @Override
    public Double costoBaseAtencion() {
        Double costoBase= 0.0;
        if(this.estaVacunado == true){
            costoBase = 400.0 + getEdad()*30.0;
            return  costoBase;
        } else { //se cobra mas por no estar vacunado
            costoBase = (400.0 + getEdad()*30.0) +200.0;
            return costoBase;
        }
    }

    @Override
    public int compareTo(Mascota o) {
        return Double.compare(this.costoBaseAtencion(), o.costoBaseAtencion());
    }
    @Override
    public Boolean esAptaParaProcedimientoEspecial(Mascota mascota) {
        if(mascota.getEdad()<10 && mascota.getPeso()<30){
            return true;
        } else {
            return false;
        }
    }
}

