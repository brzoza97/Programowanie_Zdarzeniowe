package MP3Player.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {        //rozszerzamy klasę Application
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Pane mainPane = FXMLLoader.load(getClass().getResource("/fxml/mainPane.fxml"));
        Scene scene = new Scene(mainPane);
        stage.getIcons().add(new Image("/img/icon.png"));
        stage.setScene(scene);
        stage.setTitle("Odtwarzacz MP3");
        stage.show();
    }
}