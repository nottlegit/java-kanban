package api.handlers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

class EpicHandlerTest extends HttpTaskServerTest {
    private Epic epic1;

    @BeforeEach
    @Override
    void setUp() throws IOException {
        super.setUp();

        epic1 = new Epic("Test Epic № 1", "Test Description", Status.NEW);
        getManager().add(epic1);
    }

    @Test
    @DisplayName("Получение нескольких эпиков")
    void testHandlerGet() throws Exception {
        Epic epic2 = new Epic("Test Epic № 2", "Test Description", Status.NEW);

        getManager().add(epic2);

        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create(getBaseUrl() + "/epics"))
                .GET()
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> postResponse = client.send(postRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, postResponse.statusCode());
        assertTrue(postResponse.body().contains("Test Epic № 1"));
        assertTrue(postResponse.body().contains("Test Epic № 2"));
    }

    @Test
    @DisplayName("Получение эпика по id")
    void handlerGetById() throws Exception {
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create(getBaseUrl() + "/epics/" + epic1.getId()))
                .GET()
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> postResponse = client.send(postRequest, HttpResponse.BodyHandlers.ofString());

        assertTrue(postResponse.body().contains("Test Epic № 1"));
    }

    @Test
    @DisplayName("Добавление эпика")
    void handlerPost() throws Exception {
        String taskJson = """
            {
                "listSubtasks": [],
                "id": 3,
                "title": "Test Epic № 3",
                "description": "Test Description",
                "status": "NEW",
                "duration": 0
            }
            """;

        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create(getBaseUrl() + "/epics"))
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> postResponse = client.send(postRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, postResponse.statusCode());

        Epic epic = getManager().getEpicById(3);
        assertNotNull(epic);
    }

    @Test
    @DisplayName("Удаление эпика")
    void handlerDelete() throws Exception {
        HttpRequest deleteRequest = HttpRequest.newBuilder()
                .uri(URI.create(getBaseUrl() + "/epics/" + epic1.getId()))
                .DELETE()
                .build();

        HttpResponse<String> deleteResponse = client.send(deleteRequest, HttpResponse.BodyHandlers.ofString());

        // проверяем удаление
        assertEquals(200, deleteResponse.statusCode());

        // Проверяем что задача больше не доступна
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create(getBaseUrl() + "/epics/"+ epic1.getId()))
                .GET()
                .build();

        HttpResponse<String> getResponse = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, getResponse.statusCode());
    }
}