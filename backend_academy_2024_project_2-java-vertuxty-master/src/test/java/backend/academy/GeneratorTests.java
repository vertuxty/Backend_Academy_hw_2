package backend.academy;

import backend.academy.generators.Generator;
import backend.academy.generators.GeneratorFactory;
import backend.academy.mazes.Maze;
import java.security.SecureRandom;

import backend.academy.utils.Config;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

public class GeneratorTests {

    @Nested class BaseTest {
        private final SecureRandom secureRandom = new SecureRandom();

        @Test void multiplyTest() {
            GeneratorFactory.GeneratorType[] generatorTypes = GeneratorFactory.GeneratorType.values();
            for (GeneratorFactory.GeneratorType generatorTypeFirst : generatorTypes) {
                generatorInitTest(generatorTypeFirst);
                generateTest(generatorTypeFirst);
                for (GeneratorFactory.GeneratorType generatorTypeSecond : generatorTypes) {
                    if (!generatorTypeSecond.equals(generatorTypeFirst)) {
                        generatorEqualsTest(generatorTypeFirst, generatorTypeSecond);
                    }
                }
            }
        }

        private void generatorInitTest(GeneratorFactory.GeneratorType generatorType) {
            boolean cycles = secureRandom.nextBoolean();
            assertThatCode(() -> GeneratorFactory.createGenerator(generatorType, cycles)).doesNotThrowAnyException();
            assertThatCode(() -> GeneratorFactory.createGenerator(generatorType, !cycles)).doesNotThrowAnyException();
            assertThat(GeneratorFactory.createGenerator(generatorType, cycles)).isNotNull();
            assertThat(GeneratorFactory.createGenerator(generatorType, !cycles)).isNotNull();
            assertThat(GeneratorFactory.createGenerator(generatorType, cycles)).isNotEqualTo(
                GeneratorFactory.createGenerator(generatorType, !cycles));
        }

        private void generateTest(GeneratorFactory.GeneratorType generatorType) {
            boolean cycles = secureRandom.nextBoolean();
            Generator generator = GeneratorFactory.createGenerator(generatorType, cycles);
            int height = secureRandom.nextInt(Config.MIN_HEIGHT, Config.MAX_HEIGHT + 1);
            int width = secureRandom.nextInt(Config.MIN_WIDTH, Config.MAX_WIDTH + 1);
            assertThatCode(() -> generator.generate(height, width)).doesNotThrowAnyException();
            Maze maze = generator.generate(height, width);
            assertThat(maze).isNotNull();
        }

        private void generatorEqualsTest(GeneratorFactory.GeneratorType generatorTypeFirst, GeneratorFactory.GeneratorType generatorTypeSecond) {
            boolean cycles = secureRandom.nextBoolean();
            Generator generatorFirst = GeneratorFactory.createGenerator(generatorTypeFirst, cycles);
            Generator generatorSecond = GeneratorFactory.createGenerator(generatorTypeSecond, cycles);
            assertThat(generatorFirst.getClass().getName()).isEqualTo(
                GeneratorFactory.createGenerator(generatorTypeFirst, cycles).getClass().getName());
            assertThat(generatorSecond.getClass().getName()).isEqualTo(
                GeneratorFactory.createGenerator(generatorTypeSecond, cycles).getClass().getName());
            assertThat(generatorFirst.getClass().getName()).isNotEqualTo(generatorSecond.getClass().getName());
        }
    }
}

