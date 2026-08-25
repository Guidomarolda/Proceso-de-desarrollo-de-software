package Veterinaria;

public abstract class MascotaFactory {
//esta sera la fabrica encargada de crear todos los objetos(encapsulamiento)
    public final Mascota crear(String nombre, int edad, Double peso, Tratamiento tratamiento){
        return fabricar(nombre, edad, peso, tratamiento);
    }



        protected abstract Mascota fabricar(String nombre, int edad, Double peso, Tratamiento tratamiento);
}
