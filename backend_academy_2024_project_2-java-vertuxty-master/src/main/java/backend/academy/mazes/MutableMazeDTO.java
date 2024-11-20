package backend.academy.mazes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;

@Getter
public class MutableMazeDTO {

    private final int height;
    private final int width;
    private final Cell[][] grid;
    private final Map<Cell, List<Cell>> mazeGraph;

    public MutableMazeDTO(int height, int width) {
        this.height = height;
        this.width = width;
        this.grid = new Cell[height][width];
        this.mazeGraph = new HashMap<>();
    }

    public Cell getCell(Coordinate coordinate) {
        return grid[coordinate.row()][coordinate.col()];
    }

    public Cell getCell(int row, int col) {
        return grid[row][col];
    }
}
