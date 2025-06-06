package com.dt180g.laboration_3.validation;

import com.dt180g.laboration_3.TestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * This class contains test methods for the {@link InvalidMoveException} class, which tests
 * various scenarios where an InvalidMoveException should be thrown during the HanoiEngine's
 * performMove method.
 */
public class TestInvalidMoveException extends TestBase {
    /** Resets the game before each test case is run. */
    @BeforeEach
    public void setUp() {
        engine.resetGame(3);
    }

    /**
     * Tests that an InvalidMoveException is thrown if a larger disc is placed on top
     * of a smaller disc.
     */
    @Test
    public void testInvalidMoveExceptionLargerDiscOnSmaller() {
        assertThrows(InvalidMoveException.class, () -> Stream.of("1 3", "1 3")
                        .map(move -> move.split(" "))
                        .forEach(moveArr -> engine.performMove(Integer.parseInt(moveArr[0]), Integer.parseInt(moveArr[1]), false)),
                "InvalidMoveException needs to be thrown if larger disc is placed on smaller.");
    }

    /**
     * Tests that an InvalidMoveException is thrown if the selected tower has no
     * discs to be moved.
     */
    @Test
    public void testInvalidMoveExceptionNoDiscToMove() {
        assertThrows(InvalidMoveException.class, () -> Stream.of("1 3", "1 2", "3 2", "1 3", "1 2")
                        .map(move -> move.split(" "))
                        .forEach(moveArr -> engine.performMove(Integer.parseInt(moveArr[0]), Integer.parseInt(moveArr[1]), false)),
                "InvalidMoveException needs to be thrown if the selected tower has no discs to be moved.");
    }

    /**
     * Tests that an InvalidMoveException is thrown if the same tower is used as both
     * origin and destination.
     */
    @Test
    public void testInvalidMoveExceptionSameTower() {
        assertThrows(InvalidMoveException.class, () -> engine.performMove(1, 1, false),
                "InvalidMoveException needs to be thrown if the same tower is used as destination.");
    }
}
