package com.dt180g.laboration_3.issuers;

import com.dt180g.laboration_3.receiver.HanoiEngine;
import com.dt180g.laboration_3.support.AppConfig;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests isolated to replayer object.
 * Validation will be based on files existing in test resource folder.
 * @author Erik Ström
 */
public class TestReplayer {
    public TestReplayer() {
        AppConfig.setUseLog(false);
        AppConfig.setShowReplayMoves(false);
    }

    private BufferedReader getReaderFromStream(String fileName) {
        AppConfig.setLogFileName(fileName);
        InputStream is = getClass().getClassLoader().getResourceAsStream(fileName);
        return new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
    }

    protected void runAsserts(List<Integer> exp, boolean completed) {
        HanoiEngine engine = HanoiEngine.INSTANCE;
        assertAll("Validate that tower states are correct",
                () -> assertEquals(exp.get(0), engine.getTowerState(0)),
                () -> assertEquals(exp.get(1), engine.getTowerState(1)),
                () -> assertEquals(exp.get(2), engine.getTowerState(2)),
                () -> assertEquals(exp.get(3), engine.getMoves()),
                () -> assertEquals(completed, engine.isGameCompleted())
        );
    }

    @Test
    public void testReplayerUncompleteGame() throws IOException {
        new Replayer(getReaderFromStream("HanoiTest1.log")).runReplay();
        runAsserts(Arrays.asList(1, 2, 0, 9), false);
    }

    @Test
    public void testReplayerComplete3DiscGame() throws IOException {
        new Replayer(getReaderFromStream("HanoiTest2.log")).runReplay();
        runAsserts(Arrays.asList(0, 0, 3, 7), true);
    }

    @Test
    public void testReplayerComplete4DiscGame() throws IOException {
        new Replayer(getReaderFromStream("HanoiTest3.log")).runReplay();
        runAsserts(Arrays.asList(0, 0, 4, 15), true);
    }

    @Test
    public void testReplayerWithUndoes() throws IOException {
        new Replayer(getReaderFromStream("HanoiTest4.log")).runReplay();
        runAsserts(Arrays.asList(1, 2, 0, 3), false);
    }
}
