package com.dt180g.laboration_3.receiver;

import com.dt180g.laboration_3.validation.InvalidMoveException;
import com.dt180g.laboration_3.support.AppConfig;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * The main engine for Towers of Hanoi, acting as receiver of concrete command requests.
 * The class is responsible for current game state, that none of its base rules are violated,
 * and printing the towers, discs and moves count in console.
 *
 * @author Erik Ström
 */
public enum HanoiEngine {
    /** object instance. */
    INSTANCE;
    private int discs = 0;  // amount of discs to use
    private int moves = 0;  // counter for performed moves

    // represents our three towers
    private final List<ArrayList<Integer>> towers;

    private List<Integer> pillarSpacings;

    private interface TOWER {  // to facilitate simple semantics for our towers
        int T1 = 0;
        int T2 = 1;
        int T3 = 2;
    }

    /** Create an object instance and initialize the tower lists. */
    HanoiEngine() {
        towers = IntStream.range(0, AppConfig.TOWERS_AMOUNT)
                .mapToObj(i -> new ArrayList<Integer>())
                .toList();
    }

    /**
     * Resets engine to initial state, placing all discs in the first tower.
     *
     * @param discAmount amount of discs to be used.
     */
    private void resetEngine(final int discAmount) {
        IntStream.rangeClosed(1, discAmount)
                .forEach(i -> towers.get(TOWER.T1).add(i));
        this.discs = discAmount;
        moves = 0;

        // spacing between towers need to adjust dynamically to
        // the amount of discs.
        final int firstOff = discs * 4;
        final int secondOff = discs * 5;
        pillarSpacings = Arrays.asList(firstOff, secondOff, secondOff);
    }

    /**
     * Prints the towers of Hanoi game board in ASCII art format to the provided {@link PrintStream}.
     *
     * @param out the {@link PrintStream} used for print the game board.
     */
    private void printPillars(final PrintStream out) {
        String block = "██";

        // Map to store the disc strings, based on the size of the disc
        Map<Integer, String> discObjs = IntStream.rangeClosed(1, AppConfig.DISC_AMOUNT_MAXIMUM)
                .boxed()
                .collect(Collectors.toMap(Function.identity(),
                        i -> i == 1 ? block : block.repeat(i * 2 - 1)));

        // Map to store the disc offsets, based on the size of the disc
        Map<Integer, Integer> discOffsets = IntStream.iterate(0, i -> i + 2)
                .limit(AppConfig.DISC_AMOUNT_MAXIMUM)
                .boxed()
                .collect(Collectors.toMap(i -> i / 2 + 1, Function.identity()));

        final String pillarColor = AppConfig.COLOR_PILLAR;
        final String discColor = AppConfig.COLOR_DISC;

        out.println();

        // Iterate through rows, including one extra row for the top of the pillars
        IntStream.rangeClosed(-1, discs - 1).forEach(row -> {
            // Initialize offset for disc printing
            AtomicInteger offset = new AtomicInteger(0);

            // Iterate through each tower (pillar)
            IntStream.range(0, AppConfig.TOWERS_AMOUNT).forEach(tower -> {
                // Get the spacing value for the current pillar
                int pillarSpacing = pillarSpacings.get(tower);
                // Get the number of discs in the current tower
                int towerSize = towers.get(tower).size();

                // If we are at the top row, print the top of the pillar
                if (row < 0) {
                    printTopOfPillar(out, pillarColor, pillarSpacing);
                } else if ((discs - row) > towerSize) { // If there's no disc at this position, print an empty space
                    printEmptySpace(out, pillarColor, pillarSpacing, offset.get());
                    offset.set(0);
                } else { // If there's a disc at this position, print the disc
                    int idx = row - discs + towerSize;
                    int disc = towers.get(tower).get(idx);
                    int tmpOff = discOffsets.get(disc);
                    String spacing = " ".repeat(pillarSpacing - (offset.get() + tmpOff));
                    out.printf("%s%s%s%s", discColor, spacing, discObjs.get(disc), AppConfig.COLOR_RESET);
                    offset.set(tmpOff);
                }
            });

            // Print a newline character after each row
            out.println();
        });
    }

    /**
     * Prints the top of a pillar to the provided {@link PrintStream}.
     *
     * @param out           the {@link PrintStream} used for printing.
     * @param pillarColor   the color code for the pillar.
     * @param pillarSpacing the spacing used for the pillar.
     */
    private void printTopOfPillar(final PrintStream out, final String pillarColor, final int pillarSpacing) {
        out.printf("%s%s%s%s", pillarColor, " ".repeat(pillarSpacing), "╔╗", AppConfig.COLOR_RESET);
    }

