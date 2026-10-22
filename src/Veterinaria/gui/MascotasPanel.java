package Veterinaria.gui;

/**
 * Panel de presentacion (Vista MVC) para registro, visualizacion, fotografia y comparacion de mascotas.
 * Se comunica exclusivamente con los controladores sin conocer las subclases concretas de Mascota.
 */
import Veterinaria.controladores.ConsultaController;
import Veterinaria.controladores.MascotaController;
import Veterinaria.modelo.Dueño;
import Veterinaria.modelo.Mascota;
import Veterinaria.modelo.NivelCuidado;
import Veterinaria.modelo.Tipo;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.File;

public class MascotasPanel extends JPanel implements RefreshablePanel {
    private static final int FOTO_SIZE = 135;

    private final MascotaController mascotaController;
    private final ConsultaController consultaController;
    private final Runnable onChange;
    private final DefaultComboBoxModel<Dueño> duenosModel;
    private final JComboBox<Dueño> duenosCombo;
    private final JComboBox<String> tipoMascotaCombo;
    private final JTextField nombreField;
    private final JTextField edadField;
    private final JTextField pesoField;
    private final JTextField razaField;
    private final JTextField rutaFotoField;
    private final JComboBox<String> vacunadoCombo;
    private final JComboBox<Tipo> tipoAveCombo;
    private final JComboBox<NivelCuidado> nivelCuidadoCombo;
    private final JPanel extrasPanel;
    private final CardLayout extrasLayout;
    private final DefaultListModel<Mascota> mascotasModel;
    private final JList<Mascota> mascotasList;
    private final JTextArea detalle;
    private final JLabel fotoLabel;
    private final JComboBox<Mascota> compararPrimera;
    private final JComboBox<Mascota> compararSegunda;

    public MascotasPanel(MascotaController mascotaController, ConsultaController consultaController, Runnable onChange) {
        this.mascotaController = mascotaController;
        this.consultaController = consultaController;
        this.onChange = onChange;
        this.duenosModel = new DefaultComboBoxModel<>();
        this.duenosCombo = new JComboBox<>(duenosModel);
        UiTheme.estilizarCombo(duenosCombo);

        this.tipoMascotaCombo = new JComboBox<>(new String[]{"Perro", "Gato", "Ave", "Exotico"});
        UiTheme.estilizarCombo(tipoMascotaCombo);

        this.nombreField = UiTheme.crearCampoTexto(16);
        this.edadField = UiTheme.crearCampoTexto(8);
        this.pesoField = UiTheme.crearCampoTexto(8);
        this.razaField = UiTheme.crearCampoTexto(16);
        this.rutaFotoField = UiTheme.crearCampoTexto(18);
        this.rutaFotoField.setEditable(false);

        this.vacunadoCombo = new JComboBox<>(new String[]{"true", "false"});
        UiTheme.estilizarCombo(vacunadoCombo);

        this.tipoAveCombo = new JComboBox<>(Tipo.values());
        UiTheme.estilizarCombo(tipoAveCombo);

        this.nivelCuidadoCombo = new JComboBox<>(NivelCuidado.values());
        UiTheme.estilizarCombo(nivelCuidadoCombo);

        this.extrasLayout = new CardLayout();
        this.extrasPanel = new JPanel(extrasLayout);
        this.extrasPanel.setOpaque(false);

        this.mascotasModel = new DefaultListModel<>();
        this.mascotasList = new JList<>(mascotasModel);
        UiTheme.estilizarLista(mascotasList);

        this.detalle = UiTheme.crearAreaDetalle();
        this.fotoLabel = crearVisorFoto();
        this.compararPrimera = new JComboBox<>();
        UiTheme.estilizarCombo(compararPrimera);
        this.compararSegunda = new JComboBox<>();
        UiTheme.estilizarCombo(compararSegunda);

        setLayout(new BorderLayout(12, 12));
        UiTheme.aplicarMargenPanelPrincipal(this);

        add(formulario(), BorderLayout.NORTH);
        add(panelCentral(), BorderLayout.CENTER);
        add(comparador(), BorderLayout.SOUTH);

        tipoMascotaCombo.addActionListener(e -> extrasLayout.show(extrasPanel, (String) tipoMascotaCombo.getSelectedItem()));
        mascotasList.addListSelectionListener(e -> mostrarDetalle(mascotasList.getSelectedValue()));
        refreshData();
    }

