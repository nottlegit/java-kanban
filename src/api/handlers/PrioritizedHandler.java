package api.handlers;

import com.sun.net.httpserver.HttpExchange;
import managers.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.util.List;

public class PrioritizedHandler extends BaseHandlers {

    public PrioritizedHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String httpMethod = httpExchange.getRequestMethod();

        if (!"GET".equals(httpMethod)) {
            sendInvalidRequest(httpExchange);
            return;
        }
        handlerGet(httpExchange);
    }

    @Override
    void handlerGet(HttpExchange httpExchange) throws IOException {
        List<Task> tasks = manager.getPrioritizedTasks();
        String json = gson.toJson(tasks);

        sendText(httpExchange, json);
    }

    private void sendInvalidRequest(HttpExchange exchange) throws IOException {
        byte[] responseBytes = "Invalid Request".getBytes(DEFAULT_CHARSET);
        int responseCode = 400;

        send(exchange, responseBytes, responseCode);
    }
}
