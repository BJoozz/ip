package jack.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a mutable list of tasks.
 * <p>
 * Provides methods to add, remove, and access tasks,
 * and exposes the underlying list for persistence.
 */
public class TaskList {
    /** Internal list of tasks. */
    private final ArrayList<Task> tasks;

    /**
     * Creates an empty {@code TaskList}.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a {@code TaskList} from an existing list of tasks.
     * <p>
     * If {@code fromStorage} is {@code null}, an empty list is used instead.
     *
     * @param fromStorage tasks loaded from storage, or {@code null}
     */
    public TaskList(ArrayList<Task> fromStorage) {
        this.tasks = (fromStorage == null) ? new ArrayList<>() : fromStorage;
    }
    public String findTasks(String keyword) {
        StringBuilder sb = new StringBuilder("Here are the matching tasks in your list:\n");
        int shown = 0;
        for (int i = 0; i < tasks.size(); i++) {
            Task t = tasks.get(i);
            if (t.matches(keyword)) {
                shown++;
                sb.append(String.format("%d.%s%n", shown, t));
            }
        }
        if (shown == 0) {
            return "No matching tasks found for: \"" + keyword + "\"";
        }
        return sb.toString().trim();
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return task count
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Retrieves the task at the specified index.
     *
     * @param idx zero-based index of the task
     * @return task at the given index
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public Task get(int idx) {
        return tasks.get(idx);
    }

    /**
     * Adds a task to the list.
     *
     * @param t task to add
     */
    public void add(Task t) {
        tasks.add(t);
    }

    /**
     * Removes the task at the specified index.
     *
     * @param idx zero-based index of the task to remove
     * @return the removed task
     * @throws IndexOutOfBoundsException if the index is invalid
     */
    public Task remove(int idx) {
        return tasks.remove(idx);
    }
  
    /**
     * Returns an unmodifiable view of the task list.
     *
     * @return read-only list of tasks
     */
    public List<Task> asList() {
        return Collections.unmodifiableList(tasks);
    }

    /**
     * Returns the underlying mutable list of tasks.
     * <p>
     * Used by the storage component to save tasks to disk.
     *
     * @return backing {@code ArrayList} of tasks
     */
    public ArrayList<Task> raw() {
        return tasks;
    }
}
