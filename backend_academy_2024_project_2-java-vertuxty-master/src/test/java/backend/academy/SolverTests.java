package backend.academy;

import backend.academy.mazes.Cell;
import backend.academy.mazes.Coordinate;
import backend.academy.mazes.Maze;
import backend.academy.mazes.MutableMazeDTO;
import backend.academy.solvers.Solver;
import backend.academy.solvers.SolverFactory;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

public class SolverTests {

    @Nested
    class SolverBase {
        @Test
        void multiplyTest() {
            SolverFactory.SolverEnum[] solverEnums = SolverFactory.SolverEnum.values();
            for (SolverFactory.SolverEnum solverEnumFirst : solverEnums) {
                assertThatCode(() -> SolverFactory.createSolver(solverEnumFirst)).doesNotThrowAnyException();
                assertThat(SolverFactory.createSolver(solverEnumFirst)).isNotNull();
                Solver solverFirst = SolverFactory.createSolver(solverEnumFirst);
                for (SolverFactory.SolverEnum solverEnumSecond : solverEnums) {
                    if (!solverEnumFirst.equals(solverEnumSecond)) {
                        assertThatCode(() -> SolverFactory.createSolver(solverEnumSecond)).doesNotThrowAnyException();
                        Solver solverSecond = SolverFactory.createSolver(solverEnumSecond);
                        assertThat(solverFirst.getClass()).isEqualTo(
                            SolverFactory.createSolver(solverEnumFirst).getClass());
                        assertThat(solverSecond.getClass()).isEqualTo(
                            SolverFactory.createSolver(solverEnumSecond).getClass());
                        assertThat(solverFirst.getClass()).isNotEqualTo(solverSecond.getClass());
                    }
                }
            }
        }
    }

    @Nested
    class SolveMaze {

        //        @SneakyThrows
        Maze readKnownMaze(
            String fileName,
            List<Coordinate> weightPath,
            List<Coordinate> simplePath,
            List<Coordinate> endpoints
        ) {
            try (BufferedReader reader = Files.newBufferedReader(Path.of(fileName), StandardCharsets.UTF_8)) {
                int height = Integer.parseInt(reader.readLine());
                int width = Integer.parseInt(reader.readLine());
                int k = Integer.parseInt(reader.readLine());
                Cell[][] grid = new Cell[height][width];

                Map<Cell, List<Cell>> mazeGraph = new HashMap<>();
                String edges;
                for (int i = 0; i < k; i++) {
                    edges = reader.readLine();
                    String[] args = edges.split(" ");
                    int x1 = Integer.parseInt(args[0]);
                    int y1 = Integer.parseInt(args[1]);
                    int w1 = Integer.parseInt(args[2]);

                    int x2 = Integer.parseInt(args[3]);
                    int y2 = Integer.parseInt(args[4]);
                    int w2 = Integer.parseInt(args[5]);

                    grid[x1][y1] = new Cell(new Coordinate(x1, y1), Cell.Type.values()[w1]);
                    grid[x2][y2] = new Cell(new Coordinate(x2, y2), Cell.Type.values()[w2]);

                    if (!mazeGraph.containsKey(grid[x1][y1])) {
                        mazeGraph.put(grid[x1][y1], new ArrayList<>());
                    }

                    if (!mazeGraph.get(grid[x1][y1]).contains(grid[x2][y2])) {
                        mazeGraph.get(grid[x1][y1]).add(grid[x2][y2]);
                    }

                    if (!mazeGraph.containsKey(grid[x2][y2])) {
                        mazeGraph.put(grid[x2][y2], new ArrayList<>());
                    }
                    if (!mazeGraph.get(grid[x2][y2]).contains(grid[x1][y1])) {
                        mazeGraph.get(grid[x2][y2]).add(grid[x1][y1]);
                    }
                }
                if (reader.readLine().equals("1")) {
                    String[] args = reader.readLine().split(" ");
                    int row = Integer.parseInt(args[0]);
                    int col = Integer.parseInt(args[1]);
                    endpoints.add(new Coordinate(row, col));

                    args = reader.readLine().split(" ");
                    row = Integer.parseInt(args[0]);
                    col = Integer.parseInt(args[1]);
                    endpoints.add(new Coordinate(row, col));
                    String pathInfo;
                    int weightedPath = Integer.parseInt(reader.readLine());
                    for (int i = 0; i < weightedPath; i++) {
                        pathInfo = reader.readLine();
                        args = pathInfo.split(" ");
                        int x1 = Integer.parseInt(args[0]);
                        int y1 = Integer.parseInt(args[1]);
                        weightPath.add(new Coordinate(x1, y1));
                    }
                    int simpledPath = Integer.parseInt(reader.readLine());
                    for (int i = 0; i < simpledPath; i++) {
                        pathInfo = reader.readLine();
                        args = pathInfo.split(" ");
                        int x1 = Integer.parseInt(args[0]);
                        int y1 = Integer.parseInt(args[1]);
                        simplePath.add(new Coordinate(x1, y1));
                    }
                }
                return new Maze(mazeGraph, grid);
            } catch (IOException e) {
                return null;
            }
        }

        @Test
        void solveMaze() {
            for (int i = 1; i < 4; i++) {
                String pathName = "src/test/resources/maze" + i + ".txt";
                List<Coordinate> weightPath = new ArrayList<>();
                List<Coordinate> simplePath = new ArrayList<>();
                List<Coordinate> endpoints = new ArrayList<>();

                Maze maze = readKnownMaze(pathName, weightPath, simplePath, endpoints);
                Coordinate start = endpoints.getFirst(),
                    end = endpoints.getLast();
                for (SolverFactory.SolverEnum solverEnum : SolverFactory.SolverEnum.values()) {
                    Solver solver = SolverFactory.createSolver(solverEnum);
                    List<Coordinate> path = solver.solve(maze, start, end);
                    boolean isFound = path.equals(simplePath) || path.size() == simplePath.size()
                        || path.equals(weightPath) || pathSum(maze, path) == pathSum(maze, weightPath);
                    assertThat(isFound).isTrue();
                }
            }
        }

        private int pathSum(Maze maze, List<Coordinate> path) {
            return path.stream().map(cord -> maze.getGrid()[cord.row()][cord.col()].type().weight()).reduce(0,
                Integer::sum);
        }
    }
}
