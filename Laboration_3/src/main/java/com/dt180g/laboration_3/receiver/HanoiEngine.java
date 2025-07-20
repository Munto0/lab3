package com.dt180g.laboration_3.receiver;

import com.dt180g.laboration_3.validation.InvalidMoveException;
import com.dt180g.laboration_3.support.AppConfig;

import java.io.PrintStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * The main engine for Towers of Hanoi, receiving command requests.
 *
 * @author Erik Ström
 */
public enum HanoiEngine {
    INSTANCE;

    private int discs = 0, moves = 0;
    private final List<ArrayList<Integer>> towers;
    private List<Integer> pillarSpacings;

    private interface TOWER { int T1 = 0, T2 = 1, T3 = 2; }

    HanoiEngine() {
        towers = IntStream.range(0, AppConfig.TOWERS_AMOUNT)
                .mapToObj(i -> new ArrayList<Integer>())
                .toList();
    }

    private void resetEngine(final int discAmount) {
        IntStream.rangeClosed(1, discAmount)
                .forEach(i -> towers.get(TOWER.T1).add(i));
        this.discs = discAmount;
        moves = 0;
        pillarSpacings = List.of(discs*4, discs*5, discs*5);
    }

    private void printPillars(final PrintStream out) {
        String block = "■■";
        Map<Integer,String> discObjs = IntStream.rangeClosed(1, AppConfig.DISC_AMOUNT_MAXIMUM)
                .boxed()
                .collect(Collectors.toMap(Function.identity(),
                        i -> i==1 ? block : block.repeat(i*2-1)));
        Map<Integer,Integer> offsets = IntStream.iterate(0, i -> i+2)
                .limit(AppConfig.DISC_AMOUNT_MAXIMUM)
                .boxed()
                .collect(Collectors.toMap(i -> i/2+1, Function.identity()));
        String pCol = AppConfig.COLOR_PILLAR, dCol = AppConfig.COLOR_DISC;

        out.println();
        IntStream.rangeClosed(-1, discs-1).forEach(row -> {
            AtomicInteger usedOff = new AtomicInteger(0);
            IntStream.range(0, AppConfig.TOWERS_AMOUNT).forEach(t -> {
                int space = pillarSpacings.get(t);
                int size = towers.get(t).size();
                if (row<0) {
                    out.printf("%s%s—%s", pCol, " ".repeat(space), AppConfig.COLOR_RESET);
                } else if ((discs-row)>size) {
                    out.printf("%s%s‖%s", pCol, " ".repeat(space-usedOff.get()), AppConfig.COLOR_RESET);
                    usedOff.set(0);
                } else {
                    int idx = row-discs+size;
                    int disc = towers.get(t).get(idx);
                    int off = offsets.get(disc);
                    String pad = " ".repeat(space-(usedOff.get()+off));
                    out.printf("%s%s%s%s", dCol, pad, discObjs.get(disc), AppConfig.COLOR_RESET);
                    usedOff.set(off);
                }
            });
            out.println();
        });
    }

    private void printBase(final PrintStream out) {
        String baseCol = AppConfig.COLOR_TOWER_BASE, base="—".repeat(10);
        String firstSp = " ".repeat(4*(discs-1));
        String restSp = " ".repeat(5*(discs-2)+2);
        IntStream.range(0, AppConfig.TOWERS_AMOUNT)
                .forEach(i -> out.printf("%s%s%s%s",
                        baseCol, i==0?firstSp:restSp, base, AppConfig.COLOR_RESET));
        out.println();
        String infoCol = AppConfig.COLOR_TOWER_INFO;
        String movesText = "Moves: "+moves;
        String labels = String.format("%s%sT1%s%sT2%s%sT3%s%s%s%n",
                infoCol, " ".repeat(pillarSpacings.get(0)), " ",
                " ".repeat(pillarSpacings.get(1)), " ",
                " ".repeat(pillarSpacings.get(2)), " ",
                movesText, AppConfig.COLOR_RESET);
        out.print(labels);
    }

    /** Prints the current ASCII game state. */
    public void showGameStateASCII() {
        printPillars(System.out);
        printBase(System.out);
    }

    /**
     * Performs a move (and optionally adjusts move counter).
     *
     * @param from  source tower index (1–3)
     * @param to    dest tower index (1–3)
     * @param inc   true to increment move count, false to decrement
     */
    public void performMove(final int from, final int to, final boolean inc) {
        List<Integer> f = towers.get(from-1), t = towers.get(to-1);
        if (f.isEmpty()) throw new InvalidMoveException("No disc to move!");
        if (!t.isEmpty() && f.get(0)>t.get(0))
            throw new InvalidMoveException("Larger discs cannot be placed on top smaller ones!");
        if (f==t) throw new InvalidMoveException("Destination tower needs to be different from source tower!");
        int val = f.remove(0);
        t.add(0, val);
        moves += inc?1:-1;
    }

    /** Resets the game to a fresh state with the given disc count. */
    public void resetGame(final int discAmount) {
        towers.forEach(List::clear);
        resetEngine(discAmount);
    }

    /** @return whether all discs are on the third tower. */
    public boolean isGameCompleted() {
        return towers.get(TOWER.T3).size()==discs;
    }

    public int getDiscAmount() { return discs; }
    public int getMoves()      { return moves; }
    public int getTowerState(int tower) { return towers.get(tower).size(); }
}
