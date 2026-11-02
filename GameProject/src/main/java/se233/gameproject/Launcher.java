package se233.gameproject;

import javafx.application.Application;

public class Launcher {
    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler(
                (thread, exception) -> GameExceptionHandler.handle(exception, null)
        );

        try {
            Application.launch(GameApplication.class, args);
        } catch (RuntimeException exception) {
            GameExceptionHandler.handle(exception, null);
        }
    }
}
