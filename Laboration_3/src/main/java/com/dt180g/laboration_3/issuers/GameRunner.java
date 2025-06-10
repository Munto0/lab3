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
    // our standard input stream
    private final Scanner in = new Scanner(System.in);
    // our standard output stream
    private final PrintStream out = System.out;
    // the menu options for user navigation
    private enum MenuOption {
        MOVE(1, "Perform Move"), UNDO(2, "Undo Move"), REDO(3, "Redo Move"),
        NEW_GAME(4, "New Game"), EXIT(0, "Exit");
        private final int value;
        private final String label;
        MenuOption(final int value, final String label) {
            this.value = value;
            this.label = label;
        }
        public int getValue() {
            return value;
        }
        public String getLabel() {
            return label;
        }
        public static MenuOption getByValue(final int value) {
            return Arrays.stream(MenuOption.values())
                    .filter(option -> option.getValue() == value)
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Invalid menu option value: " + value));
        }
    }
    /**
     * Creates a new object instance and configures the {@link HanoiEngine}.
     * If the {@link HanoiEngine} has a completed game, a new instance of
     * {@link HanoiEngine} is created using {@link #queryDiscAmount()} method.
     */
    public GameRunner() {
        HanoiEngine engine = HanoiEngine.INSTANCE;
        if (engine.getDiscAmount() < AppConfig.DISC_AMOUNT_MINIMUM || engine.isGameCompleted()) {
            CommandManager.INSTANCE.executeCommand(new NewGameCommand(queryDiscAmount()));
        }
    }
    /**
     * Prompts the user to input the desired number of discs for the game and returns the input.
     * The input is validated to ensure that it is within the range of allowed values.
     *
     * @return the number of discs as stated by the user.
     */
    private int queryDiscAmount() {
        out.printf("%sState amount of discs [%d..%d]%s%n",
                AppConfig.COLOR_INPUT, AppConfig.DISC_AMOUNT_MINIMUM,
                AppConfig.DISC_AMOUNT_MAXIMUM, AppConfig.COLOR_RESET);
        return getInput(AppConfig.DISC_AMOUNT_MINIMUM, AppConfig.DISC_AMOUNT_MAXIMUM);
    }
    /**
     * Support method to handle and validate numeric inputs.
     *
     * @param min lower boundary of numeric limit.
     * @param max higher boundary of numeric limit.
     * @return the value provided by user.
     */
    private int getInput(final int min, final int max) {
        int input = -1;
        while (true) {
            try {
                out.printf("%s>%s ", AppConfig.COLOR_INPUT, AppConfig.COLOR_RESET);
                input = Integer.parseInt(in.next());
                if (input > max || input < min) {
                    throw new InputMismatchException();
                }
                break;
            } catch (InputMismatchException | NumberFormatException ex) {
                String msg = String.format("%sSorry, only integer values between %d and %d are allowed!%s",
                        AppConfig.COLOR_ERROR_MSG, min, max, AppConfig.COLOR_RESET);
                out.println(msg);
            }
        }
        return input;
    }
    /**
     * Support method used to querying the user for move details.
     *
     * @return the requested move, fromTower --> toTower.
     */
    private List<Integer> promptMove() {
        return Stream.of("Origin", "Destination")
                .map(str -> {
                    out.printf("%n%sState %s Tower [1..%d]%s%n",
                            AppConfig.COLOR_INPUT, str, AppConfig.TOWERS_AMOUNT, AppConfig.COLOR_RESET);
                    return getInput(1, AppConfig.TOWERS_AMOUNT);
                })
                .collect(Collectors.toList());
    }
    /** Closes the streams to associated resources. */
    private void closeStreams() {
        in.close();
        out.close();
        HanoiLogger.getInstance().closeLogger();
    }
    /**
     * Prints a list of menu options to the console, surrounded by horizontal lines.
     * Each menu option is represented by a label and a numeric value, which are centered
     * between the horizontal lines.
     *
     * @param options the list of menu options to print.
     */
    private void printOptionItems(final List<MenuOption> options) {
        final String menuColor = AppConfig.COLOR_MENU;
        String output = options.stream()
                .map(option -> String.format("%s%d%s%s%s",
                        menuColor, option.getValue(), ". ", option.getLabel(), AppConfig.COLOR_RESET))
                .collect(Collectors.joining(" | "));
        final int lineOffset = -20;
        String line = "─".repeat(output.replace(" ", "").length() + lineOffset);
        out.printf("%s%n    %s%n%s%n", line, output, line);
    }
    /**
     * Main game simulation, which only ends either when game has been completed
     * or explicitly requested by the user.
     */
    public void runGame() {
        out.printf("%n%s%s%s", AppConfig.COLOR_BANNER, AppConfig.GAME_BANNER, AppConfig.COLOR_RESET);
        MenuOption[] options = MenuOption.values();
        MenuOption selectedOption;
        do {
            CommandManager.INSTANCE.executeCommand(new ShowCommand()); // print current game state
            printOptionItems(Arrays.asList(options));
            int input = getInput(0, options.length - 1); // get validated user input
            selectedOption = MenuOption.getByValue(input);
            switch (selectedOption) {
                case MOVE -> {
                    List<Integer> move = promptMove();
                    CommandManager.INSTANCE.executeCommand(new MoveCommand(move.get(0), move.get(1)));
                }
                case UNDO -> CommandManager.INSTANCE.undoMove(); // undo latest move
                case REDO -> CommandManager.INSTANCE.redoMove(); // redo latest undo
                case NEW_GAME -> CommandManager.INSTANCE.executeCommand(
                        new NewGameCommand(queryDiscAmount()));
                case EXIT -> {
                    closeStreams(); // make sure open streams are closed.
                    return;
                }
                default -> throw new InvalidInputException("Invalid input.");
            }
        } while (!HanoiEngine.INSTANCE.isGameCompleted());
        CommandManager.INSTANCE.executeCommand(new ShowCommand()); // show final game state.
        out.printf("%n%s%s%s", AppConfig.COLOR_GAME_COMPLETE, AppConfig.GAME_COMPLETE, AppConfig.COLOR_RESET);
        // make sure open streams are closed.
        closeStreams();
    }
}
