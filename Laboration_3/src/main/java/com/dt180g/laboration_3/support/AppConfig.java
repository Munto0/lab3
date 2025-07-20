package com.dt180g.laboration_3.support;

import java.io.File;
import java.net.URISyntaxException;
import com.dt180g.laboration_3.Lab3;

/**
 * Utility class for application-wide constants and configuration.
 *
 * @author Erik Ström
 */
public final class AppConfig {
    private AppConfig() { throw new IllegalStateException("Utility class"); }

    /* GAME CONFIG */
    public static final int TOWERS_AMOUNT = 3;
    public static final int DISC_AMOUNT_MINIMUM = 2;
    public static final int DISC_AMOUNT_MAXIMUM = 7;

    /* LOGGING */
    public static final String LOG_UNDO_SYMBOL = "U";
    private static String logFileName = "Hanoi.log";
    private static boolean useLog = true;
    private static boolean showReplayMoves = true;

    public static boolean shouldUseLog() { return useLog; }
    public static boolean shouldShowReplayMoves() { return showReplayMoves; }
    public static void setUseLog(boolean b) { useLog = b; }
    public static void setShowReplayMoves(boolean b) { showReplayMoves = b; }
    public static void setLogFileName(String n) { logFileName = n; }

    public static String getLogFilePath() throws URISyntaxException {
        String root = new File(
            Lab3.class.getProtectionDomain()
                .getCodeSource().getLocation().toURI().getPath()
        ).getParentFile().getParentFile().getParentFile().getAbsolutePath();
        return String.format("%s%s_RepoResources%s%s",
                root, File.separator, File.separator, logFileName);
    }

    /* COLOR CODES */
    public static final String COLOR_MENU        = Color.PURPLE.ansiCode;
    public static final String COLOR_INPUT       = Color.GREEN.ansiCode;
    public static final String COLOR_ERROR_MSG   = Color.RED.ansiCode;
    public static final String COLOR_RESET       = Color.RESET.ansiCode;
    public static final String COLOR_BANNER      = Color.MAGENTA.ansiCode;
    public static final String COLOR_GAME_COMPLETE = Color.YELLOW.ansiCode;
    public static final String COLOR_DISC        = Color.RED.ansiCode;
    public static final String COLOR_PILLAR      = Color.YELLOW.ansiCode;
    public static final String COLOR_TOWER_BASE  = Color.CYAN.ansiCode;
    public static final String COLOR_TOWER_INFO  = Color.BLUE.ansiCode;

    private enum Color {
        BLACK("\u001B[30m"), RED("\u001B[31m"), GREEN("\u001B[32m"),
        YELLOW("\u001B[33m"), BLUE("\u001B[34m"), PURPLE("\u001B[35m"),
        CYAN("\u001B[36m"), WHITE("\u001B[37m"), MAGENTA("\u001b[35m"),
        RESET("\u001B[0m");
        private final String ansiCode;
        Color(String code) { ansiCode = code; }
    }

    /* BANNERS (omitted for brevity) */
    public static final String GAME_BANNER = /* huge ASCII art string */;
    public static final String GAME_COMPLETE = /* huge ASCII art string */;
}
