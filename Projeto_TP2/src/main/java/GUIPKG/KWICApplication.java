package GUIPKG;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class KWICApplication extends Application {

    public static String textPath;

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/GUIPKG/tela.fxml"));
        stage.setScene(new Scene(root, 940, 600));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
