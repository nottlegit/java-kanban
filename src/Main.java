import api.HttpTaskServer;
import managers.InMemoryTaskManager;
import managers.TaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.Task;
import util.Managers;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) throws IOException {
        TaskManager manager = Managers.getDefault();
        Epic epic1 = new Epic("Test Epic № 1", "Test Description", Status.NEW);
        Epic epic2 = new Epic("Test Epic № 1", "Test Description", Status.NEW);
        Task task1 = new Task("Test Task № 1", "Test Description № 1",
                Status.NEW, Duration.ofMinutes(120), LocalDateTime.now().minus(Duration.ofDays(1)));
        Task task2 = new Task("Test Task № 2", "Test Description № 1",
                Status.NEW, Duration.ofMinutes(120), LocalDateTime.now().minus(Duration.ofDays(2)));
        manager.add(epic1);
        manager.add(epic2);
        manager.add(task1);
        manager.add(task2);
        System.out.println(manager.getListEpics());
        System.out.println(manager.getListTasks());
        HttpTaskServer httpTaskServer = new HttpTaskServer(manager);

        httpTaskServer.startServer();
    }
}