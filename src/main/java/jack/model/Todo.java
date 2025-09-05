package jack.model;

import jack.model.Task;
import jack.model.TaskType;

public class Todo extends Task {
    public Todo(String description) {
        super(description, TaskType.TODO);
    }
}
