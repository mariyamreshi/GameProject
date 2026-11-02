module se233.gameproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.logging.log4j;

    opens se233.gameproject to javafx.fxml;
    opens se233.gameproject.controller to javafx.fxml;

    exports se233.gameproject;
}
