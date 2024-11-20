package backend.academy.solvers;

import backend.academy.mazes.Cell;
import backend.academy.mazes.Coordinate;
import backend.academy.mazes.Maze;
import backend.academy.solvers.dto.DTOInitialization;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Implementation of {@link AbstractSolver}.
 * Find shortest-weight path in a maze by using A-star algorithm.
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AStarSolver extends AbstractSolver {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Coordinate> solve(Maze maze, Coordinate start, Coordinate end) {
        int n = maze.height() * maze.width();

        List<Integer> dist = new ArrayList<>();
        List<Coordinate> prev = new ArrayList<>();

        DTOInitialization.Builder builder = new DTOInitialization.Builder();
        DTOInitialization dtoInitialization = builder.setDistance(dist)
            .setPrevious(prev)
            .setSize(n)
            .setStartValue(maze.getGrid()[start.row()][start.col()].type().weight())
            .setStartPos(start.row() * maze.width() + start.col())
            .build();
        init(dtoInitialization);

        PriorityQueue<Cell> priorityQueue = initPQ(
            maze.getGrid()[start.row()][start.col()],
            (a, b) -> pathLengthToTheGoal(dist, a.coordinate(), end, maze.width())
                - pathLengthToTheGoal(dist, b.coordinate(), end, maze.width())
        );

        while (!priorityQueue.isEmpty()) {
            Cell v = priorityQueue.poll();
            Coordinate vCoordinate = v.coordinate();
            if (vCoordinate.row() == end.row() && vCoordinate.col() == end.col()) {
                break;
            }
            int vIndex = getPosCell(v, maze.width());
            for (Cell u : maze.mazeGraph().get(v)) {
                int uIndex = getPosCell(u, maze.width());
                int w = u.type().weight();
                int score = dist.get(vIndex) + w;
                if (score < dist.get(uIndex)) {
                    prev.set(uIndex, new Coordinate(vCoordinate.row(), vCoordinate.col()));
                    dist.set(uIndex, score);
                    if (!priorityQueue.contains(u)) {
                        priorityQueue.add(u);
                    }
                }
            }
        }
        return retracePathSolver(prev, start, end, maze.width(), dist.get(getPosCord(end, maze.width())));
    }

    private int heuristics(Coordinate from, Coordinate to) {
        return Math.abs(from.col() - to.col()) + Math.abs(from.row() - to.row());
    }

    private int pathLengthToTheGoal(
        List<Integer> dist, Coordinate start, Coordinate end, int width
    ) {
        return dist.get(start.col() + start.row() * width) + heuristics(start, end);
    }
}
