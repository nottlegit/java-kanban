package api.handlers;

import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.Test;
import tasks.Task;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PrioritizedHandlerTest extends BaseHandlersFromManagersMethodsTest {

    @Test
    void handlerGet() throws Exception {
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create(getBaseUrl() + "/prioritized"))
                .GET()
                .header("Content-Type", "application/json")
                .build();
        HttpResponse<String> postResponse = client.send(postRequest, HttpResponse.BodyHandlers.ofString());

        List<Task> prioritized = gson.fromJson(postResponse.body(), new TypeToken<List<Task>>(){}.getType());

        assertEquals(prioritized.size(), getManager().getPrioritizedTasks().size());
    }
}