package Veterinaria.gui;

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

            ClinicaVeterinariaService service = new ClinicaVeterinariaService(new ArchivoClinicaRepository());
            service.cargarDatos();
            MainFrame frame = new MainFrame(service);
            frame.setVisible(true);
        });
    }
}
