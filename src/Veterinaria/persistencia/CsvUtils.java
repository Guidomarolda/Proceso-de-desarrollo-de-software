package Veterinaria.persistencia;

import java.util.ArrayList;
import java.util.List;

public class CsvUtils {
    private static final char SEPARADOR = ';';
    private static final char ESCAPE = '\\';

    private CsvUtils() {
    }

    public static String linea(String... valores) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < valores.length; i++) {
            if (i > 0) {
                builder.append(SEPARADOR);
            }
            builder.append(escapar(valores[i]));
        }
        return builder.toString();
    }

    public static List<String> parsear(String linea) {
        List<String> valores = new ArrayList<>();
        StringBuilder actual = new StringBuilder();
        boolean escapando = false;
        for (int i = 0; i < linea.length(); i++) {
            char caracter = linea.charAt(i);
            if (escapando) {
                actual.append(caracter);
                escapando = false;
            } else if (caracter == ESCAPE) {
                escapando = true;
            } else if (caracter == SEPARADOR) {
                valores.add(actual.toString());
                actual.setLength(0);
            } else {
                actual.append(caracter);
            }
        }
        valores.add(actual.toString());
        return valores;
    }

    private static String escapar(String valor) {
        if (valor == null) {
            return "";
        }
        return valor
                .replace("\\", "\\\\")
                .replace(";", "\\;")
                .replace("\r", " ")
                .replace("\n", " ");
    }
}