    private JLabel crearVisorFoto() {
        JLabel label = new JLabel("Sin foto", SwingConstants.CENTER);
        label.setFont(UiTheme.FONT_BOLD);
        label.setForeground(UiTheme.TEXT_MUTED);
        label.setOpaque(true);
        label.setBackground(Color.WHITE);
        label.setPreferredSize(new Dimension(FOTO_SIZE, FOTO_SIZE));
        label.setMinimumSize(new Dimension(FOTO_SIZE, FOTO_SIZE));
        label.setBorder(BorderFactory.createLineBorder(UiTheme.BORDER_COLOR, 1));
        return label;
    }

    private JPanel formulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        UiTheme.aplicarFondoTarjeta(panel, "Registrar Nueva Mascota");
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Fila 0: Dueno, Tipo, Nombre
        addComponente(panel, gbc, 0, 0, "Dueno:", duenosCombo);
        addComponente(panel, gbc, 2, 0, "Tipo:", tipoMascotaCombo);
        addComponente(panel, gbc, 4, 0, "Nombre:", nombreField);

        // Fila 1: Edad, Peso y Atributo Especifico
        addComponente(panel, gbc, 0, 1, "Edad (anos):", edadField);
        addComponente(panel, gbc, 2, 1, "Peso (kg):", pesoField);

        extrasPanel.add(extraPerro(), "Perro");
        extrasPanel.add(extraGato(), "Gato");
        extrasPanel.add(extraAve(), "Ave");
        extrasPanel.add(extraExotico(), "Exotico");
        gbc.gridx = 4;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        panel.add(extrasPanel, gbc);

        // Fila 2: Selector de imagen opcional al registrar
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        panel.add(UiTheme.crearEtiqueta("Imagen (opcional):"), gbc);

        JPanel selectorFotoPanel = new JPanel(new BorderLayout(8, 0));
        selectorFotoPanel.setOpaque(false);
        selectorFotoPanel.add(rutaFotoField, BorderLayout.CENTER);
        JButton examinarBtn = UiTheme.crearBotonSecundario("Elegir Imagen...");
        examinarBtn.addActionListener(e -> seleccionarArchivoParaFormulario());
        selectorFotoPanel.add(examinarBtn, BorderLayout.EAST);

        gbc.gridx = 1;
        gbc.gridwidth = 5;
        gbc.weightx = 1.0;
        panel.add(selectorFotoPanel, gbc);

