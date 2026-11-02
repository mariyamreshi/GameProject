package se233.gameproject.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BrickTest {
    @Test
    void normalBrickIsDestroyedByOneHit() {
        Brick brick = new Brick(10, 20, 80, 25, 0);

        brick.hit();

        assertTrue(brick.isDestroyed());
        assertEquals(0, brick.getHp());
    }

    @Test
    void strongBrickNeedsTwoHits() {
        Brick brick = new Brick(10, 20, 80, 25, 1);

        brick.hit();

        assertFalse(brick.isDestroyed());
        assertEquals(1, brick.getHp());

        brick.hit();

        assertTrue(brick.isDestroyed());
        assertEquals(0, brick.getHp());
    }

    @Test
    void bonusBrickHasHigherScoreValue() {
        Brick brick = new Brick(10, 20, 80, 25, 2);

        assertEquals(50, brick.getScoreValue());
    }
}
