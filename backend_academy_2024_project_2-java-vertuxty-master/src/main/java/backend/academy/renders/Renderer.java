package backend.academy.renders;

import backend.academy.mazes.Coordinate;
import backend.academy.mazes.Maze;
import java.util.List;

/**
 * Interface for render data.
 * */
public interface Renderer {

    /**
     * Render maze.
     *
     * @param maze maze that we want to render
     * @return rendered maze in {@code String} format.
     * */
    String render(Maze maze);

    /**
     * Render maze with given path
     *
     * @param maze maze that we want to render
     * @param path path from one point to other in maze.
     * @return rendered maze in {@code String} format with given path.
     * */
    String render(Maze maze, List<Coordinate> path);
}
