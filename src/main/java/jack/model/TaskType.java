package jack.model;

/**
 * Enumerates the types of tasks supported by the Jack application.
 * <p>
 * Each type has an associated symbol used in task string representations.
 */
public enum TaskType {
    /** A simple to-do task. */
    TODO("T"),

    /** A task with a deadline. */
    DEADLINE("D"),

    /** A task that spans a period of time. */
    EVENT("E");

    /** Symbol used to represent this task type. */
    private final String symbol;

    /**
     * Creates a {@code TaskType} with the given symbol.
     *
     * @param symbol single-letter symbol representing the type
     */
    TaskType(String symbol) {
        this.symbol = symbol;
    }

    /**
     * Returns the symbol associated with this task type.
     *
     * @return symbol string (e.g., {@code "T"}, {@code "D"}, {@code "E"})
     */
    public String getSymbol() {
        return symbol;
    }
}
