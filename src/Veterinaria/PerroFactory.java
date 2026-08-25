package Veterinaria;

public class PerroFactory extends MascotaFactory{
private String raza;

    public PerroFactory(String raza) {
        this.raza = raza;
    }

    @Override
    protected Mascota fabricar(String nombre, int edad, Double peso, Tratamiento tratamiento) {
        return new Perro(tratamiento,peso,edad,nombre,raza);
    }
}
