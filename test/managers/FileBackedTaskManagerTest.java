package managers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.io.*;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    private FileBackedTaskManager manager;
    private File file;

    @BeforeEach
    void setUp() throws IOException {
        file = new File("manager_status.csv");
        manager = new FileBackedTaskManager();
    }

    @AfterEach
    void tearDown() {
        if (file.exists()) {
            file.delete();
        }
    }

    @Override
    protected FileBackedTaskManager createTaskManager() {
        return new FileBackedTaskManager();
    }

    @Test
    @DisplayName("Добавление задач в менеджер")
    void testSaveTasks() {
        Task task = new Task("task", "description",
                Status.NEW, Duration.ofMinutes(120), localDateTime.minus(Duration.ofDays(1)));
        Epic epic = new Epic("Test Epic", "Epic Description", Status.NEW);

        manager.add(task);
        manager.add(epic);
        Subtask subtask =new Subtask("subtask", "description",
                Status.NEW, epic.getId(), Duration.ofMinutes(120), localDateTime.minus(Duration.ofDays(2)));

        manager.add(subtask);

        assertTrue(file.exists());
        assertTrue(file.length() > 0);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String header = reader.readLine();
            assertEquals("id,type,name,status,description,duration, startTime, epic", header);

            // Проверяем количество строк (заголовок + 3 задачи)
            byte lineCount = (byte) reader.lines().count();
            assertEquals(3, lineCount);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @org.junit.jupiter.api.Test
    @DisplayName("Проверка состояния менеджера при загрузки из файла")
    void testLoadFromFile() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("id,type,name,status,description,epic\n");
            writer.write("1,TASK,Task1,NEW,Описание..,50,2025-08-24T18:10:10.676932200,\n");
            writer.write("2,EPIC,Epic2,NEW,Описание..,65,2025-08-29T18:10:10.676932200,\n");
            writer.write("3,SUBTASK,Subtask3,NEW,Описание..,17,2025-09-03T18:10:10.676932200,2\n");
        }

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(file);

        // Проверяем загруженные задачи
        assertNotNull(loadedManager.getTaskById(1));
        assertNotNull(loadedManager.getEpicById(2));
        assertNotNull(loadedManager.getSubtaskById(3));

        assertEquals("Описание..", loadedManager.getTaskById(1).getDescription());
        assertEquals(Status.NEW, loadedManager.getTaskById(1).getStatus());

        // Проверяем связь подзадачи с эпиком
        Subtask subtask = loadedManager.getSubtaskById(3);
        assertEquals(2, subtask.getIdEpic());
        assertTrue(loadedManager.getEpicById(2).getListSubtasks().contains(3));
    }
}