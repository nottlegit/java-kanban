package api.handlers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tasks.Status;
import tasks.Task;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class TaskHandlerTest extends HttpTaskServerTest {

    @Test
    @DisplayName("Создание задачи и проверка что она сохраняется")
    void testHandlerPost() throws Exception {
        String taskJson = """
            {
                "title": "Test Task № 1",
                "description": "Test Description № 1",
                "status": "NEW",
                "duration": 30,
                "startTime": "22:10:2025 13:06"
            }
            """;
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create(getBaseUrl() + "/tasks"))
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> postResponse = client.send(postRequest, HttpResponse.BodyHandlers.ofString());

        //проверяем создание
        assertEquals(201, postResponse.statusCode());

        Task task = getManager().getListTasks().getFirst();

        assertNotNull(task);
        assertEquals("Test Task № 1", task.getTitle());
        assertEquals(Duration.ofMinutes(30), task.getDuration());
    }

    @Test
    @DisplayName("Получение нескольких задач")
    void testHandlerGet() throws Exception {
        Task task1 = new Task("Test Task № 1", "Test Description № 1",
                Status.NEW, Duration.ofMinutes(120), LocalDateTime.now().minus(Duration.ofDays(1)));
        Task task2 = new Task("Test Task № 2", "Test Description № 1",
                Status.NEW, Duration.ofMinutes(120), LocalDateTime.now().minus(Duration.ofDays(2)));

        getManager().add(task1);
        getManager().add(task2);

        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create(getBaseUrl() + "/tasks"))
                .GET()
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> postResponse = client.send(postRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, postResponse.statusCode());
        assertTrue(postResponse.body().contains("Test Task № 1"));
        assertTrue(postResponse.body().contains("Test Task № 2"));
    }

    @Test
    @DisplayName("Получение задачи по ID после создания")
    void testHandlerGetById() throws Exception {
        // создаем задачу
        String taskJson = """
            {
                "id": 2,
                "title": "Test Task № 2",
                "description": "Test Description № 2",
                "status": "NEW",
                "duration": 30,
                "startTime": "22:10:2025 13:06"
            }
            """;

        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create(getBaseUrl() + "/tasks"))
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        client.send(postRequest, HttpResponse.BodyHandlers.ofString());

        // получаем задачу по ID
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create(getBaseUrl() + "/tasks/2"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("Test Task № 2"));
        assertTrue(response.body().contains("Test Description № 2"));
    }

    @Test
    @DisplayName("Удаление задачи по ID")
    void testHandlerDelete() throws Exception {
        String taskJson = """
            {
                "title": "Test Task № 3",
                "description": "Test Description № 3",
                "status": "NEW",
                "duration": 30,
                "startTime": "22:10:2025 13:06"
            }
            """;

        HttpRequest createRequest = HttpRequest.newBuilder()
                .uri(URI.create(getBaseUrl() + "/tasks"))
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        client.send(createRequest, HttpResponse.BodyHandlers.ofString());

        // удаляем задачу
        HttpRequest deleteRequest = HttpRequest.newBuilder()
                .uri(URI.create(getBaseUrl() + "/tasks/1"))
                .DELETE()
                .build();

        HttpResponse<String> deleteResponse = client.send(deleteRequest, HttpResponse.BodyHandlers.ofString());

        // проверяем удаление
        assertEquals(200, deleteResponse.statusCode());

        // Проверяем что задача больше не доступна
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create(getBaseUrl() + "/tasks/1"))
                .GET()
                .build();

        HttpResponse<String> getResponse = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, getResponse.statusCode());
    }

    @Test
    @DisplayName("Ошибка 404 при запросе несуществующей задачи")
    void testGetNonExistentTask_ShouldReturn404() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(getBaseUrl() + "/tasks/123"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());
    }

    @Test
    @DisplayName("Ошибка 400 при невалидном JSON")
    void testCreateTaskWithInvalidJson_ShouldReturn400() throws Exception {
        String invalidJson = "{ invalid json";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(getBaseUrl() + "/tasks"))
                .POST(HttpRequest.BodyPublishers.ofString(invalidJson))
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(400, response.statusCode());
    }
}
