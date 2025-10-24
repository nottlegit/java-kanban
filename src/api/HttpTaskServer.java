package api;

import api.handlers.*;
import com.sun.net.httpserver.HttpServer;
import managers.TaskManager;
import tasks.Status;
import tasks.Task;
import util.Managers;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private static final int BACKLOG = 0;

    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();
        manager.add(new Task("task1", "description1",
                Status.NEW, Duration.ofMinutes(120), LocalDateTime.now().minus(Duration.ofDays(1))));
        System.out.println(manager.getListTasks());

        try {
            HttpServer httpServer = HttpServer.create(new InetSocketAddress(PORT), BACKLOG);

            httpServer.createContext("/tasks", new TaskHandler(manager));
            httpServer.createContext("/subtasks", new SubtaskHandler(manager));
            httpServer.createContext("/epics", new EpicHandler(manager));
            httpServer.createContext("/history", new HistoryHandler(manager));
            httpServer.createContext("/prioritized", new PrioritizedHandler(manager));
            httpServer.start();
        } catch (IOException e) {
            System.err.println("Ошибка запуска сервера на порту " + PORT + ": " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
