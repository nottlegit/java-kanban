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
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {

    private FileBackedTaskManager manager;
    private File file;
    private Duration duration;
    private LocalDateTime localDateTime;

    @BeforeEach
    void setUp() throws IOException {
        file = new File("manager_status.csv");
        manager = new FileBackedTaskManager();
        duration = Duration.ofMinutes(10);
        localDateTime = LocalDateTime.now();
    }

    @AfterEach
    void tearDown() {
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    @DisplayName("Добавление задачь в менеджер")
    void testSaveTasks() {
        Task task = new Task("Test Task", "Description", Status.NEW,
                duration, localDateTime.minus(Duration.ofDays(100)));
        Epic epic = new Epic("Test Epic", "Epic Description", Status.NEW);

        manager.add(task);
        manager.add(epic);
        Subtask subtask = new Subtask("Test Subtask", "Subtask Description", Status.NEW, epic.getId(),
                duration, localDateTime.minus(Duration.ofDays(95)));

        manager.add(subtask);

        assertTrue(file.exists());
        assertTrue(file.length() > 0);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String header = reader.readLine();
            assertEquals("id,type,name,status,description,epic", header);

            // Проверяем количество строк (заголовок + 3 задачи)
            byte lineCount = (byte) reader.lines().count();
            assertEquals(3, lineCount);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Проверка состояния менеджера при загрузки из файла")
    void testLoadFromFile() throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("id,type,name,status,description,epic\n");
            writer.write("1,TASK,Task1,NEW,Task Description,10,2025-10-10T15:17:48.698796800,\n");
            writer.write("2,EPIC,Epic2,IN_PROGRESS,Epic Description,10,2025-10-10T15:17:48.698796800,\n");
            writer.write("3,SUBTASK,Subtask3,DONE,Subtask Description,10,2025-10-10T15:17:48.698796800,2,\n");
        }

        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(file);

        // Проверяем загруженные задачи
        assertNotNull(loadedManager.getTaskById(1));
        assertNotNull(loadedManager.getEpicById(2));
        assertNotNull(loadedManager.getSubtaskById(3));

        assertEquals("Task Description", loadedManager.getTaskById(1).getDescription());
        assertEquals(Status.NEW, loadedManager.getTaskById(1).getStatus());

        // Проверяем связь подзадачи с эпиком
        Subtask subtask = loadedManager.getSubtaskById(3);
        assertEquals(2, subtask.getIdEpic());
        assertTrue(loadedManager.getEpicById(2).getListSubtasks().contains(3));
    }
}