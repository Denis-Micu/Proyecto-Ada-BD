package org.poligonosur.dao;

import org.poligonosur.exception.DatosException;
import org.poligonosur.model.ResumenGasto;
import org.poligonosur.model.Suscripcion;
import org.poligonosur.util.ConexionBD;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

// Llamadas a los procedimientos almacenados del panel principal.
public class ResumenDAO {

    // Llama a sp_resumen_gasto, que devuelve el resultado en tres parámetros OUT.
    public ResumenGasto obtenerResumen(int idUsuario) throws DatosException {
        String sql = "{call sp_resumen_gasto(?, ?, ?, ?)}";

        try (Connection con = ConexionBD.obtenerConexion();
             CallableStatement cs = con.prepareCall(sql)) {

            // El primero es de entrada y los otros tres de salida
            cs.setInt(1, idUsuario);
            cs.registerOutParameter(2, Types.DECIMAL);
            cs.registerOutParameter(3, Types.DECIMAL);
            cs.registerOutParameter(4, Types.INTEGER);

            cs.execute();

            // Después de ejecutarlo ya se pueden leer los OUT
            return new ResumenGasto(
                    cs.getBigDecimal(2),
                    cs.getBigDecimal(3),
                    cs.getInt(4));
        } catch (SQLException e) {
            throw new DatosException("No se pudo calcular el resumen de gasto", e);
        }
    }

    // Llama a sp_proximos_cobros, que devuelve filas como una consulta normal.
    // Se reutiliza Suscripcion; solo se rellenan los campos que devuelve el procedimiento.
    public List<Suscripcion> listarProximosCobros(int idUsuario) throws DatosException {
        String sql = "{call sp_proximos_cobros(?)}";
        List<Suscripcion> cobros = new ArrayList<>();

        try (Connection con = ConexionBD.obtenerConexion();
             CallableStatement cs = con.prepareCall(sql)) {

            cs.setInt(1, idUsuario);

            try (ResultSet rs = cs.executeQuery()) {
                while (rs.next()) {
                    Suscripcion s = new Suscripcion();
                    s.setIdSuscripcion(rs.getInt("id_suscripcion"));
                    s.setNombreServicio(rs.getString("servicio"));
                    s.setImporte(rs.getBigDecimal("importe"));
                    s.setProximoPago(rs.getObject("proximo_pago", LocalDate.class));
                    // dias_restantes no se guarda: el controlador lo calcula con la fecha del próximo pago
                    cobros.add(s);
                }
            }
        } catch (SQLException e) {
            throw new DatosException("No se pudieron cargar los próximos cobros", e);
        }

        return cobros;
    }
}
