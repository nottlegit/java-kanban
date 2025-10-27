package api.handlers;

import api.HttpTaskServer;
import managers.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import util.Managers;

import java.io.IOException;
import java.net.http.HttpClient;

class HttpTaskServerTest {
    private TaskManager manager;
    protected HttpTaskServer server;
    protected HttpClient client;

    @BeforeEach
    void setUp() throws IOException {
        manager = Managers.getDefault();
        server = new HttpTaskServer(manager);
        server.startServer();
        client = HttpClient.newHttpClient();
    }

    @AfterEach
    void tearDown() {
        server.stop();
    }

    protected String getBaseUrl() {
        return "http://localhost:8080";
    }

    public TaskManager getManager() {
        return manager;
    }
}