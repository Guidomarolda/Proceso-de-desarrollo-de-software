package Veterinaria.gui;

import Veterinaria.controladores.ConsultaController;
import Veterinaria.modelo.Dueño;
import Veterinaria.modelo.Mascota;
import Veterinaria.modelo.Tratamiento;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.Map;

public class ConsultasPanel extends JPanel implements RefreshablePanel {
    private final ConsultaController controller;
    private final JComboBox<Mascota> mascotaCostoCombo;
    private final JComboBox<Tratamiento> tratamientoCostoCombo;
    private final JComboBox<Dueño> duenoResumenCombo;
    private final JTextArea resultado;
    private final DefaultListModel<String> seguimientoModel;

    public ConsultasPanel(ConsultaController controller) {
        this.controller = controller;
        this.mascotaCostoCombo = new JComboBox<>();
        UiTheme.estilizarCombo(mascotaCostoCombo);

        this.tratamientoCostoCombo = new JComboBox<>();
        UiTheme.estilizarCombo(tratamientoCostoCombo);

        this.duenoResumenCombo = new JComboBox<>();
        UiTheme.estilizarCombo(duenoResumenCombo);

        this.resultado = UiTheme.crearAreaDetalle();
        this.seguimientoModel = new DefaultListModel<>();

        setLayout(new BorderLayout(12, 12));
        UiTheme.aplicarMargenPanelPrincipal(this);

        add(acciones(), BorderLayout.NORTH);
        add(panelCentral(), BorderLayout.CENTER);
        refreshData();
    }

    private JPanel acciones() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 0, 8));
        UiTheme.aplicarFondoTarjeta(panel, "Consultas de Costos y Reportes por Dueno");

        JPanel costoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 4));
        costoPanel.setOpaque(false);
        JButton calcularCosto = UiTheme.crearBotonPrimario("Calcular Costo Total");
        calcularCosto.addActionListener(e -> calcularCostoTotal());
        costoPanel.add(UiTheme.crearEtiqueta("Mascota:"));
        costoPanel.add(mascotaCostoCombo);
        costoPanel.add(UiTheme.crearEtiqueta("Tratamiento:"));
        costoPanel.add(tratamientoCostoCombo);
        costoPanel.add(calcularCosto);

        JPanel resumenPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 4));
        resumenPanel.setOpaque(false);
        JButton resumen = UiTheme.crearBotonSecundario("Ver Resumen de Tratamientos");
        resumen.addActionListener(e -> mostrarResumen());
        resumenPanel.add(UiTheme.crearEtiqueta("Dueno:"));
        resumenPanel.add(duenoResumenCombo);
        resumenPanel.add(resumen);

        panel.add(costoPanel);
        panel.add(resumenPanel);
        return panel;
    }

    private JSplitPane panelCentral() {
        JPanel resultadoPanel = new JPanel(new BorderLayout());
        UiTheme.aplicarFondoTarjeta(resultadoPanel, "Resultado de la Consulta");
        resultadoPanel.add(new JScrollPane(resultado), BorderLayout.CENTER);

        JList<String> lista = new JList<>(seguimientoModel);
        UiTheme.estilizarLista(lista);
        JPanel seguimientoPanel = new JPanel(new BorderLayout());
        UiTheme.aplicarFondoTarjeta(seguimientoPanel, "Tratamientos que Requieren Seguimiento Urgente");
        seguimientoPanel.add(new JScrollPane(lista), BorderLayout.CENTER);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, resultadoPanel, seguimientoPanel);
        split.setResizeWeight(0.5);
        split.setBorder(null);
        split.setOpaque(false);
        return split;
    }

    private void calcularCostoTotal() {
        try {
            Mascota mascota = (Mascota) mascotaCostoCombo.getSelectedItem();
            Tratamiento tratamiento = (Tratamiento) tratamientoCostoCombo.getSelectedItem();
            Double costo = controller.calcularCostoTotal(mascota, tratamiento);
            StringBuilder sb = new StringBuilder();
            sb.append("=== CALCULO DE COSTO TOTAL DE CONSULTA ===\n\n");
            sb.append("Mascota: ").append(mascota.getNombre()).append(" (Costo base: $").append(mascota.costoBaseAtencion()).append(")\n");
            sb.append("Tratamiento: ").append(tratamiento.getNombreTratamiento()).append(" (Costo: $").append(tratamiento.costoFijo()).append(")\n");
            sb.append("------------------------------------------\n");
            sb.append("COSTO TOTAL: $").append(costo).append("\n");
            resultado.setText(sb.toString());
        } catch (Exception e) {
            MainFrame.mostrarError(e);
        }
    }

    private void mostrarResumen() {
        try {
            Dueño dueno = (Dueño) duenoResumenCombo.getSelectedItem();
            Map<Mascota, Integer> resumen = controller.resumenTratamientosPorMascota(dueno);
            StringBuilder builder = new StringBuilder();
            builder.append("=== RESUMEN DE TRATAMIENTOS POR MASCOTA ===\n");
            builder.append("Dueno: ").append(dueno.getNombreCompleto()).append("\n\n");
            for (Map.Entry<Mascota, Integer> entry : resumen.entrySet()) {
                builder.append(" - ").append(entry.getKey().getNombre()).append(": ")
                       .append(entry.getValue()).append(" tratamiento(s)\n");
            }
            resultado.setText(builder.toString());
        } catch (Exception e) {
            MainFrame.mostrarError(e);
        }
    }

    @Override
    public void refreshData() {
        cargarCombo(mascotaCostoCombo, controller.getMascotas());
        cargarCombo(tratamientoCostoCombo, controller.getTratamientos());
        cargarCombo(duenoResumenCombo, controller.getDuenos());
        seguimientoModel.clear();
        java.util.List<Tratamiento> urgentes = controller.tratamientosQueRequierenSeguimiento();
        if (urgentes.isEmpty()) {
            seguimientoModel.addElement("No hay tratamientos con seguimiento urgente pendiente.");
        } else {
            for (Tratamiento tratamiento : urgentes) {
                seguimientoModel.addElement(tratamiento.toString());
            }
        }
        if (resultado.getText().trim().isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append("=== PANEL DE CONSULTAS Y REPORTES ===\n\n");
            sb.append("Duenos registrados:       ").append(controller.getDuenos().size()).append("\n");
            sb.append("Mascotas registradas:     ").append(controller.getMascotas().size()).append("\n");
            sb.append("Tratamientos registrados: ").append(controller.getTratamientos().size()).append("\n");
            sb.append("Seguimientos urgentes:    ").append(urgentes.size()).append("\n\n");
            sb.append("Utilice los selectores superiores para:\n");
            sb.append(" 1. Calcular el costo total de atencion (Mascota + Tratamiento/Plan).\n");
            sb.append(" 2. Ver el resumen de tratamientos agrupados por mascota de un dueno.\n");
            resultado.setText(sb.toString());
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
