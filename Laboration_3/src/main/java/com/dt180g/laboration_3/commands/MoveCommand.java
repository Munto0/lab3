package com.dt180g.laboration_3.commands;

import com.dt180g.laboration_3.support.AppConfig;
import com.dt180g.laboration_3.receiver.HanoiEngine;
import com.dt180g.laboration_3.support.HanoiLogger;

/**
 * MoveCommand encapsulates a single move action in the Towers of Hanoi game.
 * On execution, it moves a disc from the source tower to the destination tower
 * and logs the move. On unExecute (undo), it reverses the move and logs an undo symbol.
 *
 * @author Muntaser Ibrahim
 */
public class MoveCommand implements CommandInterface {
    private final int src, dst;

    /**
     * Constructs a Move from source and destination towers.
     *
     * @param src the index of the tower to move a disc from
     * @param dst the index of the tower to move a disc to
     */
    public MoveCommand(int src, int dst) {
        this.src = src; this.dst = dst;
    }

    /**
     * performs the disc transfer in HanoiEngine and logs the move.
     */
    @Override
    public void execute() {
        HanoiEngine.INSTANCE.performMove(src, dst, true);
        HanoiLogger.getInstance().logInfo(src + " " + dst);
    }

    /** Reverses the move and logs the undo symbol. */
    public void unExecute() {
        HanoiEngine.INSTANCE.performMove(dst, src, false);
        HanoiLogger.getInstance().logInfo(AppConfig.LOG_UNDO_SYMBOL);
    }
}

