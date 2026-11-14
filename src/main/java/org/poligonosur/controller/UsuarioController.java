package org.poligonosur.controller;

import org.poligonosur.dao.UsuarioDAO;
import org.poligonosur.exception.DatosException;
import org.poligonosur.exception.ValidacionException;
import org.poligonosur.model.Usuario;
import org.poligonosur.util.Sesion;

// Inicio y cierre de sesión. Es lo que llama la pantalla de login.
public class UsuarioController {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    // Comprueba los datos, busca el usuario y, si existe, lo guarda en la sesión.
    // Lanza ValidacionException si falta algún campo o si el email o la contraseña no son correctos.
    public Usuario iniciarSesion(String email, String password) throws ValidacionException, DatosException {
        if (email == null || email.isBlank()) {
            throw new ValidacionException("Escribe el email");
        }
        if (password == null || password.isEmpty()) {
            throw new ValidacionException("Escribe la contraseña");
        }

        // Quitamos los espacios del email por si se han copiado sin querer
        Usuario usuario = usuarioDAO.iniciarSesion(email.trim(), password);

        if (usuario == null) {
            throw new ValidacionException("Email o contraseña incorrectos");
        }

        Sesion.iniciar(usuario);
        return usuario;
    }

    // Cierra la sesión del usuario actual
    public void cerrarSesion() {
        Sesion.cerrar();
    }
}
