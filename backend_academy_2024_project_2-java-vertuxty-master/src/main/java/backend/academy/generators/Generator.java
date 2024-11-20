package backend.academy.generators;

import backend.academy.mazes.Maze;

/**
 * A interface for implementing maze's generators.
 * Defines methods for generating maze.
 * */
public interface Generator {

    /**
     * Generate random maze by {@code height} and {@code width}
     *
     * @param height a height of a maze
     * @param width a width of a maze
     *
     * @return random generated {@link Maze}
     * */
    Maze generate(int height, int width);
}
