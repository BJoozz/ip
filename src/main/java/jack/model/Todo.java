package jack.model;

/**
 * Represents a simple to-do task without any date/time constraints.
 */
public class Todo extends Task {
    /**
     * Creates a new {@code Todo} task.
     *
     * @param description description of the to-do task
     */
    public Todo(String description) {
        super(description, TaskType.TODO);
    }
}
