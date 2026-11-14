package org.poligonosur.dao;

import org.poligonosur.exception.DatosException;
import org.poligonosur.model.Categoria;
import org.poligonosur.util.ConexionBD;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

// Acceso a la tabla categoria.
public class CategoriaDAO {

    // Devuelve todas las categorías ordenadas por nombre, para rellenar los desplegables.
    // Usa un Statement normal porque la consulta no lleva ningún dato del usuario.
    public List<Categoria> listar() throws DatosException {
        String sql = "SELECT id_categoria, nombre, color FROM categoria ORDER BY nombre";
        List<Categoria> categorias = new ArrayList<>();

        try (Connection con = ConexionBD.obtenerConexion();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Categoria c = new Categoria(
                        rs.getInt("id_categoria"),
                        rs.getString("nombre"),
                        rs.getString("color"));
                categorias.add(c);
            }
        } catch (SQLException e) {
            throw new DatosException("No se pudieron cargar las categorías", e);
        }

        return categorias;
    }
}
