package backend.academy;

import backend.academy.generators.Generator;
import backend.academy.generators.GeneratorFactory;
import backend.academy.mazes.Coordinate;
import backend.academy.mazes.Maze;
import backend.academy.players.AbstractPlayer;
import backend.academy.players.HumanPlayer;
import backend.academy.renders.RenderImpl;
import backend.academy.solvers.Solver;
import backend.academy.solvers.SolverFactory;
import backend.academy.utils.Messages;
import java.io.PrintStream;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

/**
 * Realization of program, that generate mazes and solve them, by user input.
 * <p>
 * NOTE: if the input turned out to be incorrect, the user is asked to enter again only this incorrect input.
 * <p>
 * 1. First, the user chooses which generator he gets the maze with.
 * <p>
 * 2. The user is given a choice about setting up the generation algorithms: whether there are cycles in the maze.
 * <p>
 * 3. The user is given a choice of the size of the maze: first the height, then the width
 * <p>
 * 4. Next comes the selection of the starting and ending points in the maze, between which we are looking for a path
 * <p>
 * 5. The user is given a list of solvers that he can use to find a path.
 * <p>
 * 6. The maze is being drawn with the found path.
 */
public class Game {

    /**
     * Wrapper over System.out.
     */
    @Setter
    @Getter
    private PrintStream printer;

    /**
     * Player of this game (user).
     */
    private final AbstractPlayer player;

    /**
     * Create instance of our game.
     */
    public Game() {
        this.player = new HumanPlayer();
        this.printer = System.out;
    }

    /**
     * The method for starting the game.
     * Launches the game, requesting user
     * input and displaying the response to the console.
     */
    @SneakyThrows
    public void run() {
        GeneratorFactory.GeneratorType genArg =
            GameLoops.argumentChoose(player, printer, GeneratorFactory.GeneratorType.class, Messages.GENERATOR_MSG);
        boolean isCyclesAllowed = GameLoops.cycleChoose(player, printer);
        int height = GameLoops.heightChoose(player, printer);
        int width = GameLoops.widthChoose(player, printer);
        Coordinate start = GameLoops.coordinateChoose(player, printer, Messages.START_POINT_MESSAGE, height, width);
        Coordinate end = GameLoops.coordinateChoose(player, printer, Messages.END_POINT_MESSAGE, height, width);
        SolverFactory.SolverEnum solArg =
            GameLoops.argumentChoose(player, printer, SolverFactory.SolverEnum.class, Messages.SOLVERS_MSG);
        Generator generator = GeneratorFactory.createGenerator(genArg, isCyclesAllowed);
        Maze maze = generator.generate(height, width);
        Solver solver = SolverFactory.createSolver(solArg);
        List<Coordinate> path = solver.solve(maze, start, end);
        RenderImpl render = new RenderImpl();
        printer.println(render.render(maze, path));
        player.close();
        printer.println(generator);
        printer.println(solver);
    }
}
