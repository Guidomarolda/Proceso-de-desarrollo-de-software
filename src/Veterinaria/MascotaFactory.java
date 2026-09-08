package Veterinaria;

/**
 * Factory consolidado para crear todas las mascotas.
 * Reemplaza los factories individuales (PerroFactory, GatoFactory, AveFactory, ExoticoFactory)
 * Soluciona el code smell "Lazy Class"
 */
public class MascotaFactory {
    
    public static Mascota crearPerro(String nombre, int edad, Double peso, 
                                     Tratamiento tratamiento, String raza) {
        return new Perro(tratamiento, peso, edad, nombre, raza);
    }
    
    public static Mascota crearGato(String nombre, int edad, Double peso, 
                                    Tratamiento tratamiento, Boolean estaVacunado) {
        return new Gato(tratamiento, peso, edad, nombre, estaVacunado);
    }
    
    public static Mascota crearAve(String nombre, int edad, Double peso, 
                                   Tratamiento tratamiento, Tipo tipo) {
        return new Ave(tratamiento, peso, edad, nombre, tipo);
    }
    
    public static Mascota crearExotico(String nombre, int edad, Double peso, 
                                       Tratamiento tratamiento, NivelCuidado nivelCuidado) {
        return new Exoticos(tratamiento, peso, edad, nombre, nivelCuidado);
    }
}
