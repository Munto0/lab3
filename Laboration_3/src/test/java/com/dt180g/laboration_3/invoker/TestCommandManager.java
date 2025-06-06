package com.dt180g.laboration_3.invoker;

import com.dt180g.laboration_3.TestBase;
import com.dt180g.laboration_3.commands.MoveCommand;
import com.dt180g.laboration_3.commands.NewGameCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestCommandManager extends TestBase {
    private int movesAmount = super.moves.size();

    private void executeMoves() {
        super.moves.stream()
                .map(move -> move.split(" "))
                .map(tmp -> new MoveCommand(Integer.parseInt(tmp[0]), Integer.parseInt(tmp[1])))
                .forEach(CommandManager.INSTANCE::executeCommand);
    }


    @BeforeEach
    public void setUp() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        engine.resetGame(3);

        // clear moves by invoking CommandManager::clearMoves(), using reflection
        Method method = CommandManager.class.getDeclaredMethod("clearMoves");
        method.setAccessible(true);
        method.invoke(CommandManager.INSTANCE);
    }

    @Test
    public void testConstructorIsPrivate() {
        assertTrue(Arrays.stream(CommandManager.class.getDeclaredConstructors())
                .allMatch(constructor -> Modifier.isPrivate(constructor.getModifiers())));
    }

    @Test
    public void testManagerDoCommands() {
        executeMoves();
        super.runAsserts(Arrays.asList(0, 0, 3, movesAmount), true);
    }

    @Test
    public void testManagerUndoCommands() {
        executeMoves();
        IntStream.range(0, movesAmount).forEach(i -> CommandManager.INSTANCE.undoMove());
        super.runAsserts(Arrays.asList(3, 0, 0, 0), false);
    }

    @Test
    public void testManagerRedoCommands() {
        executeMoves();
        IntStream.range(0, movesAmount).forEach(i -> CommandManager.INSTANCE.undoMove());
        IntStream.range(0, movesAmount).forEach(i -> CommandManager.INSTANCE.redoMove());
        super.runAsserts(Arrays.asList(0, 0, 3, movesAmount), true);
    }

    @Test
    public void testManagerClearMoves() {
        executeMoves();
        CommandManager.INSTANCE.executeCommand(new NewGameCommand(3));
        super.runAsserts(Arrays.asList(3, 0, 0, 0), false);
    }

    @Test
    public void testManagerUndoSizeFilled() {
        executeMoves();
        assertEquals(movesAmount, CommandManager.INSTANCE.getUndoAmount(),
                String.format("Amount of available undoes after adding %1$d moves needs to be %1$d", movesAmount));
    }

    @Test
    public void testManagerUndoSizeDecremented() {
        executeMoves();

        int decrement = 3;
        IntStream.range(0, decrement).forEach(i -> CommandManager.INSTANCE.undoMove());
        assertEquals(movesAmount - decrement, CommandManager.INSTANCE.getUndoAmount(),
                String.format("Amount of available undoes after adding %d moves and performing %d undo needs to be %d",
                        movesAmount, decrement, movesAmount - decrement));
    }

    @Test
    public void testManagerUndoSizeEmpty() {
        executeMoves();
        IntStream.range(0, movesAmount).forEach(i -> CommandManager.INSTANCE.undoMove());
        assertEquals(0, CommandManager.INSTANCE.getUndoAmount(),
                String.format("Amount of available undoes after adding %1$d moves and performing %1$d undoes needs to be 0.",
                        movesAmount));
    }

    @Test
    public void testManagerRedoSizeFilled() {
        executeMoves();
        IntStream.range(0, movesAmount).forEach(i -> CommandManager.INSTANCE.undoMove());
        assertEquals(movesAmount, CommandManager.INSTANCE.getRedoAmount(),
                String.format("Amount of available redoes after adding %1$d moves needs to be %1$d", movesAmount));
    }

    @Test
    public void testManagerRedoSizeDecremented() {
        executeMoves();
        IntStream.range(0, movesAmount).forEach(i -> CommandManager.INSTANCE.undoMove());

        int decrement = 2;
        IntStream.range(0, decrement).forEach(i -> CommandManager.INSTANCE.redoMove());
        assertEquals(movesAmount - decrement, CommandManager.INSTANCE.getRedoAmount(),
                String.format("Amount of available redoes after undoing %d moves and redoing %d needs to be %d",
                        movesAmount, decrement, movesAmount - decrement));
    }

    @Test
    public void testManagerRedoSizeEmpty() {
        executeMoves();
        IntStream.range(0, movesAmount).forEach(i -> CommandManager.INSTANCE.undoMove());
        IntStream.range(0, movesAmount).forEach(i -> CommandManager.INSTANCE.redoMove());
        assertEquals(0, CommandManager.INSTANCE.getRedoAmount(),
                String.format("Amount of available redoes after undoint %1$d moves and redoing %1$d needs to be 0",
                        movesAmount));
    }

    @Test
    public void testManagerRedoBehavior() {
        executeMoves();
        IntStream.range(0, movesAmount).forEach(i -> CommandManager.INSTANCE.undoMove());
        CommandManager.INSTANCE.executeCommand(new MoveCommand(1, 3));
        assertEquals(0, CommandManager.INSTANCE.getRedoAmount(),
                "Amount of available redoes after performing a new move needs to be 0");
    }
}
