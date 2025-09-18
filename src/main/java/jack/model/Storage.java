package jack.model;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles saving and loading of tasks to and from disk.
 * <p>
 * Tasks are stored in a text file {@code data/jack.txt} using a simple
 * pipe-delimited format. Each line encodes one task, including its type,
 * completion status, and description (plus additional fields for deadlines
 * and events).
 */
public class Storage {
    /** Directory used to store the data file. */
    private final Path dir = Paths.get("data");

    /** File path for storing tasks. */
    private final Path file = dir.resolve("jack.txt");

    /**
     * Loads tasks from the data file.
     * <p>
     * If the file does not exist, returns an empty list.
     * Lines that cannot be parsed are ignored.
     *
     * @return list of tasks loaded from file
     * @throws IOException if an I/O error occurs while reading the file
     */
    public ArrayList<Task> load() throws IOException {
        ArrayList<Task> tasks = new ArrayList<>();
        if (!Files.exists(file)) {
            return tasks;
        }
        List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
        for (String raw : lines) {
            if (raw == null) {
                continue;
            }
            String line = raw.trim();
            if (line.isEmpty()) {
                continue;
            }

            // Formats we write:
            // T | 1 | read book
            // D | 0 | return book | Sunday
            // E | 1 | project meeting | Mon 2pm | 4pm
            String[] p = line.split("\\s*\\|\\s*");
            try {
                String kind = p[0];
                boolean done = "1".equals(p[1]);
                switch (kind) {
                case "T": {
                    String desc = p[2];
                    Task t = new Todo(desc);
                    if (done) {
                        t.markAsDone();
                    }
                    tasks.add(t);
                    break;
                }
                case "D": {
                    String desc = p[2];
                    LocalDate by = LocalDate.parse(p[3]); // expects yyyy-MM-dd
                    Task t = new Deadline(desc, by);
                    if (done) {
                        t.markAsDone();
                    }
                    tasks.add(t);
                    break;
                }

                case "E": {
                    String desc = p[2];
                    String from = p[3];
                    String to = p[4];
                    Task t = new Event(desc, from, to);
                    if (done) {
                        t.markAsDone();
                    }
                    tasks.add(t);
                    break;
                }
                default:
                }
            } catch (Exception ignored) {
                // Stretch goal: detect & report corrupted lines.
            }
        }
        return tasks;
    }

    /**
     * Saves all tasks to the data file, creating the folder/file if needed.
     * <p>
     * Existing file contents are replaced with the current list of tasks.
     *
     * @param tasks tasks to save
     * @throws IOException if an I/O error occurs while writing the file
     */
    public void save(ArrayList<Task> tasks) throws IOException {
        if (!Files.exists(dir)) {
            Files.createDirectories(dir);
        }
        List<String> out = new ArrayList<>();
        for (Task t : tasks) {
            out.add(serialize(t));
        }
        Files.write(file, out, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    /**
     * Serializes a task into its encoded string form for persistence.
     *
     * @param t task to serialize
     * @return pipe-delimited string representing the task
     */
    private String serialize(Task t) {
        String done = t.isDone ? "1" : "0";
        if (t instanceof Todo) {
            return String.join(" | ", "T", done, t.description);
        } else if (t instanceof Deadline) {
            Deadline d = (Deadline) t;
            // write ISO format so we can parse reliably on load
            return String.join(" | ", "D", done, d.description, d.by.toString());
        } else if (t instanceof Event) {
            Event e = (Event) t;
            return String.join(" | ", "E", done, e.description, e.from, e.to);
        }
        return String.join(" | ", "T", done, t.description); // fallback
    }
}
