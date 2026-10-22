package Veterinaria.gui;

import Veterinaria.controladores.DuenoController;
import Veterinaria.modelo.Dueño;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public class DuenosPanel extends JPanel implements RefreshablePanel {
    private final DuenoController duenoController;
    private final Runnable onChange;
    private final JTextField nombreField;
    private final JTextField apellidoField;
    private final JTextField telefonoField;
    private final JTextField correoField;
    private final DefaultListModel<Dueño> model;
    private final JList<Dueño> lista;
    private final JTextArea detalle;

    public DuenosPanel(DuenoController duenoController, Runnable onChange) {
        this.duenoController = duenoController;
        this.onChange = onChange;
        this.nombreField = UiTheme.crearCampoTexto(20);
        this.apellidoField = UiTheme.crearCampoTexto(20);
        this.telefonoField = UiTheme.crearCampoTexto(20);
        this.correoField = UiTheme.crearCampoTexto(20);
        this.model = new DefaultListModel<>();
        this.lista = new JList<>(model);
        UiTheme.estilizarLista(lista);
        this.detalle = UiTheme.crearAreaDetalle();

        setLayout(new BorderLayout(12, 12));
        UiTheme.aplicarMargenPanelPrincipal(this);

        add(formulario(), BorderLayout.NORTH);
        add(panelCentral(), BorderLayout.CENTER);

        lista.addListSelectionListener(e -> mostrarDetalle(lista.getSelectedValue()));
        refreshData();
    }

    private JPanel formulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        UiTheme.aplicarFondoTarjeta(panel, "Registrar Nuevo Dueno");
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Fila 0: Nombre y Apellido
        addCampo(panel, gbc, 0, 0, "Nombre:", nombreField);
        addCampo(panel, gbc, 2, 0, "Apellido:", apellidoField);

        // Fila 1: Telefono y Correo
        addCampo(panel, gbc, 0, 1, "Telefono:", telefonoField);
        addCampo(panel, gbc, 2, 1, "Correo electronico:", correoField);

        // Boton a la derecha
        JButton registrar = UiTheme.crearBotonPrimario("Registrar Dueno");
        registrar.addActionListener(e -> registrarDueno());
        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel.add(registrar, gbc);

        return panel;
    }

    private JSplitPane panelCentral() {
        JPanel listaPanel = new JPanel(new BorderLayout());
        UiTheme.aplicarFondoTarjeta(listaPanel, "Duenos Registrados");
        listaPanel.add(new JScrollPane(lista), BorderLayout.CENTER);

        JPanel detallePanel = new JPanel(new BorderLayout());
        UiTheme.aplicarFondoTarjeta(detallePanel, "Informacion del Dueno");
        detallePanel.add(new JScrollPane(detalle), BorderLayout.CENTER);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listaPanel, detallePanel);
        split.setResizeWeight(0.6);
        split.setBorder(null);
        split.setOpaque(false);
        return split;
    }

    private void addCampo(JPanel panel, GridBagConstraints gbc, int colBase, int fila, String etiqueta, JTextField campo) {
        gbc.gridx = colBase;
        gbc.gridy = fila;
        gbc.gridheight = 1;
        gbc.weightx = 0;
        panel.add(UiTheme.crearEtiqueta(etiqueta), gbc);

        gbc.gridx = colBase + 1;
        gbc.weightx = 1.0;
        panel.add(campo, gbc);
    }

    private void registrarDueno() {
        try {
            Dueño nuevo = duenoController.registrarDueno(
                    nombreField.getText(),
                    apellidoField.getText(),
                    telefonoField.getText(),
                    correoField.getText()
            );
            nombreField.setText("");
            apellidoField.setText("");
            telefonoField.setText("");
            correoField.setText("");
            onChange.run();
            lista.setSelectedValue(nuevo, true);
        } catch (Exception e) {
            MainFrame.mostrarError(e);
        }
    }

    private void mostrarDetalle(Dueño dueno) {
        if (dueno == null) {
            detalle.setText("Seleccione un dueno de la lista izquierda para visualizar sus datos.");
            return;
        }
        detalle.setText(duenoController.obtenerDetalle(dueno));
    }

    @Override
    public void refreshData() {
        Dueño seleccionado = lista.getSelectedValue();
        model.clear();
        for (Dueño dueno : duenoController.getDuenos()) {
            model.addElement(dueno);
        }
        if (seleccionado != null) {
            lista.setSelectedValue(seleccionado, true);
        } else if (!model.isEmpty()) {
            lista.setSelectedIndex(0);
        } else {
            mostrarDetalle(null);
        }
    }
}
