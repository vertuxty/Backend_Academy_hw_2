package backend.academy.solvers;

import backend.academy.mazes.Cell;
import backend.academy.mazes.Coordinate;
import backend.academy.mazes.Maze;
import backend.academy.solvers.dto.DTOInitialization;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Implementation of {@link AbstractSolver} class.
 * Find the shortest path (not using weights of nodes) by BFS algorithm.
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED) public class BFSSolver extends AbstractSolver {

    /**
     * {@inheritDoc}
     * */
    @Override public List<Coordinate> solve(Maze maze, Coordinate start, Coordinate end) {

        int n = maze.height() * maze.width();
        int width = maze.width();

        List<Integer> dist = new ArrayList<>();
        List<Coordinate> prev = new ArrayList<>();

        DTOInitialization.Builder builder = new DTOInitialization.Builder();
        DTOInitialization dtoInitialization = builder.setDistance(dist)
            .setPrevious(prev)
            .setSize(n)
            .setStartValue(0)
            .setStartPos(start.row() * maze.width() + start.col())
            .build();
        init(dtoInitialization);

        ArrayDeque<Cell> deque = new ArrayDeque<>();

        deque.add(maze.getGrid()[start.row()][start.col()]);

        boolean[] used = new boolean[n];
        used[start.col() + start.row() * width] = true;

        while (!deque.isEmpty()) {
            Cell v = deque.pollFirst();
            Coordinate vCoordinate = v.coordinate();
            if (vCoordinate.row() == end.row() && vCoordinate.col() == end.col()) {
                break;
            }
            int vIndex = getPosCell(v, width);
            for (Cell u : maze.mazeGraph().get(v)) {
                int uIndex = getPosCell(u, width);
                if (!used[uIndex]) { // можно без used, как в случае с Dijkstra, но тогда они почти ничем не отличаются)
                    dist.set(uIndex, dist.get(vIndex) + 1);
                    used[uIndex] = true;
                    prev.set(uIndex, new Coordinate(vCoordinate.row(), vCoordinate.col()));
                    deque.addLast(u);
                }
            }
        }
        return retracePathSolver(prev, start, end, maze.width(), dist.get(getPosCord(end, maze.width())));
    }
}
