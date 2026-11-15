package org.poligonosur.ui;

import javafx.application.Application;
import javafx.stage.Stage;

// Aplicación JavaFX. Abre la ventana y empieza por el inicio de sesión.
public class App extends Application {

    @Override
    public void start(Stage stage) {
        Navegacion.setVentana(stage);
        stage.setTitle("Gestor de suscripciones");
        Navegacion.ir("Login.fxml");
        stage.show();
    }
}
