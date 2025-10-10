package tasks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
class EpicTest {
    private Duration duration;
    private LocalDateTime localDateTime;

    @BeforeEach
    void setUp() {
        duration = Duration.ofMinutes(10);
        localDateTime = LocalDateTime.now();
    }

    @Test
    void testEquals() {
        Epic epic1 = new Epic("Ремонт", "Описание..", Status.DONE,
                duration, localDateTime);
        Epic epic2 = new Epic("Ремонт", "Описание..", Status.DONE,
                duration, localDateTime);

        assertEquals(epic1, epic2, "Задачи не совпадают.");

        epic1.setId(2);
        assertNotEquals(epic1, epic2, "Задачи совпадают");
    }
}