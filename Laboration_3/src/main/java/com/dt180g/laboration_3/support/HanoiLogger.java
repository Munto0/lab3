package com.dt180g.laboration_3.support;


import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Singleton logger for the Hanoi game application.
 * Uses java.util.logging to write play and replay actions to a file for later replay.
 * <p>
 * The logger can be reset between games, and writes each move or undo symbol on its own line.
 * </p>
 *
 * @author Muntaser Ibrahim
 */
public class HanoiLogger {
    private static HanoiLogger instance;
    private Logger logger;
    private Handler handler;
    private boolean initialized = false;

    private HanoiLogger() {
        if (AppConfig.shouldUseLog()) {
            initializeLogger();
        }
    }

    /**
     * Returns the singleton instance, creating it if necessary.
     * Ensures initialization if logging is enabled and not yet initialized.
     *
     * @return the HanoiLogger instance
     */
    public static synchronized HanoiLogger getInstance() {
        if (instance == null) {
            instance = new HanoiLogger();
        }
        if (AppConfig.shouldUseLog() && !instance.initialized) {
            instance.initializeLogger();
        }
        return instance;
    }

    /**
     * Sets up java.util.logging to write to AppConfig.getLogFilePath().
     * Wraps URISyntaxException and IOException in RuntimeException.
     */
    private void initializeLogger() {
        try {
            logger = Logger.getLogger(HanoiLogger.class.getName());
            logger.setUseParentHandlers(false);

            // May throw URISyntaxException
            String path = AppConfig.getLogFilePath();
            handler = new FileHandler(path, false);

            handler.setLevel(Level.INFO);
            handler.setFormatter(new SimpleFormatter() {
                @Override
                public synchronized String format(LogRecord record) {
                    return record.getMessage() + System.lineSeparator();
                }
            });

            logger.addHandler(handler);
            logger.setLevel(Level.INFO);
            initialized = true;

        } catch (URISyntaxException e) {
            throw new RuntimeException("Failed to determine log-file path", e);
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize logger", e);
        }
    }

    /**
     * Log a single line at INFO level.
     */
    public void logInfo(String msg) {
        if (!AppConfig.shouldUseLog() || !initialized) {
            return;
        }
        logger.info(msg);
    }

    /**
     * Close and re‐open the log file (for new games).
     */
    public void resetLogger() {
        if (!AppConfig.shouldUseLog()) {
            return;
        }
        closeLogger();
        initializeLogger();
    }

    /**
     * Close file handlers so we can re‐initialize.
     */
    public void closeLogger() {
        if (handler != null) {
            handler.close();
            logger.removeHandler(handler);
        }
        initialized = false;
    }
}