        JButton registrar = UiTheme.crearBotonPrimario("Registrar Mascota");
        registrar.addActionListener(e -> registrarMascota());
        gbc.gridx = 6;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 3;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.VERTICAL;
        panel.add(registrar, gbc);
        return panel;
    }

    private JSplitPane panelCentral() {
        JPanel listaPanel = new JPanel(new BorderLayout());
        UiTheme.aplicarFondoTarjeta(listaPanel, "Mascotas Registradas");
        listaPanel.add(new JScrollPane(mascotasList), BorderLayout.CENTER);

        JPanel detallePanel = new JPanel(new BorderLayout(12, 8));
        UiTheme.aplicarFondoTarjeta(detallePanel, "Ficha de la Mascota Seleccionada");

        // Columna izquierda de la ficha: Foto de la mascota + boton para cambiarla
        JPanel contenedorFoto = new JPanel(new BorderLayout(0, 6));
        contenedorFoto.setOpaque(false);
        contenedorFoto.add(fotoLabel, BorderLayout.NORTH);
        JButton cambiarFotoBtn = UiTheme.crearBotonSecundario("Subir / Cambiar Foto");
        cambiarFotoBtn.addActionListener(e -> cambiarFotoMascotaSeleccionada());
        contenedorFoto.add(cambiarFotoBtn, BorderLayout.SOUTH);

        JPanel wrapperIzquierdo = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        wrapperIzquierdo.setOpaque(false);
        wrapperIzquierdo.add(contenedorFoto);

        detallePanel.add(wrapperIzquierdo, BorderLayout.WEST);
        detallePanel.add(new JScrollPane(detalle), BorderLayout.CENTER);

        JButton aptitud = UiTheme.crearBotonSecundario("Verificar Aptitud para Procedimiento Especial");
        aptitud.addActionListener(e -> verificarAptitud());
        JPanel accionFicha = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        accionFicha.setOpaque(false);
        accionFicha.add(aptitud);
        detallePanel.add(accionFicha, BorderLayout.SOUTH);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listaPanel, detallePanel);
        split.setResizeWeight(0.52);
        split.setBorder(null);
        split.setOpaque(false);
        return split;
    }

    private JPanel extraPerro() {
        JPanel panel = new JPanel(new BorderLayout(8, 0));
        panel.setOpaque(false);
        panel.add(UiTheme.crearEtiqueta("Raza:"), BorderLayout.WEST);
        panel.add(razaField, BorderLayout.CENTER);
        return panel;
    }

    private JPanel extraGato() {
        JPanel panel = new JPanel(new BorderLayout(8, 0));
        panel.setOpaque(false);
        panel.add(UiTheme.crearEtiqueta("Vacunado:"), BorderLayout.WEST);
        panel.add(vacunadoCombo, BorderLayout.CENTER);
        return panel;
    }

    private JPanel extraAve() {
        JPanel panel = new JPanel(new BorderLayout(8, 0));
        panel.setOpaque(false);
        panel.add(UiTheme.crearEtiqueta("Tipo de ave:"), BorderLayout.WEST);
        panel.add(tipoAveCombo, BorderLayout.CENTER);
        return panel;
    }

    private JPanel extraExotico() {
        JPanel panel = new JPanel(new BorderLayout(8, 0));
        panel.setOpaque(false);
        panel.add(UiTheme.crearEtiqueta("Nivel de cuidado:"), BorderLayout.WEST);
        panel.add(nivelCuidadoCombo, BorderLayout.CENTER);
        return panel;
    }

    private JPanel comparador() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 4));
        UiTheme.aplicarFondoTarjeta(panel, "Comparador de Costos Base entre dos Mascotas");
        JButton comparar = UiTheme.crearBotonSecundario("Comparar Costos Base");
        comparar.addActionListener(e -> compararMascotas());

        panel.add(UiTheme.crearEtiqueta("Mascota 1:"));
        panel.add(compararPrimera);
        panel.add(UiTheme.crearEtiqueta("Mascota 2:"));
        panel.add(compararSegunda);
        panel.add(comparar);
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

    private void seleccionarArchivoParaFormulario() {
        String ruta = abrirSelectorImagen();
        if (ruta != null) {
            rutaFotoField.setText(ruta);
        }
    }

    private void cambiarFotoMascotaSeleccionada() {
        Mascota mascota = mascotasList.getSelectedValue();
        if (mascota == null) {
            MainFrame.mostrarError(new IllegalArgumentException("Primero seleccione una mascota de la lista."));
            return;
        }
        String ruta = abrirSelectorImagen();
        if (ruta != null) {
            mascotaController.actualizarImagenMascota(mascota.getId(), ruta);
            mostrarDetalle(mascota);
        }
    }

    private String abrirSelectorImagen() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Seleccionar imagen de la mascota");
        chooser.setFileFilter(new FileNameExtensionFilter("Imagenes (JPG, PNG, GIF, BMP)", "jpg", "jpeg", "png", "gif", "bmp"));
        int resultado = chooser.showOpenDialog(this);
        if (resultado == JFileChooser.APPROVE_OPTION && chooser.getSelectedFile() != null) {
            return chooser.getSelectedFile().getAbsolutePath();
        }
        return null;
    }

    private void registrarMascota() {
        try {
            Dueño dueno = (Dueño) duenosCombo.getSelectedItem();
            if (dueno == null) {
                throw new IllegalArgumentException("Debe registrar o seleccionar un dueno.");
            }
            String tipo = (String) tipoMascotaCombo.getSelectedItem();
            int edad = Integer.parseInt(edadField.getText().trim());
            Double peso = Double.parseDouble(pesoField.getText().trim());
            Mascota nueva;
            if ("Perro".equals(tipo)) {
                nueva = mascotaController.registrarPerro(dueno.getId(), nombreField.getText(), edad, peso, razaField.getText());
            } else if ("Gato".equals(tipo)) {
                nueva = mascotaController.registrarGato(dueno.getId(), nombreField.getText(), edad, peso, Boolean.parseBoolean((String) vacunadoCombo.getSelectedItem()));
            } else if ("Ave".equals(tipo)) {
                nueva = mascotaController.registrarAve(dueno.getId(), nombreField.getText(), edad, peso, (Tipo) tipoAveCombo.getSelectedItem());
            } else {
                nueva = mascotaController.registrarExotico(dueno.getId(), nombreField.getText(), edad, peso, (NivelCuidado) nivelCuidadoCombo.getSelectedItem());
            }
            String rutaFoto = rutaFotoField.getText().trim();
            if (!rutaFoto.isEmpty()) {
                mascotaController.actualizarImagenMascota(nueva.getId(), rutaFoto);
            }
            limpiar();
            onChange.run();
            mascotasList.setSelectedValue(nueva, true);
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
        rutaFotoField.setText("");
    }

    private void mostrarDetalle(Mascota mascota) {
        if (mascota == null) {
            detalle.setText("Seleccione una mascota de la lista izquierda para visualizar su ficha.");
            fotoLabel.setText("");
            fotoLabel.setIcon(PetCareLogo.crearAvatarMascota("MASCOTA", FOTO_SIZE - 6));
            return;
        }
        detalle.setText(mascotaController.obtenerDetalle(mascota));
        actualizarVisorFoto(mascota);
    }

    private void actualizarVisorFoto(Mascota mascota) {
        if (mascota.tieneImagen()) {
            try {
                File archivo = new File(mascota.getRutaImagen());
                if (archivo.exists()) {
                    BufferedImage img = ImageIO.read(archivo);
                    if (img != null) {
                        Image escalada = img.getScaledInstance(FOTO_SIZE - 6, FOTO_SIZE - 6, Image.SCALE_SMOOTH);
                        fotoLabel.setText("");
                        fotoLabel.setIcon(new ImageIcon(escalada));
                        return;
                    }
                }
            } catch (Exception ignored) {
            }
        }
        fotoLabel.setText("");
        fotoLabel.setIcon(PetCareLogo.crearAvatarMascota(mascota.getTipoRegistro(), FOTO_SIZE - 6));
    }

    private void compararMascotas() {
        try {
            Mascota primera = (Mascota) compararPrimera.getSelectedItem();
            Mascota segunda = (Mascota) compararSegunda.getSelectedItem();
            int resultado = consultaController.compararMascotas(primera, segunda);
            String mensaje = resultado == 0 ? "Ambas tienen el mismo costo base." :
                    resultado > 0 ? primera.getNombre() + " es mas costosa." : segunda.getNombre() + " es mas costosa.";
            javax.swing.JOptionPane.showMessageDialog(this, mensaje);
        } catch (Exception e) {
            MainFrame.mostrarError(e);
        }
    }

    private void verificarAptitud() {
        try {
            Mascota mascota = mascotasList.getSelectedValue();
            boolean apta = mascotaController.verificarAptitud(mascota);
            javax.swing.JOptionPane.showMessageDialog(this,
                    apta ? "La mascota es apta." : "La mascota no es apta.");
        } catch (Exception e) {
            MainFrame.mostrarError(e);
        }
    }

    @Override
    public void refreshData() {
        Mascota mascotaPrevia = mascotasList.getSelectedValue();
        duenosModel.removeAllElements();
        for (Dueño dueno : mascotaController.getDuenos()) {
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
        if (mascotaPrevia != null) {
            mascotasList.setSelectedValue(mascotaPrevia, true);
        } else if (!mascotasModel.isEmpty()) {
            mascotasList.setSelectedIndex(0);
        } else {
            mostrarDetalle(null);
        }
    }
}
