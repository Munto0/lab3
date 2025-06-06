package com.dt180g.laboration_3.commands;

import com.dt180g.laboration_3.TestBase;
import com.dt180g.laboration_3.support.AppConfig;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * This class provides JUnit test cases for {@link NewGameCommand}.
 * The class extends {@link TestBase} to inherit common functionality
 * for running tests.
 *
 * @author Erik Ström
 */
public class TestNewGameCommand extends TestBase {

    /** Test case for creating a new game with two discs. */
    @Test
    public void testNewGameCommandTwoDiscs() {
        int discAmount = 2;
        new NewGameCommand(discAmount).execute();
        runAsserts(Arrays.asList(discAmount, 0, 0, 0), false);
    }

    /** Test case for creating a new game with five discs. */
    @Test
    public void testNewGameCommandFiveDiscs() {
        int discAmount = 5;
        new NewGameCommand(discAmount).execute();
        runAsserts(Arrays.asList(discAmount, 0, 0, 0), false);
    }

    /**
     * Test case for creating a new game with a full range of discs
     * from minimum to maximum values defined in {@link AppConfig}.
     */
    @Test
    public void testNewGameCommandFullRangeOfDiscs() {
        IntStream.rangeClosed(AppConfig.DISC_AMOUNT_MINIMUM, AppConfig.DISC_AMOUNT_MAXIMUM)
                .forEach(i -> {
                    new NewGameCommand(i).execute();
                    runAsserts(Arrays.asList(i, 0, 0, 0), false);
                });
    }

    /**
     * Test case for resetting the game with three discs after making
     * moves on the board.
     */
    @Test
    public void testNewGameCommandThreeDiscsOnMoves() {
        Stream.of("1 3", "1 2", "3 2")
                .map(move -> move.split(" "))
                .forEach(moveArr -> {
                    engine.performMove(Integer.parseInt(moveArr[0]), Integer.parseInt(moveArr[1]), true);
                });

        int discAmount = 3;
        new NewGameCommand(discAmount).execute();
        runAsserts(Arrays.asList(discAmount, 0, 0, 0), false);
    }

    /**
     * Test case for creating a new game with a disc amount
     * lower than the minimum value defined in {@link AppConfig}.
     */
    @Test
    public void testNewGameCommandLowerBounds() {
        new NewGameCommand(AppConfig.DISC_AMOUNT_MINIMUM - 1).execute();
        runAsserts(Arrays.asList(AppConfig.DISC_AMOUNT_MINIMUM, 0, 0, 0), false);
    }

    /**
     * Test case for creating a new game with a disc amount
     * greater than the maximum value defined in {@link AppConfig}.
     */
    @Test
    public void testNewGameCommandUpperBounds() {
        new NewGameCommand(AppConfig.DISC_AMOUNT_MAXIMUM + 1).execute();
        runAsserts(Arrays.asList(AppConfig.DISC_AMOUNT_MAXIMUM, 0, 0, 0), false);
    }
}
