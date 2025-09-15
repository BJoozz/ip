package jack.model;

public class Task {
    protected String description;
    protected boolean isDone;
    protected TaskType type;   // NEW

    public Task(String description, TaskType type) {
        this.description = description;
        this.isDone = false;
        this.type = type;
    }

    public boolean matches(String keyword) {
        if (description == null) {
            return false;
        }
        return description.toLowerCase().contains(keyword.toLowerCase());
    }
    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    public void markAsDone() { this.isDone = true; }

    public void markAsNotDone() { this.isDone = false; }

    @Override
    public String toString() {
        return "[" + type.getSymbol() + "][" + getStatusIcon() + "] " + description;
    }
}
