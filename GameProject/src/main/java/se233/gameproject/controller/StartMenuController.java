package se233.gameproject.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.KeyCode;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import se233.gameproject.GameApplication;
import se233.gameproject.GameExceptionHandler;

import java.io.IOException;
// Open the game screen when the player starts from the menu
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
                    gameStarting = false;
                    GameExceptionHandler.handle(exception, root.getScene().getWindow());
                }
            }
        });
        Platform.runLater(root::requestFocus);
    }

    @FXML
    protected void onStartButtonClick() {
        try {
            startGame();
        } catch (IOException exception) {
            gameStarting = false;
            GameExceptionHandler.handle(exception, root.getScene().getWindow());
        }
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
