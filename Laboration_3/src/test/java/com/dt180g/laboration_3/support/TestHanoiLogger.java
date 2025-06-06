package com.dt180g.laboration_3.support;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test class for {@link HanoiLogger}.
 *
 * @author Erik Ström
 */
public class TestHanoiLogger {
    /** Constructor that sets the log file name and enables logging for the test. */
    public TestHanoiLogger() {
        AppConfig.setLogFileName("Hanoi_test.log");
        AppConfig.setUseLog(true);
    }

    @BeforeEach
    public void setUp(TestInfo testInfo) {
        if(!testInfo.getTags().contains("SkipBuildup")) {
            HanoiLogger.getInstance().resetLogger();
        }
    }

    /** Deletes the log file after each test case. */
    @AfterEach
    public void tearDown(TestInfo testInfo) throws IOException, URISyntaxException {
        if(!testInfo.getTags().contains("SkipCleanup")) {
            Files.deleteIfExists(Path.of(AppConfig.getLogFilePath()));
        }
    }

    /** Tests that the constructor for {@link HanoiLogger} is private. */
    @Test
    @Tag("SkipBuildup")
    @Tag("SkipCleanup")
    public void testConstructorIsPrivate() {
        assertTrue(Arrays.stream(HanoiLogger.class.getDeclaredConstructors())
                .allMatch(constructor -> Modifier.isPrivate(constructor.getModifiers())),
                "Construction needs to be private.");
    }

    /**
     * Tests logging functionality by logging a list of values and checking that they
     * are logged correctly.
     */
    @Test
    public void testLogging() throws URISyntaxException, IOException {
        String undo = AppConfig.LOG_UNDO_SYMBOL;
        List<String> vals = Arrays.asList("3", "1 3", "1 2", "3 2", undo, undo, undo, "1 3", "1 2", "3 2");
        vals.forEach(HanoiLogger.getInstance()::logInfo);

        HanoiLogger.getInstance().closeLogger();

        try (Stream<String> lines = Files.lines(Path.of(AppConfig.getLogFilePath()))) {
            assertArrayEquals(vals.toArray(), lines.toArray(String[]::new),
                    "Contents of log isn't correct!");
        }
    }
}
