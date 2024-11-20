package backend.academy.players;

import backend.academy.mazes.Coordinate;

public abstract class AbstractPlayer implements Player {

    /**
     * Method for reading user input point coordinate
     *
     * @param height max Y's coordinate of point
     * @param width max X's coordinate of point
     *
     * @return {@link Coordinate} coordinate of position written by user.
     * */
    public abstract Coordinate readPointCoordinate(int height, int width);

    /**
     * Method for reading one of maze's size.
     * Using for reading height or width of maze.
     *
     * @param minVal minimal value that user can input.
     * @param maxVal maximum value that user can input.
     *
     * @return one of the maze size that user input.
     * */
    public abstract int getMazeSize(int minVal, int maxVal);

    /**
     * Method for reading user decision of which element from list of Class<...> (enum) elements he is choosing.
     *
     * @param clazz a class with enum constants
     * @return one of the enum values of Class<...>.
     * */
    public abstract <T> T getTypeFromInput(Class<T> clazz);

    public abstract boolean readCycleInfo();
}
