package backend.academy.utils;

import lombok.experimental.UtilityClass;

/**
 * Util class for message constants in program.
 * Implements static methods to provide messages to the user.
 */
@UtilityClass public class Messages {
    public static final Character LEFT_BRACKET = '[';
    public static final Character RIGHT_BRACKET = ']';
    public static final String START_POINT_MESSAGE = "Write from point (x1, y1) as x1 y1: ";
    public static final String END_POINT_MESSAGE = "Write to point (x1, y1) as x1 y1: ";
    public static final String GENERATOR_MSG = "Algorithm to generate maze (print number):";
    public static final String SOLVERS_MSG = "Available solvers:";
    public static final String CHOOSE_TEMPLATE = "You choose %s";
    public static final String CYCLE_MSG = "Is cycles allowed in maze?";
    public static final String BRACKET_TEMPLATE = LEFT_BRACKET + "%d" + RIGHT_BRACKET + " %s";
    public static final String HEIGHT_MSG = "Write height";
    public static final String WIDTH_MSG = "Write width";
    public static final String ERROR_OUT_OF_RANGE_TEMPLATE = "Must be in range [%s, %s]";
    public static final String ERROR_WRONG_NUMBER_FORMAT = "Try again! Wrong number format!";

    /**
     * The method is intended for substituting
     * various values into the passed template.
     * It implies the correct transmission of arguments.
     *
     * @param template Template for value substitution
     * @param args     values for substitution
     * @return result of substituting values into the passed template
     */
    public static String apply(String template, Object... args) {
        return String.format(template, args);
    }

    /**
     * A method for getting various enum constants from a given class.
     *
     * @param clazz instance of enum class.
     * @return available enum constants.
     */
    public static String availableEnumConst(Class<?> clazz) {
        Object[] availableObjects = clazz.getEnumConstants();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < availableObjects.length; i++) {
            stringBuilder
                .append(Messages.apply(Messages.BRACKET_TEMPLATE, i + 1, availableObjects[i]))
                .append('\n');
        }
        return stringBuilder.toString();
    }
}
