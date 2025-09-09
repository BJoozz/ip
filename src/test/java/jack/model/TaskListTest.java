package jack;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class TaskListTest {

    @Test
    void addTask_increasesSizeAndStoresTask() {
        TaskList list = new TaskList();
        list.add(new Todo("read book"));
        assertEquals(1, list.size());
        assertEquals("read book", list.get(0).getDescription());
    }

    @Test
    void deleteTask_removesCorrectItem() {
        TaskList list = new TaskList();
        list.add(new Todo("a"));
        list.add(new Todo("b"));
        Task removed = list.remove(0);
        assertEquals("a", removed.getDescription());
        assertEquals(1, list.size());
        assertEquals("b", list.get(0).getDescription());
    }

    @Test
    void find_matchesSubstringCaseInsensitive() {
        TaskList list = new TaskList();
        list.add(new Todo("Read CS2103 textbook"));
        list.add(new Todo("buy milk"));
        List<Task> found = list.find("cs2103");
        assertEquals(1, found.size());
        assertTrue(found.get(0).getDescription().contains("CS2103"));
    }

    @Test
    void remove_outOfBounds_throws() {
        TaskList list = new TaskList();
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(0));
    }
}
