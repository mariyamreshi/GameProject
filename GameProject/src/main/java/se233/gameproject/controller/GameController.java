package se233.gameproject.controller;

import javafx.animation.AnimationTimer;
import javafx.animation.Animation;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
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
import javafx.util.Duration;

import java.util.*;

public class GameController {
    @FXML private Canvas gameCanvas;
    @FXML private Label scoreLabel;
    @FXML private Label levelLabel;
    @FXML private HBox heartsBox;
    @FXML private VBox upgradeMenu;
    @FXML private Button upgradeBtn1, upgradeBtn2, upgradeBtn3;

    private GameModel gameModel;
    private GraphicsContext graphics;
    private AnimationTimer gameLoop;
    private int displayedLives = -1;

    private final List<String> allUpgrades = Arrays.asList(
            "Increase Paddle Size", "Increase Paddle Speed", "Decrease Ball Speed",
            "Gain Extra Life", "More Points Per Brick"
    );
    private List<String> currentUpgrades;

    // Standalone image variables
    private Image bgImage, ballImage, laserProjectile, paddleNormal, laserItemSprite;
    private Image normBrick, strongBrick, strongCrack, bonusBrick, specialBrick;

    // Arrays for break animation frames
    private Image[] normBreak = new Image[4];
    private Image[] strongCrackBreak = new Image[4];
    private Image[] bonusBreak = new Image[4];
    private Image[] specialBreak = new Image[4];

    // Inner class and variables for managing animations independently from the Model
    private Set<Brick> knownBrokenBricks = new HashSet<>();
    private List<BrokenAnimation> activeAnimations = new ArrayList<>();

    private class BrokenAnimation {
        double x, y;
        int type;
        int tick = 0;
        BrokenAnimation(double x, double y, int type) {
            this.x = x; this.y = y; this.type = type;
        }
    }

