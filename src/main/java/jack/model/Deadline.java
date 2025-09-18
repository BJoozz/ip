package jack.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Represents a task with a deadline.
 * <p>
 * The deadline is stored as a {@link LocalDate} and formatted
 * for display in a human-readable form.
 */

public class Deadline extends Task {
    /** Formatter used for displaying the deadline date. */
    private static final DateTimeFormatter OUT_FMT =
            DateTimeFormatter.ofPattern("MMM d yyyy"); // e.g., Oct 15 2019

    /** Date by which the task must be completed. */
    protected LocalDate by;

    /**
     * Creates a new {@code Deadline} task.
     *
     * @param description description of the deadline task
     * @param by date by which the task is due
     */
    public Deadline(String description, LocalDate by) {
        super(description, TaskType.DEADLINE);
        this.by = by;
    }

    /**
     * Returns the string representation of this deadline for display to the user.
     * <p>
     * The format includes the type symbol, status icon, description,
     * and the formatted due date.
     *
     * @return formatted deadline string
     */
    @Override
    public String toString() {
        // prints (by: Oct 15 2019)
        return "[" + type.getSymbol() + "][" + getStatusIcon() + "] "
                + description + " (by: " + by.format(OUT_FMT) + ")";
    }
}
