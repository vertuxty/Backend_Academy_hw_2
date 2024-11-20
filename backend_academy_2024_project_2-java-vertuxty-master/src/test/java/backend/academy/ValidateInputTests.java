package backend.academy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.security.SecureRandom;
import backend.academy.generators.GeneratorFactory;
import backend.academy.solvers.SolverFactory;
import backend.academy.utils.Config;
import backend.academy.utils.Messages;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ValidateInputTests {

    @Nested
    class InputTest {
        ByteArrayOutputStream byteArrayOutputStream;

        @BeforeEach
        void init() {
        }

        @ParameterizedTest
        @ValueSource(strings = {"-21324", "324234", "gdhfsfs", "ASDAD", "983274uiksdad", "0"})
        void chooseGeneratorTestOutOfRange(String generatorInput) {
            byteArrayOutputStream = new ByteArrayOutputStream();
            PrintStream printStream = new PrintStream(byteArrayOutputStream);
            System.setOut(printStream);
            String errorMsg;
            try {
                Integer.parseInt(generatorInput);
                errorMsg =
                    Messages.apply(Messages.ERROR_OUT_OF_RANGE_TEMPLATE, 1, GeneratorFactory.GeneratorType.values().length);
            } catch (NumberFormatException e) {
                errorMsg = Messages.ERROR_WRONG_NUMBER_FORMAT;
            }
            System.setIn(new ByteArrayInputStream(generatorInput.getBytes()));
            Game game = new Game();
            game.printer(System.out);
            StringBuilder expectedOutput = new StringBuilder();
            expectedOutput
                .append(Messages.GENERATOR_MSG)
                .append("\n")
                .append(Messages.availableEnumConst(GeneratorFactory.GeneratorType.class))
                .append("\n")
                .append(errorMsg)
                .append("\n")
                .append(Messages.GENERATOR_MSG)
                .append("\n")
                .append(Messages.availableEnumConst(GeneratorFactory.GeneratorType.class));

            assertThatThrownBy(game::run).isInstanceOf(NullPointerException.class);
            String output = byteArrayOutputStream.toString().replace("\r\n", "\n").replace("\r", "\n").trim();
            assertThat(output).isEqualTo(expectedOutput.toString().replace("\r\n", "\n").replace("\r", "\n").trim());
        }

        @Test
        void multiplyChooseGeneratorTest() {
            for (int i = 0; i < 100; i++) {
                chooseGeneratorTestCorrectChoice(i);
            }
        }

        @ParameterizedTest
        @ValueSource(ints = {1, 2, 3, 4})
        void chooseGeneratorTestCorrectChoice(int seed) {
            byteArrayOutputStream = new ByteArrayOutputStream();
            PrintStream printStream = new PrintStream(byteArrayOutputStream);
            System.setOut(printStream);
            SecureRandom secureRandom = new SecureRandom(String.valueOf(seed).getBytes());
            String input = String.valueOf(secureRandom.nextInt(1, GeneratorFactory.GeneratorType.values().length + 1)); // random choose
            System.setIn(new ByteArrayInputStream(input.getBytes()));
            Game game = new Game();
            game.printer(System.out);
            StringBuilder expectedOutput = new StringBuilder();
            expectedOutput
                .append(Messages.GENERATOR_MSG)
                .append("\n")
                .append(Messages.availableEnumConst(GeneratorFactory.GeneratorType.class))
                .append("\n")

                .append(Messages.apply(Messages.CHOOSE_TEMPLATE,
                    GeneratorFactory.GeneratorType.values()[Integer.parseInt(input) - 1]))
                .append("\n")
                .append(Messages.CYCLE_MSG)
                .append("\n")
                .append(Messages.apply(Messages.BRACKET_TEMPLATE, 1, "Yes"))
                .append("\n")
                .append(Messages.apply(Messages.BRACKET_TEMPLATE, 2, "No"));

            assertThatThrownBy(game::run).isInstanceOf(NullPointerException.class);
            String output = byteArrayOutputStream.toString().replace("\r\n", "\n").replace("\r", "\n").trim();
            assertThat(output).isEqualTo(expectedOutput.toString().replace("\r\n", "\n").replace("\r", "\n").trim());
        }

        @ParameterizedTest
        @ValueSource(strings = {"-1", "dsfsdfsf", "-1233", "234234", "27384hfs8oidfs"})
        void isCycleAllowedTestWrong(String cycleInput) {
            byteArrayOutputStream = new ByteArrayOutputStream();
            PrintStream printStream = new PrintStream(byteArrayOutputStream);
            System.setOut(printStream);
            SecureRandom secureRandom = new SecureRandom();
            String inputGenerate =
                String.valueOf(secureRandom.nextInt(1, GeneratorFactory.GeneratorType.values().length + 1)); // random choose
            String errorMsg;
            try {
                Integer.parseInt(cycleInput);
                errorMsg = Messages.apply(Messages.ERROR_OUT_OF_RANGE_TEMPLATE, 1, 2);
            } catch (NumberFormatException e) {
                errorMsg = Messages.ERROR_WRONG_NUMBER_FORMAT;
            }
            String input = inputGenerate + "\n" + cycleInput;
            System.setIn(new ByteArrayInputStream(input.getBytes()));
            Game game = new Game();
            game.printer(System.out);
            StringBuilder expectedOutput = new StringBuilder();
            expectedOutput
                .append(Messages.GENERATOR_MSG)
                .append("\n")
                .append(Messages.availableEnumConst(GeneratorFactory.GeneratorType.class))
                .append("\n")

                .append(Messages.apply(Messages.CHOOSE_TEMPLATE,
                    GeneratorFactory.GeneratorType.values()[Integer.parseInt(inputGenerate) - 1]))
                .append("\n")
                .append(Messages.CYCLE_MSG)
                .append("\n")
                .append(Messages.apply(Messages.BRACKET_TEMPLATE, 1, "Yes")).append("\n")
                .append(Messages.apply(Messages.BRACKET_TEMPLATE, 2, "No")).append("\n")
                .append(errorMsg)
                .append("\n")
                .append(Messages.CYCLE_MSG)
                .append("\n")
                .append(Messages.apply(Messages.BRACKET_TEMPLATE, 1, "Yes")).append("\n")
                .append(Messages.apply(Messages.BRACKET_TEMPLATE, 2, "No")).append("\n");
            assertThatThrownBy(game::run).isInstanceOf(NullPointerException.class);
            String output = byteArrayOutputStream.toString().replace("\r\n", "\n").replace("\r", "\n").trim();
            assertThat(output).isEqualTo(expectedOutput.toString().replace("\r\n", "\n").replace("\r", "\n").trim());
        }

        @ParameterizedTest
        @ValueSource(ints = {1, 2})
        void isCycleAllowedTestCorrect(int cycleInput) {
            byteArrayOutputStream = new ByteArrayOutputStream();
            PrintStream printStream = new PrintStream(byteArrayOutputStream);
            System.setOut(printStream);
            SecureRandom secureRandom = new SecureRandom();
            String inputGenerate =
                String.valueOf(secureRandom.nextInt(1, GeneratorFactory.GeneratorType.values().length + 1)); // random choose
            String input = inputGenerate + "\n" + cycleInput;
            System.setIn(new ByteArrayInputStream(input.getBytes()));
            Game game = new Game();
            game.printer(System.out);
            StringBuilder expectedOutput = new StringBuilder();
            expectedOutput
                .append(Messages.GENERATOR_MSG)
                .append("\n")
                .append(Messages.availableEnumConst(GeneratorFactory.GeneratorType.class))
                .append("\n")

                .append(Messages.apply(Messages.CHOOSE_TEMPLATE,
                    GeneratorFactory.GeneratorType.values()[Integer.parseInt(inputGenerate) - 1]))
                .append("\n")
                .append(Messages.CYCLE_MSG)
                .append("\n")
                .append(Messages.apply(Messages.BRACKET_TEMPLATE, 1, "Yes"))
                .append("\n")
                .append(Messages.apply(Messages.BRACKET_TEMPLATE, 2, "No"))
                .append("\n")//                .append(MessagesUtil.apply(MessagesUtil.CHOOSE_TEMPLATE, ))
                .append(Messages.HEIGHT_MSG);

            assertThatThrownBy(game::run).isInstanceOf(NullPointerException.class);
            String output = byteArrayOutputStream.toString().replace("\r\n", "\n").replace("\r", "\n").trim();
            assertThat(output).isEqualTo(expectedOutput.toString().replace("\r\n", "\n").replace("\r", "\n").trim());
        }

        @ParameterizedTest
        @ValueSource(strings = {"-8765323", "-1", "0", "1", "23", "17", "0324", "2323", "10", "9", "36", "FGHJkdsfs",
            "-923482dsfdsfs", "I(ujsnmda"})
        void heightTest(String input) {
            try {
                int s = Integer.parseInt(input);
                if (s > Config.MAX_HEIGHT || s < Config.MIN_HEIGHT) {
                    heightTestWrong(input,
                        Messages.apply(Messages.ERROR_OUT_OF_RANGE_TEMPLATE, Config.MIN_HEIGHT,
                            Config.MAX_HEIGHT));
                } else {
                    heightTestCorrect(input);
                }
            } catch (NumberFormatException e) {
                heightTestWrong(input, Messages.ERROR_WRONG_NUMBER_FORMAT);
            }
        }

        private void heightTestCorrect(String inputHeight) {
            byteArrayOutputStream = new ByteArrayOutputStream();
            PrintStream printStream = new PrintStream(byteArrayOutputStream);
            System.setOut(printStream);

            SecureRandom secureRandom = new SecureRandom();
            String inputGenerate =
                String.valueOf(secureRandom.nextInt(1, GeneratorFactory.GeneratorType.values().length + 1)); // random choose
            String inputCycle = String.valueOf(secureRandom.nextInt(1, 3));
            String input = inputGenerate + "\n" + inputCycle + "\n" + inputHeight;
            System.setIn(new ByteArrayInputStream(input.getBytes()));

            Game game = new Game();
            game.printer(System.out);

            StringBuilder expectedOutput = new StringBuilder();
            expectedOutput
                .append(Messages.GENERATOR_MSG)
                .append("\n")
                .append(Messages.availableEnumConst(GeneratorFactory.GeneratorType.class))
                .append("\n")

                .append(Messages.apply(Messages.CHOOSE_TEMPLATE,
                    GeneratorFactory.GeneratorType.values()[Integer.parseInt(inputGenerate) - 1]))
                .append("\n")
                .append(Messages.CYCLE_MSG)
                .append("\n")
                .append(Messages.apply(Messages.BRACKET_TEMPLATE, 1, "Yes"))
                .append("\n")
                .append(Messages.apply(Messages.BRACKET_TEMPLATE, 2, "No"))
                .append("\n")//                .append(MessagesUtil.apply(MessagesUtil.CHOOSE_TEMPLATE, ))
                .append(Messages.HEIGHT_MSG).append("\n")
                .append(Messages.apply(Messages.CHOOSE_TEMPLATE, inputHeight))
                .append("\n").append(Messages.WIDTH_MSG);

            assertThatThrownBy(game::run).isInstanceOf(NullPointerException.class);
            String output = byteArrayOutputStream.toString().replace("\r\n", "\n").replace("\r", "\n").trim();
            assertThat(output).isEqualTo(expectedOutput.toString().replace("\r\n", "\n").replace("\r", "\n").trim());
        }

        private void heightTestWrong(String inputHeight, String errorMsg) {
            byteArrayOutputStream = new ByteArrayOutputStream();
            PrintStream printStream = new PrintStream(byteArrayOutputStream);
            System.setOut(printStream);

            SecureRandom secureRandom = new SecureRandom();
            String inputGenerate =
                String.valueOf(secureRandom.nextInt(1, GeneratorFactory.GeneratorType.values().length + 1)); // random choose
            String inputCycle = String.valueOf(secureRandom.nextInt(1, 3));
            String input = inputGenerate + "\n" + inputCycle + "\n" + inputHeight;
            System.setIn(new ByteArrayInputStream(input.getBytes()));

            Game game = new Game();
            game.printer(System.out);

            StringBuilder expectedOutput = new StringBuilder();
            expectedOutput
                .append(Messages.GENERATOR_MSG)
                .append("\n")
                .append(Messages.availableEnumConst(GeneratorFactory.GeneratorType.class))                .append("\n")

                .append(Messages.apply(Messages.CHOOSE_TEMPLATE,
                    GeneratorFactory.GeneratorType.values()[Integer.parseInt(inputGenerate) - 1]))
                .append("\n")
                .append(Messages.CYCLE_MSG)
                .append("\n")
                .append(Messages.apply(Messages.BRACKET_TEMPLATE, 1, "Yes"))
                .append("\n")
                .append(Messages.apply(Messages.BRACKET_TEMPLATE, 2, "No"))
                .append("\n")//                .append(MessagesUtil.apply(MessagesUtil.CHOOSE_TEMPLATE, ))
                .append(Messages.HEIGHT_MSG).append("\n")
                .append(errorMsg)
                .append("\n").append(Messages.HEIGHT_MSG);

            assertThatThrownBy(game::run).isInstanceOf(NullPointerException.class);
            String output = byteArrayOutputStream.toString().replace("\r\n", "\n").replace("\r", "\n").trim();
            assertThat(output).isEqualTo(expectedOutput.toString().replace("\r\n", "\n").replace("\r", "\n").trim());
        }

        @ParameterizedTest
        @ValueSource(strings = {"-0924232", "-0043524234", "sfs72sdaa a", "0", "1,", "1", "2", "30", "790=3", "5324",
            "000123", "87832309283", "17", "27"})
        void widthTest(String val) {
            try {
                int v = Integer.parseInt(val);
                if (v < Config.MIN_WIDTH || v > Config.MAX_WIDTH) {
                    widthTestWrong(val, Messages.apply(Messages.ERROR_OUT_OF_RANGE_TEMPLATE, Config.MIN_WIDTH,
                        Config.MAX_WIDTH));
                } else {
                    widthTestCorrect(val);
                }
            } catch (NumberFormatException e) {
                widthTestWrong(val, Messages.ERROR_WRONG_NUMBER_FORMAT);
            }
        }

        private void widthTestWrong(String inputWidth, String errorMsg) {
            byteArrayOutputStream = new ByteArrayOutputStream();
            PrintStream printStream = new PrintStream(byteArrayOutputStream);
            System.setOut(printStream);

            SecureRandom secureRandom = new SecureRandom();
            String inputGenerate =
                String.valueOf(secureRandom.nextInt(1, GeneratorFactory.GeneratorType.values().length + 1)); // random choose
            String inputCycle = String.valueOf(secureRandom.nextInt(1, 3));
            String inputHeight = String.valueOf(secureRandom.nextInt(Config.MIN_HEIGHT, Config.MAX_HEIGHT + 1));
            String input = inputGenerate + "\n" + inputCycle + "\n" + inputHeight + "\n" + inputWidth;
            System.setIn(new ByteArrayInputStream(input.getBytes()));

            Game game = new Game();
            game.printer(System.out);

            StringBuilder expectedOutput = new StringBuilder();
            expectedOutput
                .append(Messages.GENERATOR_MSG)
                .append("\n")
                .append(Messages.availableEnumConst(GeneratorFactory.GeneratorType.class))
                .append("\n")

                .append(Messages.apply(Messages.CHOOSE_TEMPLATE,
                    GeneratorFactory.GeneratorType.values()[Integer.parseInt(inputGenerate) - 1]))
                .append("\n")
                .append(Messages.CYCLE_MSG)
                .append("\n")
                .append(Messages.apply(Messages.BRACKET_TEMPLATE, 1, "Yes")).append("\n")
                .append(Messages.apply(Messages.BRACKET_TEMPLATE, 2, "No"))
                .append("\n")//                .append(MessagesUtil.apply(MessagesUtil.CHOOSE_TEMPLATE, ))
                .append(Messages.HEIGHT_MSG).append("\n")
                .append(Messages.apply(Messages.CHOOSE_TEMPLATE, inputHeight)).append("\n")
                .append(Messages.WIDTH_MSG).append("\n")
                .append(errorMsg).append("\n")
                .append(Messages.WIDTH_MSG);

            assertThatThrownBy(game::run).isInstanceOf(NullPointerException.class);
            String output = byteArrayOutputStream.toString().replace("\r\n", "\n").replace("\r", "\n").trim();
            assertThat(output).isEqualTo(expectedOutput.toString().replace("\r\n", "\n").replace("\r", "\n").trim());
        }

        private void widthTestCorrect(String inputWidth) {
            byteArrayOutputStream = new ByteArrayOutputStream();
            PrintStream printStream = new PrintStream(byteArrayOutputStream);
            System.setOut(printStream);

            SecureRandom secureRandom = new SecureRandom();
            String inputGenerate =
                String.valueOf(secureRandom.nextInt(1, GeneratorFactory.GeneratorType.values().length + 1)); // random choose
            String inputCycle = String.valueOf(secureRandom.nextInt(1, 3));
            String inputHeight = String.valueOf(secureRandom.nextInt(Config.MIN_HEIGHT, Config.MAX_HEIGHT + 1));
            String input = inputGenerate + "\n" + inputCycle + "\n" + inputHeight + "\n" + inputWidth;
            System.setIn(new ByteArrayInputStream(input.getBytes()));

            Game game = new Game();
            game.printer(System.out);

            StringBuilder expectedOutput = new StringBuilder();
            expectedOutput
                .append(Messages.GENERATOR_MSG)
                .append("\n")
                .append(Messages.availableEnumConst(GeneratorFactory.GeneratorType.class))
                .append("\n")

                .append(Messages.apply(Messages.CHOOSE_TEMPLATE,
                    GeneratorFactory.GeneratorType.values()[Integer.parseInt(inputGenerate) - 1]))
                .append("\n")
                .append(Messages.CYCLE_MSG)
                .append("\n")
                .append(Messages.apply(Messages.BRACKET_TEMPLATE, 1, "Yes")).append("\n")
                .append(Messages.apply(Messages.BRACKET_TEMPLATE, 2, "No"))
                .append("\n")//                .append(MessagesUtil.apply(MessagesUtil.CHOOSE_TEMPLATE, ))
                .append(Messages.HEIGHT_MSG).append("\n")
                .append(Messages.apply(Messages.CHOOSE_TEMPLATE, inputHeight)).append("\n")
                .append(Messages.WIDTH_MSG).append("\n")
                .append(Messages.apply(Messages.CHOOSE_TEMPLATE, inputWidth)).append("\n")
                .append(Messages.START_POINT_MESSAGE)
                .append("\n");

            assertThatThrownBy(game::run).isInstanceOf(NullPointerException.class);
            String output = byteArrayOutputStream.toString().replace("\r\n", "\n").replace("\r", "\n").trim();
            assertThat(output).isEqualTo(expectedOutput.toString().replace("\r\n", "\n").replace("\r", "\n").trim());
        }

        @ParameterizedTest
        @CsvSource({
            "0, 0",
            "1, \"\"",
            "1, -1",
            "ada, 2",
            "2, adad",
            "3, 4",
            "5, 6",
            "5, 5"
        })
        void pathPointTestStart(String x, String y) {
            SecureRandom sc = new SecureRandom();
            int height = sc.nextInt(Config.MIN_HEIGHT, Config.MAX_HEIGHT + 1);
            int width = sc.nextInt(Config.MIN_WIDTH, Config.MAX_WIDTH + 1);
            String errorMsg;
            if (x.isEmpty() || y.isEmpty()) {
                errorMsg = "Write both point coordinate (x, y)";
            } else {
                try {
                    int xv = Integer.parseInt(x);
                    int yv = Integer.parseInt(y);
                    if (xv < 0 || xv >= height) {
                        errorMsg = "Coordinate x " +
                            Messages.apply(Messages.ERROR_OUT_OF_RANGE_TEMPLATE, 0,
                                height - 1);
                    } else if (yv < 0 || yv >= width) {
                        errorMsg = "Coordinate y " +
                            Messages.apply(Messages.ERROR_OUT_OF_RANGE_TEMPLATE, 0,
                                width - 1);
                    } else {
                        pathPointStartTestCorrect(height, width, x, y);
                        return;
                    }
                } catch (NumberFormatException e) {
                    errorMsg = Messages.ERROR_WRONG_NUMBER_FORMAT;
                }
            }
            pathPointStartTestWrong(height, width, x, y, errorMsg);
        }

        private void pathPointStartTestWrong(int height, int width, String x, String y, String errorMsg) {
            byteArrayOutputStream = new ByteArrayOutputStream();
            PrintStream printStream = new PrintStream(byteArrayOutputStream);
            System.setOut(printStream);
            SecureRandom secureRandom = new SecureRandom();
            String inputGenerate =
                String.valueOf(secureRandom.nextInt(1, GeneratorFactory.GeneratorType.values().length + 1)); // random choose
            String inputCycle = String.valueOf(secureRandom.nextInt(1, 3));
            String inputHeight = String.valueOf(height);
            String inputWidth = String.valueOf(width);
            String input =
                inputGenerate + "\n" + inputCycle + "\n" + inputHeight + "\n" + inputWidth + "\n" + x + " " + y;
            System.setIn(new ByteArrayInputStream(input.getBytes()));

            Game game = new Game();
            game.printer(System.out);

            StringBuilder expectedOutput = new StringBuilder();
            expectedOutput
                .append(Messages.GENERATOR_MSG)
                .append("\n")
                .append(Messages.availableEnumConst(GeneratorFactory.GeneratorType.class))
                .append("\n")

                .append(Messages.apply(Messages.CHOOSE_TEMPLATE,
                    GeneratorFactory.GeneratorType.values()[Integer.parseInt(inputGenerate) - 1]))
                .append("\n")
                .append(Messages.CYCLE_MSG)
                .append("\n")
                .append(Messages.apply(Messages.BRACKET_TEMPLATE, 1, "Yes")).append("\n")
                .append(Messages.apply(Messages.BRACKET_TEMPLATE, 2, "No"))
                .append("\n")//                .append(MessagesUtil.apply(MessagesUtil.CHOOSE_TEMPLATE, ))
                .append(Messages.HEIGHT_MSG).append("\n")
                .append(Messages.apply(Messages.CHOOSE_TEMPLATE, inputHeight)).append("\n")
                .append(Messages.WIDTH_MSG).append("\n")
                .append(Messages.apply(Messages.CHOOSE_TEMPLATE, inputWidth)).append("\n")
                .append(Messages.START_POINT_MESSAGE)
                .append("\n").append(errorMsg).append("\n").append(Messages.START_POINT_MESSAGE).append("\n");

            assertThatThrownBy(game::run).isInstanceOf(NullPointerException.class);
            String output = byteArrayOutputStream.toString().replace("\r\n", "\n").replace("\r", "\n").trim();
            assertThat(output).isEqualTo(expectedOutput.toString().replace("\r\n", "\n").replace("\r", "\n").trim());
        }

        private void pathPointStartTestCorrect(int height, int width, String x, String y) {
            byteArrayOutputStream = new ByteArrayOutputStream();
            PrintStream printStream = new PrintStream(byteArrayOutputStream);
            System.setOut(printStream);
            SecureRandom secureRandom = new SecureRandom();
            String inputGenerate =
                String.valueOf(secureRandom.nextInt(1, GeneratorFactory.GeneratorType.values().length + 1)); // random choose
            String inputCycle = String.valueOf(secureRandom.nextInt(1, 3));
            String inputHeight = String.valueOf(height);
            String inputWidth = String.valueOf(width);
            String input =
                inputGenerate + "\n" + inputCycle + "\n" + inputHeight + "\n" + inputWidth + "\n" + x + " " + y;
            System.setIn(new ByteArrayInputStream(input.getBytes()));

            Game game = new Game();
            game.printer(System.out);

            StringBuilder expectedOutput = new StringBuilder();
            expectedOutput
                .append(Messages.GENERATOR_MSG)
                .append("\n")
                .append(Messages.availableEnumConst(GeneratorFactory.GeneratorType.class))
                .append("\n")

                .append(Messages.apply(Messages.CHOOSE_TEMPLATE,
                    GeneratorFactory.GeneratorType.values()[Integer.parseInt(inputGenerate) - 1]))
                .append("\n")
                .append(Messages.CYCLE_MSG)
                .append("\n")
                .append(Messages.apply(Messages.BRACKET_TEMPLATE, 1, "Yes")).append("\n")
                .append(Messages.apply(Messages.BRACKET_TEMPLATE, 2, "No"))
                .append("\n")//                .append(MessagesUtil.apply(MessagesUtil.CHOOSE_TEMPLATE, ))
                .append(Messages.HEIGHT_MSG).append("\n")
                .append(Messages.apply(Messages.CHOOSE_TEMPLATE, inputHeight)).append("\n")
                .append(Messages.WIDTH_MSG).append("\n")
                .append(Messages.apply(Messages.CHOOSE_TEMPLATE, inputWidth)).append("\n")
                .append(Messages.START_POINT_MESSAGE)
                .append("\n").append(Messages.END_POINT_MESSAGE).append("\n");

            assertThatThrownBy(game::run).isInstanceOf(NullPointerException.class);
            String output = byteArrayOutputStream.toString().replace("\r\n", "\n").replace("\r", "\n").trim();
            assertThat(output).isEqualTo(expectedOutput.toString().replace("\r\n", "\n").replace("\r", "\n").trim());
        }

        @ParameterizedTest
        @CsvSource({
            "0, 0",
            "1, \"\"",
            "1, -1",
            "ada, 2",
            "2, adad",
            "3, 4",
            "5, 6",
            "5, 5"
        })
        void pathPointTestEnd(String x, String y) {
            SecureRandom sc = new SecureRandom();
            String errorMsg;
            int height = sc.nextInt(Config.MIN_HEIGHT, Config.MAX_HEIGHT + 1);
            int width = sc.nextInt(Config.MIN_WIDTH, Config.MAX_WIDTH + 1);
            if (x.isEmpty() || y.isEmpty()) {
                errorMsg = "Write both point coordinate (x, y)";
            } else {
                try {
                    int xv = Integer.parseInt(x);
                    int yv = Integer.parseInt(y);
                    if (xv < 0 || xv >= height) {
                        errorMsg = "Coordinate x " +
                            Messages.apply(Messages.ERROR_OUT_OF_RANGE_TEMPLATE, Config.MIN_HEIGHT - 1,
                                height - 1);
                    } else if (yv < 0 || yv >= width) {
                        errorMsg = "Coordinate y " +
                            Messages.apply(Messages.ERROR_OUT_OF_RANGE_TEMPLATE, Config.MIN_WIDTH - 1,
                                width - 1);
                    } else {
                        pathPointEndTestCorrect(height,
                            width, x, y);
                        return;
                    }
                } catch (NumberFormatException e) {
                    errorMsg = Messages.ERROR_WRONG_NUMBER_FORMAT;
                }
            }
            pathPointEndTestWrong(height,
                width, x, y, errorMsg);
        }

        private void pathPointEndTestWrong(int height, int width, String x, String y, String errorMsg) {
            byteArrayOutputStream = new ByteArrayOutputStream();
            PrintStream printStream = new PrintStream(byteArrayOutputStream);
            System.setOut(printStream);
            SecureRandom secureRandom = new SecureRandom();
            String inputGenerate =
                String.valueOf(secureRandom.nextInt(1, GeneratorFactory.GeneratorType.values().length + 1)); // random choose
            String inputCycle = String.valueOf(secureRandom.nextInt(1, 3));
            String inputHeight = String.valueOf(height);
            String inputWidth = String.valueOf(width);
            String inputStartX = String.valueOf(secureRandom.nextInt(0, height));
            String inputStartY = String.valueOf(secureRandom.nextInt(0, width));
            String input =
                inputGenerate + "\n" + inputCycle + "\n" + inputHeight + "\n" + inputWidth + "\n" + inputStartX + " " +
                    inputStartY + "\n" + x + " " + y;
            System.setIn(new ByteArrayInputStream(input.getBytes()));

            Game game = new Game();
            game.printer(System.out);

            StringBuilder expectedOutput = new StringBuilder();
            expectedOutput
                .append(Messages.GENERATOR_MSG)
                .append("\n")
                .append(Messages.availableEnumConst(GeneratorFactory.GeneratorType.class))
                .append("\n")

                .append(Messages.apply(Messages.CHOOSE_TEMPLATE,
                    GeneratorFactory.GeneratorType.values()[Integer.parseInt(inputGenerate) - 1]))
                .append("\n")
                .append(Messages.CYCLE_MSG)
                .append("\n")
                .append(Messages.apply(Messages.BRACKET_TEMPLATE, 1, "Yes")).append("\n")
                .append(Messages.apply(Messages.BRACKET_TEMPLATE, 2, "No"))
                .append("\n")//                .append(MessagesUtil.apply(MessagesUtil.CHOOSE_TEMPLATE, ))
                .append(Messages.HEIGHT_MSG).append("\n")
                .append(Messages.apply(Messages.CHOOSE_TEMPLATE, inputHeight)).append("\n")
                .append(Messages.WIDTH_MSG).append("\n")
                .append(Messages.apply(Messages.CHOOSE_TEMPLATE, inputWidth)).append("\n")
                .append(Messages.START_POINT_MESSAGE)
                .append("\n").append(Messages.END_POINT_MESSAGE).append("\n").append(errorMsg).append("\n")
                .append(Messages.END_POINT_MESSAGE).append("\n");

            assertThatThrownBy(game::run).isInstanceOf(NullPointerException.class);
            String output = byteArrayOutputStream.toString().replace("\r\n", "\n").replace("\r", "\n").trim();
            assertThat(output).isEqualTo(expectedOutput.toString().replace("\r\n", "\n").replace("\r", "\n").trim());
        }

        private void pathPointEndTestCorrect(int height, int width, String x, String y) {
            byteArrayOutputStream = new ByteArrayOutputStream();
            PrintStream printStream = new PrintStream(byteArrayOutputStream);
            System.setOut(printStream);
            SecureRandom secureRandom = new SecureRandom();
            String inputGenerate =
                String.valueOf(secureRandom.nextInt(1, GeneratorFactory.GeneratorType.values().length + 1)); // random choose
            String inputCycle = String.valueOf(secureRandom.nextInt(1, 3));
            String inputHeight = String.valueOf(height);
            String inputWidth = String.valueOf(width);
            String inputStartX = String.valueOf(secureRandom.nextInt(0, height));
            String inputStartY = String.valueOf(secureRandom.nextInt(0, width));
            String input =
                inputGenerate + "\n" + inputCycle + "\n" + inputHeight + "\n" + inputWidth + "\n" + inputStartX + " " +
                    inputStartY + "\n" + x + " " + y;
            System.setIn(new ByteArrayInputStream(input.getBytes()));

            Game game = new Game();
            game.printer(System.out);

            StringBuilder expectedOutput = new StringBuilder();
            expectedOutput
                .append(Messages.GENERATOR_MSG)
                .append("\n")
                .append(Messages.availableEnumConst(GeneratorFactory.GeneratorType.class))
                .append("\n")

                .append(Messages.apply(Messages.CHOOSE_TEMPLATE,
                    GeneratorFactory.GeneratorType.values()[Integer.parseInt(inputGenerate) - 1]))
                .append("\n")
                .append(Messages.CYCLE_MSG)
                .append("\n")
                .append(Messages.apply(Messages.BRACKET_TEMPLATE, 1, "Yes")).append("\n")
                .append(Messages.apply(Messages.BRACKET_TEMPLATE, 2, "No"))
                .append("\n")//                .append(MessagesUtil.apply(MessagesUtil.CHOOSE_TEMPLATE, ))
                .append(Messages.HEIGHT_MSG).append("\n")
                .append(Messages.apply(Messages.CHOOSE_TEMPLATE, inputHeight)).append("\n")
                .append(Messages.WIDTH_MSG).append("\n")
                .append(Messages.apply(Messages.CHOOSE_TEMPLATE, inputWidth)).append("\n")
                .append(Messages.START_POINT_MESSAGE)
                .append("\n").append(Messages.END_POINT_MESSAGE).append("\n").append(Messages.SOLVERS_MSG)
                .append("\n").append(Messages.availableEnumConst(SolverFactory.SolverEnum.class));

            assertThatThrownBy(game::run).isInstanceOf(NullPointerException.class);
            String output = byteArrayOutputStream.toString().replace("\r\n", "\n").replace("\r", "\n").trim();
            assertThat(output).isEqualTo(expectedOutput.toString().replace("\r\n", "\n").replace("\r", "\n").trim());
        }

        @ParameterizedTest
        @ValueSource(strings = {"-324234", "0", "12313", "--aaada2123", "1", "2", "3", "4", "23424", "sdf2du8ada"})
        void solversTest(String inputSolver) {
            try {
                int v = Integer.parseInt(inputSolver);
                if (v < 1 || v > SolverFactory.SolverEnum.values().length) {
                    solversTestWrong(inputSolver,
                        Messages.apply(Messages.ERROR_OUT_OF_RANGE_TEMPLATE, 1, SolverFactory.SolverEnum.values().length));
                } else {
                    solversTestCorrect(inputSolver);
                }
            } catch (NumberFormatException e) {
                solversTestWrong(inputSolver, Messages.ERROR_WRONG_NUMBER_FORMAT);
            }
        }

        private void solversTestCorrect(String inputSolver) {
            byteArrayOutputStream = new ByteArrayOutputStream();
            PrintStream printStream = new PrintStream(byteArrayOutputStream);
            System.setOut(printStream);

            SecureRandom secureRandom = new SecureRandom();
            String inputGenerate =
                String.valueOf(secureRandom.nextInt(1, GeneratorFactory.GeneratorType.values().length + 1)); // random choose
            String inputCycle = String.valueOf(secureRandom.nextInt(1, 3));
            String inputHeight = String.valueOf(secureRandom.nextInt(Config.MIN_HEIGHT, Config.MAX_HEIGHT + 1));
            String inputWidth = String.valueOf(secureRandom.nextInt(Config.MIN_WIDTH, Config.MAX_WIDTH + 1));
            String inputStartX = String.valueOf(secureRandom.nextInt(0, Integer.parseInt(inputHeight)));
            String inputStartY = String.valueOf(secureRandom.nextInt(0, Integer.parseInt(inputWidth)));
            String inputEndX = String.valueOf(secureRandom.nextInt(0, Integer.parseInt(inputHeight)));
            String inputEndY = String.valueOf(secureRandom.nextInt(0, Integer.parseInt(inputWidth)));
            String input =
                inputGenerate + "\n" + inputCycle + "\n" + inputHeight + "\n" + inputWidth + "\n" + inputStartX + " " +
                    inputStartY + "\n" + inputEndX + " " + inputEndY + "\n" + inputSolver;
            System.setIn(new ByteArrayInputStream(input.getBytes()));

            Game game = new Game();
            game.printer(System.out);

            StringBuilder expectedOutput = new StringBuilder();
            expectedOutput
                .append(Messages.GENERATOR_MSG)
                .append("\n")
                .append(Messages.availableEnumConst(GeneratorFactory.GeneratorType.class))
                .append("\n")

                .append(Messages.apply(Messages.CHOOSE_TEMPLATE,
                    GeneratorFactory.GeneratorType.values()[Integer.parseInt(inputGenerate) - 1]))
                .append("\n")
                .append(Messages.CYCLE_MSG)
                .append("\n")
                .append(Messages.apply(Messages.BRACKET_TEMPLATE, 1, "Yes")).append("\n")
                .append(Messages.apply(Messages.BRACKET_TEMPLATE, 2, "No"))
                .append("\n")//                .append(MessagesUtil.apply(MessagesUtil.CHOOSE_TEMPLATE, ))
                .append(Messages.HEIGHT_MSG).append("\n")
                .append(Messages.apply(Messages.CHOOSE_TEMPLATE, inputHeight)).append("\n")
                .append(Messages.WIDTH_MSG).append("\n")
                .append(Messages.apply(Messages.CHOOSE_TEMPLATE, inputWidth)).append("\n")
                .append(Messages.START_POINT_MESSAGE)
                .append("\n").append(Messages.END_POINT_MESSAGE).append("\n").append(Messages.SOLVERS_MSG)
                .append("\n").append(Messages.availableEnumConst(SolverFactory.SolverEnum.class));

            assertThatCode(game::run).doesNotThrowAnyException();
        }

        private void solversTestWrong(String inputSolver, String errorMsg) {
            byteArrayOutputStream = new ByteArrayOutputStream();
            PrintStream printStream = new PrintStream(byteArrayOutputStream);
            System.setOut(printStream);

            SecureRandom secureRandom = new SecureRandom();
            String inputGenerate =
                String.valueOf(secureRandom.nextInt(1, GeneratorFactory.GeneratorType.values().length + 1)); // random choose
            String inputCycle = String.valueOf(secureRandom.nextInt(1, 3));
            String inputHeight = String.valueOf(secureRandom.nextInt(Config.MIN_HEIGHT, Config.MAX_HEIGHT + 1));
            String inputWidth = String.valueOf(secureRandom.nextInt(Config.MIN_WIDTH, Config.MAX_WIDTH + 1));
            String inputStartX = String.valueOf(secureRandom.nextInt(0, Integer.parseInt(inputHeight)));
            String inputStartY = String.valueOf(secureRandom.nextInt(0, Integer.parseInt(inputWidth)));
            String inputEndX = String.valueOf(secureRandom.nextInt(0, Integer.parseInt(inputHeight)));
            String inputEndY = String.valueOf(secureRandom.nextInt(0, Integer.parseInt(inputWidth)));
            String input =
                inputGenerate + "\n" + inputCycle + "\n" + inputHeight + "\n" + inputWidth + "\n" + inputStartX + " " +
                    inputStartY + "\n" + inputEndX + " " + inputEndY + "\n" + inputSolver;
            System.setIn(new ByteArrayInputStream(input.getBytes()));

            Game game = new Game();
            game.printer(System.out);

            StringBuilder expectedOutput = new StringBuilder();
            expectedOutput
                .append(Messages.GENERATOR_MSG)
                .append("\n")
                .append(Messages.availableEnumConst(GeneratorFactory.GeneratorType.class))
                .append("\n")
                .append(Messages.apply(Messages.CHOOSE_TEMPLATE,
                    GeneratorFactory.GeneratorType.values()[Integer.parseInt(inputGenerate) - 1]))
                .append("\n")
                .append(Messages.CYCLE_MSG)
                .append("\n")
                .append(Messages.apply(Messages.BRACKET_TEMPLATE, 1, "Yes")).append("\n")
                .append(Messages.apply(Messages.BRACKET_TEMPLATE, 2, "No"))
                .append("\n")//                .append(MessagesUtil.apply(MessagesUtil.CHOOSE_TEMPLATE, ))
                .append(Messages.HEIGHT_MSG).append("\n")
                .append(Messages.apply(Messages.CHOOSE_TEMPLATE, inputHeight)).append("\n")
                .append(Messages.WIDTH_MSG).append("\n")
                .append(Messages.apply(Messages.CHOOSE_TEMPLATE, inputWidth)).append("\n")
                .append(Messages.START_POINT_MESSAGE)
                .append("\n").append(Messages.END_POINT_MESSAGE).append("\n").append(Messages.SOLVERS_MSG)
                .append("\n").append(Messages.availableEnumConst(SolverFactory.SolverEnum.class)).append("\n").append(errorMsg)
                .append("\n")
                .append(Messages.SOLVERS_MSG).append("\n")
                .append(Messages.availableEnumConst(SolverFactory.SolverEnum.class));

            assertThatThrownBy(game::run).isInstanceOf(NullPointerException.class);
            String output = byteArrayOutputStream.toString().replace("\r\n", "\n").replace("\r", "\n").trim();
            assertThat(output).isEqualTo(expectedOutput.toString().replace("\r\n", "\n").replace("\r", "\n").trim());
        }
    }
}
