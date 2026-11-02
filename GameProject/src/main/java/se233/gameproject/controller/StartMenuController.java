package se233.gameproject.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.KeyCode;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import se233.gameproject.GameApplication;

import java.io.IOException;
import java.io.UncheckedIOException;

public class StartMenuController {
    @FXML
    private StackPane root;

    private boolean gameStarting;

    @FXML
    public void initialize() {
        root.setFocusTraversable(true);
        root.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.SPACE) {
                event.consume();
                try {
                    startGame();
                } catch (IOException exception) {
                    throw new UncheckedIOException(exception);
                }
            }
        });
        Platform.runLater(root::requestFocus);
    }

    @FXML
    protected void onStartButtonClick() throws IOException {
        startGame();
    }

    private void startGame() throws IOException {
        if (gameStarting) {
            return;
        }
        gameStarting = true;

        FXMLLoader loader = new FXMLLoader(GameApplication.class.getResource("hello-view.fxml"));
        Scene gameScene = new Scene(loader.load(), 800, 640);
        Stage stage = (Stage) root.getScene().getWindow();
        stage.setScene(gameScene);
    }
}
