module se233.gameproject {
    requires javafx.controls;
    requires javafx.fxml;


    opens se233.gameproject to javafx.fxml;
    exports se233.gameproject;
}