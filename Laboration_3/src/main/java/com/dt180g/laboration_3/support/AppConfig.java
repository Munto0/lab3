package com.dt180g.laboration_3.support;

import java.io.File;
import java.net.URISyntaxException;
import com.dt180g.laboration_3.Lab3;


/**
 * The AppConfig class provides a collection of constants and help methods which are
 * used throughout the Hanoi game application. These constants include game-related
 * values, logging configurations, color codes, and banners. The class is final and
 * cannot be instantiated, as all constants are static.
 *
 * @author Erik Ström
 */
public final class AppConfig {
    // make sure the class cannot be instantiated.
    private AppConfig() { throw new IllegalStateException("Utility class"); }

    /* -------------------------
    GAME RELATED CONFIGURATIONS
    ------------------------- */

    /** The number of towers in the game of Hanoi. This value should never be changed. */
    public static final int TOWERS_AMOUNT = 3;

    /** The minimum number of discs in the game of Hanoi. */
    public static final int DISC_AMOUNT_MINIMUM = 2;

    /** The maximum number of discs in the game of Hanoi. */
    public static final int DISC_AMOUNT_MAXIMUM = 7;

    /* -------------------------
    LOGGING CONFIGURATIONS
    ------------------------- */

    /** Symbol used in log to identify undo moves. */
    public static final String LOG_UNDO_SYMBOL = "U";

    /** The name of log file. */
    private static String logFileName = "Hanoi.log";

    /** Whether the log should be used. Important for tests. */
    private static boolean useLog = true;

    /** Whether moves should be printed during replay. */
    private static boolean showReplayMoves = true;

    /**
     * Accessor for {@link AppConfig#useLog}.
     * Logs should be enabled during play / replay, but disabled for most tests.
     *
     * @return whether logging is enabled.
     */
    public static boolean shouldUseLog() { return useLog; }

    /**
     * Accessor for {@link AppConfig#showReplayMoves}.
     *
     * @return whether moves should be shown in console during replay.
     */
    public static boolean shouldShowReplayMoves() { return showReplayMoves; }

    /**
     * Mutator for {@link AppConfig#useLog}.
     *
     * @param useLog whether logging should be enabled.
     */
    public static void setUseLog(final boolean useLog) { AppConfig.useLog = useLog; }

    /**
     * Mutator for {@link AppConfig#showReplayMoves}.
     *
     * @param showReplayMoves whether show of moves should be enabled during replay.
     */
    public static void setShowReplayMoves(final boolean showReplayMoves) {
        AppConfig.showReplayMoves = showReplayMoves;
    }

    /**
     * Mutator for {@link AppConfig#logFileName}.
     * Tests will need to use a different file for output.
     *
     * @param logFileName name of log file.
     */
    public static void setLogFileName(final String logFileName) {
        AppConfig.logFileName = logFileName;
    }

    /**
     * Retrieves the file path to the log file in the external resources folder.
     * The method gets the path to the root of the project using the getCodeSource method, and
     * then appends the path to the external resources folder where the log file is stored.
     *
     * @return the absolute path to the log file.
     * @throws URISyntaxException if a URI syntax exception occurs.
     */
    public static String getLogFilePath() throws URISyntaxException {
        String rootPath = new File(Lab3.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath())
                .getParentFile().getParentFile().getParentFile().getAbsolutePath();

        return String.format("%s%s_RepoResources%s%s", rootPath, File.separator, File.separator, logFileName);
    }

    /* -------------------------
    COLOR CONFIGURATIONS
    ------------------------- */

    /** The ANSI color code for the menu text. */
    public static final String COLOR_MENU = Color.PURPLE.ansiCode;

    /** The ANSI color code for user input text. */
    public static final String COLOR_INPUT = Color.GREEN.ansiCode;

    /** The ANSI color code for error messages. */
    public static final String COLOR_ERROR_MSG = Color.RED.ansiCode;

    /** The ANSI color code for resetting colors. */
    public static final String COLOR_RESET = Color.RESET.ansiCode;

    /** The ANSI color code for the game banner. */
    public static final String COLOR_BANNER = Color.MAGENTA.ansiCode;

    /** The ANSI color code for the "game complete" message. */
    public static final String COLOR_GAME_COMPLETE = Color.YELLOW.ansiCode;

