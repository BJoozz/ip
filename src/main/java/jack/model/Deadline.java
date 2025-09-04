package jack.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Deadline extends Task {
    protected LocalDate by;  // store as real date

    private static final DateTimeFormatter OUT_FMT =
            DateTimeFormatter.ofPattern("MMM d yyyy"); // e.g., Oct 15 2019

    public Deadline(String description, LocalDate by) {
        super(description, TaskType.DEADLINE);
        this.by = by;
    }

    @Override
    public String toString() {
        // prints (by: Oct 15 2019)
        return "[" + type.getSymbol() + "][" + getStatusIcon() + "] "
                + description + " (by: " + by.format(OUT_FMT) + ")";
    }
}
