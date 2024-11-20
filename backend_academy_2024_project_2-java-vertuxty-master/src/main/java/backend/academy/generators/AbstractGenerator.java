package backend.academy.generators;

import backend.academy.mazes.Cell;
import backend.academy.mazes.Coordinate;
import backend.academy.mazes.Edge;
import backend.academy.mazes.Maze;
import backend.academy.mazes.MutableMazeDTO;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Implementation of a {@link Generator} interface.
 * This class define functionality for generator's algorithms.
 */
public abstract class AbstractGenerator implements Generator {

    private static final int FLOOR_BOUND = 1;

    private static final int CEIL_BOUND = 100;

    private static final double PROBABILITY = 0.07;

    protected final SecureRandom secureRandom;

    protected final boolean isCycleAllowed;

    /**
     * Constructor for creating instance of generator.
     *
     * @param isCycleAllowed boolean flag,
     *                       that define can we have cycle in maze or not
     */
    protected AbstractGenerator(final boolean isCycleAllowed) {
        this.secureRandom = new SecureRandom();
        this.isCycleAllowed = isCycleAllowed;
    }

    /**
     * Method for generating type of {@link Cell} with it's {@link Coordinate}
     *
     * @param grid  a maze where cell is located
     * @param cords coordinates of cell.
     */
    protected void addRandomCellIfNecessary(Cell[][] grid, Coordinate cords) {
        int row = cords.row();
        int col = cords.col();
        if (grid[row][col] != null) {
            return;
        }
        int bound = Cell.Type.length();
        Cell.Type type = Cell.Type.values()[secureRandom.nextInt(1, bound)];
        grid[row][col] = new Cell(cords, type);
    }

    /**
     * Method that determines with some probability
     * whether to delete a wall or not in the maze.
     *
     * @param mutableDTO mutable structure of maze.
     * @param start      coordinate of cell behind wall
     * @param end        coordinate of cell behind other wall's side
     */
    protected void addAccordingToProbability(MutableMazeDTO mutableDTO, Coordinate start, Coordinate end) {
        if (isCycleAllowed && (1.0 > PROBABILITY * (secureRandom.nextInt(FLOOR_BOUND, CEIL_BOUND)))) {
            addEdges(mutableDTO, start, end);
        }
    }

    /**
     * Method that deleted wall between to cells.
     * Also create edge (path) between them in {@code both} way.
     *
     * @param mutableDTO mutable structure of maze.
     * @param fromCell   coordinate of cell behind wall
     * @param toCell     coordinate of cell behind other wall's side
     * @see #addOrientedEdge(Map, Cell, Cell)
     */
    protected void addEdges(MutableMazeDTO mutableDTO, Coordinate fromCell, Coordinate toCell) {
        Cell cell1 = mutableDTO.getCell(fromCell);
        Cell cell2 = mutableDTO.getCell(toCell);
        addOrientedEdge(mutableDTO.mazeGraph(), cell1, cell2);
        addOrientedEdge(mutableDTO.mazeGraph(), cell2, cell1);
    }

    /**
     * Method that deleted wall between to cells.
     * Also create edge (path) between them in {@code one} way.
     *
     * @param mazeGraph graph of all path between cells in maze.
     * @param fromCell  coordinate of cell behind wall
     * @param toCell    coordinate of cell behind other wall's side
     * @see #addEdges(MutableMazeDTO, Coordinate, Coordinate)
     */
    protected void addOrientedEdge(Map<Cell, List<Cell>> mazeGraph, Cell fromCell, Cell toCell) {
        List<Cell> adj = mazeGraph.computeIfAbsent(fromCell, k -> new ArrayList<>());
        if (!adj.contains(toCell)) {
            adj.add(toCell);
        }
    }

    /**
     * Add wall between two cell in maze.
     *
     * @param grid  grid of our mutable maze DTO
     * @param walls list of walls in maze
     * @param edge  edge that represent wall.
     * @see Cell
     * @see MutableMazeDTO
     * @see Maze
     * @see #addNearWalls(Cell[][], List, Coordinate)
     */
    protected void addWall(
        Cell[][] grid,
        List<Edge> walls,
        Edge edge
    ) {
        if (edge.to().row() < 0 || edge.to().row() >= grid.length || edge.to().col() < 0
            || edge.to().col() >= grid[0].length
        ) {
            return;
        }
        walls.add(edge);
    }

    /**
     * Method for base maze initialization
     * before generation by algorithm.
     *
     * @param mutableDTO mutable maze structure that need to be init.
     */
    protected void initMutableMazeDTO(MutableMazeDTO mutableDTO) {
        for (int row = 0; row < mutableDTO.height(); row++) {
            for (int col = 0; col < mutableDTO.width(); col++) {
                addRandomCellIfNecessary(mutableDTO.grid(), new Coordinate(row, col));
            }
        }
    }

    /**
     * Add walls around cell.
     * Cell is represented by it's {@link Coordinate}.
     *
     * @param grid  field of mutable maze DTO, where we need to add walls.
     * @param walls list of walls in maze
     * @param from  (X, Y)'s coordinate of cell in maze
     * @see MutableMazeDTO
     * @see Maze
     * @see #addWall(Cell[][], List, Edge)
     */
    protected void addNearWalls(
        Cell[][] grid,
        List<Edge> walls,
        Coordinate from
    ) {
        addWall(grid, walls, new Edge(from, new Coordinate(from.row() - 1, from.col())));
        addWall(grid, walls, new Edge(from, new Coordinate(from.row() + 1, from.col())));
        addWall(grid, walls, new Edge(from, new Coordinate(from.row(), from.col() + 1)));
        addWall(grid, walls, new Edge(from, new Coordinate(from.row(), from.col() - 1)));
    }

}
