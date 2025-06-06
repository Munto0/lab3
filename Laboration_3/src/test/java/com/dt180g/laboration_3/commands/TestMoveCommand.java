package com.dt180g.laboration_3.commands;

import com.dt180g.laboration_3.TestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.stream.Collector;

/**
 * This class provides JUnit test cases for {@link MoveCommand}.
 * The class extends {@link TestBase} to inherit common functionality
 * for running tests.
 *
 * @author Erik Ström
 */
public class TestMoveCommand extends TestBase {
    /**
     * Executes the specified number of moves from the moves list.
     *
     * @param amount The number of moves to execute.
     * @return A Deque containing the executed MoveCommands in reverse order.
     */
    private Deque<MoveCommand> executeMoves(int amount) {
        // Create a stream from the moves sublist up to the specified amount
        return moves.subList(0, amount)
                .stream()
                // Split each move string into an array containing fromTower and toTower as strings
                .map(move -> move.split(" "))
                // Map each array to a new MoveCommand instance with parsed fromTower and toTower values
                .map(tmp -> new MoveCommand(Integer.parseInt(tmp[0]), Integer.parseInt(tmp[1])))
                // Execute each MoveCommand in the stream
                .peek(MoveCommand::execute)
                // Collect the stream elements into a Deque in reverse order using a custom collector
                .collect(Collector.of(
                        // Supplier: Create a new LinkedList as a deque
                        LinkedList::new,
                        // Accumulator: Add the MoveCommand to the beginning of the deque, reversing the order
                        LinkedList::addFirst,
                        // Combiner: Merge two deques, if needed, by adding deque1 to the beginning of deque2
                        (deque1, deque2) -> {
                            deque2.addAll(deque1);
                            return deque2;
                        }));
    }

    /**
     * Unexecutes the specified MoveCommands by calling their unExecute method.
     *
     * @param cmds The Deque containing the MoveCommands to unexecute.
     */
    private void unexecuteMoves(Deque<MoveCommand> cmds) {
        while (!cmds.isEmpty())
            cmds.poll().unExecute();
    }

    /** Sets up the test by resetting the Hanoi engine to the initial state. */
    @BeforeEach
    public void setUp() {
        engine.resetGame(3);
    }

    /** Tests executing a single move. */
    @Test
    public void testMoveExecuteSingle() {
        executeMoves(1);
        super.runAsserts(Arrays.asList(2, 0, 1, 1), false);
    }

    /** Tests executing five moves. */
    @Test
    public void testMoveExecuteFive() {
        executeMoves(5);
        super.runAsserts(Arrays.asList(1, 1, 1, 5), false);
    }

    /** Tests executing all moves in the moves list. */
    @Test
    public void testMoveExecuteComplete() {
        executeMoves(moves.size());
        super.runAsserts(Arrays.asList(0, 0, 3, moves.size()), true);
    }

    /** Tests unexecuting a single move. */
    @Test
    public void testMoveUnexecuteSingle() {
        unexecuteMoves(executeMoves(1));
        super.runAsserts(Arrays.asList(3, 0, 0, 0), false);
    }

    /** Tests unexecuting five moves. */
    @Test
    public void testMoveUnexecuteFive() {
        unexecuteMoves(executeMoves(5));
        super.runAsserts(Arrays.asList(3, 0, 0, 0), false);
    }

    /** Tests unexecuting all moves in the moves list. */
    @Test
    public void testMoveUnexecuteComplete() {
        unexecuteMoves(executeMoves(moves.size()));
        super.runAsserts(Arrays.asList(3, 0, 0, 0), false);
    }
}