package se233.gameproject.model;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.Image;

public class GameModel {
    private final double gameWidth = 800;
    private final double gameHeight = 600;

    private final Ball ball;
    private final Paddle paddle;
    private final List<Brick> bricks;

    private int score;
    private int lives;

    private boolean isBallStuck;
    private boolean isGameOver;
    private boolean isStageClear;

    private double ballSpeedY = -3.0;
    private double maxBallSpeedX = 5.0;

    public GameModel() {
        ball = new Ball(400, 400, 10, 0, 0);
        paddle = new Paddle(350, 540, 100, 15, 20);
        bricks = new ArrayList<>();

        score = 0;
        lives = 3;
        isBallStuck = true;
        isGameOver = false;
        isStageClear = false;
        createBricks();
    }

    public void releaseBall(){
        if (isBallStuck) {
            isBallStuck = false;
            ball.setVelocityX(0);
            ball.setVelocityY(ballSpeedY);
        }
    }
    private void createBricks() {
        int rows = 2; // initial 4
        int columns = 2; //initial 8
        double brickWidth = 80;
        double brickHeight = 25;
        double gap = 5;
        double startX = 37.5;
        double startY = 60;

        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                double x = startX + column * (brickWidth + gap);
                double y = startY + row * (brickHeight + gap);

                bricks.add(new Brick(x, y, brickWidth, brickHeight));
            }
        }
    }
    public void update() {
        if (isBallStuck){
            ball.setX(paddle.getX() + (paddle.getWidth()/2));
            ball.setY(paddle.getY() - ball.getRadius());
        } else {
            ball.move();
            // Wall collision logic
            // Check if it hits the left wall AND is moving left
            if (ball.getX() - ball.getRadius() <= 0 && ball.getVelocityX() < 0) {
                ball.bounceHorizontally();
            }
            else if (ball.getX() + ball.getRadius() >= gameWidth && ball.getVelocityX() > 0) {
                ball.bounceHorizontally();
            }
            if (ball.getY() - ball.getRadius() <= 0 && ball.getVelocityY() < 0) {
                ball.bounceVertically();
            }
            if (ball.getY() - ball.getRadius() >= gameHeight){
                lives--;
                if (lives > 0) {
                    isBallStuck = true;
                } else {
                    isGameOver = true;
                }
            }
            // Paddle collision logic
            Paddle paddle = this.paddle;
            Ball ball = this.ball;
            boolean ballMovingDown = ball.getVelocityY() > 0;
            boolean overlapsPaddle =
                    ball.getX() + ball.getRadius() >= paddle.getX()
                            && ball.getX() - ball.getRadius() <= paddle.getX() + paddle.getWidth()
                            && ball.getY() + ball.getRadius() >= paddle.getY()
                            && ball.getY() - ball.getRadius() <= paddle.getY() + paddle.getHeight();
            if (ballMovingDown && overlapsPaddle) {
                double paddleCenter = paddle.getX() + (paddle.getWidth()/2);
                double hitPoint = ball.getX() - paddleCenter;
                double normalizedHitPoint = hitPoint / (paddle.getWidth()/2);

                ball.setVelocityX(normalizedHitPoint * maxBallSpeedX);
                ball.setVelocityY(ballSpeedY);
                ball.setY(paddle.getY() - ball.getRadius());
            }
            // Brick collision logic
            for (Brick brick : bricks) {
                if (brick.isDestroyed()) {
                    continue;
                }
                boolean overlapsBrick =
                        ball.getX() + ball.getRadius() >= brick.getX()
                                && ball.getX() - ball.getRadius() <= brick.getX() + brick.getWidth()
                                && ball.getY() + ball.getRadius() >= brick.getY()
                                && ball.getY() - ball.getRadius() <= brick.getY() + brick.getHeight();
                if (overlapsBrick) {
                    brick.hit();
                    score += 10;
                    ball.bounceVertically();
                    break;
                }
            }
            boolean allDestroyed = true;
            for (Brick brick : bricks){
                if (!brick.isDestroyed()){
                    allDestroyed = false;
                    break;
                }
            }
            if (allDestroyed) {
                isStageClear = true; // no brick left
            }
        }
    }

    public void applyUpgrade(int choice) {
        if (choice == 1) {
            // +paddle size
            paddle.setWidth(paddle.getWidth() + 30);
        } else if (choice == 2) {
            // -Ball Speed
            ballSpeedY = ballSpeedY * 0.8;
        } else if (choice == 3) {
            // Gain Extra Life
            lives++;
        }

        isStageClear = false;
        isBallStuck = true;
        bricks.clear();
        createBricks();
    }

        public double getGameWidth() {
            return gameWidth;
        }

        public double getGameHeight() {
            return gameHeight;
        }

        public Ball getBall() {
            return ball;
        }

        public Paddle getPaddle() {
            return paddle;
        }

        public List<Brick> getBricks() {
            return bricks;
        }

        public int getScore() {
            return score;
        }

        public int getLives() {
            return lives;
        }

        public boolean isGameOver(){
            return isGameOver;
        }

        public boolean isStageClear() {
            return isStageClear;
        }
}