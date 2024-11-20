package backend.academy.generators;

import backend.academy.mazes.Coordinate;
import backend.academy.mazes.Edge;
import backend.academy.mazes.Maze;
import backend.academy.mazes.MutableMazeDTO;
import java.util.ArrayList;
import java.util.List;

/**
 * Class implementation of {@link AbstractGenerator}.
 * Generate maze using Kruskal's algorithm.
 */
public class KruskalGenerator extends AbstractGenerator {

    /**
     * Variable of edges using for kruskal's algorithm correct work.
     */
    private final List<Edge> edges;

    /**
     * Protected constructor for creating instance of KruskalGenerator.
     *
     * @see GeneratorFactory
     */
    protected KruskalGenerator(final boolean isCycleAllowed) {
        super(isCycleAllowed);
        edges = new ArrayList<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initMutableMazeDTO(MutableMazeDTO mutableDTO) {
        for (int row = 0; row < mutableDTO.height(); row++) {
            for (int col = 0; col < mutableDTO.width(); col++) {
                Coordinate cellCord = new Coordinate(row, col);
                addRandomCellIfNecessary(mutableDTO.grid(), cellCord);
                addNearWalls(mutableDTO.grid(), edges, cellCord);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Maze generate(int height, int width) {

        MutableMazeDTO mutableDTO = new MutableMazeDTO(height, width);

        initMutableMazeDTO(mutableDTO);

        DisjointSetUnion disjointSetUnion = new DisjointSetUnion(height * width); // система непересекающихся множеств

        while (!edges.isEmpty()) {
            int randomEdgeIndex = secureRandom.nextInt(edges.size());
            Edge edge = edges.remove(randomEdgeIndex);
            edges.remove(new Edge(edge.to(), edge.from()));
            Coordinate from = edge.from();
            Coordinate to = edge.to();
            int repFrom = from.col() + from.row() * width;
            int repTo = to.col() + to.row() * width;

            if (disjointSetUnion.getRep(repFrom) != disjointSetUnion.getRep(repTo)) {
                disjointSetUnion.union(repFrom, repTo);
                addEdges(mutableDTO, from, to);
            } else {
                addAccordingToProbability(mutableDTO, from, to);
            }
        }
        return new Maze(mutableDTO);
    }
}
