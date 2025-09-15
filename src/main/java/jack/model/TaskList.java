package jack.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TaskList {
    private final ArrayList<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public TaskList(ArrayList<Task> fromStorage) {
        this.tasks = (fromStorage == null) ? new ArrayList<>() : fromStorage;
    }

    public int size() {
        return tasks.size();
    }

    public Task get(int idx) {
        return tasks.get(idx);
    }

    public void add(Task t) {
        tasks.add(t);
    }

    public Task remove(int idx) {
        return tasks.remove(idx);
    }

    public List<Task> asList() {
        return Collections.unmodifiableList(tasks);
    }

    public ArrayList<Task> raw() {
        return tasks;
    }
}
