package com.dt180g.laboration_3.issuers;

import com.dt180g.laboration_3.receiver.HanoiEngine;
import com.dt180g.laboration_3.invoker.CommandManager;
import com.dt180g.laboration_3.commands.MoveCommand;
import com.dt180g.laboration_3.commands.NewGameCommand;
import com.dt180g.laboration_3.commands.ShowCommand;
import com.dt180g.laboration_3.support.AppConfig;
import com.dt180g.laboration_3.support.HanoiLogger;
import com.dt180g.laboration_3.validation.InvalidInputException;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The main command issuer for the game, and responsible for user interactions.
 * Provides a user interface through menu options, handles user input and issues
 * commands when requested.
 *
 * @author Erik Ström
 */
public class GameRunner {
    private final Scanner in = new Scanner(System.in);
    private final PrintStream out = System.out;

    private enum MenuOption {
        MOVE(1, "Perform Move"), UNDO(2, "Undo Move"), REDO(3, "Redo Move"),
        NEW_GAME(4, "New Game"), EXIT(0, "Exit");

        private final int value;
        private final String label;

        MenuOption(final int value, final String label) {
            this.value = value;
            this.label = label;
        }

        public int getValue() { return value; }
        public String getLabel() { return label; }

        public static MenuOption getByValue(final int value) {
            return Arrays.stream(MenuOption.values())
                    .filter(o -> o.getValue() == value)
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Invalid menu option value: " + value));
        }
    }

    public GameRunner() {
        HanoiEngine engine = HanoiEngine.INSTANCE;
        if (engine.getDiscAmount() < AppConfig.DISC_AMOUNT_MINIMUM || engine.isGameCompleted()) {
            CommandManager.INSTANCE.executeCommand(new NewGameCommand(queryDiscAmount()));
        }
    }

    private int queryDiscAmount() {
        out.printf("%sState amount of discs [%d..%d]%s%n",
                AppConfig.COLOR_INPUT, AppConfig.DISC_AMOUNT_MINIMUM,
                AppConfig.DISC_AMOUNT_MAXIMUM, AppConfig.COLOR_RESET);
        return getInput(AppConfig.DISC_AMOUNT_MINIMUM, AppConfig.DISC_AMOUNT_MAXIMUM);
    }

    private int getInput(final int min, final int max) {
        while (true) {
            try {
                out.printf("%s>%s ", AppConfig.COLOR_INPUT, AppConfig.COLOR_RESET);
                int input = Integer.parseInt(in.next());
                if (input < min || input > max) throw new InputMismatchException();
                return input;
            } catch (InputMismatchException | NumberFormatException ex) {
                out.printf("%sSorry, only integer values between %d and %d are allowed!%s%n",
                        AppConfig.COLOR_ERROR_MSG, min, max, AppConfig.COLOR_RESET);
            }
        }
    }

    private List<Integer> promptMove() {
        return Stream.of("Origin", "Destination")
                .map(label -> {
                    out.printf("%n%sState %s Tower [1..%d]%s%n",
                            AppConfig.COLOR_INPUT, label, AppConfig.TOWERS_AMOUNT, AppConfig.COLOR_RESET);
                    return getInput(1, AppConfig.TOWERS_AMOUNT);
                })
                .collect(Collectors.toList());
    }

    private void closeStreams() {
        in.close();
        out.close();
        HanoiLogger.getInstance().closeLogger();
    }

    private void printOptionItems(final List<MenuOption> options) {
        String output = options.stream()
                .map(o -> String.format("%s%d%s%s%s",
                        AppConfig.COLOR_MENU, o.getValue(), ". ", o.getLabel(), AppConfig.COLOR_RESET))
                .collect(Collectors.joining(" | "));
        String border = "─".repeat(output.replace(" ", "").length() - 20);
        out.printf("%s%n    %s%n%s%n", border, output, border);
    }

    public void runGame() {
        out.printf("%n%s%s%s", AppConfig.COLOR_BANNER, AppConfig.GAME_BANNER, AppConfig.COLOR_RESET);

        MenuOption[] opts = MenuOption.values();
        MenuOption sel;
        do {
            CommandManager.INSTANCE.executeCommand(new ShowCommand());
            printOptionItems(Arrays.asList(opts));

            sel = MenuOption.getByValue(getInput(0, opts.length - 1));
            switch (sel) {
                case MOVE -> {
                    var mv = promptMove();
                    CommandManager.INSTANCE.executeCommand(new MoveCommand(mv.get(0), mv.get(1)));
                }
                case UNDO -> CommandManager.INSTANCE.undoMove();
                case REDO -> CommandManager.INSTANCE.redoMove();
                case NEW_GAME -> CommandManager.INSTANCE.executeCommand(new NewGameCommand(queryDiscAmount()));
                case EXIT -> { closeStreams(); return; }
                default -> throw new InvalidInputException("Invalid input.");
            }
        } while (!HanoiEngine.INSTANCE.isGameCompleted());

        CommandManager.INSTANCE.executeCommand(new ShowCommand());
        out.printf("%n%s%s%s", AppConfig.COLOR_GAME_COMPLETE, AppConfig.GAME_COMPLETE, AppConfig.COLOR_RESET);
        closeStreams();
    }
}
