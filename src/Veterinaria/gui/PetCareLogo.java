package Veterinaria.gui;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

/**
 * Componente grafico vectorial (Java2D) que renderiza el logo oficial de "PetCare Plus"
 * en la esquina superior de la interfaz, el icono de la ventana y el avatar por defecto de mascotas.
 */
public class PetCareLogo extends JPanel {

    public PetCareLogo() {
        setOpaque(false);
        setPreferredSize(new Dimension(250, 56));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        configurarCalidad(g2);

        // 1. Dibujar el emblema circular de la veterinaria (huella + cruz medica)
        dibujarEmblema(g2, 4, 4, 48);

        // 2. Nombre principal de la veterinaria "PetCare+"
        g2.setFont(new Font("Segoe UI", Font.BOLD, 22));
        g2.setColor(Color.WHITE);
        g2.drawString("PetCare", 62, 28);

        // El signo "+" en verde menta brillante
        g2.setColor(new Color(110, 235, 195));
        g2.drawString("+", 147, 28);

        // 3. Subtitulo "CLINICA VETERINARIA"
        g2.setFont(new Font("Segoe UI", Font.BOLD, 10));
        g2.setColor(new Color(200, 235, 240));
        g2.drawString("CLINICA VETERINARIA", 63, 44);

        g2.dispose();
    }

    /**
     * Genera una imagen del emblema para usar como icono de la ventana (JFrame.setIconImage).
     */
    public static Image crearIconoVentana() {
        int size = 64;
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        configurarCalidad(g2);
        dibujarEmblema(g2, 2, 2, size - 4);
        g2.dispose();
        return img;
    }

    /**
     * Genera un avatar ilustrado por defecto cuando una mascota no tiene fotografia cargada.
     */
    public static ImageIcon crearAvatarMascota(String tipoMascota, int size) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        configurarCalidad(g2);

        // Fondo suave redondeado
        g2.setColor(new Color(235, 246, 248));
        g2.fillRoundRect(0, 0, size, size, 16, 16);

        // Medallon central con la huella
        int medalSize = (int) (size * 0.56);
        int mx = (size - medalSize) / 2;
        int my = (int) (size * 0.12);
        dibujarEmblema(g2, mx, my, medalSize);

        // Etiqueta inferior con el tipo de mascota
        String etiqueta = (tipoMascota == null || tipoMascota.isEmpty()) ? "MASCOTA" : tipoMascota;
        g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
        g2.setColor(UiTheme.PRIMARY_DARK);
        FontMetrics fm = g2.getFontMetrics();
        int tx = (size - fm.stringWidth(etiqueta)) / 2;
        int ty = (int) (size * 0.84);
        g2.drawString(etiqueta, tx, ty);

        g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        g2.setColor(UiTheme.TEXT_MUTED);
        String sub = "Sin foto cargada";
        int sx = (size - g2.getFontMetrics().stringWidth(sub)) / 2;
        g2.drawString(sub, sx, ty + 14);

        g2.dispose();
        return new ImageIcon(img);
    }

    private static void configurarCalidad(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    }

    private static void dibujarEmblema(Graphics2D g2, int x, int y, int size) {
        // Fondo circular blanco con degradado suave
        GradientPaint gp = new GradientPaint(x, y, Color.WHITE, x + size, y + size, new Color(225, 248, 245));
        g2.setPaint(gp);
        g2.fill(new Ellipse2D.Double(x, y, size, size));

        // Borde circular verde agua brillante
        g2.setColor(new Color(95, 220, 185));
        g2.setStroke(new BasicStroke(2.5f));
        g2.draw(new Ellipse2D.Double(x + 1, y + 1, size - 2, size - 2));

        // Cruz veterinaria sutil en la esquina superior derecha del medallon
        double cx = x + size * 0.72;
        double cy = y + size * 0.24;
        double arm = size * 0.14;
        double thick = size * 0.06;
        g2.setColor(new Color(40, 175, 140));
        g2.fill(new RoundRectangle2D.Double(cx - arm / 2, cy - thick / 2, arm, thick, 2, 2));
        g2.fill(new RoundRectangle2D.Double(cx - thick / 2, cy - arm / 2, thick, arm, 2, 2));

        // Huella de mascota (Paw print) en color Teal principal
        g2.setColor(UiTheme.PRIMARY_DARK);

        // Almohadilla central
        double padW = size * 0.36;
        double padH = size * 0.28;
        double padX = x + (size - padW) / 2.0;
        double padY = y + size * 0.50;
        g2.fill(new Ellipse2D.Double(padX, padY, padW, padH));

        // 4 deditos superiores de la huella
        double toeW = size * 0.13;
        double toeH = size * 0.17;
        g2.fill(new Ellipse2D.Double(x + size * 0.20, y + size * 0.34, toeW, toeH));
        g2.fill(new Ellipse2D.Double(x + size * 0.35, y + size * 0.24, toeW, toeH));
        g2.fill(new Ellipse2D.Double(x + size * 0.51, y + size * 0.24, toeW, toeH));
        g2.fill(new Ellipse2D.Double(x + size * 0.65, y + size * 0.34, toeW, toeH));
    }
}
