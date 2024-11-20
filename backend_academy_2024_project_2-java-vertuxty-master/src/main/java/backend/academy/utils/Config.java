package backend.academy.utils;

import backend.academy.Game;
import lombok.experimental.UtilityClass;

/**
 * Utility class for constants that used in {@link Game}
 * */
@UtilityClass
public class Config {

    /**
     * Max {@code height} in maze
     * */
    public static final int MAX_HEIGHT = 30;

    /**
     * Max {@code width} in maze
     * */
    public static final int MAX_WIDTH = 30;

    /**
     * Min {@code height} in maze
     * */
    public static final int MIN_HEIGHT = 1;

    /**
     * Min {@code width} in maze
     * */
    public static final int MIN_WIDTH = 1;
}
