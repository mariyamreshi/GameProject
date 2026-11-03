package se233.gameproject.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameModelTest {

    // A new game should give the player three starting lives.
    @Test
    void newGameStartsWithThreeLives() {
        GameModel game = new GameModel();

        assertEquals(3, game.getLives());
    }

    // A new game should begin with no points earned.
    @Test
    void newGameStartsWithZeroScore() {
        GameModel game = new GameModel();

        assertEquals(0, game.getScore());
    }

    // The ball should be created at the expected starting position.
    @Test
    void newGamePlacesBallAtStartingPosition() {
        GameModel game = new GameModel();

        assertEquals(400.0, game.getBall().getX(), 0.001);
        assertEquals(400.0, game.getBall().getY(), 0.001);
    }

    // Releasing the ball should give it an upward vertical velocity.
    @Test
    void releasingBallLaunchesItUpward() {
        GameModel game = new GameModel();

        game.releaseBall();

        assertTrue(game.getBall().getVelocityY() < 0);
    }

    // When the ball falls below the play area, the player should lose one life.
    @Test
    void losingBallDecreasesLivesByOne() {
        GameModel game = new GameModel();
        game.releaseBall();

        Ball ball = game.getBall();
        ball.setX(400);
        ball.setY(610);
        ball.setVelocityY(1);

        game.update();

        assertEquals(2, game.getLives());
    }

    // Choosing the extra-life upgrade should increase the lives count by one.
    @Test
    void gainExtraLifeUpgradeAddsOneLife() {
        GameModel game = new GameModel();

        game.applyUpgrade("Gain Extra Life");

        assertEquals(4, game.getLives());
    }

    // Choosing the paddle-size upgrade should make the paddle wider.
    @Test
    void increasePaddleSizeUpgradeWidensPaddle() {
        GameModel game = new GameModel();

        game.applyUpgrade("Increase Paddle Size");

        assertEquals(130.0, game.getPaddle().getWidth(), 0.001);
    }

    // Choosing the paddle-speed upgrade should increase the paddle's speed.
    @Test
    void increasePaddleSpeedUpgradeMakesPaddleFaster() {
        GameModel game = new GameModel();

        game.applyUpgrade("Increase Paddle Speed");

        assertEquals(25.0, game.getPaddle().getSpeed(), 0.001);
    }

    // Choosing the ball-speed upgrade should reduce the ball's upward speed.
    @Test
    void decreaseBallSpeedUpgradeSlowsBall() {
        GameModel game = new GameModel();

        game.applyUpgrade("Decrease Ball Speed");
        game.releaseBall();

        assertEquals(-2.4, game.getBall().getVelocityY(), 0.001);
    }

    // The points upgrade should multiply the score awarded for destroying a brick.
    @Test
    void morePointsUpgradeDoublesBrickScore() {
        GameModel game = new GameModel();

        game.applyUpgrade("More Points Per Brick");
        game.releaseBall();

        game.getBricks().clear();
        game.getBricks().add(new Brick(50, 60, 80, 25, 2));

        Ball ball = game.getBall();
        ball.setX(70);
        ball.setY(50);
        ball.setVelocityY(3);

        game.update();

        assertEquals(100, game.getScore());
    }
}