    @FXML
    public void initialize() {
        gameModel = new GameModel();
        graphics = gameCanvas.getGraphicsContext2D();
        graphics.setImageSmoothing(false); // Disable smoothing for crisp pixel art

        loadAllImages();

        gameCanvas.setFocusTraversable(true);
        gameCanvas.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.LEFT) gameModel.getPaddle().moveLeft(0);
            else if (event.getCode() == KeyCode.RIGHT) gameModel.getPaddle().moveRight(gameModel.getGameWidth());
            else if (event.getCode() == KeyCode.SPACE) gameModel.releaseBall();
        });

        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (gameModel.isGameOver()){
                    stop();
                    gameCanvas.setRotate(0);
                    drawGameOver();
                    return;
                }
                if (gameModel.isStageClear()){
                    stop();
                    gameCanvas.setRotate(0);
                    drawStageClear();
                    return;
                }
                gameModel.update();

                int dizzyFrames = gameModel.getDizzyActiveFrames();
                if (dizzyFrames > 0) {
                    double elapsedFrames = GameModel.DIZZY_DURATION_FRAMES - dizzyFrames;
                    gameCanvas.setRotate(
                            elapsedFrames * 720.0 / GameModel.DIZZY_DURATION_FRAMES
                    );
                } else {
                    gameCanvas.setRotate(0);
                }

                drawGame();
            }
        };

        drawGame();
        gameLoop.start();
        gameCanvas.requestFocus();
    }

    // Helper method to safely load images and alert if a file is missing
    private Image loadImageSafe(String relativePath, double width, double height) {
        java.net.URL url = getClass().getResource(relativePath);
        if (url == null) {
            System.err.println("❌ File not Found: " + relativePath);
            throw new IllegalStateException("Cannot find image: " + relativePath);
        }
        // If width and height are 0, use the original image size
        if (width == 0 && height == 0) {
            return new Image(url.toExternalForm());
        }
        return new Image(url.toExternalForm(), width, height, false, false);
    }

    private void loadAllImages() {
        String path = "/se233/gameproject/images/";
        double bw = 80, bh = 25; // Standard brick dimensions

        // 1. Load background, ball, laser, and paddle
        bgImage = loadImageSafe(path + "backgrounds/bg3_lavender_stars.png", 800, 600);
        ballImage = loadImageSafe(path + "extras/ball.png", 15, 15);
        laserProjectile = loadImageSafe(path + "extras/laser.png", 10, 20);
        paddleNormal = loadImageSafe(path + "paddle/paddle_normal.png", 100, 20);

        // 2. Load normal state bricks
        normBrick = loadImageSafe(path + "bricks/brick_normal.png", bw, bh);
        strongBrick = loadImageSafe(path + "bricks/brick_strong.png", bw, bh);
        strongCrack = loadImageSafe(path + "bricks/brick_strongcrack.png", bw, bh);
        bonusBrick = loadImageSafe(path + "bricks/brick_bonus.png", bw, bh);
        specialBrick = loadImageSafe(path + "bricks/brick_special.png", bw, bh);

        // 3. Load brick breaking animation frames (4 frames per type)
        for (int i = 0; i < 4; i++) {
            normBreak[i] = loadImageSafe(path + "bricks/break/normal_break_" + i + ".png", bw, bh);
            strongCrackBreak[i] = loadImageSafe(path + "bricks/break/strongcrack_break_" + i + ".png", bw, bh);
            bonusBreak[i] = loadImageSafe(path + "bricks/break/bonus_break_" + i + ".png", bw, bh);
            specialBreak[i] = loadImageSafe(path + "bricks/break/special_break_" + i + ".png", bw, bh);
        }

        // 4. Load item capsule sprite
        Image powerupsSheet = loadImageSafe(path + "Powerups.png", 0, 0);
        laserItemSprite = cropSprite(powerupsSheet, 0, 16, 16, 8);
    }

    private void drawGame() {
        // Draw background
        graphics.drawImage(bgImage, 0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());

        // Draw normal bricks or queue break animations if destroyed
        for (Brick brick : gameModel.getBricks()) {
            if (!brick.isDestroyed()) {
                Image currentBrickImg = normBrick;
                if (brick.getType() == 1) currentBrickImg = (brick.getHp() == 2) ? strongBrick : strongCrack;
                else if (brick.getType() == 2) currentBrickImg = bonusBrick;
                else if (brick.getType() == 3) currentBrickImg = specialBrick;

                graphics.drawImage(currentBrickImg, brick.getX(), brick.getY(), brick.getWidth(), brick.getHeight());
            } else {
                // If newly destroyed, add to the animation queue
                if (!knownBrokenBricks.contains(brick)) {
                    knownBrokenBricks.add(brick);
                    activeAnimations.add(new BrokenAnimation(brick.getX(), brick.getY(), brick.getType()));
                }
            }
        }

        // Draw brick breaking animations
        Iterator<BrokenAnimation> it = activeAnimations.iterator();
        while (it.hasNext()) {
            BrokenAnimation anim = it.next();
            int frame = anim.tick / 4; // Change frame every 4 ticks

            if (frame > 3) {
                it.remove(); // Remove animation after playing all 4 frames
            } else {
                Image frameImg = normBreak[frame];
                if (anim.type == 1) frameImg = strongCrackBreak[frame];
                else if (anim.type == 2) frameImg = bonusBreak[frame];
                else if (anim.type == 3) frameImg = specialBreak[frame];

                graphics.drawImage(frameImg, anim.x, anim.y, 80, 25);
                anim.tick++;
            }
        }

        Paddle paddle = gameModel.getPaddle();
        graphics.drawImage(paddleNormal, paddle.getX(), paddle.getY(), paddle.getWidth(), paddle.getHeight());

        Ball ball = gameModel.getBall();
        graphics.drawImage(ballImage, ball.getX() - ball.getRadius(), ball.getY() - ball.getRadius(), ball.getRadius() * 2, ball.getRadius() * 2);

        for (se233.gameproject.model.Laser laser : gameModel.getLasers()) {
            graphics.drawImage(laserProjectile, laser.getX(), laser.getY(), laser.getWidth(), laser.getHeight());
        }

        for (se233.gameproject.model.Item item : gameModel.getItems()) {
            if ("Laser".equals(item.getType())) {
                graphics.drawImage(laserItemSprite, item.getX(), item.getY(), item.getWidth(), item.getHeight());
            } else if ("Dizzy".equals(item.getType())) {
                double x = item.getX();
                double y = item.getY();
                double width = item.getWidth();
                double height = item.getHeight();

                // Lavender orbit with a bright planet-colored center.
                graphics.setStroke(Color.web("#594B7D"));
                graphics.setLineWidth(2.0);
                graphics.strokeOval(x - 2, y + 6, width + 4, height - 12);

                graphics.setFill(Color.web("#FFD36C"));
                graphics.fillOval(x, y, width, height);
                graphics.setStroke(Color.web("#594B7D"));
                graphics.strokeOval(x, y, width, height);

                graphics.setFill(Color.web("#594B7D"));
                graphics.setFont(Font.font(13));
                graphics.fillText("D", x + 9, y + 17);

                graphics.setFill(Color.WHITE);
                graphics.fillOval(x + 19, y + 4, 4, 4);
            }
        }

        updateHud();
    }

    private void updateHud() {
        scoreLabel.setText(String.format("%06d", gameModel.getScore()));
        levelLabel.setText(String.format("LEVEL %02d", gameModel.getStageLevel()));

        int lives = gameModel.getLives();
        if (lives == displayedLives) {
            return;
        }

        heartsBox.getChildren().clear();
        int heartSlots = Math.max(3, lives);
        for (int i = 0; i < heartSlots; i++) {
            boolean filled = i < lives;
            Label heart = new Label(filled ? "♥" : "♡");
            heart.setStyle(filled
                    ? "-fx-text-fill: #f47f83; -fx-font-size: 23px; -fx-effect: dropshadow(gaussian, rgba(244, 127, 131, 0.25), 4, 0.2, 0, 1);"
                    : "-fx-text-fill: #d8cde1; -fx-font-size: 23px;");
            heartsBox.getChildren().add(heart);

            if (filled) {
                ScaleTransition pulse = new ScaleTransition(Duration.seconds(0.75), heart);
                pulse.setFromX(1.0);
                pulse.setFromY(1.0);
                pulse.setToX(1.16);
                pulse.setToY(1.16);
                pulse.setAutoReverse(true);
                pulse.setCycleCount(Animation.INDEFINITE);
                pulse.setDelay(Duration.millis(i * 120));
                pulse.play();
            }
        }

        displayedLives = lives;
    }

    private void drawGameOver(){
        graphics.setFill(Color.RED);
        graphics.setFont(Font.font(48));
        graphics.fillText("GAME OVER", gameCanvas.getWidth() / 2-140, gameCanvas.getHeight()/2);
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

    @FXML public void selectUpgrade1() { applyUpgradeAndNextStage(0); }
    @FXML public void selectUpgrade2() { applyUpgradeAndNextStage(1); }
    @FXML public void selectUpgrade3() { applyUpgradeAndNextStage(2); }

    private void applyUpgradeAndNextStage(int buttonIndex){
        upgradeMenu.setVisible(false);
        gameModel.applyUpgrade(currentUpgrades.get(buttonIndex));

        // Clear animation queue for the next stage
        knownBrokenBricks.clear();
        activeAnimations.clear();

        gameLoop.start();
        gameCanvas.requestFocus();
    }

    private Image cropSprite(Image sheet, int sourceX, int sourceY, int width, int height) {
        PixelReader reader = sheet.getPixelReader();
        WritableImage sprite = new WritableImage(width, height);
        PixelWriter writer = sprite.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                writer.setColor(x, y, reader.getColor(sourceX + x, sourceY + y));
            }
        }
        return sprite;
    }
}
