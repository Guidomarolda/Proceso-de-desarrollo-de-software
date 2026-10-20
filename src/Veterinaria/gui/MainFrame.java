package Veterinaria.gui;

import Veterinaria.controladores.ConsultaController;
import Veterinaria.controladores.DuenoController;
import Veterinaria.controladores.MascotaController;
import Veterinaria.controladores.TratamientoController;
import Veterinaria.facade.ClinicaVeterinariaFacade;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
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
        super("PetCare Plus");
        this.panels = new ArrayList<>();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1020, 720);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JTabbedPane tabs = new JTabbedPane();
        DuenosPanel duenosPanel = new DuenosPanel(duenoController, this::refreshAll);
        MascotasPanel mascotasPanel = new MascotasPanel(mascotaController, consultaController, this::refreshAll);
        TratamientosPanel tratamientosPanel = new TratamientosPanel(tratamientoController, this::refreshAll);
        ConsultasPanel consultasPanel = new ConsultasPanel(consultaController);

        panels.add(duenosPanel);
        panels.add(mascotasPanel);
        panels.add(tratamientosPanel);
        panels.add(consultasPanel);

        tabs.addTab("Duenos", duenosPanel);
        tabs.addTab("Mascotas", mascotasPanel);
        tabs.addTab("Tratamientos", tratamientosPanel);
        tabs.addTab("Consultas", consultasPanel);
        add(tabs, BorderLayout.CENTER);
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
