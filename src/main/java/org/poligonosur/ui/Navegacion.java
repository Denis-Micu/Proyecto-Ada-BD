package org.poligonosur.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

// Cambia de pantalla dentro de la misma ventana y enseña mensajes al usuario.
public class Navegacion {

    private static Stage ventana;

    public static void setVentana(Stage stage) {
        ventana = stage;
    }

    // Carga el FXML, lo pone en la ventana y devuelve su controlador
    // para poder pasarle datos (por ejemplo la suscripción que se va a ver)
    public static <T> T ir(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(Navegacion.class.getResource(fxml));
            Parent raiz = loader.load();
            Scene escena = new Scene(raiz, 1000, 700);
            // La misma hoja de estilos para todas las pantallas
            escena.getStylesheets().add(Navegacion.class.getResource("estilos.css").toExternalForm());
            ventana.setScene(escena);
            return loader.getController();
        } catch (IOException e) {
            throw new RuntimeException("No se pudo abrir la pantalla " + fxml, e);
        }
    }

    public static void mostrarError(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR, mensaje);
        alerta.setHeaderText(null);
        alerta.showAndWait();
    }

    public static void mostrarInfo(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION, mensaje);
        alerta.setHeaderText(null);
        alerta.showAndWait();
    }

    // Pregunta sí o no. Devuelve true si el usuario pulsa Aceptar.
    public static boolean confirmar(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION, mensaje);
        alerta.setHeaderText(null);
        return alerta.showAndWait().filter(b -> b.getButtonData().isDefaultButton()).isPresent();
    }
}
