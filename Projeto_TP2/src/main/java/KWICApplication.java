import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class KWICApplication extends Application {

    public static String textPath;
    public static String stopWordPath;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/GUIPKG/tela.fxml"));
        Parent root = loader.load();

        stage.setTitle("Keyword in Context (KWIC) Application");
        stage.setScene(new Scene(root, 940, 600));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
