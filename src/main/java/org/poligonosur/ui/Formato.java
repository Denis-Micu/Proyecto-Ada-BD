package org.poligonosur.ui;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

// Convierte importes, fechas y valores de la base de datos en texto para las pantallas.
public class Formato {

    private static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // 15.99 -> "15,99 €"
    public static String importe(BigDecimal importe) {
        if (importe == null) {
            return "";
        }
        return String.format("%.2f €", importe).replace('.', ',');
    }

    // 2026-11-12 -> "12/11/2026"
    public static String fecha(LocalDate fecha) {
        if (fecha == null) {
            return "";
        }
        return fecha.format(FECHA);
    }

    // "MENSUAL" -> "Mensual"
    public static String texto(String valor) {
        if (valor == null || valor.isEmpty()) {
            return "";
        }
        return valor.charAt(0) + valor.substring(1).toLowerCase();
    }
}
