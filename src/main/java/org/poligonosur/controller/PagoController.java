package org.poligonosur.controller;

import org.poligonosur.dao.PagoDAO;
import org.poligonosur.dao.SuscripcionDAO;
import org.poligonosur.exception.DatosException;
import org.poligonosur.exception.ValidacionException;
import org.poligonosur.model.Pago;
import org.poligonosur.model.Suscripcion;
import org.poligonosur.util.Sesion;

import java.time.LocalDate;
import java.util.List;

// Historial de pagos y registro de un pago. Lo usa la pantalla de detalle de suscripción.
public class PagoController {

    private final PagoDAO pagoDAO = new PagoDAO();
    private final SuscripcionDAO suscripcionDAO = new SuscripcionDAO();

    // Devuelve los pagos de una suscripción del usuario conectado
    public List<Pago> listarHistorial(int idSuscripcion) throws ValidacionException, DatosException {
        // Comprobamos que la suscripción es suya antes de enseñar los pagos
        buscarSuscripcionDelUsuario(idSuscripcion);
        return pagoDAO.listarPorSuscripcion(idSuscripcion);
    }

    // Registra el pago de hoy por el importe de la suscripción y avanza el próximo pago.
    // Devuelve la nueva fecha del próximo pago para enseñarla en la pantalla.
    public LocalDate registrarPago(int idSuscripcion) throws ValidacionException, DatosException {
        // Se vuelve a leer de la base de datos para no avanzar desde una fecha antigua
        Suscripcion s = buscarSuscripcionDelUsuario(idSuscripcion);

        if (!"ACTIVA".equals(s.getEstado())) {
            throw new ValidacionException("Solo se pueden pagar suscripciones activas");
        }

        Pago pago = new Pago(0, idSuscripcion, LocalDate.now(), s.getImporte(), "PAGADO");
        LocalDate nuevoProximoPago = calcularSiguientePago(s.getProximoPago(), s.getPeriodicidad());

        // El DAO guarda el pago y la nueva fecha en una transacción
        pagoDAO.registrarPago(pago, nuevoProximoPago);
        return nuevoProximoPago;
    }

    // Suma a la fecha los meses que tocan según la periodicidad
    private LocalDate calcularSiguientePago(LocalDate fecha, String periodicidad) throws ValidacionException {
        switch (periodicidad) {
            case "MENSUAL":
                return fecha.plusMonths(1);
            case "TRIMESTRAL":
                return fecha.plusMonths(3);
            case "SEMESTRAL":
                return fecha.plusMonths(6);
            case "ANUAL":
                return fecha.plusYears(1);
            default:
                throw new ValidacionException("Periodicidad desconocida: " + periodicidad);
        }
    }

    // Busca la suscripción entre las del usuario conectado.
    // Si no está es que no existe o que es de otro usuario.
    private Suscripcion buscarSuscripcionDelUsuario(int idSuscripcion) throws ValidacionException, DatosException {
        if (!Sesion.hayUsuario()) {
            throw new ValidacionException("Tienes que iniciar sesión");
        }

        List<Suscripcion> suscripciones = suscripcionDAO.listarPorUsuario(Sesion.getUsuario().getIdUsuario());
        for (Suscripcion s : suscripciones) {
            if (s.getIdSuscripcion() == idSuscripcion) {
                return s;
            }
        }
        throw new ValidacionException("No se encontró la suscripción");
    }
}
