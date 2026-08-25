package Veterinaria.gui;

import Veterinaria.Dueño;
import Veterinaria.Mascota;
import Veterinaria.Tratamiento;
import Veterinaria.servicios.ClinicaVeterinariaService;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.Map;

public class ConsultasPanel extends JPanel implements RefreshablePanel {
    private final ClinicaVeterinariaService service;
    private final JComboBox<Mascota> mascotaCostoCombo;
    private final JComboBox<Tratamiento> tratamientoCostoCombo;
    private final JComboBox<Dueño> duenoResumenCombo;
    private final JTextArea resultado;
    private final DefaultListModel<Tratamiento> seguimientoModel;

    public ConsultasPanel(ClinicaVeterinariaService service) {
        this.service = service;
        this.mascotaCostoCombo = new JComboBox<>();
        this.tratamientoCostoCombo = new JComboBox<>();
        this.duenoResumenCombo = new JComboBox<>();
        this.resultado = new JTextArea();
        this.seguimientoModel = new DefaultListModel<>();

        setLayout(new BorderLayout(12, 12));
        add(acciones(), BorderLayout.NORTH);
        resultado.setEditable(false);
        add(new JScrollPane(resultado), BorderLayout.CENTER);
        add(seguimientos(), BorderLayout.SOUTH);
        refreshData();
    }

    private JPanel acciones() {
        JPanel panel = new JPanel(new GridLayout(2, 1));

        JPanel costoPanel = new JPanel();
        JButton calcularCosto = new JButton("Calcular costo total");
        calcularCosto.addActionListener(e -> calcularCostoTotal());
        costoPanel.add(new JLabel("Mascota"));
        costoPanel.add(mascotaCostoCombo);
        costoPanel.add(new JLabel("Tratamiento"));
        costoPanel.add(tratamientoCostoCombo);
        costoPanel.add(calcularCosto);

        JPanel resumenPanel = new JPanel();
        JButton resumen = new JButton("Resumen tratamientos");
        resumen.addActionListener(e -> mostrarResumen());
        resumenPanel.add(new JLabel("Dueno"));
        resumenPanel.add(duenoResumenCombo);
        resumenPanel.add(resumen);

        panel.add(costoPanel);
        panel.add(resumenPanel);
        return panel;
    }

    private JScrollPane seguimientos() {
        JList<Tratamiento> lista = new JList<>(seguimientoModel);
        return new JScrollPane(lista);
    }

    private void calcularCostoTotal() {
        try {
            Mascota mascota = (Mascota) mascotaCostoCombo.getSelectedItem();
            Tratamiento tratamiento = (Tratamiento) tratamientoCostoCombo.getSelectedItem();
            Double costo = service.calcularCostoTotal(mascota, tratamiento);
            resultado.setText("Costo total de consulta: " + costo);
        } catch (Exception e) {
            MainFrame.mostrarError(e);
        }
    }

    private void mostrarResumen() {
        try {
            Dueño dueno = (Dueño) duenoResumenCombo.getSelectedItem();
            Map<Mascota, Integer> resumen = service.resumenTratamientosPorMascota(dueno);
            StringBuilder builder = new StringBuilder();
            builder.append("Resumen de tratamientos por mascota\n");
            for (Map.Entry<Mascota, Integer> entry : resumen.entrySet()) {
                builder.append(entry.getKey().getNombre()).append(": ").append(entry.getValue()).append("\n");
            }
            resultado.setText(builder.toString());
        } catch (Exception e) {
            MainFrame.mostrarError(e);
        }
    }

    @Override
    public void refreshData() {
        cargarCombo(mascotaCostoCombo, service.getMascotas());
        cargarCombo(tratamientoCostoCombo, service.getTratamientos());
        cargarCombo(duenoResumenCombo, service.getDuenos());
        seguimientoModel.clear();
        for (Tratamiento tratamiento : service.tratamientosQueRequierenSeguimiento()) {
            seguimientoModel.addElement(tratamiento);
        }
    }

    private <T> void cargarCombo(JComboBox<T> combo, java.util.List<T> valores) {
        DefaultComboBoxModel<T> model = new DefaultComboBoxModel<>();
        for (T valor : valores) {
            model.addElement(valor);
        }
        combo.setModel(model);
    }
}
