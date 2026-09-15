package Veterinaria.persistencia;

/** Contrato de persistencia independiente del almacenamiento utilizado. */
public interface ClinicaRepository {
    DatosClinica cargar();
    void guardar(DatosClinica datos);
}
