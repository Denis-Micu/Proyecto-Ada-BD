package org.poligonosur.dao;

import org.poligonosur.exception.DatosException;
import org.poligonosur.model.Pago;
import org.poligonosur.util.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Acceso a la tabla pago.
public class PagoDAO {

    // Devuelve el historial de pagos de una suscripción, del más reciente al más antiguo.
    public List<Pago> listarPorSuscripcion(int idSuscripcion) throws DatosException {
        String sql = "SELECT id_pago, id_suscripcion, fecha_pago, importe, estado "
                + "FROM pago WHERE id_suscripcion = ? ORDER BY fecha_pago DESC";
        List<Pago> pagos = new ArrayList<>();

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idSuscripcion);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Pago p = new Pago(
                            rs.getInt("id_pago"),
                            rs.getInt("id_suscripcion"),
                            rs.getObject("fecha_pago", LocalDate.class),
                            rs.getBigDecimal("importe"),
                            rs.getString("estado"));
                    pagos.add(p);
                }
            }
        } catch (SQLException e) {
            throw new DatosException("No se pudo cargar el historial de pagos", e);
        }

        return pagos;
    }

    // Registra un pago y avanza la fecha del próximo pago de la suscripción.
    // Son dos cambios que tienen que hacerse los dos o ninguno, por eso van en una transacción.
    // La nueva fecha la calcula el controlador según la periodicidad.
    public void registrarPago(Pago pago, LocalDate nuevoProximoPago) throws DatosException {
        String sqlPago = "INSERT INTO pago (id_suscripcion, fecha_pago, importe, estado) VALUES (?, ?, ?, ?)";
        String sqlSuscripcion = "UPDATE suscripcion SET proximo_pago = ? WHERE id_suscripcion = ?";

        try (Connection con = ConexionBD.obtenerConexion()) {
            // Desactivamos el autocommit para que nada se guarde hasta el commit
            con.setAutoCommit(false);

            try (PreparedStatement psPago = con.prepareStatement(sqlPago);
                 PreparedStatement psSuscripcion = con.prepareStatement(sqlSuscripcion)) {

                // 1. Guardar el pago
                psPago.setInt(1, pago.getIdSuscripcion());
                psPago.setObject(2, pago.getFechaPago());
                psPago.setBigDecimal(3, pago.getImporte());
                psPago.setString(4, pago.getEstado());
                psPago.executeUpdate();

                // 2. Avanzar el próximo pago de la suscripción
                psSuscripcion.setObject(1, nuevoProximoPago);
                psSuscripcion.setInt(2, pago.getIdSuscripcion());
                psSuscripcion.executeUpdate();

                // Si los dos han ido bien, se guardan a la vez
                con.commit();
            } catch (SQLException e) {
                // Si algo falla, se deshace todo, también el pago si ya se había insertado
                con.rollback();
                throw new DatosException("No se pudo registrar el pago", e);
            }
        } catch (SQLException e) {
            // Aquí llegan los errores al conectar, al desactivar el autocommit, al hacer el rollback o al cerrar
            throw new DatosException("No se pudo registrar el pago", e);
        }
    }
}
