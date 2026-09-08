package Veterinaria.servicios;

import Veterinaria.Ave;
import Veterinaria.Cirujia;
import Veterinaria.Dueño;
import Veterinaria.Estado;
import Veterinaria.Exoticos;
import Veterinaria.Gato;
import Veterinaria.Mascota;
import Veterinaria.NivelComplejidad;
import Veterinaria.NivelCuidado;
import Veterinaria.Perro;
import Veterinaria.Tipo;
import Veterinaria.Tratamiento;
import Veterinaria.TratamientoCurativo;
import Veterinaria.TratamientoPreventivo;
import Veterinaria.persistencia.ArchivoClinicaRepository;
import Veterinaria.persistencia.DatosClinica;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ClinicaVeterinariaService {
    private final ArchivoClinicaRepository repository;
    private final List<Dueño> duenos;

    public ClinicaVeterinariaService(ArchivoClinicaRepository repository) {
        this.repository = repository;
        this.duenos = new ArrayList<>();
    }

    public void cargarDatos() {
        duenos.clear();
        duenos.addAll(repository.cargar().getDuenos());
    }

    public void guardarDatos() {
        repository.guardar(new DatosClinica(duenos));
    }

    public Dueño registrarDueno(String nombre, String apellido, String telefono, String correo) {
        Dueño dueno = new Dueño(nombre, apellido, telefono, correo, (Mascota) null);
        duenos.add(dueno);
        guardarDatos();
        return dueno;
    }

    public Mascota registrarPerro(String duenoId, String nombre, int edad, Double peso, String raza) {
        Mascota mascota = new Perro((Tratamiento) null, peso, edad, nombre, raza);
        agregarMascotaADueno(duenoId, mascota);
        return mascota;
    }

    public Mascota registrarGato(String duenoId, String nombre, int edad, Double peso, Boolean vacunado) {
        Mascota mascota = new Gato((Tratamiento) null, peso, edad, nombre, vacunado);
        agregarMascotaADueno(duenoId, mascota);
        return mascota;
    }

    public Mascota registrarAve(String duenoId, String nombre, int edad, Double peso, Tipo tipo) {
        Mascota mascota = new Ave((Tratamiento) null, peso, edad, nombre, tipo);
        agregarMascotaADueno(duenoId, mascota);
        return mascota;
    }

    public Mascota registrarExotico(String duenoId, String nombre, int edad, Double peso, NivelCuidado nivelCuidado) {
        Mascota mascota = new Exoticos((Tratamiento) null, peso, edad, nombre, nivelCuidado);
        agregarMascotaADueno(duenoId, mascota);
        return mascota;
    }

    public Tratamiento registrarPreventivo(String mascotaId, String nombre, String fechaInicio, Estado estado, int frecuencia) {
        Tratamiento tratamiento = new TratamientoPreventivo(nombre, fechaInicio, estado, frecuencia);
        agregarTratamientoAMascota(mascotaId, tratamiento);
        return tratamiento;
    }

    public Tratamiento registrarCurativo(String mascotaId, String nombre, String fechaInicio, Estado estado, int duracion, String diagnostico) {
        Tratamiento tratamiento = new TratamientoCurativo(nombre, fechaInicio, estado, duracion, diagnostico);
        agregarTratamientoAMascota(mascotaId, tratamiento);
        return tratamiento;
    }

    public Tratamiento registrarCirujia(String mascotaId, String nombre, String fechaInicio, Estado estado, String tipoCirujia, NivelComplejidad nivelComplejidad) {
        Tratamiento tratamiento = new Cirujia(nombre, fechaInicio, estado, tipoCirujia, nivelComplejidad);
        agregarTratamientoAMascota(mascotaId, tratamiento);
        return tratamiento;
    }

    public List<Dueño> getDuenos() {
        return Collections.unmodifiableList(duenos);
    }

    public List<Mascota> getMascotas() {
        List<Mascota> mascotas = new ArrayList<>();
        for (Dueño dueno : duenos) {
            mascotas.addAll(dueno.getMascotas());
        }
        return Collections.unmodifiableList(mascotas);
    }

    public List<Tratamiento> getTratamientos() {
        List<Tratamiento> tratamientos = new ArrayList<>();
        for (Mascota mascota : getMascotas()) {
            tratamientos.addAll(mascota.getTratamientos());
        }
        return Collections.unmodifiableList(tratamientos);
    }

    public Double calcularCostoTotal(Mascota mascota, Tratamiento tratamiento) {
        validarNoNulo(mascota, "mascota");
        validarNoNulo(tratamiento, "tratamiento");
        return mascota.costoBaseAtencion() + tratamiento.costoFijo();
    }

    public int compararMascotas(Mascota primera, Mascota segunda) {
        validarNoNulo(primera, "primera mascota");
        validarNoNulo(segunda, "segunda mascota");
        return primera.compareTo(segunda);
    }

    public List<Tratamiento> tratamientosQueRequierenSeguimiento() {
        List<Tratamiento> tratamientos = new ArrayList<>();
        for (Tratamiento tratamiento : getTratamientos()) {
            if (tratamiento.requiereAtencionUrgente()) {
                tratamientos.add(tratamiento);
            }
        }
        return Collections.unmodifiableList(tratamientos);
    }

    public Map<Mascota, Integer> resumenTratamientosPorMascota(Dueño dueno) {
        validarNoNulo(dueno, "dueno");
        Map<Mascota, Integer> resumen = new LinkedHashMap<>();
        for (Mascota mascota : dueno.getMascotas()) {
            resumen.put(mascota, mascota.getTratamientos().size());
        }
        return resumen;
    }

    public Dueño buscarDuenoPorId(String id) {
        for (Dueño dueno : duenos) {
            if (dueno.getId().equals(id)) {
                return dueno;
            }
        }
        throw new IllegalArgumentException("No existe un dueño con el id indicado.");
    }

    public Mascota buscarMascotaPorId(String id) {
        for (Mascota mascota : getMascotas()) {
            if (mascota.getId().equals(id)) {
                return mascota;
            }
        }
        throw new IllegalArgumentException("No existe una mascota con el id indicado.");
    }

    private void agregarMascotaADueno(String duenoId, Mascota mascota) {
        Dueño dueno = buscarDuenoPorId(duenoId);
        dueno.agregarMascota(mascota);
        guardarDatos();
    }

    private void agregarTratamientoAMascota(String mascotaId, Tratamiento tratamiento) {
        Mascota mascota = buscarMascotaPorId(mascotaId);
        mascota.agregarTratamiento(tratamiento);
        guardarDatos();
    }

    private void validarNoNulo(Object valor, String campo) {
        if (valor == null) {
            throw new IllegalArgumentException("Debe seleccionar " + campo + ".");
        }
    }
}
