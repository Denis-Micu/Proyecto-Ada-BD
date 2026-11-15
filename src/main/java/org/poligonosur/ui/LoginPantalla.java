package org.poligonosur.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.poligonosur.controller.UsuarioController;
import org.poligonosur.exception.DatosException;
import org.poligonosur.exception.ValidacionException;

// Pantalla de inicio de sesión (Login.fxml)
public class LoginPantalla {

    @FXML
    private TextField txtEmail;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private Label lblError;

    private final UsuarioController usuarioController = new UsuarioController();

    @FXML
    private void entrar() {
        try {
            usuarioController.iniciarSesion(txtEmail.getText(), txtPassword.getText());
            Navegacion.ir("Panel.fxml");
        } catch (ValidacionException | DatosException e) {
            lblError.setText(e.getMessage());
            // El hueco del error solo aparece cuando hay un error
            lblError.setVisible(true);
            lblError.setManaged(true);
        }
    }

    // El registro no está hecho en este prototipo, solo está en el diseño
    @FXML
    private void registrarse() {
        Navegacion.mostrarInfo("El registro todavía no está disponible");
    }
}
