package se233.gameproject.controller;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import se233.gameproject.model.Ball;
import se233.gameproject.model.Brick;
import se233.gameproject.model.GameModel;
import se233.gameproject.model.Paddle;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.text.Font;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GameController {
    private Image spriteSheet;
    @FXML
    private Canvas gameCanvas;

    @FXML
    private Label scoreLabel;
    @FXML
    private Label livesLabel;

    @FXML
    private VBox upgradeMenu;
    @FXML
    private Button upgradeBtn1;
    @FXML
    private Button upgradeBtn2;
    @FXML
    private Button upgradeBtn3;

    private GameModel gameModel;
    private GraphicsContext graphics;
    private AnimationTimer gameLoop;
    private Image paddleSprite;

    private final List<String> allUpgrades = Arrays.asList(
            "Increase Paddle Size",
            "Increase Paddle Speed",
            "Decrease Ball Speed",
            "Gain Extra Life",
            "More Points Per Brick"
    );
    private List<String> currentUpgrades;

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
            } else if (event.getCode() == KeyCode.SPACE) {
                gameModel.releaseBall();
            }
        });

        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (gameModel.isGameOver()){
                    stop();
                    drawGameOver();
                    return;
                }
                if (gameModel.isStageClear()){
                    stop();
                    drawStageClear();
                    return;
                }
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
                if (brick.getType() == 1){
                    if (brick.getHp() == 2) graphics.setFill(Color.DARKGRAY);
                    else graphics.setFill(Color.LIGHTGRAY);
                } else if (brick.getType() == 2) {
                    graphics.setFill(Color.GOLD);
                } else if (brick.getType() == 3) {
                    graphics.setFill(Color.MAGENTA);
                } else {
                    graphics.setFill(Color.CORNFLOWERBLUE);
                }

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

        graphics.setFill(Color.RED);
        for (se233.gameproject.model.Laser laser : gameModel.getLasers()) {
            graphics.fillRect(
                    laser.getX(),
                    laser.getY(),
                    laser.getWidth(),
                    laser.getHeight()
            );
        }

        for (se233.gameproject.model.Item item : gameModel.getItems()) {
            if (item.getType().equals("Laser")) {
                graphics.setFill(Color.ORANGE); // Item color
                graphics.fillRect(item.getX(), item.getY(), item.getWidth(), item.getHeight());

                graphics.setFill(Color.WHITE);
                graphics.setFont(Font.font(10));
                graphics.fillText("L", item.getX() + 6, item.getY() + 9);
            }
        }
        scoreLabel.setText("Score: " + gameModel.getScore());
        livesLabel.setText("Lives: " + gameModel.getLives());
    }

    private void drawGameOver(){
        graphics.setFill(Color.RED);
        graphics.setFont(Font.font(48));
        graphics.fillText("GAME OVER", gameCanvas.getWidth() / 2-140, gameCanvas.getHeight()/2);
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

    private void drawStageClear() {
        gameLoop.stop();

        Collections.shuffle(allUpgrades);
        currentUpgrades = allUpgrades.subList(0,3);

        upgradeBtn1.setText(currentUpgrades.get(0));
        upgradeBtn2.setText(currentUpgrades.get(1));
        upgradeBtn3.setText(currentUpgrades.get(2));

        upgradeMenu.setVisible(true);
    }
    // btn event
    @FXML
    public void selectUpgrade1() { applyUpgradeAndNextStage(0); }

    @FXML
    public void selectUpgrade2() { applyUpgradeAndNextStage(1); }

    @FXML
    public void selectUpgrade3() { applyUpgradeAndNextStage(2); }

    private void applyUpgradeAndNextStage(int buttonIndex){
        upgradeMenu.setVisible(false);

        gameModel.applyUpgrade(currentUpgrades.get(buttonIndex));

        gameLoop.start();
        gameCanvas.requestFocus();
    }

}