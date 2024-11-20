package backend.academy.players;

import backend.academy.Game;
import java.io.IOException;

/**
 * Interface for implementing player in {@link Game}.
 * Defines methods for players moves.
 * */
public interface Player {

    /**
     * The method that is used for the player's move
     *
     * @return result of move.
     * @throws IOException throws when incorrect logic or move.
     * */
    String makeMove() throws IOException;

    /**
     * Method that close player
     *
     * @throws IOException throws if failed to close the user correctly
     * */
    void close() throws IOException;
}
