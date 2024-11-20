package backend.academy;

import lombok.experimental.UtilityClass;

/**
 * Main logic.
 */
@UtilityClass public class Main {
    public static void main(String[] args) {
        Game game = new Game();
        game.run();
    }
}
