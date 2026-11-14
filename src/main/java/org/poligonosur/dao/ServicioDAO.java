package org.poligonosur.dao;

import org.poligonosur.exception.DatosException;
import org.poligonosur.model.Servicio;
import org.poligonosur.util.ConexionBD;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

// Acceso a la tabla servicio.
public class ServicioDAO {

    // Devuelve todos los servicios ordenados por nombre, para rellenar los desplegables.
    // Usa un Statement normal porque la consulta no lleva ningún dato del usuario.
    public List<Servicio> listar() throws DatosException {
        String sql = "SELECT id_servicio, id_categoria, nombre, url, logo FROM servicio ORDER BY nombre";
        List<Servicio> servicios = new ArrayList<>();

        try (Connection con = ConexionBD.obtenerConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Servicio s = new Servicio(
                        rs.getInt("id_servicio"),
                        // getObject y no getInt: si la categoría es NULL, getInt devolvería 0
                        rs.getObject("id_categoria", Integer.class),
                        rs.getString("nombre"),
                        rs.getString("url"),
                        rs.getString("logo"));
                servicios.add(s);
            }
        } catch (SQLException e) {
            throw new DatosException("No se pudieron cargar los servicios", e);
        }

        return servicios;
    }
}
