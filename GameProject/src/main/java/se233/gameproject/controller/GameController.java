package se233.gameproject.controller;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import se233.gameproject.model.Ball;
import se233.gameproject.model.Brick;
import se233.gameproject.model.GameModel;
import se233.gameproject.model.Paddle;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

public class GameController {
    private Image spriteSheet;
    @FXML
    private Canvas gameCanvas;

    @FXML
    private Label scoreLabel;

    @FXML
    private Label livesLabel;

    private GameModel gameModel;
    private GraphicsContext graphics;
    private AnimationTimer gameLoop;
    private Image paddleSprite;

    @FXML
    public void initialize() {
        gameModel = new GameModel();
        graphics = gameCanvas.getGraphicsContext2D();
        var imageUrl = getClass().getResource(
                "/se233/gameproject/images/vaus.png"
        );

        if (imageUrl == null) {
            throw new IllegalStateException("Could not find vaus.png");
        }

        spriteSheet = new Image(imageUrl.toExternalForm());
        paddleSprite = createPaddleSprite();

        gameCanvas.setFocusTraversable(true);
        gameCanvas.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.LEFT) {
                gameModel.getPaddle().moveLeft(0);
            } else if (event.getCode() == KeyCode.RIGHT) {
                gameModel.getPaddle().moveRight(gameModel.getGameWidth());
            }
        });

        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                gameModel.update();
                drawGame();
            }
        };

        drawGame();
        gameLoop.start();
        gameCanvas.requestFocus();
    }

    private void drawGame() {
        graphics.setFill(Color.web("#101626"));
        graphics.fillRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());

        for (Brick brick : gameModel.getBricks()) {
            if (!brick.isDestroyed()) {
                graphics.setFill(Color.CORNFLOWERBLUE);
                graphics.fillRect(
                        brick.getX(),
                        brick.getY(),
                        brick.getWidth(),
                        brick.getHeight()
                );
            }
        }

        Paddle paddle = gameModel.getPaddle();
        graphics.drawImage(
                paddleSprite,
                paddle.getX(),
                paddle.getY(),
                paddle.getWidth(),
                paddle.getHeight()
        );

        Ball ball = gameModel.getBall();
        graphics.setFill(Color.ORANGE);
        graphics.fillOval(
                ball.getX() - ball.getRadius(),
                ball.getY() - ball.getRadius(),
                ball.getRadius() * 2,
                ball.getRadius() * 2
        );

        scoreLabel.setText("Score: " + gameModel.getScore());
        livesLabel.setText("Lives: " + gameModel.getLives());
    }
    private Image createPaddleSprite() {
        int sourceX = 177;
        int sourceY = 17;
        int sourceWidth = 31;
        int sourceHeight = 8;

        PixelReader reader = spriteSheet.getPixelReader();
        WritableImage sprite = new WritableImage(sourceWidth, sourceHeight);
        PixelWriter writer = sprite.getPixelWriter();

        for (int y = 0; y < sourceHeight; y++) {
            for (int x = 0; x < sourceWidth; x++) {
                var pixel = reader.getColor(sourceX + x, sourceY + y);

                // Turn the sheet's solid green background transparent.
                if (pixel.getGreen() > 0.25
                        && pixel.getRed() < 0.1
                        && pixel.getBlue() < 0.1) {
                    writer.setColor(x, y, javafx.scene.paint.Color.TRANSPARENT);
                } else {
                    writer.setColor(x, y, pixel);
                }
            }
        }

        return sprite;
    }
}