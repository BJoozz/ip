package jack.model;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DeadlineTest {

    @Test
    @DisplayName("toString() shows [D], description, and formatted date 'MMM d yyyy'")
    void toString_formatsCorrectly() {
        Deadline d = new Deadline("submit report", LocalDate.of(2025, 10, 1));
        String s = d.toString();

        assertTrue(s.startsWith("[D]"), "Should start with [D]");
        assertTrue(s.contains("submit report"), "Should include description");
        assertTrue(s.contains("Oct 1 2025"), "Should include formatted date 'Oct 1 2025'");
    }

    @Test
    @DisplayName("markAsDone/markAsNotDone toggle status icon in toString")
    void mark_toggleStatus() {
        Deadline d = new Deadline("finish iP", LocalDate.of(2025, 9, 30));

        assertTrue(d.toString().contains("[ ]"), "Initially undone");

        d.markAsDone();
        assertTrue(d.toString().contains("[X]"), "After markAsDone(), should show [X]");

        d.markAsNotDone();
        assertTrue(d.toString().contains("[ ]"), "After markAsNotDone(), should show [ ]");
    }
}
