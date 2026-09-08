package Veterinaria;

import Veterinaria.gui.PetCarePlusApp;

import java.util.List;

public class Main {

    //metodo 1 del enunciado
    public static Double calcularCostoTotal(Mascota mascota, Tratamiento tratamiento){
        Double costoTotalCalculado = mascota.costoBaseAtencion() + tratamiento.costoFijo();
        return costoTotalCalculado;
        
    }
    
    public static long resumenTratamientosPorMascota(Dueño dueno, List<Tratamiento> tratamientos) {
        if (dueno == null) return 0;
        return tratamientos.stream()
                .filter(t -> dueno.getMascotas().stream()
                        .anyMatch(mascota -> mascota.getTratamientos().contains(t)))
                .count();
    }

    public static void main(String[] args) {
        PetCarePlusApp.main(args);
    }
}
