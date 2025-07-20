package com.dt180g.laboration_3;

import com.dt180g.laboration_3.issuers.GameRunner;
import com.dt180g.laboration_3.issuers.Replayer;
import com.dt180g.laboration_3.support.AppConfig;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;

/**
 * The main starting point for the third lab assignment.
 * Responsible for creation of command issuing entities.
 *
 * @author Erik Ström
 */
public final class Lab3 {

    /**
     * Queries the user for input of whether to replay previous game.
     *
     * @return result of user provided input.
     */
    private static boolean askForReplay() {
        System.out.printf("%sDo you wish to continue previous game? (Y/N)%s%n",
                AppConfig.COLOR_INPUT, AppConfig.COLOR_RESET);

        Scanner scan = new Scanner(System.in);
        while (true) {
            System.out.printf("%s>%s ", AppConfig.COLOR_INPUT, AppConfig.COLOR_RESET);
            String input = scan.next().trim().toLowerCase();
            switch (input) {
                case "y" -> { return true; }
                case "n" -> { return false; }
                default -> System.out.printf("%sInvalid input. Please enter 'Y' or 'N'.%s%n",
                        AppConfig.COLOR_ERROR_MSG, AppConfig.COLOR_RESET);
            }
        }
    }

    /**
     * The main entry point of the application.
     *
     * If a log file exists and the user decides to replay the previous game, a new
     * {@link GameRunner} instance is created based on the game state stored in the log file.
     * Otherwise, a new game is started with a fresh instance of {@link GameRunner}.
     *
     * If there is an issue with restoring the previous game state, a new game is started
     * with a fresh instance of {@link GameRunner}.
     *
     * @param args the command line arguments, which are not used in this application.
     */
    public static void main(final String... args) {
        try {
            // check if a log file exists and ask the user if they want to replay the previous game
            if (new File(AppConfig.getLogFilePath()).exists() && askForReplay()) {
                new Replayer().runReplay();
            }
        } catch (IOException | URISyntaxException | NumberFormatException | IndexOutOfBoundsException e) {
            // In case there was a problem restoring previous game, just create a new game.
            System.out.printf("%sThere was a problem restoring previous game. Starting a new Game!.%s%n",
                    AppConfig.COLOR_ERROR_MSG, AppConfig.COLOR_RESET);
        } finally {
            new GameRunner().runGame(); // start the game
        }
    }
}
