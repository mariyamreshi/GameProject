package se233.gameproject.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameModelTest {
    @Test
    void startsWithThreeLivesAndZeroScore() {
        GameModel model = new GameModel();

        assertEquals(3, model.getLives());
        assertEquals(0, model.getScore());
    }

    @Test
    void losingBallRemovesOneLife() {
        GameModel model = new GameModel();
        model.getBall().setY(620);
        model.releaseBall();

        model.update();

        assertEquals(2, model.getLives());
    }

    @Test
    void losingAllLivesEndsTheGame() {
        GameModel model = new GameModel();

        for (int i = 0; i < 3; i++) {
            model.getBall().setY(620);
            model.releaseBall();
            model.update();
        }

        assertEquals(0, model.getLives());
        assertTrue(model.isGameOver());
    }

    @Test
    void destroyingBrickAddsItsPoints() {
        GameModel model = new GameModel();
        model.getBricks().clear();
        Brick brick = new Brick(390, 380, 80, 25, 0);
        model.getBricks().add(brick);
        model.releaseBall();

        model.update();

        assertTrue(brick.isDestroyed());
        assertEquals(10, model.getScore());
    }

    @Test
    void extraLifeUpgradeIncreasesLivesAndStartsNextStage() {
        GameModel model = new GameModel();

        model.applyUpgrade("Gain Extra Life");

        assertEquals(4, model.getLives());
        assertEquals(32, model.getBricks().size());
    }

    @Test
    void scoreUpgradeDoublesPointsForTheNextBrick() {
        GameModel model = new GameModel();
        model.applyUpgrade("More Points Per Brick");
        model.getBricks().clear();
        Brick brick = new Brick(390, 380, 80, 25, 0);
        model.getBricks().add(brick);
        model.releaseBall();

        model.update();

        assertTrue(brick.isDestroyed());
        assertEquals(20, model.getScore());
    }
}
