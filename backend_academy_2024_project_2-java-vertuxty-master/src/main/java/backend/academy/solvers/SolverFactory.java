package backend.academy.solvers;

import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;

/**
 * Factory class for creating instances
 * of path find solver algorithms.
 * */
@UtilityClass
public class SolverFactory {

    /**
     * Enum that representing
     * different types of solvers
     * that can be created by {@link SolverFactory}
     * */
    @RequiredArgsConstructor
    public enum SolverEnum {
        /**
         * BFS algorithm solver.
         * @see BFSSolver
         * */
        BFS(BFSSolver::new),
        /**
         * A-star algorithm solver.
         * Using weight of cell.
         * @see AStarSolver
         * */
        ASTAR(AStarSolver::new);

        private final Supplier<Solver> constructor;

    }

    /**
     * Method for creating solver of specified type.
     *
     * @param solverEnum a identifier of solver type.
     * @return instance of a {@link Solver}.
     *
     * @see SolverEnum
     * */
    public Solver createSolver(SolverEnum solverEnum) {
        Supplier<Solver> supplier = solverEnum.constructor;
        if (supplier != null) {
            return supplier.get();
        } else {
            throw new IllegalArgumentException("No solver with name: " + solverEnum);
        }
    }
}
