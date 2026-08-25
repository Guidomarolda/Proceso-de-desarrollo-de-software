package Veterinaria;

public class AveFactory extends  MascotaFactory {
    private Tipo tipo;

    public AveFactory(Tipo tipo) {
        this.tipo = tipo;
    }

    @Override
    protected Mascota fabricar(String nombre, int edad, Double peso, Tratamiento tratamiento) {
        return new Ave(tratamiento,peso,edad,nombre,tipo);
    }
}
