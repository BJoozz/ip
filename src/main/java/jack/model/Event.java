package jack.model;

/**
 * Represents an event task with a start and end time.
 */
public class Event extends Task {
    /** Start time of the event (stored as a string). */
    protected String from;

    /** End time of the event (stored as a string). */
    protected String to;

    /**
     * Creates a new {@code Event} task.
     *
     * @param description description of the event
     * @param from start time of the event
     * @param to end time of the event
     */
    public Event(String description, String from, String to) {
        super(description, TaskType.EVENT);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns the string representation of this event for display to the user.
     * <p>
     * The format includes the type symbol, status icon, description,
     * and the event time range.
     *
     * @return formatted event string
     */
    @Override
    public String toString() {
        return "[" + type.getSymbol() + "][" + getStatusIcon() + "] "
                + description + " (from: " + from + " to: " + to + ")";
    }
}
