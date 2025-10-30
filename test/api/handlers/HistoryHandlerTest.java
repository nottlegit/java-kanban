package api.handlers;

import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tasks.Task;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistoryHandlerTest extends BaseHandlersFromManagersMethodsTest {

    @Test
    @DisplayName("Получение истории")
    void handlerGet() throws Exception {
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create(getBaseUrl() + "/history"))
                .GET()
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> postResponse = client.send(postRequest, HttpResponse.BodyHandlers.ofString());

        List<Task> tasks = gson.fromJson(postResponse.body(), new TypeToken<List<Task>>(){}.getType());

        assertEquals(tasks.size(), getManager().getHistory().size());
    }
}