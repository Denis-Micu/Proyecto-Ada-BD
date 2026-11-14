package org.poligonosur.dao;

import org.poligonosur.exception.DatosException;
import org.poligonosur.model.Suscripcion;
import org.poligonosur.util.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Acceso a la tabla suscripcion.
public class SuscripcionDAO {

    // Devuelve las suscripciones de un usuario, ordenadas por la fecha del próximo pago.
    // Hace un JOIN con servicio para tener también el nombre del servicio.
    public List<Suscripcion> listarPorUsuario(int idUsuario) throws DatosException {
        String sql = "SELECT su.id_suscripcion, su.id_usuario, su.id_servicio, su.importe, su.periodicidad, "
                + "su.fecha_inicio, su.proximo_pago, su.estado, su.dias_aviso, su.notas, "
                + "se.nombre AS nombre_servicio "
                + "FROM suscripcion su "
                + "JOIN servicio se ON se.id_servicio = su.id_servicio "
                + "WHERE su.id_usuario = ? "
                + "ORDER BY su.proximo_pago";
        List<Suscripcion> suscripciones = new ArrayList<>();

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Suscripcion s = new Suscripcion(
                            rs.getInt("id_suscripcion"),
                            rs.getInt("id_usuario"),
                            rs.getInt("id_servicio"),
                            rs.getBigDecimal("importe"),
                            rs.getString("periodicidad"),
                            rs.getObject("fecha_inicio", LocalDate.class),
                            rs.getObject("proximo_pago", LocalDate.class),
                            rs.getString("estado"),
                            rs.getInt("dias_aviso"),
                            rs.getString("notas"));
                    s.setNombreServicio(rs.getString("nombre_servicio"));
                    suscripciones.add(s);
                }
            }
        } catch (SQLException e) {
            throw new DatosException("No se pudieron cargar las suscripciones", e);
        }

        return suscripciones;
    }

    // Guarda una suscripción nueva. El id lo pone la base de datos (AUTO_INCREMENT).
    public void insertar(Suscripcion s) throws DatosException {
        String sql = "INSERT INTO suscripcion (id_usuario, id_servicio, importe, periodicidad, "
                + "fecha_inicio, proximo_pago, estado, dias_aviso, notas) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, s.getIdUsuario());
            ps.setInt(2, s.getIdServicio());
            ps.setBigDecimal(3, s.getImporte());
            ps.setString(4, s.getPeriodicidad());
            ps.setObject(5, s.getFechaInicio());
            ps.setObject(6, s.getProximoPago());
            ps.setString(7, s.getEstado());
            ps.setInt(8, s.getDiasAviso());
            ps.setString(9, s.getNotas());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatosException("No se pudo guardar la suscripción", e);
        }
    }

    // Cambia los datos de una suscripción.
    // En el WHERE también va el usuario, para que nadie pueda modificar una suscripción que no es suya.
    public void modificar(Suscripcion s) throws DatosException {
        String sql = "UPDATE suscripcion SET id_servicio = ?, importe = ?, periodicidad = ?, "
                + "fecha_inicio = ?, proximo_pago = ?, estado = ?, dias_aviso = ?, notas = ? "
                + "WHERE id_suscripcion = ? AND id_usuario = ?";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, s.getIdServicio());
            ps.setBigDecimal(2, s.getImporte());
            ps.setString(3, s.getPeriodicidad());
            ps.setObject(4, s.getFechaInicio());
            ps.setObject(5, s.getProximoPago());
            ps.setString(6, s.getEstado());
            ps.setInt(7, s.getDiasAviso());
            ps.setString(8, s.getNotas());
            ps.setInt(9, s.getIdSuscripcion());
            ps.setInt(10, s.getIdUsuario());

            // Si no se ha cambiado ninguna fila es que la suscripción no existe (o no es de ese usuario)
            if (ps.executeUpdate() == 0) {
                throw new DatosException("No se encontró la suscripción");
            }
        } catch (SQLException e) {
            throw new DatosException("No se pudo modificar la suscripción", e);
        }
    }
}
