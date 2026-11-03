package se233.gameproject;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.Window;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** Handles application errors in one place: logs the cause and informs the player. */
public final class GameExceptionHandler {
    private static final Logger logger = LogManager.getLogger(GameExceptionHandler.class);

    private GameExceptionHandler() {
    }
//receive message and throw error
    public static void handle(Throwable exception, Window owner) {
        Throwable cause = rootCause(exception);
        String message = cause.getMessage();
        //make sure there is message, then log
        if (message == null || message.isBlank()) {
            message = cause.getClass().getSimpleName();
        }

        logger.error("The game could not continue: {}", message, exception);

        String alertMessage = message;
        //error dialog, displays error msg
        Runnable showAlert = () -> {
            try {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Game Error");
                alert.setHeaderText("The game ran into a problem.");
                alert.setContentText(alertMessage);
                if (owner != null && owner.isShowing()) {
                    alert.initOwner(owner);
                }
                //shows and waits for player to close it
                alert.showAndWait();
            } catch (RuntimeException alertFailure) {
                // Logging above still records the original error if JavaFX cannot show a dialog.
                logger.error("Could not display the game error dialog", alertFailure);
            }
        };
// show dialog safely
        if (Platform.isFxApplicationThread()) {
            showAlert.run();
        } else {
            try {
                Platform.runLater(showAlert);
            } catch (IllegalStateException toolkitNotReady) {
                logger.error("JavaFX was not ready to display the game error dialog", toolkitNotReady);
            }
        }
    }

    private static Throwable rootCause(Throwable exception) {
        Throwable cause = exception;
        while (cause.getCause() != null && cause.getCause() != cause) {
            cause = cause.getCause();
        }
        return cause;
    }
}
