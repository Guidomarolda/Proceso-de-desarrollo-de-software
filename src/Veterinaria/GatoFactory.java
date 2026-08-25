package Veterinaria;

public class GatoFactory extends MascotaFactory{
    private Boolean estaVacunado;

    public GatoFactory(Boolean estaVacunado) {
        this.estaVacunado = estaVacunado;
    }
    @Override
    protected Mascota fabricar(String nombre, int edad, Double peso, Tratamiento tratamiento) {
        return new Gato(tratamiento,peso,edad,nombre,estaVacunado);
    }

}
