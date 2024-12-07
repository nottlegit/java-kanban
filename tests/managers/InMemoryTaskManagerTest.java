package managers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;
import util.Managers;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    TaskManager manager;
    Task task1;
    Task task2;
    Epic epic1;
    Epic epic2;
    Subtask subtask1;
    Subtask subtask2;

    @BeforeEach
    public void beforeEach() {
        manager = Managers.getDefault();
        task1 = new Task("task1", "Описание..", Status.DONE);
        task2 = new Task("task2", "Описание..", Status.NEW);
        epic1 = new Epic(task1);
        epic2 = new Epic(task2);
        subtask1 = new Subtask("subtask1", "Описание..", Status.NEW, epic1.getId());
        subtask2 = new Subtask("subtask2", "Описание..", Status.NEW, epic2.getId());
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
        manager.add(epic1);
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

        assertEquals(10, manager.getHistory().size());
    }
}