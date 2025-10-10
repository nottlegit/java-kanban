package tasks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
class EpicTest {
    @Test
    void testEquals() {
        Epic epic1 = new Epic("Ремонт", "Описание..", Status.DONE);
        Epic epic2 = new Epic("Ремонт", "Описание..", Status.DONE);

        assertEquals(epic1, epic2, "Задачи не совпадают.");

        epic1.setId(2);
        assertNotEquals(epic1, epic2, "Задачи совпадают");
    }
}