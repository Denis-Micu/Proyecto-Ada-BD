package org.poligonosur;

import javafx.application.Application;
import org.poligonosur.ui.App;

// Punto de entrada del programa.
// No hereda de Application para que se pueda ejecutar desde el IDE sin configurar nada de JavaFX.
public class Main {

    public static void main(String[] args) {
        Application.launch(App.class, args);
    }
}
