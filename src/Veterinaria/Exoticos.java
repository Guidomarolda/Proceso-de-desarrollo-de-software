package Veterinaria;

import java.util.UUID;

public class Exoticos extends  Mascota {
    private NivelCuidado nivelCuidado;

    public Exoticos(Tratamiento tratamiento, Double peso, int edad, String nombre, NivelCuidado nivelCuidado) {
        this(UUID.randomUUID().toString(), tratamiento, peso, edad, nombre, nivelCuidado);
    }

    public Exoticos(String id, Double peso, int edad, String nombre, NivelCuidado nivelCuidado) {
        this(id, null, peso, edad, nombre, nivelCuidado);
    }

    private Exoticos(String id, Tratamiento tratamiento, Double peso, int edad, String nombre, NivelCuidado nivelCuidado) {
        super(id, tratamiento, peso, edad, nombre);
        setNivelCuidado(nivelCuidado);
    }

    public NivelCuidado getNivelCuidado() {
        return nivelCuidado;
    }

    public void setNivelCuidado(NivelCuidado nivelCuidado) {
        if (nivelCuidado == null) {
            throw new IllegalArgumentException("El nivel de cuidado es obligatorio.");
        }
        this.nivelCuidado = nivelCuidado;
    }

    @Override
    public Double costoBaseAtencion() {
        Double costoBase = 800.0 + getEdad()*100.0;
        return  costoBase;
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
