package tasks;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {
    @Test
    void testEquals() {
        Subtask subtask1 = new Subtask("Купить материалы", "Описание..", Status.NEW, 1);
        Subtask subtask2 = new Subtask("Нанять строителей", "Описание..", Status.NEW, 2);

        assertEquals(subtask1, subtask2, "Задачи не совпадают.");

        subtask1.setId(2);
        assertNotEquals(subtask1, subtask2, "Задачи совпадают");
    }
}