package org.poligonosur.util;

import org.poligonosur.model.Usuario;

// Guarda el usuario que ha iniciado sesión mientras la aplicación está abierta.
// Es estática para que cualquier controlador pueda saber quién es el usuario sin pasárselo cada vez.
public class Sesion {

    private static Usuario usuarioActual;

    // Se llama al iniciar sesión correctamente
    public static void iniciar(Usuario usuario) {
        usuarioActual = usuario;
    }

    // Se llama al cerrar sesión
    public static void cerrar() {
        usuarioActual = null;
    }

    // Devuelve el usuario conectado, o null si no hay ninguno
    public static Usuario getUsuario() {
        return usuarioActual;
    }

    // true si hay alguien con la sesión iniciada
    public static boolean hayUsuario() {
        return usuarioActual != null;
    }
}
