package se233.gameproject.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GameModelTest {
    @Test
    void newGameStartsWithThreeLives() {
        GameModel game = new GameModel();

        assertEquals(3, game.getLives());
    }
}