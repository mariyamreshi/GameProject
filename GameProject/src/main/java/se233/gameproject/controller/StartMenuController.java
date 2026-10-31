package se233.gameproject.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import se233.gameproject.GameApplication;

import java.io.IOException;

public class StartMenuController {
    @FXML
    protected void onStartButtonClick(ActionEvent event) throws IOException{
        // Load Main game
        FXMLLoader loader = new FXMLLoader(GameApplication.class.getResource("hello-view.fxml"));
        Scene gameScene = new Scene(loader.load(), 800, 640);
        // pull current stage and change scene
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(gameScene);
    }
}
