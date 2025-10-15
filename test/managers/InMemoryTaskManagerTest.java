package managers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends TaskManagerTest<TaskManager> {
    @Override
    protected InMemoryTaskManager createTaskManager() {
        return new InMemoryTaskManager();
    }

    @Test
    @DisplayName("Сортировка списка задач по времени начала")
    void prioritizedTasksOrderTest() {
        Task task1 = new Task("task1", "description1",
                Status.NEW, Duration.ofMinutes(120), localDateTime.minus(Duration.ofDays(1)));
        Task task2 = new Task("task2", "description2",
                Status.NEW, Duration.ofMinutes(120), localDateTime.minus(Duration.ofDays(3)));
        Epic epic = new Epic("epic", "description", Status.NEW);

        manager.add(task1);
        manager.add(task2);
        manager.add(epic);

        Subtask subtask = new Subtask("subtask", "description",
                Status.NEW, epic.getId(), Duration.ofMinutes(120), localDateTime.minus(Duration.ofDays(2)));

        manager.add(subtask);

        List<Task> prioritized = manager.getPrioritizedTasks();

        assertEquals(task2.getId(), prioritized.getFirst().getId(),
                "Задачи должны быть отсортированы по времени начала");
        assertEquals(task1.getId(), prioritized.getLast().getId(),
                "Задачи должны быть отсортированы по времени начала");
    }
}