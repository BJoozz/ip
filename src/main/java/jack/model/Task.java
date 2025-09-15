package jack.model;

/**
 * Represents a generic task in the Jack task manager.
 * <p>
 * Subclasses provide specific task types.
 */
public class Task {
    /** Description text of the task. */
    protected String description;

    /** Whether this task is marked as done. */
    protected boolean isDone;

    /** Type of this task (e.g., TODO, DEADLINE, EVENT). */
    protected TaskType type;

    /**
     * Creates a new {@code Task} with the given description and type.
     *
     * @param description description of the task
     * @param type type of the task
     */
    public Task(String description, TaskType type) {
        this.description = description;
        this.isDone = false;
        this.type = type;
    }
    
    /**
     * Checks if the task's description contains the given keyword.
     *
     * @param keyword the word to search for
     * @return true if the description is not null and contains the keyword
     *         (case-insensitive), false otherwise
     */
    public boolean matches(String keyword) {
        if (description == null) {
            return false;
        }
        return description.toLowerCase().contains(keyword.toLowerCase());
    }
  
    /**
     * Returns the status icon of this task.
     *
     * @return {@code "X"} if the task is done; a space character otherwise
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    /**
     * Marks this task as done.
     */
    public void markAsDone() {
        this.isDone = true;
    }

    /**
     * Marks this task as not done.
     */
    public void markAsNotDone() {
        this.isDone = false;
    }

    /**
     * Returns the string representation of this task for display to the user.
     * <p>
     * The format includes the task type symbol, status icon, and description.
     *
     * @return formatted task string
     */
    @Override
    public String toString() {
        return "[" + type.getSymbol() + "][" + getStatusIcon() + "] " + description;
    }
}
