package backend.academy.generators;

import backend.academy.mazes.Cell;
import backend.academy.mazes.Coordinate;
import backend.academy.mazes.Edge;
import backend.academy.mazes.Maze;
import backend.academy.mazes.MutableMazeDTO;
import java.util.ArrayList;
import java.util.List;

/**
 * Class implementation of {@link AbstractGenerator}.
 * Generate maze using Prime's algorithm base on walls deleting.
 */
public class PrimeGenerator extends AbstractGenerator {

    /**
     * Protected constructor for creating instance of PrimeGeneratorByWalls.
     *
     * @see GeneratorFactory
     */
    protected PrimeGenerator(boolean singleSolution) {
        super(singleSolution);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Maze generate(int height, int width) {

        MutableMazeDTO mutableDTO = new MutableMazeDTO(height, width);
        initMutableMazeDTO(mutableDTO);
        DisjointSetUnion disjointSetUnion = new DisjointSetUnion(height * width);

        List<Edge> walls = new ArrayList<>();
        Cell cell = mutableDTO.getCell(new Coordinate(secureRandom.nextInt(height), secureRandom.nextInt(width)));
        addNearWalls(mutableDTO.grid(), walls, cell.coordinate());

        while (!walls.isEmpty()) {
            int ind = secureRandom.nextInt(walls.size());
            Edge edge = walls.remove(ind);
            Coordinate from = edge.from();
            Coordinate to = edge.to();
            int repFrom = from.row() * width + from.col();
            int repTo = to.row() * width + to.col();
            if (disjointSetUnion.getRep(repFrom) != disjointSetUnion.getRep(repTo)) {
                disjointSetUnion.union(repFrom, repTo);
                addEdges(mutableDTO, from, to);
                addNearWalls(mutableDTO.grid(), walls, to);
            } else {
                addAccordingToProbability(mutableDTO, from, to);
            }
        }
        return new Maze(mutableDTO);
    }
}
