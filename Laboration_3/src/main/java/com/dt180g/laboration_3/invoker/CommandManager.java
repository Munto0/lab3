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
    /**
     * Used by NewGameCommand to reset history.
     */
    private void clearMoves() {
        undoStack.clear();
        redoStack.clear();
    }
    /**
     * NewGameCommand clears history. MoveCommand adds to undo stack and clears redo stack.
     * Invalid moves are caught and reported to console.
     *
     * @param cmd the command to execute
     */
    public void executeCommand(CommandInterface cmd) {
        try {
            cmd.execute();
            if (cmd instanceof NewGameCommand) {
                clearMoves();              // now calls your new method
            } else if (cmd instanceof MoveCommand) {
                undoStack.push((MoveCommand)cmd);
                redoStack.clear();
            }
        } catch (InvalidMoveException e) {
            System.out.println(e.getMessage());
        }
    }
    public void undoMove() {
        if (!undoStack.isEmpty()) {
            MoveCommand m = undoStack.pop();
            m.unExecute();
            redoStack.push(m);
        }
    }
    public void redoMove() {
        if (!redoStack.isEmpty()) {
            MoveCommand m = redoStack.pop();
            m.execute();
            undoStack.push(m);
        }
    }
    /**
     * @return the number of moves currently available to undo and to redo
     */
    public int getUndoAmount() { return undoStack.size(); }
    public int getRedoAmount() { return redoStack.size(); }
}
