package org.poligonosur.util;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    // Cargar el archivo .env
    private static final Dotenv dotenv = Dotenv.load();

    // Obtener los datos de conexión
    private static final String HOST = dotenv.get("DB_HOST");
    private static final String USER = dotenv.get("DB_USER");
    private static final String PASSWORD = dotenv.get("DB_PASSWORD");

    // Construir la URL de conexión
    private static final String URL = "jdbc:mysql://" + HOST;

    // Obtener conexión
    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}