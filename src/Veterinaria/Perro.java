package Veterinaria;

import java.util.UUID;

public class Perro extends Mascota{
    private String raza;

    public Perro(Tratamiento tratamiento, Double peso, int edad, String nombre, String raza) {
        this(UUID.randomUUID().toString(), tratamiento, peso, edad, nombre, raza);
    }

    public Perro(String id, Double peso, int edad, String nombre, String raza) {
        this(id, null, peso, edad, nombre, raza);
    }

    private Perro(String id, Tratamiento tratamiento, Double peso, int edad, String nombre, String raza) {
        super(id, tratamiento, peso, edad, nombre);
        setRaza(raza);
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = validarTexto(raza, "raza");
    }

    @Override
    public Double costoBaseAtencion() {
        Double costoBase = 500.0 + getEdad()*50.0;
        return costoBase;
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
