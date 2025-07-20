package com.dt180g.laboration_3.support;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.*;

/**
 * Singleton logger for the Hanoi game application.
 * Writes each move or undo symbol on its own line to a file.
 *
 * @author Muntaser Ibrahim
 */
public class HanoiLogger {
    private static HanoiLogger instance;
    private Logger logger;
    private Handler handler;
    private boolean initialized = false;

    private HanoiLogger() {
        if (AppConfig.shouldUseLog()) initializeLogger();
    }

    public static synchronized HanoiLogger getInstance() {
        if (instance == null) instance = new HanoiLogger();
        if (AppConfig.shouldUseLog() && !instance.initialized) {
            instance.initializeLogger();
        }
        return instance;
    }

    private void initializeLogger() {
        try {
            logger = Logger.getLogger(HanoiLogger.class.getName());
            logger.setUseParentHandlers(false);
            String path = AppConfig.getLogFilePath();
            handler = new FileHandler(path, false);
            handler.setLevel(Level.INFO);
            handler.setFormatter(new SimpleFormatter(){
                @Override public synchronized String format(LogRecord r) {
                    return r.getMessage() + System.lineSeparator();
                }
            });
            logger.addHandler(handler);
            logger.setLevel(Level.INFO);
            initialized = true;
        } catch (URISyntaxException|IOException e) {
            throw new RuntimeException("Failed to initialize logger", e);
        }
    }

    /** Logs a single line at INFO level. */
    public void logInfo(String msg) {
        if (!AppConfig.shouldUseLog() || !initialized) return;
        logger.info(msg);
    }

    /** Closes and re-opens the log (for new games). */
    public void resetLogger() {
        if (!AppConfig.shouldUseLog()) return;
        closeLogger();
        initializeLogger();
    }

    /** Close file handlers so we can re-initialize. */
    public void closeLogger() {
        if (handler != null) {
            handler.close();
            logger.removeHandler(handler);
        }
        initialized = false;
    }
}
