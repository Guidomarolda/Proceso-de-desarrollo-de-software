package Veterinaria.gui;

/**
 * Panel de presentacion para registro, visualizacion y comparacion de mascotas.
 */
import Veterinaria.Ave;
import Veterinaria.Dueño;
import Veterinaria.Exoticos;
import Veterinaria.Gato;
import Veterinaria.Mascota;
import Veterinaria.NivelCuidado;
import Veterinaria.Perro;
import Veterinaria.Tipo;
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

import Veterinaria.controladores.MascotaController;

public class MascotasPanel extends JPanel implements RefreshablePanel {
    private final MascotaController mascotaController;
    private final ClinicaVeterinariaService service;
    private final Runnable onChange;
    private final DefaultComboBoxModel<Dueño> duenosModel;
    private final JComboBox<Dueño> duenosCombo;
    private final JComboBox<String> tipoMascotaCombo;
    private final JTextField nombreField;
    private final JTextField edadField;
    private final JTextField pesoField;
    private final JTextField razaField;
    private final JComboBox<String> vacunadoCombo;
    private final JComboBox<Tipo> tipoAveCombo;
    private final JComboBox<NivelCuidado> nivelCuidadoCombo;
    private final JPanel extrasPanel;
    private final CardLayout extrasLayout;
    private final DefaultListModel<Mascota> mascotasModel;
    private final JList<Mascota> mascotasList;
    private final JTextArea detalle;
    private final JComboBox<Mascota> compararPrimera;
    private final JComboBox<Mascota> compararSegunda;

    public MascotasPanel(ClinicaVeterinariaService service, Runnable onChange) {
        this(service.getMascotaController(), service, onChange);
    }

    public MascotasPanel(MascotaController mascotaController, ClinicaVeterinariaService service, Runnable onChange) {
        this.mascotaController = mascotaController;
        this.service = service;
        this.onChange = onChange;
        this.duenosModel = new DefaultComboBoxModel<>();
        this.duenosCombo = new JComboBox<>(duenosModel);
        this.tipoMascotaCombo = new JComboBox<>(new String[]{"Perro", "Gato", "Ave", "Exotico"});
        this.nombreField = new JTextField(14);
        this.edadField = new JTextField(5);
        this.pesoField = new JTextField(6);
        this.razaField = new JTextField(14);
        this.vacunadoCombo = new JComboBox<>(new String[]{"true", "false"});
        this.tipoAveCombo = new JComboBox<>(Tipo.values());
        this.nivelCuidadoCombo = new JComboBox<>(NivelCuidado.values());
        this.extrasLayout = new CardLayout();
        this.extrasPanel = new JPanel(extrasLayout);
        this.mascotasModel = new DefaultListModel<>();
        this.mascotasList = new JList<>(mascotasModel);
        this.detalle = new JTextArea();
        this.compararPrimera = new JComboBox<>();
        this.compararSegunda = new JComboBox<>();

        setLayout(new BorderLayout(12, 12));
        add(formulario(), BorderLayout.NORTH);
        add(new JScrollPane(mascotasList), BorderLayout.CENTER);
        detalle.setEditable(false);
        add(new JScrollPane(detalle), BorderLayout.EAST);
        add(comparador(), BorderLayout.SOUTH);

        tipoMascotaCombo.addActionListener(e -> extrasLayout.show(extrasPanel, (String) tipoMascotaCombo.getSelectedItem()));
        mascotasList.addListSelectionListener(e -> mostrarDetalle(mascotasList.getSelectedValue()));
        refreshData();
    }

    private JPanel formulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addComponente(panel, gbc, 0, 0, "Dueno", duenosCombo);
        addComponente(panel, gbc, 2, 0, "Tipo", tipoMascotaCombo);
        addComponente(panel, gbc, 4, 0, "Nombre", nombreField);
        addComponente(panel, gbc, 6, 0, "Edad", edadField);
        addComponente(panel, gbc, 8, 0, "Peso", pesoField);

        extrasPanel.add(extraPerro(), "Perro");
        extrasPanel.add(extraGato(), "Gato");
        extrasPanel.add(extraAve(), "Ave");
        extrasPanel.add(extraExotico(), "Exotico");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 8;
        panel.add(extrasPanel, gbc);

