package backend.academy.mazes;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Getter;

/**
 * Class for maze representation in {@link backend.academy.Game}.
 * Maze is Immutable, use {@link MutableMazeDTO} for mutable representation of maze.
 */

@Getter
public class Maze {

    @Getter(AccessLevel.NONE)
    private final Cell[][] grid;
    private final Map<Cell, List<Cell>> mazeGraph;
    private final int height;
    private final int width;

    public Maze(Map<Cell, List<Cell>> mazeGraph, Cell[][] grid) {
        this.grid = Arrays.stream(grid).map(Cell[]::clone).toArray(Cell[][]::new);
        this.mazeGraph = Collections.unmodifiableMap(mazeGraph);
        this.height = grid.length;
        this.width = grid[0].length;
    }

    public Maze(MutableMazeDTO mutableDTO) {
        this(mutableDTO.mazeGraph(), mutableDTO.grid());
    }

    public Cell[][] getGrid() {
        return Arrays.stream(grid).map(Cell[]::clone).toArray(Cell[][]::new);
    }
}
