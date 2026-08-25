package Veterinaria;

public class ExoticoFactory  extends  MascotaFactory{
    private NivelCuidado nivelCuidado;

    public ExoticoFactory(NivelCuidado nivelCuidado) {
        this.nivelCuidado = nivelCuidado;
    }

    @Override
    protected Mascota fabricar(String nombre, int edad, Double peso, Tratamiento tratamiento) {
        return new Exoticos(tratamiento,peso,edad,nombre,nivelCuidado);
    }
}
