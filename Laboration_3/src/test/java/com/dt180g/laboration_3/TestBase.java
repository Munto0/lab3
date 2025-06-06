package com.dt180g.laboration_3;

import com.dt180g.laboration_3.receiver.HanoiEngine;
import com.dt180g.laboration_3.support.AppConfig;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * The base from which all test classes derive. Declaring common interface
 * shared by all tests.
 * @author Erik Ström
 */
public class TestBase {
    protected final HanoiEngine engine = HanoiEngine.INSTANCE;
    protected final List<String> moves = Arrays.asList(
            "1 3", "1 2", "3 2", "1 3", "2 1", "2 3", "1 3");

    protected TestBase() {
        AppConfig.setUseLog(false);
        engine.resetGame(3);
    }

    protected void runAsserts(List<Integer> exp, boolean completed) {
        assertAll("Validate that tower states are correct",
                () -> assertEquals(exp.get(0), engine.getTowerState(0)),
                () -> assertEquals(exp.get(1), engine.getTowerState(1)),
                () -> assertEquals(exp.get(2), engine.getTowerState(2)),
                () -> assertEquals(exp.get(3), engine.getMoves()),
                () -> assertEquals(completed, engine.isGameCompleted())
        );
    }
}
