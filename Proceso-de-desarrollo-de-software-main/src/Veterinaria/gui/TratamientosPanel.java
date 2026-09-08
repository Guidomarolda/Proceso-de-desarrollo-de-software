package Veterinaria.gui;

import Veterinaria.Cirujia;
import Veterinaria.Estado;
import Veterinaria.Mascota;
import Veterinaria.NivelComplejidad;
import Veterinaria.Tratamiento;
import Veterinaria.TratamientoCurativo;
import Veterinaria.TratamientoPreventivo;
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
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public class TratamientosPanel extends JPanel implements RefreshablePanel {
    private final ClinicaVeterinariaService service;
    private final Runnable onChange;
    private final DefaultComboBoxModel<Mascota> mascotasModel;
    private final JComboBox<Mascota> mascotasCombo;
    private final JComboBox<String> tipoCombo;
    private final JTextField nombreField;
    private final JTextField fechaField;
    private final JComboBox<Estado> estadoCombo;
    private final JTextField frecuenciaField;
    private final JTextField duracionField;
    private final JTextField diagnosticoField;
    private final JTextField tipoCirujiaField;
    private final JComboBox<NivelComplejidad> complejidadCombo;
    private final JPanel extrasPanel;
    private final CardLayout extrasLayout;
    private final DefaultListModel<Tratamiento> tratamientosModel;
    private final JList<Tratamiento> tratamientosList;
    private final JTextArea detalle;

    public TratamientosPanel(ClinicaVeterinariaService service, Runnable onChange) {
        this.service = service;
        this.onChange = onChange;
        this.mascotasModel = new DefaultComboBoxModel<>();
        this.mascotasCombo = new JComboBox<>(mascotasModel);
        this.tipoCombo = new JComboBox<>(new String[]{"Preventivo", "Curativo", "Cirujia"});
        this.nombreField = new JTextField(14);
        this.fechaField = new JTextField(10);
        this.estadoCombo = new JComboBox<>(Estado.values());
        this.frecuenciaField = new JTextField(5);
        this.duracionField = new JTextField(5);
        this.diagnosticoField = new JTextField(18);
        this.tipoCirujiaField = new JTextField(14);
        this.complejidadCombo = new JComboBox<>(NivelComplejidad.values());
        this.extrasLayout = new CardLayout();
        this.extrasPanel = new JPanel(extrasLayout);
        this.tratamientosModel = new DefaultListModel<>();
        this.tratamientosList = new JList<>(tratamientosModel);
        this.detalle = new JTextArea();

        setLayout(new BorderLayout(12, 12));
        add(formulario(), BorderLayout.NORTH);
        add(new JScrollPane(tratamientosList), BorderLayout.CENTER);
        detalle.setEditable(false);
        add(new JScrollPane(detalle), BorderLayout.EAST);

        tipoCombo.addActionListener(e -> extrasLayout.show(extrasPanel, (String) tipoCombo.getSelectedItem()));
        tratamientosList.addListSelectionListener(e -> mostrarDetalle(tratamientosList.getSelectedValue()));
        refreshData();
    }

    private JPanel formulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addComponente(panel, gbc, 0, 0, "Mascota", mascotasCombo);
        addComponente(panel, gbc, 2, 0, "Tipo", tipoCombo);
        addComponente(panel, gbc, 4, 0, "Nombre", nombreField);
        addComponente(panel, gbc, 6, 0, "Fecha", fechaField);
        addComponente(panel, gbc, 8, 0, "Estado", estadoCombo);

        extrasPanel.add(extraPreventivo(), "Preventivo");
        extrasPanel.add(extraCurativo(), "Curativo");
        extrasPanel.add(extraCirujia(), "Cirujia");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 8;
        panel.add(extrasPanel, gbc);

        JButton registrar = new JButton("Registrar tratamiento");
        registrar.addActionListener(e -> registrarTratamiento());
        gbc.gridx = 8;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        panel.add(registrar, gbc);
        return panel;
    }

    private JPanel extraPreventivo() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Frecuencia meses"));
        panel.add(frecuenciaField);
        return panel;
    }

    private JPanel extraCurativo() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Duracion dias"));
        panel.add(duracionField);
        panel.add(new JLabel("Diagnostico"));
        panel.add(diagnosticoField);
        return panel;
    }

    private JPanel extraCirujia() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Tipo cirujia"));
        panel.add(tipoCirujiaField);
        panel.add(new JLabel("Complejidad"));
        panel.add(complejidadCombo);
        return panel;
    }

    private void addComponente(JPanel panel, GridBagConstraints gbc, int x, int y, String etiqueta, java.awt.Component componente) {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = 1;
        panel.add(new JLabel(etiqueta), gbc);
        gbc.gridx = x + 1;
        panel.add(componente, gbc);
    }

    private void registrarTratamiento() {
        try {
            Mascota mascota = (Mascota) mascotasCombo.getSelectedItem();
            if (mascota == null) {
                throw new IllegalArgumentException("Debe registrar o seleccionar una mascota.");
            }
            String tipo = (String) tipoCombo.getSelectedItem();
            Estado estado = (Estado) estadoCombo.getSelectedItem();
            if ("Preventivo".equals(tipo)) {
                service.registrarPreventivo(mascota.getId(), nombreField.getText(), fechaField.getText(), estado,
                        Integer.parseInt(frecuenciaField.getText()));
            } else if ("Curativo".equals(tipo)) {
                service.registrarCurativo(mascota.getId(), nombreField.getText(), fechaField.getText(), estado,
                        Integer.parseInt(duracionField.getText()), diagnosticoField.getText());
            } else {
                service.registrarCirujia(mascota.getId(), nombreField.getText(), fechaField.getText(), estado,
                        tipoCirujiaField.getText(), (NivelComplejidad) complejidadCombo.getSelectedItem());
            }
            limpiar();
            onChange.run();
        } catch (NumberFormatException e) {
            MainFrame.mostrarError(new IllegalArgumentException("Los campos numericos deben tener valores validos."));
        } catch (Exception e) {
            MainFrame.mostrarError(e);
        }
    }

    private void limpiar() {
        nombreField.setText("");
        fechaField.setText("");
        frecuenciaField.setText("");
        duracionField.setText("");
        diagnosticoField.setText("");
        tipoCirujiaField.setText("");
    }

    private void mostrarDetalle(Tratamiento tratamiento) {
        if (tratamiento == null) {
            detalle.setText("");
            return;
        }
        StringBuilder builder = new StringBuilder();
        builder.append("Nombre: ").append(tratamiento.getNombreTratamiento()).append("\n");
        builder.append("Fecha inicio: ").append(tratamiento.getFechaInicio()).append("\n");
        builder.append("Estado: ").append(tratamiento.getEstado()).append("\n");
        builder.append("Costo: ").append(tratamiento.costoFijo()).append("\n");
        builder.append("Requiere seguimiento: ").append(tratamiento.requiereAtencionUrgente() ? "Si" : "No").append("\n");
        if (tratamiento instanceof TratamientoPreventivo) {
            builder.append("Frecuencia: ").append(((TratamientoPreventivo) tratamiento).getFrecuenciaRecomendada()).append(" meses\n");
        }
        if (tratamiento instanceof TratamientoCurativo) {
            builder.append("Duracion: ").append(((TratamientoCurativo) tratamiento).getDuracion()).append(" dias\n");
            builder.append("Diagnostico: ").append(((TratamientoCurativo) tratamiento).getDiagnostico()).append("\n");
        }
        if (tratamiento instanceof Cirujia) {
            builder.append("Tipo cirujia: ").append(((Cirujia) tratamiento).getTipoCirujia()).append("\n");
            builder.append("Complejidad: ").append(((Cirujia) tratamiento).getNivelComplejidad()).append("\n");
        }
        detalle.setText(builder.toString());
    }

    @Override
    public void refreshData() {
        mascotasModel.removeAllElements();
        for (Mascota mascota : service.getMascotas()) {
            mascotasModel.addElement(mascota);
        }
        tratamientosModel.clear();
        for (Tratamiento tratamiento : service.getTratamientos()) {
            tratamientosModel.addElement(tratamiento);
        }
    }
}
