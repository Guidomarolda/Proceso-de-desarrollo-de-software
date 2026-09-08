package Veterinaria.gui;

import Veterinaria.Dueño;
import Veterinaria.servicios.ClinicaVeterinariaService;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public class DuenosPanel extends JPanel implements RefreshablePanel {
    private final ClinicaVeterinariaService service;
    private final Runnable onChange;
    private final JTextField nombreField;
    private final JTextField apellidoField;
    private final JTextField telefonoField;
    private final JTextField correoField;
    private final DefaultListModel<Dueño> model;
    private final JList<Dueño> lista;
    private final JTextArea detalle;

    public DuenosPanel(ClinicaVeterinariaService service, Runnable onChange) {
        this.service = service;
        this.onChange = onChange;
        this.nombreField = new JTextField(18);
        this.apellidoField = new JTextField(18);
        this.telefonoField = new JTextField(18);
        this.correoField = new JTextField(18);
        this.model = new DefaultListModel<>();
        this.lista = new JList<>(model);
        this.detalle = new JTextArea();

        setLayout(new BorderLayout(12, 12));
        add(formulario(), BorderLayout.NORTH);
        add(new JScrollPane(lista), BorderLayout.CENTER);
        detalle.setEditable(false);
        add(new JScrollPane(detalle), BorderLayout.EAST);

        lista.addListSelectionListener(e -> mostrarDetalle(lista.getSelectedValue()));
        refreshData();
    }

    private JPanel formulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addCampo(panel, gbc, 0, "Nombre", nombreField);
        addCampo(panel, gbc, 1, "Apellido", apellidoField);
        addCampo(panel, gbc, 2, "Telefono", telefonoField);
        addCampo(panel, gbc, 3, "Correo", correoField);

        JButton registrar = new JButton("Registrar dueno");
        registrar.addActionListener(e -> registrarDueno());
        gbc.gridx = 8;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        panel.add(registrar, gbc);
        return panel;
    }

    private void addCampo(JPanel panel, GridBagConstraints gbc, int columna, String etiqueta, JTextField campo) {
        gbc.gridx = columna * 2;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        panel.add(new JLabel(etiqueta), gbc);
        gbc.gridx = columna * 2 + 1;
        panel.add(campo, gbc);
    }

    private void registrarDueno() {
        try {
            service.registrarDueno(nombreField.getText(), apellidoField.getText(), telefonoField.getText(), correoField.getText());
            nombreField.setText("");
            apellidoField.setText("");
            telefonoField.setText("");
            correoField.setText("");
            onChange.run();
        } catch (Exception e) {
            MainFrame.mostrarError(e);
        }
    }

    private void mostrarDetalle(Dueño dueno) {
        if (dueno == null) {
            detalle.setText("");
            return;
        }
        StringBuilder builder = new StringBuilder();
        builder.append("Nombre: ").append(dueno.getNombreCompleto()).append("\n");
        builder.append("Telefono: ").append(dueno.getTelefono()).append("\n");
        builder.append("Correo: ").append(dueno.getCorreo()).append("\n");
        builder.append("Mascotas registradas: ").append(dueno.getMascotas().size()).append("\n");
        detalle.setText(builder.toString());
    }

    @Override
    public void refreshData() {
        Dueño seleccionado = lista.getSelectedValue();
        model.clear();
        for (Dueño dueno : service.getDuenos()) {
            model.addElement(dueno);
        }
        if (seleccionado != null) {
            lista.setSelectedValue(seleccionado, true);
        }
    }
}
