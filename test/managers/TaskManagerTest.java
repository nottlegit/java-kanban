package managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.time.Duration;
import java.time.LocalDateTime;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {
    protected LocalDateTime localDateTime;
    protected T manager;

    protected abstract T createTaskManager();

    @BeforeEach
    public void beforeEach() {
        manager = createTaskManager();
        localDateTime = LocalDateTime.now();
    }

    @Test
    @DisplayName("Добавление и получение задачи")
    void addAndGetTaskTest() {
        List<Task> tasks = manager.getListTasks();
        List<Epic> epics = manager.getListEpics();
        List<Subtask> subtasks = manager.getListSubtasks();

        assertTrue(tasks.isEmpty(), "tasks должен быть пустым");
        assertTrue(epics.isEmpty(), "epics должен быть пустым");
        assertTrue(subtasks.isEmpty(), "subtasks должен быть пустым");

        Task task1 = new Task("task1", "description1",
                Status.NEW, Duration.ofMinutes(120), localDateTime.minus(Duration.ofDays(1)));
        Epic epic1 = new Epic("epic1", "description1", Status.NEW);

        manager.add(task1);
        manager.add(epic1);

        Subtask subtask1 = new Subtask("subtask1", "description1",
                Status.NEW, epic1.getId(), Duration.ofMinutes(120), localDateTime.minus(Duration.ofDays(2)));

        manager.add(subtask1);

        Task getedTask = manager.getTaskById(task1.getId());
        Epic getedEpic = manager.getEpicById(epic1.getId());
        Subtask getedSubtask = manager.getSubtaskById(subtask1.getId());

        assertEquals(task1, getedTask, "Tasks не совпадают");
        assertEquals(epic1, getedEpic, "Epics не совпадают");
        assertEquals(subtask1, getedSubtask, "Subtasks не совпадают");
    }

    @Test
    @DisplayName("Обновление задач")
    void updateTaskTest() {
        Epic epic1 = new Epic("epic1", "description1", Status.NEW);
        Task task1 = new Task("task1", "description1",
                Status.NEW, Duration.ofMinutes(120), localDateTime.minus(Duration.ofDays(1)));

        manager.add(task1);
        manager.add(epic1);

        Subtask subtask1 = new Subtask("subtask1", "description1",
                Status.NEW, epic1.getId(), Duration.ofMinutes(120), localDateTime.minus(Duration.ofDays(2)));

        manager.add(subtask1);

        task1.setDescription("Update description");
        epic1.setDescription("Update description");
        subtask1.setDescription("Update description");

        manager.update(task1);
        manager.update(epic1);
        manager.update(subtask1);

        Task updatedTask = manager.getTaskById(task1.getId());
        Epic updatedEpic = manager.getEpicById(epic1.getId());
        Subtask updatedSubtask = manager.getSubtaskById(subtask1.getId());

        assertEquals("Update description", updatedTask.getDescription());
        assertEquals("Update description", updatedEpic.getDescription());
        assertEquals("Update description", updatedSubtask.getDescription());
    }

    @Test
    @DisplayName("Удаление всех задач")
    void deleteAllTaskTest() {
        Task task1 = new Task("task1", "description1",
                Status.NEW, Duration.ofMinutes(120), localDateTime.minus(Duration.ofDays(1)));
        Epic epic1 = new Epic("epic1", "description1", Status.NEW);

        manager.add(task1);
        manager.add(epic1);

        Subtask subtask1 = new Subtask("subtask1", "description1",
                Status.NEW, epic1.getId(), Duration.ofMinutes(120), localDateTime.minus(Duration.ofDays(2)));

        manager.add(subtask1);

        assertFalse(manager.getListTasks().isEmpty(), "tasks не должен быть пустым");
        assertFalse(manager.getListEpics().isEmpty(), "epics не должен быть пустым");
        assertFalse(manager.getListSubtasks().isEmpty(), "subtasks не должен быть пустым");
        // deleteAll
        manager.deleteAllTasks();
        assertTrue(manager.getListTasks().isEmpty(), "tasks должен быть пустым");
        // Subtask не существует без Epic
        manager.deleteAllEpics();
        assertTrue(manager.getListEpics().isEmpty(), "epics должен быть пустым");
        assertTrue(manager.getListSubtasks().isEmpty(), "subtasks должен быть пустым");

        epic1 = new Epic("epic1", "description1", Status.NEW);

        manager.add(epic1);

        subtask1 = new Subtask("subtask1", "description1",
                Status.NEW, epic1.getId(), Duration.ofMinutes(120), localDateTime.minus(Duration.ofDays(2)));

        manager.add(subtask1);
        manager.deleteAllSubtasks();
        manager.deleteAllEpics();
        assertTrue(manager.getListSubtasks().isEmpty(), "subtasks должен быть пустым");
    }

    @Test
    @DisplayName("Удаление задач по id")
    void deleteTaskByIdTest() {
        Task task1 = new Task("task1", "description1",
                Status.NEW, Duration.ofMinutes(120), localDateTime.minus(Duration.ofDays(1)));
        Epic epic1 = new Epic("epic1", "description1", Status.NEW);

        manager.add(task1);
        manager.add(epic1);

        Subtask subtask1 = new Subtask("subtask1", "description1",
                Status.NEW, epic1.getId(), Duration.ofMinutes(120), localDateTime.minus(Duration.ofDays(2)));

        manager.add(subtask1);

        manager.deleteTaskById(task1.getId());
        manager.deleteEpicById(epic1.getId());

        assertTrue(manager.getListTasks().isEmpty(), "tasks должен быть пустым");
        assertTrue(manager.getListEpics().isEmpty(), "epics должен быть пустым");
        assertTrue(manager.getListSubtasks().isEmpty(), "subtasks должен быть пустым");

        Epic epic2 = new Epic("epic1", "description1", Status.NEW);

        manager.add(epic2);

        Subtask subtask2 = new Subtask("subtask1", "description1",
                Status.NEW, epic2.getId(), Duration.ofMinutes(120), localDateTime.minus(Duration.ofDays(2)));

        manager.add(subtask2);

        manager.deleteSubtaskById(subtask2.getId());

        assertTrue(manager.getListSubtasks().isEmpty(), "subtasks должен быть пустым");
    }

    @Test
    @DisplayName("Проверка статуса Epic(все subtask Status.NEW)")
    void epicStatusAllNew(){
        Epic epic1 = new Epic("epic1", "description1", Status.NEW);

        manager.add(epic1);

        Subtask subtask1 = new Subtask("subtask1", "description1",
                Status.NEW, epic1.getId(), Duration.ofMinutes(120), localDateTime.minus(Duration.ofDays(2)));
        Subtask subtask2 = new Subtask("subtask2", "description2",
                Status.NEW, epic1.getId(), Duration.ofMinutes(120), localDateTime.minus(Duration.ofDays(1)));

        manager.add(subtask1);
        manager.add(subtask2);

        assertEquals(Status.NEW, epic1.getStatus(), "Статус Эпика должен быть NEW");
    }

    @Test
    @DisplayName("Проверка статуса Epic(все subtask Status.Done)")
    void epicStatusAllDone() {
        Epic epic1 = new Epic("epic1", "description1", Status.NEW);

        manager.add(epic1);

        Subtask subtask1 = new Subtask("subtask1", "description1",
                Status.DONE, epic1.getId(), Duration.ofMinutes(120), localDateTime.minus(Duration.ofDays(2)));
        Subtask subtask2 = new Subtask("subtask2", "description2",
                Status.DONE, epic1.getId(), Duration.ofMinutes(120), localDateTime.minus(Duration.ofDays(1)));

        manager.add(subtask1);
        manager.add(subtask2);

        assertEquals(Status.DONE, epic1.getStatus(), "Статус Эпика должен быть DONE");
    }

    @Test
    @DisplayName("Проверка статуса Epic(subtask Status.DONE, Status.NEW)")
    void epicStatusDoneAndNew() {
        Epic epic1 = new Epic("epic1", "description1", Status.NEW);

        manager.add(epic1);

        Subtask subtask1 = new Subtask("subtask1", "description1",
                Status.DONE, epic1.getId(), Duration.ofMinutes(120), localDateTime.minus(Duration.ofDays(2)));
        Subtask subtask2 = new Subtask("subtask2", "description2",
                Status.NEW, epic1.getId(), Duration.ofMinutes(120), localDateTime.minus(Duration.ofDays(1)));

        manager.add(subtask1);
        manager.add(subtask2);

        assertEquals(Status.IN_PROGRESS, epic1.getStatus(), "Статус Эпика должен быть IN_PROGRESS");
    }

    @Test
    @DisplayName("Проверка статуса Epic(все subtask Status.IN_PROGRESS)")
    void epicStatusAllInProgress() {
        Epic epic1 = new Epic("epic1", "description1", Status.NEW);

        manager.add(epic1);

        Subtask subtask1 = new Subtask("subtask1", "description1",
                Status.IN_PROGRESS, epic1.getId(), Duration.ofMinutes(120), localDateTime.minus(Duration.ofDays(2)));
        Subtask subtask2 = new Subtask("subtask2", "description2",
                Status.IN_PROGRESS, epic1.getId(), Duration.ofMinutes(120), localDateTime.minus(Duration.ofDays(1)));

        manager.add(subtask1);
        manager.add(subtask2);

        assertEquals(Status.IN_PROGRESS, epic1.getStatus(), "Статус Эпика должен быть IN_PROGRESS");
    }

    @Test
    @DisplayName("Получение subtasks из epic")
    void getSubtasksInEpicTest() {
        Epic epic1 = new Epic("epic1", "description1", Status.NEW);

        manager.add(epic1);

        Subtask subtask1 = new Subtask("subtask1", "description1",
                Status.IN_PROGRESS, epic1.getId(), Duration.ofMinutes(120), localDateTime.minus(Duration.ofDays(2)));
        Subtask subtask2 = new Subtask("subtask2", "description2",
                Status.IN_PROGRESS, epic1.getId(), Duration.ofMinutes(120), localDateTime.minus(Duration.ofDays(1)));

        manager.add(subtask1);
        manager.add(subtask2);

        List<Subtask> epicSubtasks = manager.getListEpicSubtasksById(epic1.getId());
        assertEquals(2, epicSubtasks.size(), "Должны быть возвращены все подзадачи эпика");
    }

    @Test
    @DisplayName("Добавление задач с пересечениями")
    void timeOverlapDetectionTest() {
        Task task1 = new Task("task1", "description1",
                Status.NEW, Duration.ofMinutes(120), localDateTime.minus(Duration.ofDays(1)));
        Task task2 = new Task( "task1", "description1",
                Status.NEW, Duration.ofMinutes(120), localDateTime.minus(Duration.ofDays(1)));

        manager.add(task1);
        manager.add(task2);

        List<Task> tasks = manager.getListTasks();
        assertEquals(1, tasks.size(),
                "Не должна добавляться задача с пересекающимся временем");
    }

    @Test
    @DisplayName("Добавление задачи без пересечений")
    void noTimeOverlapTest() {
        Task task1 = new Task("task1", "description1",
                Status.NEW, Duration.ofMinutes(120), localDateTime.minus(Duration.ofDays(1)));
        Task task2 = new Task( "task1", "description1",
                Status.NEW, Duration.ofMinutes(120), localDateTime.minus(Duration.ofDays(2)));

        manager.add(task1);
        manager.add(task2);

        List<Task> tasks = manager.getListTasks();
        assertEquals(2, tasks.size(),
                "Обе задачи должны добавиться (нет пересечения)");
    }

    @Test
    @DisplayName("Добавление задачи без времени")
    void testTasksWithoutTime() {
        Task task1 = new Task("task1", "description1",
                Status.NEW, Duration.ofMinutes(120), localDateTime.minus(Duration.ofDays(1)));
        Task task2 = new Task( "task1", "description1",
                Status.NEW, null, null);

        manager.add(task1);
        manager.add(task2);

        List<Task> tasks = manager.getListTasks();
        assertEquals(2, tasks.size(),
                "Задачи без времени должны добавляться без проверки пересечений");
    }
}