package com.dt180g.laboration_3.commands;
import com.dt180g.laboration_3.receiver.HanoiEngine;
import com.dt180g.laboration_3.support.HanoiLogger;
import com.dt180g.laboration_3.support.AppConfig;
/**
 * NewGameCommand initializes or restarts a Towers of Hanoi game with a specified number of discs.
 * It ensures the disc count is within the  limits, resets
 *  and logs the initial count.
 *
 * @author Muntaser Ibrahim
 */
public class NewGameCommand implements CommandInterface {
    private final int discs;
    /**
     * Constructs a NewGameCommand with the  disc count.
     *
     * @param discs the desired number of discs to start the game
     */
    public NewGameCommand(int discs) {
        this.discs = discs;
    }
    /**
     * Executes the new game command , resetting the game engine, resetting the logger
     * and logging the starting disc count.
     */
    @Override
    public void execute() {
        int count = Math.min(Math.max(discs, AppConfig.DISC_AMOUNT_MINIMUM),
                AppConfig.DISC_AMOUNT_MAXIMUM);
        HanoiEngine.INSTANCE.resetGame(count);
        HanoiLogger.getInstance().resetLogger();
        HanoiLogger.getInstance().logInfo(String.valueOf(count));
    }
}
