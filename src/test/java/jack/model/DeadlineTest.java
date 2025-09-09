package jack;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DeadlineTest {

    @Test
    @DisplayName("toString() shows [D] status, description, and formatted date 'MMM d yyyy'")
    void toString_formatsCorrectly() {
        // Expected API: new Deadline(String description, LocalDate byDate)
        Deadline d = new Deadline("submit report", LocalDate.of(2025, 10, 1));
        String s = d.toString();

        assertTrue(s.startsWith("[D]"), "Should start with [D]");
        assertTrue(s.contains("submit report"), "Should include the description");
        assertTrue(s.contains("Oct 1 2025"), "Should format date as 'Oct 1 2025'");
    }

    @Test
    @DisplayName("mark/unmark toggles the completion state reflected in toString")
    void markUnmark_updatesStatusIcon() {
        Deadline d = new Deadline("finish iP", LocalDate.of(2025, 9, 30));

        String before = d.toString();
        assertTrue(before.contains("[ ]"), "Initially should be undone: [ ]");

        d.mark();
        String marked = d.toString();
        assertTrue(marked.contains("[X]"), "After mark(), should show [X]");

        d.unmark();
        String unmarked = d.toString();
        assertTrue(unmarked.contains("[ ]"), "After unmark(), should show [ ]");
    }
}
