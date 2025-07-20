package com.dt180g.laboration_3.invoker;

import com.dt180g.laboration_3.commands.*;
import com.dt180g.laboration_3.validation.InvalidMoveException;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Singleton manager responsible for executing game commands, as well as undoing and redoing moves
 * in the Towers of Hanoi application. It maintains two stacks (undo and redo) of MoveCommand
 * objects to track history and support reversal of actions.
 *
 * NewGameCommand clears both stacks to reset history. MoveCommand instances are pushed to
 * the undo stack on execution, and popped/pushed between undo and redo stacks on undo/redo.
 *
 * @author Muntaser Ibrahim
 */
public class CommandManager {
    public static final CommandManager INSTANCE = new CommandManager();

    private final Deque<MoveCommand> undoStack = new ArrayDeque<>(),
            redoStack = new ArrayDeque<>();

    private CommandManager() { }

    /** Clears both undo and redo history. Used by NewGameCommand. */
    private void clearMoves() {
        undoStack.clear();
        redoStack.clear();
    }

    /**
     * Executes a command. NewGameCommand clears history; MoveCommand pushes to undo and clears redo.
     * Invalid moves are caught and reported.
     */
    public void executeCommand(CommandInterface cmd) {
        try {
            cmd.execute();
            if (cmd instanceof NewGameCommand) {
                clearMoves();
            } else if (cmd instanceof MoveCommand) {
                undoStack.push((MoveCommand)cmd);
                redoStack.clear();
            }
        } catch (InvalidMoveException e) {
            System.out.println(e.getMessage());
        }
    }

    /** Undoes the last move if available. */
    public void undoMove() {
        if (!undoStack.isEmpty()) {
            MoveCommand m = undoStack.pop();
            m.unExecute();
            redoStack.push(m);
        }
    }

    /** Redoes the last undone move if available. */
    public void redoMove() {
        if (!redoStack.isEmpty()) {
            MoveCommand m = redoStack.pop();
            m.execute();
            undoStack.push(m);
        }
    }

    /** @return number of moves available to undo. */
    public int getUndoAmount() { return undoStack.size(); }
    /** @return number of moves available to redo. */
    public int getRedoAmount() { return redoStack.size(); }
}
