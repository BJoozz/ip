package jack.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TaskListTest {

    @Test
    @DisplayName("add() increases size and stores the correct task")
    void add_increasesSizeAndStoresTask() {
        TaskList list = new TaskList(new ArrayList<>());
        Deadline d = new Deadline("read SE guide", LocalDate.of(2025, 9, 20));

        int before = list.size();
        list.add(d);

        assertEquals(before + 1, list.size(), "Size should increase by 1");
        assertEquals(d, list.get(list.size() - 1), "Last element should be the added task");
    }

    @Test
    @DisplayName("remove(index) removes the right element and checks bounds")
    void remove_removesAndChecksBounds() {
        TaskList list = new TaskList(new ArrayList<>());
        list.add(new Todo("t1"));
        list.add(new Todo("t2"));
        list.add(new Todo("t3"));

        Task removed = list.remove(1); // remove middle element
        assertTrue(removed.toString().contains("t2"), "Removed task should contain 't2'");
        assertEquals(2, list.size(), "Size should decrease by 1");

        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(42));
    }
}
