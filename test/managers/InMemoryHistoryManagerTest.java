package managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import tasks.Status;
import tasks.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    private InMemoryHistoryManager historyManager;
    private Task task1;
    private Task task2;
    private Task task3;

    @BeforeEach
    void setUp() {
        historyManager = new InMemoryHistoryManager();
        // Создаем тестовые задачи
        task1 = createTask(1,"Задача 1", Status.DONE);
        task2 = createTask(2,"Задача 2", Status.NEW);
        task3 = createTask(3,"Задача 3", Status.IN_PROGRESS);
    }

    // Вспомогательный метод для создания задач
    private Task createTask(int id, String title, Status status) {
        Task task = new Task(title, "Описание..", status);
        task.setId(id);
        return task;
    }

    // ========== ТЕСТЫ ДЛЯ МЕТОДА ADD ==========

    @Test
    @DisplayName("Добавление первой задачи в пустую историю")
    void addFirstTask() {
        historyManager.add(task1);

        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size());
        assertEquals(task1, history.get(0));
    }

    @Test
    @DisplayName("Добавление нескольких задач в правильном порядке")
    void addMultipleTasks() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        List<Task> history = historyManager.getHistory();
        assertEquals(3, history.size());
        assertEquals(task1, history.get(0));
        assertEquals(task2, history.get(1));
        assertEquals(task3, history.get(2));
    }

    @Test
    @DisplayName("Добавление null задачи - ничего не происходит")
    void addNullTask() {
        historyManager.add(task1);
        historyManager.add(null);

        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size());
        assertEquals(task1, history.get(0));
    }

    @Test
    @DisplayName("Добавление дубликата из начала(head) - задача перемещается в конец")
    void addDuplicateFromHeadTask() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        // Добавляем task1 снова - он должен переместиться в конец
        historyManager.add(task1);

        List<Task> history = historyManager.getHistory();
        assertEquals(3, history.size());
        assertEquals(task2, history.get(0));
        assertEquals(task3, history.get(1));
        assertEquals(task1, history.get(2));
    }

    @Test
    @DisplayName("Добавление дубликата tail - задача перезаписывается в конец")
    void addDuplicateFromTailTask() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        // Добавляем task3 снова - он должен перезаписаться в конец
        historyManager.add(task3);

        List<Task> history = historyManager.getHistory();
        assertEquals(3, history.size());
        assertEquals(task1, history.get(0));
        assertEquals(task2, history.get(1));
        assertEquals(task3, history.get(2));
    }

    @Test
    @DisplayName("Добавление дубликата - tail становится в середину")
    void addDuplicateFromMiddleTask() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        // Добавляем task2 снова - он должен переместиться в конец, а task3 встать в середину
        historyManager.add(task2);

        List<Task> history = historyManager.getHistory();
        assertEquals(3, history.size());
        assertEquals(task1, history.get(0));
        assertEquals(task3, history.get(1));
        assertEquals(task2, history.get(2));
    }

    @Test
    @DisplayName("Добавление дубликата когда только одна задача")
    void addDuplicateWhenOnlyOneTask() {
        historyManager.add(task1);
        historyManager.add(task1);

        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size());
        assertEquals(task1, history.get(0));
    }

    // ========== ТЕСТЫ ДЛЯ МЕТОДА REMOVE ==========

    @Test
    @DisplayName("Удаление единственной задачи")
    void removeSingleTask() {
        historyManager.add(task1);
        historyManager.remove(task1);

        List<Task> history = historyManager.getHistory();
        assertTrue(history.isEmpty());
    }

    @Test
    @DisplayName("Удаление первой задачи из нескольких")
    void removeFirstTask() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove(task1);

        List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size());
        assertEquals(task2, history.get(0));
        assertEquals(task3, history.get(1));
    }

    @Test
    @DisplayName("Удаление последней задачи из нескольких")
    void removeLastTask() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove(task3);

        List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size());
        assertEquals(task1, history.get(0));
        assertEquals(task2, history.get(1));
    }

    @Test
    @DisplayName("Удаление средней задачи")
    void removeMiddleTask() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove(task2);

        List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size());
        assertEquals(task1, history.get(0));
        assertEquals(task3, history.get(1));
    }

    @Test
    @DisplayName("Удаление null задачи - ничего не происходит")
    void removeNullTask() {
        historyManager.add(task1);
        historyManager.add(task2);

        historyManager.remove(null);

        List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size());
    }

    @Test
    @DisplayName("Удаление несуществующей задачи")
    void removeNonExistentTask() {
        historyManager.add(task1);
        historyManager.add(task2);

        historyManager.remove(task3);

        List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size());
        assertEquals(task1, history.get(0));
        assertEquals(task2, history.get(1));
    }

    @Test
    @DisplayName("Удаление из пустой истории")
    void removeFromEmptyHistory() {
        historyManager.remove(task1);

        List<Task> history = historyManager.getHistory();
        assertTrue(history.isEmpty());
    }

    // ========== КОМПЛЕКСНЫЕ ТЕСТЫ ==========

    @Test
    @DisplayName("Сложный сценарий: добавление, удаление, повторное добавление")
    void complexScenario() {
        // Добавляем задачи
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        // Удаляем среднюю
        historyManager.remove(task2);

        // Добавляем новую задачу
        Task task4 = createTask(4,"Задача 4", Status.DONE);
        historyManager.add(task4);

        // Добавляем снова удаленную задачу
        historyManager.add(task2);

        List<Task> history = historyManager.getHistory();
        assertEquals(4, history.size());
        assertEquals(task1, history.get(0));
        assertEquals(task3, history.get(1));
        assertEquals(task4, history.get(2));
        assertEquals(task2, history.get(3));
    }

    @Test
    @DisplayName("Пустая история остается пустой")
    void emptyHistoryStaysEmpty() {
        List<Task> history = historyManager.getHistory();
        assertTrue(history.isEmpty());
        assertEquals(0, history.size());
    }
}