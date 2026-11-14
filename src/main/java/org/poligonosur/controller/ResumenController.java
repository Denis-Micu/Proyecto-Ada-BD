package org.poligonosur.controller;

import org.poligonosur.dao.ResumenDAO;
import org.poligonosur.exception.DatosException;
import org.poligonosur.exception.ValidacionException;
import org.poligonosur.model.ResumenGasto;
import org.poligonosur.model.Suscripcion;
import org.poligonosur.util.Sesion;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

// Resumen de gasto y avisos de próximos cobros. Lo usa el panel principal.
public class ResumenController {

    private final ResumenDAO resumenDAO = new ResumenDAO();

    // Gasto mensual, gasto anual y número de suscripciones activas del usuario conectado
    public ResumenGasto obtenerResumen() throws ValidacionException, DatosException {
        return resumenDAO.obtenerResumen(idUsuarioActual());
    }

    // Suscripciones activas del usuario conectado que se cobran pronto (o que ya han vencido)
    public List<Suscripcion> listarProximosCobros() throws ValidacionException, DatosException {
        return resumenDAO.listarProximosCobros(idUsuarioActual());
    }

    // Días que faltan para el cobro. Si es negativo, el cobro ya ha vencido.
    public long diasRestantes(Suscripcion s) {
        return ChronoUnit.DAYS.between(LocalDate.now(), s.getProximoPago());
    }

    // Texto del aviso para enseñarlo en el panel, por ejemplo "Netflix: se cobra en 3 días (12.99 €)"
    public String textoAviso(Suscripcion s) {
        long dias = diasRestantes(s);
        String cuando;
        if (dias < 0) {
            cuando = "venció hace " + (-dias) + (dias == -1 ? " día" : " días");
        } else if (dias == 0) {
            cuando = "se cobra hoy";
        } else {
            cuando = "se cobra en " + dias + (dias == 1 ? " día" : " días");
        }
        return s.getNombreServicio() + ": " + cuando + " (" + s.getImporte() + " €)";
    }

    // Devuelve el id del usuario conectado, o lanza un error si nadie ha iniciado sesión
    private int idUsuarioActual() throws ValidacionException {
        if (!Sesion.hayUsuario()) {
            throw new ValidacionException("Tienes que iniciar sesión");
        }
        return Sesion.getUsuario().getIdUsuario();
    }
}
