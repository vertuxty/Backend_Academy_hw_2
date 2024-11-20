package backend.academy.renders;

import lombok.experimental.UtilityClass;
import org.jline.utils.AttributedStyle;

/**
 * Util class for colors that used in console output (render {@link backend.academy.mazes.Maze}).
 * */
@UtilityClass
public class MazeStyles {
    public static final AttributedStyle WALL_STYLE = AttributedStyle.DEFAULT.background(AttributedStyle.RED);
    public static final AttributedStyle ALLOWED_PATH_STYLE = AttributedStyle.DEFAULT.background(AttributedStyle.YELLOW);
    public static final AttributedStyle FOUND_PATH_STYLE = AttributedStyle.DEFAULT.background(AttributedStyle.MAGENTA);
    public static final AttributedStyle END_POINT_STYLE = AttributedStyle.DEFAULT.background(AttributedStyle.CYAN);
}