        JButton registrar = new JButton("Registrar mascota");
        registrar.addActionListener(e -> registrarMascota());
        gbc.gridx = 8;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        panel.add(registrar, gbc);
        return panel;
    }

    private JPanel extraPerro() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Raza"));
        panel.add(razaField);
        return panel;
    }

    private JPanel extraGato() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Vacunado"));
        panel.add(vacunadoCombo);
        return panel;
    }

    private JPanel extraAve() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Tipo de ave"));
        panel.add(tipoAveCombo);
        return panel;
    }

    private JPanel extraExotico() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Nivel de cuidado"));
        panel.add(nivelCuidadoCombo);
        return panel;
    }

    private JPanel comparador() {
        JPanel panel = new JPanel();
        JButton comparar = new JButton("Comparar costos");
        JButton aptitud = new JButton("Verificar aptitud");
        comparar.addActionListener(e -> compararMascotas());
        aptitud.addActionListener(e -> verificarAptitud());
        panel.add(new JLabel("Mascota 1"));
        panel.add(compararPrimera);
        panel.add(new JLabel("Mascota 2"));
        panel.add(compararSegunda);
        panel.add(comparar);
        panel.add(aptitud);
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

    private void registrarMascota() {
        try {
            Dueño dueno = (Dueño) duenosCombo.getSelectedItem();
            if (dueno == null) {
                throw new IllegalArgumentException("Debe registrar o seleccionar un dueno.");
            }
            String tipo = (String) tipoMascotaCombo.getSelectedItem();
            int edad = Integer.parseInt(edadField.getText());
            Double peso = Double.parseDouble(pesoField.getText());
            if ("Perro".equals(tipo)) {
                mascotaController.registrarPerro(dueno.getId(), nombreField.getText(), edad, peso, razaField.getText());
            } else if ("Gato".equals(tipo)) {
                mascotaController.registrarGato(dueno.getId(), nombreField.getText(), edad, peso, Boolean.parseBoolean((String) vacunadoCombo.getSelectedItem()));
            } else if ("Ave".equals(tipo)) {
                mascotaController.registrarAve(dueno.getId(), nombreField.getText(), edad, peso, (Tipo) tipoAveCombo.getSelectedItem());
            } else {
                mascotaController.registrarExotico(dueno.getId(), nombreField.getText(), edad, peso, (NivelCuidado) nivelCuidadoCombo.getSelectedItem());
            }
            limpiar();
            onChange.run();
        } catch (NumberFormatException e) {
            MainFrame.mostrarError(new IllegalArgumentException("Edad y peso deben ser valores numericos validos."));
        } catch (Exception e) {
            MainFrame.mostrarError(e);
        }
    }

    private void limpiar() {
        nombreField.setText("");
        edadField.setText("");
        pesoField.setText("");
        razaField.setText("");
    }

    private void mostrarDetalle(Mascota mascota) {
        if (mascota == null) {
            detalle.setText("");
            return;
        }
        StringBuilder builder = new StringBuilder();
        builder.append("Nombre: ").append(mascota.getNombre()).append("\n");
        builder.append("Edad: ").append(mascota.getEdad()).append("\n");
        builder.append("Peso: ").append(mascota.getPeso()).append("\n");
        builder.append("Costo base: ").append(mascota.costoBaseAtencion()).append("\n");
        builder.append("Apta especial: ").append(mascota.esAptaParaProcedimientoEspecial() ? "Si" : "No").append("\n");
        builder.append("Tratamientos: ").append(mascota.getTratamientos().size()).append("\n");
        if (mascota instanceof Perro) builder.append("Raza: ").append(((Perro) mascota).getRaza()).append("\n");
        if (mascota instanceof Gato) builder.append("Vacunado: ").append(((Gato) mascota).getEstaVacunado()).append("\n");
        if (mascota instanceof Ave) builder.append("Tipo: ").append(((Ave) mascota).getTipo()).append("\n");
        if (mascota instanceof Exoticos) builder.append("Nivel cuidado: ").append(((Exoticos) mascota).getNivelCuidado()).append("\n");
        detalle.setText(builder.toString());
    }

    private void compararMascotas() {
        try {
            Mascota primera = (Mascota) compararPrimera.getSelectedItem();
            Mascota segunda = (Mascota) compararSegunda.getSelectedItem();
            int resultado = mascotaController.compararMascotas(primera, segunda);
            String mensaje = resultado == 0 ? "Ambas tienen el mismo costo base." :
                    resultado > 0 ? primera.getNombre() + " es mas costosa." : segunda.getNombre() + " es mas costosa.";
            javax.swing.JOptionPane.showMessageDialog(this, mensaje);
        } catch (Exception e) {
            MainFrame.mostrarError(e);
        }
    }

    private void verificarAptitud() {
        Mascota mascota = mascotasList.getSelectedValue();
        if (mascota == null) {
            MainFrame.mostrarError(new IllegalArgumentException("Debe seleccionar una mascota de la lista."));
            return;
        }
        javax.swing.JOptionPane.showMessageDialog(this,
                mascota.esAptaParaProcedimientoEspecial() ? "La mascota es apta." : "La mascota no es apta.");
    }

    @Override
    public void refreshData() {
        duenosModel.removeAllElements();
        for (Dueño dueno : service.getDuenos()) {
            duenosModel.addElement(dueno);
        }
        mascotasModel.clear();
        compararPrimera.removeAllItems();
        compararSegunda.removeAllItems();
        for (Mascota mascota : mascotaController.getMascotas()) {
            mascotasModel.addElement(mascota);
            compararPrimera.addItem(mascota);
            compararSegunda.addItem(mascota);
        }
    }
}
