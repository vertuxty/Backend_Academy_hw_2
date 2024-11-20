package backend.academy.generators;

import com.google.common.base.Function;
import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;

/**
 * Factory class for creating
 * instances of maze's generating algorithms
 */
@UtilityClass
public class GeneratorFactory {

    /**
     * Enum that representing
     * different types of generators
     * that can be created by {@link GeneratorFactory}
     */
    @RequiredArgsConstructor
    public enum GeneratorType {
        /**
         * Kruskal algorithm
         *
         * @see KruskalGenerator
         */
        KRUSKAL(KruskalGenerator::new),
        /**
         * Euler's algorithm
         *
         * @see EulerGenerator
         */
        EULER(EulerGenerator::new),
        /**
         * Prime's algorithm
         *
         * @see PrimeGenerator
         */
        PRIME_BY_WALL(PrimeGenerator::new);

        private final Function<Boolean, Generator> constructor;
    }

    /**
     * Method for creating generator of specified type.
     *
     * @param generatorType a identifier of generator type
     * @return instance of a {@link Generator}
     * @see GeneratorType
     */
    public Generator createGenerator(GeneratorType generatorType, boolean isCyclesAllowed) {
        Function<Boolean, Generator> generatorConstructor = generatorType.constructor;
        if (generatorConstructor != null) {
            return generatorConstructor.apply(isCyclesAllowed);
        } else {
            throw new IllegalArgumentException("No generator with name " + generatorType);
        }
    }
}
