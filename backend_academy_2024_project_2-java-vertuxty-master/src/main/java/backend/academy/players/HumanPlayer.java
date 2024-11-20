package backend.academy.players;

import backend.academy.Game;
import backend.academy.mazes.Coordinate;
import backend.academy.utils.Messages;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import lombok.SneakyThrows;

/**
 * Realization of {@link Player} interface.
 * Represent Human Player in {@link Game}.
 */
@SuppressWarnings("RegexpSinglelineJava")
public class HumanPlayer extends AbstractPlayer {

    /**
     * Read data from console.
     */
    private final BufferedReader bufferedReader;

    /**
     * Create instance of HumanPlayer.
     * Initializing object.
     */
    public HumanPlayer() {
        this.bufferedReader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
    }

    /**
     * {@inheritDoc}
     */
    @SneakyThrows
    @Override
    public String makeMove() {
        String input = bufferedReader.readLine();
        String line;
        if (input != null) {
            line = input.toUpperCase();
        } else {
            throw new NullPointerException("No input");
        }
        return line;
    }

    @SneakyThrows
    @Override
    public boolean readCycleInfo() {
        String input = makeMove();
        try {
            int playerDecision = Integer.parseInt(input);
            if (playerDecision == 1) {
                return true;
            } else if (playerDecision != 2) {
                throw new IllegalStateException(Messages.apply(Messages.ERROR_OUT_OF_RANGE_TEMPLATE, 1, 2));
            }
            return false;
        } catch (NumberFormatException e) {
            throw new IllegalStateException(Messages.ERROR_WRONG_NUMBER_FORMAT, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @SneakyThrows
    @Override
    public Coordinate readPointCoordinate(int height, int width) {
        String[] args = makeMove().split(" ");
        try {
            if (args.length != 2) {
                throw new IllegalStateException("Write both point coordinate (x, y)");
            }
            int x = Integer.parseInt(args[0]);
            int y = Integer.parseInt(args[1]);
            if (x < 0 || x >= height) {
                throw new IllegalStateException("Coordinate x "
                    + Messages.apply(Messages.ERROR_OUT_OF_RANGE_TEMPLATE, 0,
                    height - 1));
            }
            if (y < 0 || y >= width) {
                throw new IllegalStateException("Coordinate y "
                    + Messages.apply(Messages.ERROR_OUT_OF_RANGE_TEMPLATE, 0,
                    width - 1));
            }
            return new Coordinate(x, y);
        } catch (NumberFormatException e) {
            throw new IllegalStateException(Messages.ERROR_WRONG_NUMBER_FORMAT, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @SneakyThrows
    @Override
    public int getMazeSize(int minVal, int maxVal) {
        String input = makeMove();
        try {
            int size = Integer.parseInt(input);
            if (size < minVal || size > maxVal) {
                throw new IllegalStateException(Messages.apply(Messages.ERROR_OUT_OF_RANGE_TEMPLATE, minVal, maxVal));
            }
            return size;
        } catch (NumberFormatException e) {
            throw new IllegalStateException(Messages.ERROR_WRONG_NUMBER_FORMAT, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @SneakyThrows
    @Override
    public <T> T getTypeFromInput(Class<T> clazz) {
        String input = makeMove();
        try {
            int playerDecision = Integer.parseInt(input);
            return clazz.getEnumConstants()[playerDecision - 1];
        } catch (NumberFormatException e) {
            throw new IllegalStateException(Messages.ERROR_WRONG_NUMBER_FORMAT, e);
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalStateException(
                Messages.apply(Messages.ERROR_OUT_OF_RANGE_TEMPLATE, 1, clazz.getEnumConstants().length), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @SneakyThrows
    @Override
    public void close() {
        bufferedReader.close();
    }
}
