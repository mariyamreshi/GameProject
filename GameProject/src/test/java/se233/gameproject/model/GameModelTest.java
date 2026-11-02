package se233.gameproject.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameModelTest {

    @Test
    void newGameStartsWithThreeLives() {
        GameModel game = new GameModel();

        assertEquals(3, game.getLives());
    }

    @Test
    void newGameStartsWithZeroScore() {
        GameModel game = new GameModel();

        assertEquals(0, game.getScore());
    }

    @Test
    void newGamePlacesBallAtStartingPosition() {
        GameModel game = new GameModel();

        assertEquals(400.0, game.getBall().getX(), 0.001);
        assertEquals(400.0, game.getBall().getY(), 0.001);
    }

    @Test
    void releasingBallLaunchesItUpward() {
        GameModel game = new GameModel();

        game.releaseBall();

        assertTrue(game.getBall().getVelocityY() < 0);
    }

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

    @Test
    void gainExtraLifeUpgradeAddsOneLife() {
        GameModel game = new GameModel();

        game.applyUpgrade("Gain Extra Life");

        assertEquals(4, game.getLives());
    }

    @Test
    void increasePaddleSizeUpgradeWidensPaddle() {
        GameModel game = new GameModel();

        game.applyUpgrade("Increase Paddle Size");

        assertEquals(130.0, game.getPaddle().getWidth(), 0.001);
    }

    @Test
    void increasePaddleSpeedUpgradeMakesPaddleFaster() {
        GameModel game = new GameModel();

        game.applyUpgrade("Increase Paddle Speed");

        assertEquals(25.0, game.getPaddle().getSpeed(), 0.001);
    }

    @Test
    void decreaseBallSpeedUpgradeSlowsBall() {
        GameModel game = new GameModel();

        game.applyUpgrade("Decrease Ball Speed");
        game.releaseBall();

        assertEquals(-2.4, game.getBall().getVelocityY(), 0.001);
    }

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