package tasks;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    //@org.junit.jupiter.api.Test
    @Test
    void testEquals() {

        Task task1 = new Task("Ремонт", "Описание..", Status.DONE,
                Duration.ofMinutes(10), LocalDateTime.now());
        Task task2 = new Task("Уборка", "Описание..", Status.NEW,
                Duration.ofMinutes(10), LocalDateTime.now());

        assertEquals(task1, task2, "Задачи не совпадают.");

        task1.setId(2);
        assertNotEquals(task1, task2, "Задачи совпадают");
    }
}