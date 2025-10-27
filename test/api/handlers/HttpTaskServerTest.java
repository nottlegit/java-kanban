package api.handlers;

import com.sun.net.httpserver.HttpServer;
import managers.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import util.Managers;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.http.HttpClient;

class HttpTaskServerTest {
    private TaskManager manager;
    private final int TEST_PORT = 8081;
    protected HttpServer server;
    protected HttpClient client;

    @BeforeEach
    void setUp() throws IOException {
        manager = Managers.getDefault();
        server = HttpServer.create(new InetSocketAddress(TEST_PORT), 0);

        server.createContext("/tasks", new TaskHandler(manager));
        server.createContext("/subtasks", new SubtaskHandler(manager));
        server.createContext("/epics", new EpicHandler(manager));
        server.createContext("/history", new HistoryHandler(manager));
        server.createContext("/prioritized", new PrioritizedHandler(manager));

        server.start();
        client = HttpClient.newHttpClient();
    }

    @AfterEach
    void tearDown() {
        if (server != null) {
            server.stop(0);
        }
    }

    protected String getBaseUrl() {
        return "http://localhost:" + TEST_PORT;
    }

    public TaskManager getManager() {
        return manager;
    }
}