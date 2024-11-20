package backend.academy.renders;

import backend.academy.mazes.Cell;
import backend.academy.mazes.Coordinate;
import backend.academy.mazes.Maze;
import java.util.List;
import java.util.Map;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

/**
 * Implementation of interface {@link Renderer}.
 */
public class RenderImpl implements Renderer {

    /**
     * {@inheritDoc}
     */
    @Override
    public String render(Maze maze) {
        return render(maze, List.of());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String render(Maze maze, List<Coordinate> path) {

        Map<Cell, List<Cell>> mazeGraph = maze.mazeGraph();
        Cell[][] grid = maze.getGrid();
        AttributedString[][] attributedGrid = new AttributedString[maze.height()][maze.width()];

        AttributedStringBuilder sb = new AttributedStringBuilder();
        initCeilBorderWall(sb, maze.width() * 2 + 1);

        for (int row = 0; row < maze.height(); row++) {

            AttributedStringBuilder currRow = createNewMazeRow();
            AttributedStringBuilder downRow = createNewMazeRow();

            for (int col = 0; col < maze.width(); col++) {
                Cell cell = grid[row][col];

                defineCellAttributeStyle(
                    path,
                    attributedGrid,
                    cell
                );

                currRow.append(attributedGrid[row][col]);

                if (row < maze.height() - 1) {

                    renderPartOfRow(
                        mazeGraph,
                        attributedGrid,
                        downRow,
                        path,
                        cell,
                        grid[row + 1][col]
                    );

                } else {
                    downRow.append(" ", MazeStyles.WALL_STYLE);
                }

                if (col < maze.width() - 1) {

                    renderPartOfRow(
                        mazeGraph,
                        attributedGrid,
                        currRow,
                        path,
                        cell,
                        grid[row][col + 1]
                    );

                } else {
                    currRow.append(" ", MazeStyles.WALL_STYLE);
                }

                downRow.append(" ", MazeStyles.WALL_STYLE);
            }

            sb.append(currRow)
                .append(System.lineSeparator())
                .append(downRow)
                .append(System.lineSeparator());
        }

        return sb.toAnsi();
    }

    private static void renderPartOfRow(
        Map<Cell, List<Cell>> mazeGraph,
        AttributedString[][] attributedGrid,
        AttributedStringBuilder rowAttributed,
        List<Coordinate> path,
        Cell cell,
        Cell nearCell
    ) {
        Coordinate coordinate = cell.coordinate();
        Coordinate nearCellCoordinate = nearCell.coordinate();
        AttributedStyle cellAttributeStyle = attributedGrid[coordinate.row()][coordinate.col()].styleAt(0);
        if (mazeGraph.get(cell).contains(nearCell)) {
            defineCellAttributeStyle(
                path,
                attributedGrid,
                nearCell
            );
            AttributedStyle nearCellAttributedStyle =
                attributedGrid[nearCellCoordinate.row()][nearCellCoordinate.col()].styleAt(0);
            if (nearCellAttributedStyle.equals(cellAttributeStyle)) {
                rowAttributed.append(" ", cellAttributeStyle);
            } else {
                defineStyleBetweenCellsIfStylesNotEquals(rowAttributed, cellAttributeStyle,
                    nearCellAttributedStyle);
            }
        } else {
            rowAttributed.append(" ", MazeStyles.WALL_STYLE);
        }
    }

    private static void defineStyleBetweenCellsIfStylesNotEquals(
        AttributedStringBuilder rowAttributed,
        AttributedStyle style,
        AttributedStyle nearCellAttributedStyle
    ) {
        if (isEndpointAndPathStyle(style, nearCellAttributedStyle)
            || isEndpointAndPathStyle(nearCellAttributedStyle, style)
        ) {
            rowAttributed.append(" ", MazeStyles.FOUND_PATH_STYLE);
        } else {
            rowAttributed.append(" ", MazeStyles.ALLOWED_PATH_STYLE);
        }
    }

    private static boolean isEndpointAndPathStyle(
        AttributedStyle style,
        AttributedStyle nearCellAttributedStyle
    ) {
        return style.equals(MazeStyles.END_POINT_STYLE)
            && nearCellAttributedStyle.equals(MazeStyles.FOUND_PATH_STYLE);
    }

    private static void defineCellAttributeStyle(
        List<Coordinate> path,
        AttributedString[][] attributedGrid,
        Cell cell
    ) {
        boolean locatedInPath = path.contains(cell.coordinate());
        int row = cell.coordinate().row();
        int col = cell.coordinate().col();
        if (attributedGrid[row][col] != null) {
            return;
        }
        if (!path.isEmpty()) {
            Coordinate start = path.getFirst();
            Coordinate end = path.getLast();
            if (row == start.row() && col == start.col()
                || row == end.row() && col == end.col()) {
                attributedGrid[row][col] = newAttributedString(cell, MazeStyles.END_POINT_STYLE);
            } else if (locatedInPath) {
                attributedGrid[row][col] = newAttributedString(cell, MazeStyles.FOUND_PATH_STYLE);
            } else {
                attributedGrid[row][col] = newAttributedString(cell, MazeStyles.ALLOWED_PATH_STYLE);
            }
        } else {
            attributedGrid[row][col] = newAttributedString(cell, MazeStyles.ALLOWED_PATH_STYLE);
        }
    }

    private static AttributedString newAttributedString(Cell cell, AttributedStyle style) {
        return new AttributedString(String.valueOf(cell.type().weight()), style);
    }

    private static AttributedStringBuilder createNewMazeRow() {
        AttributedStringBuilder attributedStringBuilder = new AttributedStringBuilder();
        attributedStringBuilder.append(" ", MazeStyles.WALL_STYLE);
        return attributedStringBuilder;
    }

    private static void initCeilBorderWall(AttributedStringBuilder attributedStringBuilder, int width) {
        int cnt = 0;
        while (cnt < width) {
            attributedStringBuilder.append(" ", MazeStyles.WALL_STYLE);
            cnt++;
        }
        attributedStringBuilder.append(System.lineSeparator());
    }
}
