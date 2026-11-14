package org.poligonosur.dao;

import org.poligonosur.exception.DatosException;
import org.poligonosur.model.Usuario;
import org.poligonosur.util.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

// Acceso a la tabla usuario.
public class UsuarioDAO {

    // Busca un usuario con ese email y contraseña. Si no coincide ninguno devuelve null.
    // Usa PreparedStatement porque el email y la contraseña los escribe el usuario.
    public Usuario iniciarSesion(String email, String password) throws DatosException {
        String sql = "SELECT id_usuario, nombre, email, password, fecha_registro "
                + "FROM usuario WHERE email = ? AND password = ?";

        try (Connection con = ConexionBD.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, password);

            // El ResultSet va en su propio try porque se crea después de poner los parámetros
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                            rs.getInt("id_usuario"),
                            rs.getString("nombre"),
                            rs.getString("email"),
                            rs.getString("password"),
                            rs.getObject("fecha_registro", LocalDateTime.class));
                }
            }
        } catch (SQLException e) {
            throw new DatosException("No se pudo comprobar el inicio de sesión", e);
        }

        // No hay ningún usuario con ese email y contraseña
        return null;
    }
}
