package managers;

import managers.exceptions.HasTimeOverlapWithAnyException;
import managers.exceptions.NotFoundException;
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
    void TestAddAndGetTask() {
        List<Task> tasks = manager.getListTasks();
        List<Epic> epics = manager.getListEpics();
        List<Subtask> subtasks = manager.getListSubtasks();

        assertTrue(tasks.isEmpty(), "tasks должен быть пустым");
        assertTrue(epics.isEmpty(), "epics должен быть пустым");
        assertTrue(subtasks.isEmpty(), "subtasks должен быть пустым");

        Task task = new Task("task", "description",
                Status.NEW, Duration.ofMinutes(120), localDateTime.minus(Duration.ofDays(1)));
        Epic epic = new Epic("epic", "description", Status.NEW);

        manager.add(task);
        manager.add(epic);

        Subtask subtask = new Subtask("subtask", "description",
                Status.NEW, epic.getId(), Duration.ofMinutes(120), localDateTime.minus(Duration.ofDays(2)));

        manager.add(subtask);

        Task getedTask = manager.getTaskById(task.getId());
        Epic getedEpic = manager.getEpicById(epic.getId());
        Subtask getedSubtask = manager.getSubtaskById(subtask.getId());

        assertEquals(task, getedTask, "Tasks не совпадают");
        assertEquals(epic, getedEpic, "Epics не совпадают");
        assertEquals(subtask, getedSubtask, "Subtasks не совпадают");
    }

    @Test
    @DisplayName("Обновление задач")
    void TestUpdateTask() {
        Epic epic = new Epic("epic", "description", Status.NEW);
        Task task = new Task("task", "description",
                Status.NEW, Duration.ofMinutes(120), localDateTime.minus(Duration.ofDays(1)));

        manager.add(task);
        manager.add(epic);

        Subtask subtask = new Subtask("subtask", "description",
                Status.NEW, epic.getId(), Duration.ofMinutes(120), localDateTime.minus(Duration.ofDays(2)));

        manager.add(subtask);

        task.setDescription("Update description");
        epic.setDescription("Update description");
        subtask.setDescription("Update description");

        manager.update(task);
        manager.update(epic);
        manager.update(subtask);

        Task updatedTask = manager.getTaskById(task.getId());
        Epic updatedEpic = manager.getEpicById(epic.getId());
        Subtask updatedSubtask = manager.getSubtaskById(subtask.getId());

        assertEquals("Update description", updatedTask.getDescription());
        assertEquals("Update description", updatedEpic.getDescription());
        assertEquals("Update description", updatedSubtask.getDescription());
    }

    @Test
    @DisplayName("Удаление всех задач")
    void TestDeleteAllTask() {
        Task task = new Task("task", "description",
                Status.NEW, Duration.ofMinutes(120), localDateTime.minus(Duration.ofDays(1)));
        Epic epic = new Epic("epic", "description", Status.NEW);

        manager.add(task);
        manager.add(epic);

        Subtask subtask = new Subtask("subtask", "description",
                Status.NEW, epic.getId(), Duration.ofMinutes(120), localDateTime.minus(Duration.ofDays(2)));

        manager.add(subtask);

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

        epic = new Epic("epic", "description", Status.NEW);

        manager.add(epic);

        subtask = new Subtask("subtask", "description",
                Status.NEW, epic.getId(), Duration.ofMinutes(120), localDateTime.minus(Duration.ofDays(2)));

        manager.add(subtask);
        manager.deleteAllSubtasks();
        manager.deleteAllEpics();
        assertTrue(manager.getListSubtasks().isEmpty(), "subtasks должен быть пустым");
    }

    @Test
    @DisplayName("Удаление задач по id")
    void TestDeleteTaskById() {
        Task task = new Task("task", "description",
                Status.NEW, Duration.ofMinutes(120), localDateTime.minus(Duration.ofDays(1)));
        Epic epic = new Epic("epic", "description", Status.NEW);

        manager.add(task);
        manager.add(epic);

        Subtask subtask = new Subtask("subtask", "description",
                Status.NEW, epic.getId(), Duration.ofMinutes(120), localDateTime.minus(Duration.ofDays(2)));

        manager.add(subtask);

        manager.deleteTaskById(task.getId());
        manager.deleteEpicById(epic.getId());

        assertTrue(manager.getListTasks().isEmpty(), "tasks должен быть пустым");
        assertTrue(manager.getListEpics().isEmpty(), "epics должен быть пустым");
        assertTrue(manager.getListSubtasks().isEmpty(), "subtasks должен быть пустым");

        Epic epic2 = new Epic("epic", "description", Status.NEW);

        manager.add(epic2);

        Subtask subtask2 = new Subtask("subtask", "description",
                Status.NEW, epic2.getId(), Duration.ofMinutes(120), localDateTime.minus(Duration.ofDays(2)));

        manager.add(subtask2);

        manager.deleteSubtaskById(subtask2.getId());

        assertTrue(manager.getListSubtasks().isEmpty(), "subtasks должен быть пустым");
    }

    @Test
    @DisplayName("Проверка статуса Epic(все subtask Status.NEW)")
    void TestEpicStatusAllNew(){
        Epic epic = new Epic("epic", "description", Status.NEW);

        manager.add(epic);

        Subtask subtask = new Subtask("subtask", "description",
                Status.NEW, epic.getId(), Duration.ofMinutes(120), localDateTime.minus(Duration.ofDays(2)));
        Subtask subtask2 = new Subtask("subtask2", "description2",
                Status.NEW, epic.getId(), Duration.ofMinutes(120), localDateTime.minus(Duration.ofDays(1)));

        manager.add(subtask);
        manager.add(subtask2);

        assertEquals(Status.NEW, epic.getStatus(), "Статус Эпика должен быть NEW");
    }

    @Test
    @DisplayName("Проверка статуса Epic(все subtask Status.Done)")
    void TestEpicStatusAllDone() {
        Epic epic = new Epic("epic", "description", Status.NEW);

        manager.add(epic);

        Subtask subtask = new Subtask("subtask", "description",
                Status.DONE, epic.getId(), Duration.ofMinutes(120), localDateTime.minus(Duration.ofDays(2)));
        Subtask subtask2 = new Subtask("subtask2", "description2",
                Status.DONE, epic.getId(), Duration.ofMinutes(120), localDateTime.minus(Duration.ofDays(1)));

        manager.add(subtask);
        manager.add(subtask2);

        assertEquals(Status.DONE, epic.getStatus(), "Статус Эпика должен быть DONE");
    }

    @Test
    @DisplayName("Проверка статуса Epic(subtask Status.DONE, Status.NEW)")
    void TestEpicStatusDoneAndNew() {
        Epic epic = new Epic("epic", "description", Status.NEW);

        manager.add(epic);

        Subtask subtask = new Subtask("subtask", "description",
                Status.DONE, epic.getId(), Duration.ofMinutes(120), localDateTime.minus(Duration.ofDays(2)));
        Subtask subtask2 = new Subtask("subtask2", "description2",
                Status.NEW, epic.getId(), Duration.ofMinutes(120), localDateTime.minus(Duration.ofDays(1)));

        manager.add(subtask);
        manager.add(subtask2);

        assertEquals(Status.IN_PROGRESS, epic.getStatus(), "Статус Эпика должен быть IN_PROGRESS");
    }

    @Test
    @DisplayName("Проверка статуса Epic(все subtask Status.IN_PROGRESS)")
    void TestEpicStatusAllInProgress() {
        Epic epic = new Epic("epic", "description", Status.NEW);

        manager.add(epic);

        Subtask subtask = new Subtask("subtask", "description",
                Status.IN_PROGRESS, epic.getId(), Duration.ofMinutes(120), localDateTime.minus(Duration.ofDays(2)));
        Subtask subtask2 = new Subtask("subtask2", "description2",
                Status.IN_PROGRESS, epic.getId(), Duration.ofMinutes(120), localDateTime.minus(Duration.ofDays(1)));

        manager.add(subtask);
        manager.add(subtask2);

        assertEquals(Status.IN_PROGRESS, epic.getStatus(), "Статус Эпика должен быть IN_PROGRESS");
    }

    @Test
    @DisplayName("Получение subtasks из epic")
    void TestGetSubtasksInEpic() {
        Epic epic = new Epic("epic", "description", Status.NEW);

        manager.add(epic);

        Subtask subtask = new Subtask("subtask", "description",
                Status.IN_PROGRESS, epic.getId(), Duration.ofMinutes(120), localDateTime.minus(Duration.ofDays(2)));
        Subtask subtask2 = new Subtask("subtask2", "description2",
                Status.IN_PROGRESS, epic.getId(), Duration.ofMinutes(120), localDateTime.minus(Duration.ofDays(1)));

        manager.add(subtask);
        manager.add(subtask2);

        List<Subtask> epicSubtasks = manager.getListEpicSubtasksById(epic.getId());
        assertEquals(2, epicSubtasks.size(), "Должны быть возвращены все подзадачи эпика");
    }

    @Test
    @DisplayName("Добавление задач с пересечениями")
    void TestTimeOverlapDetection() {
        Task task = new Task("task", "description",
                Status.NEW, Duration.ofMinutes(120), localDateTime.minus(Duration.ofDays(1)));
        Task task2 = new Task( "task", "description",
                Status.NEW, Duration.ofMinutes(120), localDateTime.minus(Duration.ofDays(1)));

        manager.add(task);
        try {
            manager.add(task2);
        } catch (HasTimeOverlapWithAnyException e) {
            System.out.println("Задача не добавлена, есть пересечение");
        }

        List<Task> tasks = manager.getListTasks();
        assertEquals(1, tasks.size(),
                "Не должна добавляться задача с пересекающимся временем");
    }

    @Test
    @DisplayName("Добавление задачи без пересечений")
    void noTimeOverlapTest() {
        Task task = new Task("task", "description",
                Status.NEW, Duration.ofMinutes(120), localDateTime.minus(Duration.ofDays(1)));
        Task task2 = new Task( "task", "description",
                Status.NEW, Duration.ofMinutes(120), localDateTime.minus(Duration.ofDays(2)));

        manager.add(task);
        manager.add(task2);

        List<Task> tasks = manager.getListTasks();
        assertEquals(2, tasks.size(),
                "Обе задачи должны добавиться (нет пересечения)");
    }

    @Test
    @DisplayName("Добавление задачи без времени")
    void testTasksWithoutTime() {
        Task task = new Task("task", "description",
                Status.NEW, Duration.ofMinutes(120), localDateTime.minus(Duration.ofDays(1)));
        Task task2 = new Task( "task", "description",
                Status.NEW, null, null);

        manager.add(task);
        manager.add(task2);

        List<Task> tasks = manager.getListTasks();
        assertEquals(2, tasks.size(),
                "Задачи без времени должны добавляться без проверки пересечений");
    }
}