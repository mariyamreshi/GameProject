package se233.gameproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.io.IOException;

public class GameApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                GameApplication.class.getResource("hello-view.fxml")
        );

        Scene scene = new Scene(loader.load(), 800, 640);

        stage.setTitle("Brick Breaker");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}