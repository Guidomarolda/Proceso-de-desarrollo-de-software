package Veterinaria.gui;

import Veterinaria.ClinicaVeterinaria;
import Veterinaria.controladores.DuenoController;
import Veterinaria.controladores.MascotaController;
import Veterinaria.controladores.TratamientoController;
import Veterinaria.controladores.ConsultaController;
import Veterinaria.servicios.ConsultaVeterinariaService;
import Veterinaria.persistencia.ArchivoClinicaRepository;
import Veterinaria.servicios.ClinicaVeterinariaService;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class PetCarePlusApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }

            ClinicaVeterinaria clinica = new ClinicaVeterinaria();
            ClinicaVeterinariaService service =
                    new ClinicaVeterinariaService(new ArchivoClinicaRepository(), clinica);
            service.cargarDatos();
            ConsultaController consultas =
                    new ConsultaController(new ConsultaVeterinariaService(clinica));
            MainFrame frame = new MainFrame(new DuenoController(service),
                    new MascotaController(service), new TratamientoController(service), consultas);
            frame.setVisible(true);
        });
    }
}
