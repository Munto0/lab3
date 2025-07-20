package com.dt180g.laboration_3.issuers;

import com.dt180g.laboration_3.commands.MoveCommand;
import com.dt180g.laboration_3.commands.NewGameCommand;
import com.dt180g.laboration_3.commands.ShowCommand;
import com.dt180g.laboration_3.invoker.CommandManager;
import com.dt180g.laboration_3.support.AppConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Reads a saved log and replays the game.
 *
 * @author Muntaser Ibrahim
 */
public class Replayer {
    private final BufferedReader reader;

    /** Default constructor reads from the log file path. */
    public Replayer() throws IOException, URISyntaxException {
        reader = new BufferedReader(new java.io.FileReader(AppConfig.getLogFilePath()));
    }

    /** Test constructor that takes any BufferedReader. */
    public Replayer(BufferedReader reader) {
        this.reader = reader;
    }

    /** Runs the replay: first line is disc count, then moves or undos. */
    public void runReplay() throws IOException {
        String line = reader.readLine();
        int discs = Integer.parseInt(line.trim());
        CommandManager.INSTANCE.executeCommand(new NewGameCommand(discs));

        while ((line = reader.readLine()) != null) {
            if (line.equals(AppConfig.LOG_UNDO_SYMBOL)) {
                CommandManager.INSTANCE.undoMove();
            } else {
                String[] parts = line.split(" ");
                int src = Integer.parseInt(parts[0]);
                int dst = Integer.parseInt(parts[1]);
                CommandManager.INSTANCE.executeCommand(new MoveCommand(src, dst));
            }
            if (AppConfig.shouldShowReplayMoves()) {
                new ShowCommand().execute();
            }
        }
    }
}
