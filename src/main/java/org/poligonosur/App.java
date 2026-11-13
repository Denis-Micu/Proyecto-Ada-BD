package org.poligonosur;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.util.Objects;

public class App extends Application {
        @Override
        public void start(Stage stage) throws Exception {

            Parent root = FXMLLoader.load(
                    Objects.requireNonNull(
                            getClass().getResource("/org/poligonosur/ui/Login.fxml")
                    )
            );

            Scene scene = new Scene(root);

            stage.setTitle("Login");
            stage.setScene(scene);
            stage.show();
        }

        public static void main(String[] args) {
            launch(args);
        }
}
