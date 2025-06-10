package com.dt180g.laboration_3.issuers;
import com.dt180g.laboration_3.commands.MoveCommand;
import com.dt180g.laboration_3.commands.NewGameCommand;
import com.dt180g.laboration_3.commands.ShowCommand;
import com.dt180g.laboration_3.invoker.CommandManager;
import com.dt180g.laboration_3.support.AppConfig;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
public class Replayer {
    private final BufferedReader reader;
    /**
     * Default constructor reads from the log file path.
     * @throws IOException if reading fails
     * @throws URISyntaxException if the log-file path URI is invalid
     *
     * @author Muntaser Ibrahim
     */
    public Replayer() throws IOException, URISyntaxException {
        reader = new BufferedReader(new FileReader(AppConfig.getLogFilePath()));
    }
    /**
     * Test constructor that takes any BufferedReader.
     */
    public Replayer(BufferedReader reader) {
        this.reader = reader;
    }
    /**
     * Runs the replay: first line is disc count, then sequences of moves or undos.
     * @throws IOException if reading from the reader fails
     */
    public void runReplay() throws IOException {
        // Read initial disc count
        String line = reader.readLine();
        int discs = Integer.parseInt(line.trim());
        CommandManager.INSTANCE.executeCommand(new NewGameCommand(discs));
        // Replay each subsequent line
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