    /** The ANSI color code for the discs. */
    public static final String COLOR_DISC = Color.RED.ansiCode;

    /** The ANSI color code for the pillars. */
    public static final String COLOR_PILLAR = Color.YELLOW.ansiCode;

    /** The ANSI color code for the tower bases. */
    public static final String COLOR_TOWER_BASE = Color.CYAN.ansiCode;

    /** The ANSI color code for the tower information. */
    public static final String COLOR_TOWER_INFO = Color.BLUE.ansiCode;

    private enum Color {
        BLACK("\u001B[30m"), RED("\u001B[31m"), GREEN("\u001B[32m"),
        YELLOW("\u001B[33m"), BLUE("\u001B[34m"), PURPLE("\u001B[35m"),
        CYAN("\u001B[36m"), WHITE("\u001B[37m"), MAGENTA("\u001b[35m"),
        RESET("\u001B[0m");

        private final String ansiCode;

        /**
         * Constructs a new color with the given ANSI escape code.
         *
         * @param ansiCode the ANSI escape code for the color
         */
        Color(final String ansiCode) {
            this.ansiCode = ansiCode;
        }

        /**
         * Returns the ANSI escape code for the color.
         *
         * @return the ANSI escape code
         */
        @Override public String toString() {
            return ansiCode;
        }
    }

    /* --------------------
    BANNERS
    -------------------- */

    /** The main banner text for the game. */
    public static final String GAME_BANNER = """
████████╗ ██████╗ ██╗    ██╗███████╗██████╗ ███████╗     ██████╗ ███████╗    ██╗  ██╗ █████╗ ███╗   ██╗ ██████╗ ██╗
╚══██╔══╝██╔═══██╗██║    ██║██╔════╝██╔══██╗██╔════╝    ██╔═══██╗██╔════╝    ██║  ██║██╔══██╗████╗  ██║██╔═══██╗██║
   ██║   ██║   ██║██║ █╗ ██║█████╗  ██████╔╝███████╗    ██║   ██║█████╗      ███████║███████║██╔██╗ ██║██║   ██║██║
   ██║   ██║   ██║██║███╗██║██╔══╝  ██╔══██╗╚════██║    ██║   ██║██╔══╝      ██╔══██║██╔══██║██║╚██╗██║██║   ██║██║
   ██║   ╚██████╔╝╚███╔███╔╝███████╗██║  ██║███████║    ╚██████╔╝██║         ██║  ██║██║  ██║██║ ╚████║╚██████╔╝██║
   ╚═╝    ╚═════╝  ╚══╝╚══╝ ╚══════╝╚═╝  ╚═╝╚══════╝     ╚═════╝ ╚═╝         ╚═╝  ╚═╝╚═╝  ╚═╝╚═╝  ╚═══╝ ╚═════╝ ╚═╝
    """;

    /** The banner used for game completion. */
    public static final String GAME_COMPLETE = """
 ██████╗  █████╗ ███╗   ███╗███████╗     ██████╗ ██████╗ ███╗   ███╗██████╗ ██╗     ███████╗████████╗███████╗██████╗
██╔════╝ ██╔══██╗████╗ ████║██╔════╝    ██╔════╝██╔═══██╗████╗ ████║██╔══██╗██║     ██╔════╝╚══██╔══╝██╔════╝██╔══██╗
██║  ███╗███████║██╔████╔██║█████╗      ██║     ██║   ██║██╔████╔██║██████╔╝██║     █████╗     ██║   █████╗  ██║  ██║
██║   ██║██╔══██║██║╚██╔╝██║██╔══╝      ██║     ██║   ██║██║╚██╔╝██║██╔═══╝ ██║     ██╔══╝     ██║   ██╔══╝  ██║  ██║
╚██████╔╝██║  ██║██║ ╚═╝ ██║███████╗    ╚██████╗╚██████╔╝██║ ╚═╝ ██║██║     ███████╗███████╗   ██║   ███████╗██████╔╝
 ╚═════╝ ╚═╝  ╚═╝╚═╝     ╚═╝╚══════╝     ╚═════╝ ╚═════╝ ╚═╝     ╚═╝╚═╝     ╚══════╝╚══════╝   ╚═╝   ╚══════╝╚═════╝
    """;
}
