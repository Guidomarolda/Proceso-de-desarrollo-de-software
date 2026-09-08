package Veterinaria.gui;

import Veterinaria.servicios.ClinicaVeterinariaService;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame {
    private final List<RefreshablePanel> panels;

    public MainFrame(ClinicaVeterinariaService service) {
        super("PetCare Plus");
        this.panels = new ArrayList<>();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(980, 680);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JTabbedPane tabs = new JTabbedPane();
        DuenosPanel duenosPanel = new DuenosPanel(service, this::refreshAll);
        MascotasPanel mascotasPanel = new MascotasPanel(service, this::refreshAll);
        TratamientosPanel tratamientosPanel = new TratamientosPanel(service, this::refreshAll);
        ConsultasPanel consultasPanel = new ConsultasPanel(service);

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
