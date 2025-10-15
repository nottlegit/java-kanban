package tasks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {
    private Duration duration;
    private LocalDateTime localDateTime;

    @BeforeEach
    void setUp() {
        duration = Duration.ofMinutes(10);
        localDateTime = LocalDateTime.now();
    }

    @Test
    void testEquals() {
        Subtask subtask1 = new Subtask("Купить материалы", "Описание..", Status.NEW, 1,
                duration, localDateTime);
        Subtask subtask2 = new Subtask("Нанять строителей", "Описание..", Status.NEW, 2,
                duration, localDateTime);

        assertEquals(subtask1, subtask2, "Задачи не совпадают.");

        subtask1.setId(2);
        assertNotEquals(subtask1, subtask2, "Задачи совпадают");
    }
}