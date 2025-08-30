import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class Storage {
    private final Path dir = Paths.get("data");          // relative, OS-independent
    private final Path file = dir.resolve("qb7.txt");    // change name if you like

    /** Load tasks from data file. If file doesn't exist yet, return empty list. */
    public ArrayList<Task> load() throws IOException {
        ArrayList<Task> tasks = new ArrayList<>();
        if (!Files.exists(file)) {
            return tasks; // first run: nothing to load
        }
        List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
        for (String raw : lines) {
            if (raw == null) continue;
            String line = raw.trim();
            if (line.isEmpty()) continue;

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
                        if (done) t.markAsDone();
                        tasks.add(t);
                        break;
                    }
                    case "D": {
                        String desc = p[2];
                        String by = p[3];
                        Task t = new Deadline(desc, by);
                        if (done) t.markAsDone();
                        tasks.add(t);
                        break;
                    }
                    case "E": {
                        String desc = p[2];
                        String from = p[3];
                        String to = p[4];
                        Task t = new Event(desc, from, to);
                        if (done) t.markAsDone();
                        tasks.add(t);
                        break;
                    }
                    default:
                        // unknown record → skip
                }
            } catch (Exception ignored) {
                // Stretch goal: detect & report corrupted lines.
            }
        }
        return tasks;
    }

    /** Save all tasks to data file, creating folder/file if needed. */
    public void save(ArrayList<Task> tasks) throws IOException {
        if (!Files.exists(dir)) Files.createDirectories(dir);
        List<String> out = new ArrayList<>();
        for (Task t : tasks) {
            out.add(serialize(t));
        }
        Files.write(file, out, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    private String serialize(Task t) {
        String done = t.isDone ? "1" : "0";
        if (t instanceof Todo) {
            return String.join(" | ", "T", done, t.description);
        } else if (t instanceof Deadline) {
            Deadline d = (Deadline) t;
            return String.join(" | ", "D", done, d.description, d.by);
        } else if (t instanceof Event) {
            Event e = (Event) t;
            return String.join(" | ", "E", done, e.description, e.from, e.to);
        }
        return String.join(" | ", "T", done, t.description); // fallback
    }
}
