package backend.academy.mazes;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Class for cell in maze
 *
 * @param type type of cell.
 * @see Type
 */
public record Cell(Coordinate coordinate, Type type) {

    /**
     * Enum class that define different types of cell
     *
     * @see Cell
     */
    @Getter
    @RequiredArgsConstructor
    public enum Type {
        WALL(-1),
        PLANE(1),
        GOLD(2),
        PASSAGE(3),
        SAND(4),
        WATER(5),
        COALS(6),
        SWAMP(7),
        NAILS(8),
        LAVA(9);

        /**
         * Weight of cell.
         * Different types have different weight.
         */
        private final int weight;

        public static int length() {
            return Type.values().length;
        }
    }
}


