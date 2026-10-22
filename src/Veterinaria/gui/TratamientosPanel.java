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
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
        UiTheme.estilizarCombo(mascotasCombo);

        this.tipoCombo = new JComboBox<>(new String[]{"Preventivo", "Curativo", "Cirujia", "Plan Compuesto"});
        UiTheme.estilizarCombo(tipoCombo);

        this.nombreField = UiTheme.crearCampoTexto(22);
        this.fechaField = UiTheme.crearCampoTexto(16);
        this.fechaField.setText(fechaHoy());
        this.estadoCombo = new JComboBox<>(Estado.values());
        UiTheme.estilizarCombo(estadoCombo);

        this.frecuenciaField = UiTheme.crearCampoTexto(14);
        this.duracionField = UiTheme.crearCampoTexto(12);
        this.diagnosticoField = UiTheme.crearCampoTexto(26);
        this.tipoCirujiaField = UiTheme.crearCampoTexto(22);
        this.complejidadCombo = new JComboBox<>(NivelComplejidad.values());
        UiTheme.estilizarCombo(complejidadCombo);

        this.subTratamientosModel = new DefaultListModel<>();
        this.subTratamientosList = new JList<>(subTratamientosModel);
        UiTheme.estilizarLista(subTratamientosList);
        this.subTratamientosList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        this.urgenciaCheck = new JCheckBox("Guardia (+$800)");
        UiTheme.estilizarCheck(urgenciaCheck);
        this.medicacionCheck = new JCheckBox("Medicacion (+$500)");
        UiTheme.estilizarCheck(medicacionCheck);
        this.internacionCheck = new JCheckBox("Internacion ($400/dia) - Dias:");
        UiTheme.estilizarCheck(internacionCheck);
        this.diasInternacionField = UiTheme.crearCampoTexto(5);
        this.diasInternacionField.setMinimumSize(new Dimension(60, 32));
        this.diasInternacionField.setPreferredSize(new Dimension(70, 32));
        this.diasInternacionField.setEnabled(false);
        this.diasInternacionField.setBackground(new Color(242, 240, 228));
        this.internacionCheck.addActionListener(e -> actualizarEstadoInternacion());

        this.extrasLayout = new CardLayout();
        this.extrasPanel = new JPanel(extrasLayout);
        this.extrasPanel.setOpaque(false);

        this.tratamientosModel = new DefaultListModel<>();
        this.tratamientosList = new JList<>(tratamientosModel);
        UiTheme.estilizarLista(tratamientosList);
        this.detalle = UiTheme.crearAreaDetalle();

        setLayout(new BorderLayout(12, 12));
        UiTheme.aplicarMargenPanelPrincipal(this);

        add(formulario(), BorderLayout.NORTH);
        add(panelCentral(), BorderLayout.CENTER);

        tipoCombo.addActionListener(e -> extrasLayout.show(extrasPanel, (String) tipoCombo.getSelectedItem()));
        tratamientosList.addListSelectionListener(e -> mostrarDetalle(tratamientosList.getSelectedValue()));
        refreshData();
    }

    private JPanel formulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        UiTheme.aplicarFondoTarjeta(panel, "Registrar Nuevo Tratamiento o Plan Compuesto");
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Fila 0: Mascota, Tipo, Estado
        addComponente(panel, gbc, 0, 0, "Mascota:", mascotasCombo);
        addComponente(panel, gbc, 2, 0, "Tipo:", tipoCombo);
        addComponente(panel, gbc, 4, 0, "Estado:", estadoCombo);

        // Fila 1: Nombre del Tratamiento (ocupa 3 columnas) y Fecha de Inicio (ocupa 3 columnas)
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        panel.add(UiTheme.crearEtiqueta("Nombre tratamiento:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        panel.add(nombreField, gbc);

        gbc.gridx = 3;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        panel.add(UiTheme.crearEtiqueta("Fecha inicio (dd/mm/aaaa):"), gbc);

        gbc.gridx = 4;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        panel.add(fechaField, gbc);

        // Fila 2: Datos especificos del tipo ocupando todo el ancho (6 columnas)
        extrasPanel.add(extraPreventivo(), "Preventivo");
        extrasPanel.add(extraCurativo(), "Curativo");
        extrasPanel.add(extraCirujia(), "Cirujia");
        extrasPanel.add(extraPlanCompuesto(), "Plan Compuesto");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 6;
        gbc.weightx = 1.0;
        panel.add(extrasPanel, gbc);

        // Fila 3: Decoradores
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 6;
        gbc.weightx = 1.0;
        panel.add(panelDecoradores(), gbc);

        // Boton a la derecha abarcando las 4 filas
        JButton registrar = UiTheme.crearBotonPrimario("Registrar Tratamiento");
        registrar.addActionListener(e -> registrarTratamiento());
        gbc.gridx = 6;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 4;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel.add(registrar, gbc);
        return panel;
    }

    private JSplitPane panelCentral() {
        JPanel listaPanel = new JPanel(new BorderLayout());
        UiTheme.aplicarFondoTarjeta(listaPanel, "Tratamientos Registrados");
        listaPanel.add(new JScrollPane(tratamientosList), BorderLayout.CENTER);

        JPanel detallePanel = new JPanel(new BorderLayout());
        UiTheme.aplicarFondoTarjeta(detallePanel, "Detalle del Tratamiento / Plan / Decoradores");
        detallePanel.add(new JScrollPane(detalle), BorderLayout.CENTER);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listaPanel, detallePanel);
        split.setResizeWeight(0.6);
        split.setBorder(null);
        split.setOpaque(false);
        return split;
    }

    private JPanel extraPreventivo() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 0, 2, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.weightx = 0;
        panel.add(UiTheme.crearEtiqueta("Frecuencia recomendada (meses):"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(frecuenciaField, gbc);
        return panel;
    }

    private JPanel extraCurativo() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 0, 2, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.weightx = 0;
        panel.add(UiTheme.crearEtiqueta("Duracion (dias):"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.35;
        panel.add(duracionField, gbc);

        gbc.gridx = 2;
        gbc.insets = new Insets(2, 12, 2, 8);
        gbc.weightx = 0;
        panel.add(UiTheme.crearEtiqueta("Diagnostico clinico:"), gbc);

        gbc.gridx = 3;
        gbc.insets = new Insets(2, 0, 2, 0);
        gbc.weightx = 0.65;
        panel.add(diagnosticoField, gbc);
        return panel;
    }

    private JPanel extraCirujia() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 0, 2, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.weightx = 0;
        panel.add(UiTheme.crearEtiqueta("Tipo de cirujia:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.6;
        panel.add(tipoCirujiaField, gbc);

        gbc.gridx = 2;
        gbc.insets = new Insets(2, 12, 2, 8);
        gbc.weightx = 0;
        panel.add(UiTheme.crearEtiqueta("Complejidad:"), gbc);

        gbc.gridx = 3;
        gbc.insets = new Insets(2, 0, 2, 0);
        gbc.weightx = 0.4;
        panel.add(complejidadCombo, gbc);
        return panel;
    }

    private JPanel extraPlanCompuesto() {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setOpaque(false);
        panel.add(UiTheme.crearEtiqueta("Sub-tratamientos (Ctrl+Click):"), BorderLayout.WEST);
        JScrollPane scroll = new JScrollPane(subTratamientosList);
        scroll.setPreferredSize(new Dimension(400, 60));
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private JPanel panelDecoradores() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(UiTheme.BG_DETAIL);
        panel.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(UiTheme.BORDER_COLOR, 1),
                javax.swing.BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 6, 0, 6);
        gbc.gridy = 0;

        gbc.gridx = 0;
        panel.add(UiTheme.crearEtiqueta("Adicionales (Decorator):"), gbc);
        gbc.gridx = 1;
        panel.add(urgenciaCheck, gbc);
        gbc.gridx = 2;
        panel.add(medicacionCheck, gbc);
        gbc.gridx = 3;
        panel.add(internacionCheck, gbc);
        gbc.gridx = 4;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(diasInternacionField, gbc);
        return panel;
    }

    private void addComponente(JPanel panel, GridBagConstraints gbc, int x, int y, String etiqueta, java.awt.Component componente) {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0;
        panel.add(UiTheme.crearEtiqueta(etiqueta), gbc);

        gbc.gridx = x + 1;
        gbc.weightx = 1.0;
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
                diasInternacion = Integer.parseInt(diasInternacionField.getText().trim());
            }
            boolean tieneDecoradores = conUrgencia || conMedicacion || diasInternacion > 0;
            Tratamiento creado;

            if (!tieneDecoradores) {
                if ("Preventivo".equals(tipo)) {
                    creado = tratamientoController.registrarPreventivo(mascota.getId(), nombreField.getText(), fechaField.getText(), estado,
                            Integer.parseInt(frecuenciaField.getText().trim()));
                } else if ("Curativo".equals(tipo)) {
                    creado = tratamientoController.registrarCurativo(mascota.getId(), nombreField.getText(), fechaField.getText(), estado,
                            Integer.parseInt(duracionField.getText().trim()), diagnosticoField.getText());
                } else if ("Cirujia".equals(tipo)) {
                    creado = tratamientoController.registrarCirujia(mascota.getId(), nombreField.getText(), fechaField.getText(), estado,
                            tipoCirujiaField.getText(), (NivelComplejidad) complejidadCombo.getSelectedItem());
                } else {
                    List<Tratamiento> seleccionados = subTratamientosList.getSelectedValuesList();
                    creado = tratamientoController.registrarPlanCompuesto(mascota.getId(), nombreField.getText(), fechaField.getText(), estado, seleccionados);
                }
            } else {
                Tratamiento base;
                if ("Preventivo".equals(tipo)) {
                    base = TratamientoFactory.crearPreventivo(nombreField.getText(), fechaField.getText(), estado,
                            Integer.parseInt(frecuenciaField.getText().trim()));
                } else if ("Curativo".equals(tipo)) {
                    base = TratamientoFactory.crearCurativo(nombreField.getText(), fechaField.getText(), estado,
                            Integer.parseInt(duracionField.getText().trim()), diagnosticoField.getText());
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
                creado = tratamientoController.registrarTratamientoDecorado(mascota.getId(), base, conUrgencia, conMedicacion, diasInternacion);
            }
            limpiar();
            onChange.run();
            tratamientosList.setSelectedValue(creado, true);
        } catch (NumberFormatException e) {
            MainFrame.mostrarError(new IllegalArgumentException("Los campos numericos deben tener valores validos."));
        } catch (Exception e) {
            MainFrame.mostrarError(e);
        }
    }

    private void limpiar() {
        nombreField.setText("");
        fechaField.setText(fechaHoy());
        frecuenciaField.setText("");
        duracionField.setText("");
        diagnosticoField.setText("");
        tipoCirujiaField.setText("");
        urgenciaCheck.setSelected(false);
        medicacionCheck.setSelected(false);
        internacionCheck.setSelected(false);
        actualizarEstadoInternacion();
        subTratamientosList.clearSelection();
    }

    private void actualizarEstadoInternacion() {
        boolean activo = internacionCheck.isSelected();
        diasInternacionField.setEnabled(activo);
        diasInternacionField.setBackground(activo ? Color.WHITE : new Color(242, 240, 228));
        if (activo && diasInternacionField.getText().trim().isEmpty()) {
            diasInternacionField.setText("1");
        } else if (!activo) {
            diasInternacionField.setText("");
        }
    }

    private static String fechaHoy() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    private void mostrarDetalle(Tratamiento tratamiento) {
        if (tratamiento == null) {
            detalle.setText("Seleccione un tratamiento de la lista izquierda para ver su costo y detalle.");
            return;
        }
        detalle.setText(tratamientoController.obtenerDetalle(tratamiento));
    }

    @Override
    public void refreshData() {
        Tratamiento seleccionadoPrevio = tratamientosList.getSelectedValue();
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
        if (seleccionadoPrevio != null) {
            tratamientosList.setSelectedValue(seleccionadoPrevio, true);
        } else if (!tratamientosModel.isEmpty()) {
            tratamientosList.setSelectedIndex(0);
        } else {
            mostrarDetalle(null);
        }
    }
}
