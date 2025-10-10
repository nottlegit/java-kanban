package managers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;
import util.Managers;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    private TaskManager manager;
    private Task task1;
    private Task task2;
    private Epic epic1;
    private Epic epic2;
    private Subtask subtask1;
    private Subtask subtask2;
    private Duration duration;
    private LocalDateTime localDateTime;


    @BeforeEach
    public void beforeEach() {
        duration = Duration.ofMinutes(10);
        localDateTime = LocalDateTime.now();
        manager = Managers.getDefault();
        task1 = new Task("task1", "Описание..", Status.DONE,
                duration, localDateTime);
        task2 = new Task("task2", "Описание..", Status.NEW,
                duration, localDateTime);
        epic1 = new Epic(task1);
        epic2 = new Epic(task2);
        subtask1 = new Subtask("subtask1", "Описание..", Status.NEW, epic1.getId(),
                duration, localDateTime);
        subtask2 = new Subtask("subtask2", "Описание..", Status.NEW, epic2.getId(),
                duration, localDateTime);
    }

    @Test
    void checkingTheImmutabilityOfTheTask() {
        int idBeforeAdding = task1.getId();
        Status statusBeforeAdding = task1.getStatus();
        String descriptionBeforeAdding = task1.getDescription();
        String titleBeforeAdding = task1.getTitle();

        manager.add(task1);

        assertEquals(idBeforeAdding, task1.getId());
        assertEquals(statusBeforeAdding, task1.getStatus());
        assertEquals(descriptionBeforeAdding, task1.getDescription());
        assertEquals(titleBeforeAdding, task1.getTitle());
    }

    @Test
    void addAnEpicAsASubtask() {
        manager.add(epic1);
        assertTrue(manager.getListSubtasks().isEmpty());
        assertFalse(manager.getListEpics().isEmpty());
    }

    @Test
    void checkingTheWorkGetHistory() {
        manager.add(task1);
        manager.add(task2);
        manager.add(epic1);
        manager.add(epic2);
        subtask1 = new Subtask("subtask1", "Описание..", Status.NEW, epic1.getId(),
                duration, localDateTime);
        subtask2 = new Subtask("subtask2", "Описание..", Status.NEW, epic2.getId(),
                duration, localDateTime);
        manager.add(subtask1);
        manager.add(subtask2);

        manager.getTaskById(task1.getId());
        manager.getTaskById(task2.getId());

        assertEquals(2, manager.getHistory().size());

        manager.getEpicById(epic1.getId());
        manager.getEpicById(epic2.getId());

        assertEquals(4, manager.getHistory().size());

        manager.getSubtaskById(subtask1.getId());
        manager.getSubtaskById(subtask2.getId());
        manager.getTaskById(task1.getId());
        manager.getTaskById(task2.getId());
        manager.getEpicById(epic1.getId());
        manager.getEpicById(epic2.getId());
        manager.getEpicById(epic1.getId());
        manager.getEpicById(epic2.getId());

        System.out.println(manager.getHistory());
        assertEquals(6, manager.getHistory().size());
    }
}