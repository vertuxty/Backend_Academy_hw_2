package backend.academy.solvers;

import backend.academy.mazes.Cell;
import backend.academy.mazes.Coordinate;
import backend.academy.mazes.Maze;
import backend.academy.solvers.dto.DTOInitialization;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Abstract implementation of {@link Solver} interface.
 * This class includes base functionality for pathfinding algorithms.
 * */
public abstract class AbstractSolver implements Solver {

    protected static final int INF = Integer.MAX_VALUE;

    /**
     * Final method for path retrace.
     * Retrace path in graphю
     *
     * @param prev a list of previous nodes in path.
     * @param start starter point in path.
     * @param end final point in path.
     * @param width a {@link Maze}'s width
     * @param distEnd a distance from from to to.
     *
     * @return a retraced path.
     * */
    protected final List<Coordinate> retracePathSolver(
        List<Coordinate> prev,
        Coordinate start,
        Coordinate end,
        int width,
        int distEnd
    ) {
        if (distEnd == INF) {
            return List.of();
        }
        Coordinate target = end;
        List<Coordinate> path = new ArrayList<>(0);
        while (!target.equals(start)) {
            path.add(target);
            target = prev.get(target.col() + target.row() * width);
        }
        path.add(start);
        Collections.reverse(path);
        return path;
    }

    /**
     * Base method for initialization distances
     * and previous nodes in algorithm.
     * Initializing list of distances by {@link #INF} value
     * and place {@code startValue} at a {@code startPos}
     * Initializing list of previous nodes by {@code null}
     *
     * @param dtoInitialization DTO with information about init data for solvers.
     * */
    protected void init(
        DTOInitialization dtoInitialization
    ) {
        dtoInitialization.dist().add(INF);
        for (int i = 0; i < dtoInitialization.n(); i++) {
            dtoInitialization.dist().add(INF);
            dtoInitialization.prev().add(null);
        }
        dtoInitialization.dist().set(dtoInitialization.startPos(), dtoInitialization.startValue());
    }

    /**
     * Get number of cell.
     *
     * @param v {@link Cell} in a {@link Maze}
     * @param width width in a maze
     *
     * @return number of cell in maze
     * */
    protected int getPosCell(Cell v, int width) {
        Coordinate coordinate = v.coordinate();
        return coordinate.col() + coordinate.row() * width;
    }

    /**
     * Get number of cell by coordinate.
     * @param v {@link Coordinate} of a {@link Cell} in a maze
     * @param width width in a {@link Maze}
     *
     * @return number of cell in maze
     * */
    protected int getPosCord(Coordinate v, int width) {
        return v.col() + v.row() * width;
    }

    /**
     * Initialization method for {@link PriorityQueue}
     * with custom {@link Comparator} and from value
     *
     * @param startValue from value in a priority queue
     * @param comparator comparator for priority queue.
     * @return a priority queue.
     * */
    protected <T> PriorityQueue<T> initPQ(T startValue, Comparator<T> comparator) {
        PriorityQueue<T> priorityQueue = new PriorityQueue<>(comparator);
        priorityQueue.add(startValue);
        return priorityQueue;
    }
}
