package backend.academy;

import backend.academy.mazes.Coordinate;
import backend.academy.players.AbstractPlayer;
import backend.academy.utils.Config;
import backend.academy.utils.Messages;
import java.io.PrintStream;
import lombok.experimental.UtilityClass;

@UtilityClass
public class GameLoops {

    public boolean cycleChoose(AbstractPlayer player, PrintStream printer) {
        while (true) {
            printer.println(Messages.CYCLE_MSG);
            printer.println(Messages.apply(Messages.BRACKET_TEMPLATE, 1, "Yes"));
            printer.println(Messages.apply(Messages.BRACKET_TEMPLATE, 2, "No"));
            try {
                return player.readCycleInfo();
            } catch (IllegalStateException e) {
                printer.println(e.getMessage());
            }
        }
    }

    public <T> T argumentChoose(AbstractPlayer player, PrintStream printer, Class<T> clazz, String message) {
        T genArg;
        while (true) {
            printer.println(message);
            printer.println(Messages.availableEnumConst(clazz));
            try {
                genArg = player.getTypeFromInput(clazz);
                break;
            } catch (IllegalStateException e) {
                printer.println(e.getMessage());
            }
        }
        printer.println(Messages.apply(Messages.CHOOSE_TEMPLATE, genArg.toString()));
        return genArg;
    }

    public int heightChoose(AbstractPlayer player, PrintStream printer) {
        int height;
        while (true) {
            printer.println(Messages.HEIGHT_MSG);
            try {
                height = player.getMazeSize(Config.MIN_HEIGHT, Config.MAX_HEIGHT);
                printer.println(Messages.apply(Messages.CHOOSE_TEMPLATE, height));
                break;
            } catch (IllegalStateException e) {
                printer.println(e.getMessage());
            }
        }
        return height;
    }

    public int widthChoose(AbstractPlayer player, PrintStream printer) {
        int width;
        while (true) {
            printer.println(Messages.WIDTH_MSG);
            try {
                width = player.getMazeSize(Config.MIN_WIDTH, Config.MAX_WIDTH);
                printer.println(Messages.apply(Messages.CHOOSE_TEMPLATE, width));
                break;
            } catch (IllegalStateException e) {
                printer.println(e.getMessage());
            }
        }
        return width;
    }

    public Coordinate coordinateChoose(
        AbstractPlayer player,
        PrintStream printer,
        String message,
        int height,
        int width
    ) {
        Coordinate start;
        while (true) {
            printer.println(message);
            try {
                start = player.readPointCoordinate(height, width);
                break;
            } catch (IllegalStateException e) {
                printer.println(e.getMessage());
            }
        }
        return start;
    }
}
