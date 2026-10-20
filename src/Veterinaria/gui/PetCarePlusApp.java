package Veterinaria.gui;

import Veterinaria.facade.ClinicaVeterinariaFacade;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Punto de arranque de la interfaz grafica que utiliza ClinicaVeterinariaFacade
 * para desacoplar el inicio de la aplicacion de la configuracion de los subsistemas internos.
 */
public class PetCarePlusApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
            }

            ClinicaVeterinariaFacade facade = new ClinicaVeterinariaFacade();
            facade.inicializarSistema();

            MainFrame frame = new MainFrame(facade);
            frame.setVisible(true);
        });
    }
}
