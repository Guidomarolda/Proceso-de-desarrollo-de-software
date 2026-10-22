package Veterinaria.gui;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;

/**
 * Utilidad visual de la capa Vista (Swing) para dar un estilo consistente,
 * colores agradables y tamanos legibles a todos los formularios y paneles.
 */
public final class UiTheme {
    // Tonos amarillo marfil / vainilla ultra clarito para los fondos de la interfaz
    public static final Color BG_MAIN = new Color(255, 253, 238);       // Amarillo marfil muy clarito principal
    public static final Color BG_CARD = new Color(255, 254, 247);       // Crema casi blanco para tarjetas
    public static final Color BG_DETAIL = new Color(255, 252, 235);     // Vainilla muy tenue para areas de detalle
    public static final Color PRIMARY = new Color(26, 115, 132);        // Teal clinico para cabecera y botones primarios
    public static final Color PRIMARY_DARK = new Color(18, 86, 99);
    public static final Color SECONDARY_BG = new Color(235, 245, 247);
    public static final Color BORDER_COLOR = new Color(224, 216, 185);  // Borde calido sutil
    public static final Color INPUT_BORDER = new Color(195, 188, 160);  // Borde limpio para las cajas de texto
    public static final Color TEXT_DARK = new Color(33, 43, 54);
    public static final Color TEXT_MUTED = new Color(99, 115, 129);

    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_REGULAR = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_DETAIL = new Font("Consolas", Font.PLAIN, 13);

    private UiTheme() {
    }

    public static JTextField crearCampoTexto(int columnas) {
        JTextField field = new JTextField(columnas);
        field.setFont(FONT_REGULAR);
        field.setForeground(TEXT_DARK);
        // Fondo blanco puro para que resalte claramente sobre el fondo amarillo clarito
        field.setBackground(Color.WHITE);
        field.setCaretColor(PRIMARY_DARK);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(INPUT_BORDER, 1),
                BorderFactory.createEmptyBorder(4, 10, 4, 10)
        ));
        int anchoPreferido = Math.max(columnas * 11, 140);
        field.setMinimumSize(new Dimension(100, 34));
        field.setPreferredSize(new Dimension(anchoPreferido, 34));
        return field;
    }

    public static <T> void estilizarCombo(JComboBox<T> combo) {
        combo.setFont(FONT_REGULAR);
        combo.setForeground(TEXT_DARK);
        combo.setBackground(Color.WHITE);
        combo.setMinimumSize(new Dimension(130, 34));
        combo.setPreferredSize(new Dimension(180, 34));
    }

    public static JLabel crearEtiqueta(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(FONT_BOLD);
        label.setForeground(TEXT_DARK);
        return label;
    }

    public static JButton crearBotonPrimario(String texto) {
        JButton button = new JButton(texto);
        button.setFont(FONT_BOLD);
        button.setBackground(PRIMARY);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorderPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return button;
    }

    public static JButton crearBotonSecundario(String texto) {
        JButton button = new JButton(texto);
        button.setFont(FONT_BOLD);
        button.setBackground(SECONDARY_BG);
        button.setForeground(PRIMARY_DARK);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PRIMARY, 1),
                BorderFactory.createEmptyBorder(6, 14, 6, 14)
        ));
        return button;
    }

    public static void estilizarCheck(JCheckBox check) {
        check.setFont(FONT_REGULAR);
        check.setForeground(TEXT_DARK);
        check.setOpaque(false);
    }

    public static <T> void estilizarLista(JList<T> lista) {
        lista.setFont(FONT_REGULAR);
        lista.setForeground(TEXT_DARK);
        lista.setBackground(Color.WHITE);
        lista.setSelectionBackground(SECONDARY_BG);
        lista.setSelectionForeground(PRIMARY_DARK);
        lista.setFixedCellHeight(28);
    }

    public static JTextArea crearAreaDetalle() {
        JTextArea area = new JTextArea(12, 28);
        area.setEditable(false);
        area.setFont(FONT_REGULAR);
        area.setForeground(TEXT_DARK);
        area.setBackground(BG_DETAIL);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        return area;
    }

    public static Border crearBordeConTitulo(String titulo) {
        TitledBorder titled = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                " " + titulo + " "
        );
        titled.setTitleFont(FONT_SUBTITLE);
        titled.setTitleColor(PRIMARY_DARK);
        return BorderFactory.createCompoundBorder(
                titled,
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        );
    }

    public static void aplicarFondoTarjeta(JPanel panel, String titulo) {
        panel.setBackground(BG_CARD);
        panel.setBorder(crearBordeConTitulo(titulo));
    }

    public static void aplicarMargenPanelPrincipal(JComponent panel) {
        panel.setBackground(BG_MAIN);
        panel.setBorder(BorderFactory.createEmptyBorder(12, 14, 12, 14));
    }
}
