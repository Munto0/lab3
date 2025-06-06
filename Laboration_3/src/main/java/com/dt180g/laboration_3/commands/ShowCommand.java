package com.dt180g.laboration_3.commands;

import com.dt180g.laboration_3.receiver.HanoiEngine;

/**
 * Concrete command, used to show current game state.
 * The command will trigger {@link HanoiEngine#showGameStateASCII()}, which in
 * turn will print the three towers, discs, and amount of moves made.
 *
 * @author Erik Ström
 */
public class ShowCommand implements CommandInterface {
    /** Default constructor for show command. */
    public ShowCommand() { }

    /** {@inheritDoc} */
    @Override public void execute() {
        HanoiEngine.INSTANCE.showGameStateASCII();
    }
}