    /**
     * Prints an empty space where there's no disc to the provided {@link PrintStream}.
     *
     * @param out           the {@link PrintStream} used for printing.
     * @param pillarCol     the color code for the pillar.
     * @param pillarSpace   the spacing for the pillar.
     * @param off           the offset to use for the empty space.
     */
    private void printEmptySpace(final PrintStream out, final String pillarCol, final int pillarSpace, final int off) {
        String offset = " ".repeat(pillarSpace - off);
        out.printf("%s%s%s%s", pillarCol, offset, "║║", AppConfig.COLOR_RESET);
    }

    /**
     * Prints the base of the towers in ASCII art format to the provided {@link PrintStream}.
     *
     * @param out the {@link PrintStream} used for printing.
     */
    private void printBase(final PrintStream out) {
        final String baseColor = AppConfig.COLOR_TOWER_BASE;

        final String base = "▀".repeat(10);

        // Calculate the base spacing for the first tower (pillar) based on the number of discs.
        final String baseSpacingFirst = " ".repeat(4 * (discs - 1));

        // Calculate the base spacing for the rest of the towers (pillars) based on the number of discs.
        final String baseSpacingRest = " ".repeat(5 * (discs - 2) + 2);

        // Loop through each tower (pillar) and print the base
        IntStream.range(0, AppConfig.TOWERS_AMOUNT)
                .forEach(i -> out.printf("%s%s%s%s", baseColor, (i == 0)
                        ? baseSpacingFirst : baseSpacingRest, base, AppConfig.COLOR_RESET));

        out.println();

        final String textColor = AppConfig.COLOR_TOWER_INFO;
        final int textSpacing = 5;

        List<String> str = Arrays.asList("T1", "T2", "T3", "Moves: " + moves);

        // Format the strings for each tower and join them together
        String sb = String.join("",
                str.subList(0, str.size() - 1).stream()
                        .map(s -> String.format("%s%s%s%s", textColor,
                                " ".repeat(pillarSpacings.get(str.indexOf(s))), s, AppConfig.COLOR_RESET))
                        .toArray(String[]::new))
        + String.format("%s%s%s%s\n", textColor, " ".repeat(textSpacing), // Add the moves counter
                str.get(str.size() - 1), AppConfig.COLOR_RESET);

        out.print(sb);
    }

    /**
     * Prints the current state of the Towers of Hanoi game board in ASCII art
     * format to the standard output stream.
     */
    public void showGameStateASCII() {
        PrintStream out = System.out;

        // Print the pillars and discs
        printPillars(out);

        // Print the base of the towers
        printBase(out);
    }

    /**
     * Responsible for updating game state by performing requested move action.
     *
     * @param from source tower for the disc to move.
     * @param to destination tower for the disc to move.
     * @param incrementMove update move counter, either increment or decrement.
     */
    public void performMove(final int from, final int to, final boolean incrementMove) {
        List<Integer> fromTower = towers.get(from - 1);     // source tower
        List<Integer> toTower = towers.get(to - 1);         // destination tower

        if (fromTower.size() == 0) {
            throw new InvalidMoveException("No disc to move!");
        } else if (toTower.size() > 0 && fromTower.get(0) > toTower.get(0)) {
            throw new InvalidMoveException("Larger discs cannot be placed on top smaller ones!");
        } else if (fromTower == toTower) {
            throw new InvalidMoveException("Destination tower needs to be different from source tower!");
        }

        // we have a legal move
        int fromValue = fromTower.get(0);
        toTower.add(0, fromValue);
        fromTower.remove(0);

        moves += (incrementMove) ? 1 : -1;
    }

    /**
     * Responsible for resetting game states.
     *
     * @param discAmount amount of discs to be used.
     */
    public void resetGame(final int discAmount) {
        //this.discs = discAmount;
        towers.forEach(List::clear);
        resetEngine(discAmount);  // resets the towers to initial states
    }

    /**
     * Determines whether the game is completed, i.e. that all discs have been placed
     * in the third tower.
     *
     * @return whether the game is completed.
     */
    public boolean isGameCompleted() {
        return towers.get(TOWER.T3).size() == discs;
    }

    /**
     * Conveys current state of requested tower.
     *
     * @param tower the tower which is requested for query.
     * @return current state of requested tower, in terms of disc amount.
     */
    public int getTowerState(final int tower) {
        return towers.get(tower).size();
    }

    /**
     * Simple accessor to get the amount of moves performed.
     *
     * @return current move count.
     */
    public int getMoves() {
        return moves;
    }

    /**
     * Simple accessor to get the amount of discs.
     *
     * @return current disc amount.
     */
    public int getDiscAmount() { return discs; }
}
