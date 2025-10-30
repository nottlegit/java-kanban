package api.handlers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskHandlerTest extends HttpTaskServerTest {
    private int idEpic;
    private final int idSubtask = 30;

    @BeforeEach
    @Override
    void setUp() throws IOException {
        super.setUp();
        // subtask не может существовать без эпика
        Epic epic = new Epic("epic", "description", Status.NEW);
        getManager().add(epic);
        idEpic = epic.getId();
    }

    @Test
    @DisplayName("Создание подзадачи и проверка что она сохраняется")
    void testHandlerPost() throws Exception {
        String taskJson = String.format("""
            {
                "idEpic": %d,
                "id": %d,
                "title": "Test Subtask № 1",
                "description": "Test Description № 1",
                "status": "NEW",
                "duration": 120,
                "startTime": "22:10:2025 13:06"
            }
            """, idEpic, idSubtask);
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create(getBaseUrl() + "/subtasks"))
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> postResponse = client.send(postRequest, HttpResponse.BodyHandlers.ofString());

        //проверяем создание
        assertEquals(201, postResponse.statusCode());

        Subtask subtask = getManager().getSubtaskById(30);
        // проверяем что задача в менеджере соответствует той, что отправили в запросе
        assertNotNull(subtask);
        assertEquals(idSubtask, subtask.getId());
        assertEquals(idEpic, subtask.getIdEpic());
        assertEquals("Test Subtask № 1", subtask.getTitle());
    }

    @Test
    @DisplayName("Получение нескольких задач")
    void testHandlerGet() throws Exception {
        Subtask subtask1 = new Subtask("Test Subtask № 1", "Test Description № 1",
                Status.NEW, idEpic, Duration.ofMinutes(120), LocalDateTime.now().minus(Duration.ofDays(1)));
        Subtask subtask2 = new Subtask("Test Subtask № 2", "Test Description № 1",
                Status.NEW, idEpic, Duration.ofMinutes(120), LocalDateTime.now().minus(Duration.ofDays(2)));

        getManager().add(subtask1);
        getManager().add(subtask2);

        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create(getBaseUrl() + "/subtasks"))
                .GET()
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> postResponse = client.send(postRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, postResponse.statusCode());
        assertTrue(postResponse.body().contains("Test Subtask № 1"));
        assertTrue(postResponse.body().contains("Test Subtask № 2"));
    }

    @Test
    @DisplayName("Получение задачи по ID после создания")
    void testHandlerGetById() throws Exception {
        String taskJson = String.format("""
            {
                "idEpic": %d,
                "id": %d,
                "title": "Test Subtask № 2",
                "description": "Test Description № 2",
                "status": "NEW",
                "duration": 120,
                "startTime": "22:10:2025 13:06"
            }
            """, idEpic, idSubtask);

        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create(getBaseUrl() + "/subtasks"))
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        client.send(postRequest, HttpResponse.BodyHandlers.ofString());

        // получаем задачу по ID
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create(getBaseUrl() + "/subtasks/" + idSubtask))
                .GET()
                .build();

        HttpResponse<String> response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("Test Subtask № 2"));
        assertTrue(response.body().contains("Test Description № 2"));
    }

    @Test
    @DisplayName("Удаление задачи по ID")
    void testHandlerDelete() throws Exception {
        String taskJson = String.format("""
            {
                "idEpic": %d,
                "id": %d,
                "title": "Test Subtask № 2",
                "description": "Test Description № 2",
                "status": "NEW",
                "duration": 120,
                "startTime": "22:10:2025 13:06"
            }
            """, idEpic, idSubtask);

        HttpRequest createRequest = HttpRequest.newBuilder()
                .uri(URI.create(getBaseUrl() + "/subtasks"))
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        client.send(createRequest, HttpResponse.BodyHandlers.ofString());

        // удаляем задачу
        HttpRequest deleteRequest = HttpRequest.newBuilder()
                .uri(URI.create(getBaseUrl() + "/subtasks/" + idSubtask))
                .DELETE()
                .build();

        HttpResponse<String> deleteResponse = client.send(deleteRequest, HttpResponse.BodyHandlers.ofString());

        // проверяем удаление
        assertEquals(200, deleteResponse.statusCode());

        // Проверяем что задача больше не доступна
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create(getBaseUrl() + "/subtasks/" + idSubtask))
                .GET()
                .build();

        HttpResponse<String> getResponse = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, getResponse.statusCode());
    }

    @Test
    @DisplayName("Ошибка 404 при запросе несуществующей задачи")
    void testGetNonExistentTask_ShouldReturn404() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(getBaseUrl() + "/subtasks/123"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
    }
}