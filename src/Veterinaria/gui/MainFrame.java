package Veterinaria.gui;

import Veterinaria.controladores.ConsultaController;
import Veterinaria.controladores.DuenoController;
import Veterinaria.controladores.MascotaController;
import Veterinaria.controladores.TratamientoController;
import Veterinaria.facade.ClinicaVeterinariaFacade;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame {
    private final List<RefreshablePanel> panels;

    public MainFrame(ClinicaVeterinariaFacade facade) {
        this(facade.getDuenoController(),
             facade.getMascotaController(),
             facade.getTratamientoController(),
             facade.getConsultaController());
    }

    public MainFrame(DuenoController duenoController, MascotaController mascotaController,
                     TratamientoController tratamientoController, ConsultaController consultaController) {
        super("PetCare Plus - Clinica Veterinaria");
        this.panels = new ArrayList<>();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIconImage(PetCareLogo.crearIconoVentana());
        setSize(1080, 740);
        setMinimumSize(new java.awt.Dimension(920, 620));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(UiTheme.BG_MAIN);

        add(crearCabecera(), BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(UiTheme.FONT_BOLD);
        tabs.setBackground(UiTheme.BG_MAIN);
        tabs.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));

        DuenosPanel duenosPanel = new DuenosPanel(duenoController, this::refreshAll);
        MascotasPanel mascotasPanel = new MascotasPanel(mascotaController, consultaController, this::refreshAll);
        TratamientosPanel tratamientosPanel = new TratamientosPanel(tratamientoController, this::refreshAll);
        ConsultasPanel consultasPanel = new ConsultasPanel(consultaController);

        panels.add(duenosPanel);
        panels.add(mascotasPanel);
        panels.add(tratamientosPanel);
        panels.add(consultasPanel);

        tabs.addTab("  1. Duenos  ", duenosPanel);
        tabs.addTab("  2. Mascotas  ", mascotasPanel);
        tabs.addTab("  3. Tratamientos  ", tratamientosPanel);
        tabs.addTab("  4. Consultas y Costos  ", consultasPanel);
        add(tabs, BorderLayout.CENTER);
    }

    private JPanel crearCabecera() {
        JPanel header = new JPanel(new BorderLayout(16, 0));
        header.setBackground(UiTheme.PRIMARY);
        header.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Logo vectorial en la esquina superior izquierda con emblema y nombre de la veterinaria
        PetCareLogo logo = new PetCareLogo();

        JLabel subtitulo = new JLabel("Sistema de Gestion Integral  |  Pacientes, Tratamientos y Consultas");
        subtitulo.setFont(UiTheme.FONT_BOLD);
        subtitulo.setForeground(new Color(220, 242, 245));

        header.add(logo, BorderLayout.WEST);
        header.add(subtitulo, BorderLayout.EAST);
        return header;
    }

    public void refreshAll() {
        for (RefreshablePanel panel : panels) {
            panel.refreshData();
        }
    }

    static void mostrarError(Exception e) {
        JOptionPane.showMessageDialog(null, e.getMessage(), "Operacion no valida", JOptionPane.ERROR_MESSAGE);
    }
}
