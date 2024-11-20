package backend.academy.solvers;

import backend.academy.mazes.Coordinate;
import backend.academy.mazes.Maze;
import java.util.List;

/**
 * A interface for implementing path solvers algorithms in maze.
 * Defines method for finding path and default method for retrace path.
 * */
public interface Solver {

    /**
     * Find path in the maze from from to to.
     * May be implemented for pathfinding with weighted elements.
     *
     * @param maze {@link Maze} that need to be solved
     * @param start starter point in path.
     * @param end final point in path.
     * @return a list of {@link Coordinate} in path, ordered from from to to.
     * */
    List<Coordinate> solve(Maze maze, Coordinate start, Coordinate end);
}
