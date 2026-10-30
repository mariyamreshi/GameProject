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

    public GameModel() {
        ball = new Ball(400, 400, 10, 3, -3);
        paddle = new Paddle(350, 540, 100, 15, 20);
        bricks = new ArrayList<>();

        score = 0;
        lives = 3;
        createBricks();
    }
    private void createBricks() {
        int rows = 4;
        int columns = 8;
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
            ball.move();

            if (ball.getX() - ball.getRadius() <= 0
                    || ball.getX() + ball.getRadius() >= gameWidth) {
                ball.bounceHorizontally();
            }

            if (ball.getY() - ball.getRadius() <= 0) {
                ball.bounceVertically();
            }
            Paddle paddle = this.paddle;
            Ball ball = this.ball;

            boolean ballMovingDown = ball.getVelocityY() > 0;
            boolean overlapsPaddle =
                    ball.getX() + ball.getRadius() >= paddle.getX()
                            && ball.getX() - ball.getRadius() <= paddle.getX() + paddle.getWidth()
                            && ball.getY() + ball.getRadius() >= paddle.getY()
                            && ball.getY() - ball.getRadius() <= paddle.getY() + paddle.getHeight();

            if (ballMovingDown && overlapsPaddle) {
                ball.bounceVertically();
            }
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





}
