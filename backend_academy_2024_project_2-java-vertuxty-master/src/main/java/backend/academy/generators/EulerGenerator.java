package backend.academy.generators;

import backend.academy.mazes.Coordinate;
import backend.academy.mazes.Maze;
import backend.academy.mazes.MutableMazeDTO;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Class implementation of {@link AbstractGenerator}.
 * Generate maze using Euler's algorithm.
 */
public class EulerGenerator extends AbstractGenerator {

    /**
     * Protected constructor for creating instance of EulerGenerator.
     *
     * @see GeneratorFactory
     */
    protected EulerGenerator(boolean isCycleAllowed) {
        super(isCycleAllowed);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Maze generate(int height, int width) {
        MutableMazeDTO mutableDTO = new MutableMazeDTO(height, width);
        initMutableMazeDTO(mutableDTO);
        DisjointSetUnion disjointSetUnion = new DisjointSetUnion(height * width + 1);

        Set<Integer> usedRepresentative = new HashSet<>();
        List<Integer> bottomRowNextRepresentatives = new ArrayList<>();

        for (int row = 0; row < height - 1; row++) {
            generateRowStructure(mutableDTO, disjointSetUnion, row);
            usedRepresentative.clear();
            generateWaysToBottomRow(
                mutableDTO,
                disjointSetUnion,
                usedRepresentative,
                bottomRowNextRepresentatives,
                row
            );
        }
        int row = height - 1;
        for (int col = 0; col < width - 1; col++) {
            if (disjointSetUnion.getRep(row * width + col) != disjointSetUnion.getRep(row * width + col + 1)) {
                disjointSetUnion.union(row * width + col, disjointSetUnion.getRep(row * width + col + 1));
                addEdges(mutableDTO, new Coordinate(row, col), new Coordinate(row, col + 1));
            }
        }
        return new Maze(mutableDTO);

    }

    private void generateWaysToBottomRow(
        MutableMazeDTO mutableDTO,
        DisjointSetUnion disjointSetUnion,
        Set<Integer> usedRepresentative,
        List<Integer> bottomRowNextRepresentatives,
        int currentRow
    ) {
        int width = mutableDTO.grid()[0].length;
        for (int col = 0; col < width; col++) {
            if (usedRepresentative.contains(disjointSetUnion.getRep(currentRow * width + col))
                && secureRandom.nextBoolean()) {
                continue;
            }
            int countOfWaysToBottomRow = 1;
            bottomRowNextRepresentatives.clear();
            for (int colNear = 0; colNear < width; colNear++) {
                if (disjointSetUnion.getRep(currentRow * width + col)
                    == disjointSetUnion.getRep(currentRow * width + colNear) && colNear != col) {
                    ++countOfWaysToBottomRow;
                    bottomRowNextRepresentatives.add(colNear);
                }
            }
            if (countOfWaysToBottomRow == 1) {
                addEdges(mutableDTO, new Coordinate(currentRow, col), new Coordinate(currentRow + 1, col));
                disjointSetUnion.union(currentRow * width + col, (currentRow + 1) * width + col);
            } else {
                usedRepresentative.add(disjointSetUnion.getRep(currentRow * width + col));
                int currCol =
                    bottomRowNextRepresentatives.remove(secureRandom.nextInt(bottomRowNextRepresentatives.size()));
                if (disjointSetUnion.getRep(currentRow * width + currCol)
                    != disjointSetUnion.getRep((currentRow + 1) * width + currCol)) {
                    disjointSetUnion.union(currentRow * width + currCol,
                        (currentRow + 1) * width + currCol);
                    addEdges(mutableDTO, new Coordinate(currentRow, currCol), new Coordinate(currentRow + 1, currCol));
                }
            }
        }
    }

    private void generateRowStructure(MutableMazeDTO mutableDTO, DisjointSetUnion disjointSetUnion, int row) {
        int width = mutableDTO.grid()[0].length;
        for (int col = 0; col < width - 1; col++) {
            Coordinate cellCord = mutableDTO.getCell(row, col).coordinate();
            Coordinate nearCellCord = mutableDTO.getCell(row, col + 1).coordinate();
            if (disjointSetUnion.getRep(row * width + col + 1)
                != disjointSetUnion.getRep(row * width + col)) {
                if (secureRandom.nextBoolean()) {
                    addEdges(mutableDTO, cellCord, nearCellCord);
                    disjointSetUnion.union(row * width + col, row * width + col + 1);
                }
            } else {
                addAccordingToProbability(mutableDTO, cellCord, nearCellCord);
            }
        }
    }
}
