package com.dt180g.laboration_3.validation;

import com.dt180g.laboration_3.support.AppConfig;

/**
 * An exception class used to indicate that the user has provided invalid input for the
 * Towers of Hanoi game. This exception is a runtime exception, so it does not need to
 * be explicitly caught and handled by the caller.
 *
 * @author Erik Ström
 */
public class InvalidInputException extends RuntimeException {

    /**
     * Constructs a new InvalidInputException with the provided error message.
     * The error message will be formatted with ANSI color codes to display the message in red.
     *
     * @param errorMsg the error message to display
     */
    public InvalidInputException(final String errorMsg) {
        // Call the super constructor with the formatted error message
        super(String.format("%s%s%s", AppConfig.COLOR_ERROR_MSG, errorMsg, AppConfig.COLOR_RESET));
    }
}
