package Veterinaria.gui;

import Veterinaria.controladores.TratamientoController;
import Veterinaria.modelo.Estado;
import Veterinaria.modelo.Mascota;
import Veterinaria.modelo.NivelComplejidad;
import Veterinaria.modelo.Tratamiento;
import Veterinaria.modelo.TratamientoFactory;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

/**
 * Panel de presentacion (Vista MVC) para registro y visualizacion de Tratamientos,
 * soportando Planes Compuestos (Composite) y Adicionales Dinamicos (Decorator).
 */
public class TratamientosPanel extends JPanel implements RefreshablePanel {
    private final TratamientoController tratamientoController;
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
    private final DefaultListModel<Tratamiento> subTratamientosModel;
    private final JList<Tratamiento> subTratamientosList;
    private final JCheckBox urgenciaCheck;
    private final JCheckBox medicacionCheck;
    private final JCheckBox internacionCheck;
    private final JTextField diasInternacionField;
    private final JPanel extrasPanel;
    private final CardLayout extrasLayout;
    private final DefaultListModel<Tratamiento> tratamientosModel;
    private final JList<Tratamiento> tratamientosList;
    private final JTextArea detalle;

    public TratamientosPanel(TratamientoController tratamientoController, Runnable onChange) {
        this.tratamientoController = tratamientoController;
        this.onChange = onChange;
        this.mascotasModel = new DefaultComboBoxModel<>();
        this.mascotasCombo = new JComboBox<>(mascotasModel);
        this.tipoCombo = new JComboBox<>(new String[]{"Preventivo", "Curativo", "Cirujia", "Plan Compuesto"});
        this.nombreField = new JTextField(14);
        this.fechaField = new JTextField(10);
        this.estadoCombo = new JComboBox<>(Estado.values());
        this.frecuenciaField = new JTextField(5);
        this.duracionField = new JTextField(5);
        this.diagnosticoField = new JTextField(18);
        this.tipoCirujiaField = new JTextField(14);
        this.complejidadCombo = new JComboBox<>(NivelComplejidad.values());
        this.subTratamientosModel = new DefaultListModel<>();
        this.subTratamientosList = new JList<>(subTratamientosModel);
        this.subTratamientosList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        this.urgenciaCheck = new JCheckBox("Guardia Urgencia (+$800)");
        this.medicacionCheck = new JCheckBox("Medicacion Especial (+$500)");
        this.internacionCheck = new JCheckBox("Internacion ($400/dia) - Dias:");
        this.diasInternacionField = new JTextField(3);
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
        extrasPanel.add(extraPlanCompuesto(), "Plan Compuesto");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 8;
        panel.add(extrasPanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 8;
        panel.add(panelDecoradores(), gbc);

        JButton registrar = new JButton("Registrar tratamiento");
        registrar.addActionListener(e -> registrarTratamiento());
        gbc.gridx = 8;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.gridheight = 2;
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

    private JPanel extraPlanCompuesto() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Sub-tratamientos (Ctrl+Click):"));
        JScrollPane scroll = new JScrollPane(subTratamientosList);
        scroll.setPreferredSize(new Dimension(320, 48));
        panel.add(scroll);
        return panel;
    }

    private JPanel panelDecoradores() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Adicionales (Decorator):"));
        panel.add(urgenciaCheck);
        panel.add(medicacionCheck);
        panel.add(internacionCheck);
        panel.add(diasInternacionField);
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
            boolean conUrgencia = urgenciaCheck.isSelected();
            boolean conMedicacion = medicacionCheck.isSelected();
            int diasInternacion = 0;
            if (internacionCheck.isSelected()) {
                diasInternacion = Integer.parseInt(diasInternacionField.getText());
            }
            boolean tieneDecoradores = conUrgencia || conMedicacion || diasInternacion > 0;

            if (!tieneDecoradores) {
                if ("Preventivo".equals(tipo)) {
                    tratamientoController.registrarPreventivo(mascota.getId(), nombreField.getText(), fechaField.getText(), estado,
                            Integer.parseInt(frecuenciaField.getText()));
                } else if ("Curativo".equals(tipo)) {
                    tratamientoController.registrarCurativo(mascota.getId(), nombreField.getText(), fechaField.getText(), estado,
                            Integer.parseInt(duracionField.getText()), diagnosticoField.getText());
                } else if ("Cirujia".equals(tipo)) {
                    tratamientoController.registrarCirujia(mascota.getId(), nombreField.getText(), fechaField.getText(), estado,
                            tipoCirujiaField.getText(), (NivelComplejidad) complejidadCombo.getSelectedItem());
                } else {
                    List<Tratamiento> seleccionados = subTratamientosList.getSelectedValuesList();
                    tratamientoController.registrarPlanCompuesto(mascota.getId(), nombreField.getText(), fechaField.getText(), estado, seleccionados);
                }
            } else {
                Tratamiento base;
                if ("Preventivo".equals(tipo)) {
                    base = TratamientoFactory.crearPreventivo(nombreField.getText(), fechaField.getText(), estado,
                            Integer.parseInt(frecuenciaField.getText()));
                } else if ("Curativo".equals(tipo)) {
                    base = TratamientoFactory.crearCurativo(nombreField.getText(), fechaField.getText(), estado,
                            Integer.parseInt(duracionField.getText()), diagnosticoField.getText());
                } else if ("Cirujia".equals(tipo)) {
                    base = TratamientoFactory.crearCirujia(nombreField.getText(), fechaField.getText(), estado,
                            tipoCirujiaField.getText(), (NivelComplejidad) complejidadCombo.getSelectedItem());
                } else {
                    List<Tratamiento> seleccionados = subTratamientosList.getSelectedValuesList();
                    if (seleccionados.isEmpty()) {
                        throw new IllegalArgumentException("Debe seleccionar al menos un sub-tratamiento para conformar el Plan Compuesto.");
                    }
                    base = TratamientoFactory.crearPlanCompuesto(nombreField.getText(), fechaField.getText(), estado, seleccionados);
                }
                tratamientoController.registrarTratamientoDecorado(mascota.getId(), base, conUrgencia, conMedicacion, diasInternacion);
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
        urgenciaCheck.setSelected(false);
        medicacionCheck.setSelected(false);
        internacionCheck.setSelected(false);
        diasInternacionField.setText("");
        subTratamientosList.clearSelection();
    }

    private void mostrarDetalle(Tratamiento tratamiento) {
        detalle.setText(tratamientoController.obtenerDetalle(tratamiento));
    }

    @Override
    public void refreshData() {
        mascotasModel.removeAllElements();
        for (Mascota mascota : tratamientoController.getMascotas()) {
            mascotasModel.addElement(mascota);
        }
        tratamientosModel.clear();
        subTratamientosModel.clear();
        for (Tratamiento tratamiento : tratamientoController.getTratamientos()) {
            tratamientosModel.addElement(tratamiento);
            subTratamientosModel.addElement(tratamiento);
        }
    }
}
