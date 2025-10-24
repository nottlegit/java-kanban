package api.handlers;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import managers.TaskManager;
import managers.exceptions.HasTimeOverlapWithAnyException;
import managers.exceptions.NotFoundException;
import tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

public class TaskHandler extends BaseHandlers {

    public TaskHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String httpMethod = httpExchange.getRequestMethod();
        Optional<Integer> idOptional = getTheIdFromThePath(httpExchange);

        if (idOptional.isEmpty()) {
            switch (httpMethod) {
                case "GET":
                    handlerGet(httpExchange);
                    return;
                case "POST":
                    handlerPost(httpExchange);
                    return;
                default:
                    sendInvalidId(httpExchange);
                    return;
            }
        }
        int id = idOptional.get();

        switch (httpMethod) {
            case "GET":
                handlerGetById(httpExchange, id);
                break;
            case "DELETE":
                handlerDelete(httpExchange, id);
                break;
            default:
                break;
        }
    }

    @Override
    void handlerGet(HttpExchange httpExchange) throws IOException {
        List<Task> tasks = manager.getListTasks();
        String json = gson.toJson(tasks);

        sendText(httpExchange, json, 200);
    }

    void handlerGetById(HttpExchange httpExchange, int id) throws IOException {
        try {
            Task task = manager.getTaskById(id);

            sendText(httpExchange, gson.toJson(task), 200);
        } catch (NotFoundException e) {
            sendNotFound(httpExchange);
        }
    }

    void handlerPost(HttpExchange httpExchange) throws IOException {
        Optional<Task> taskOptional = parseTask(httpExchange.getRequestBody());

        if(taskOptional.isEmpty()) {
            sendInvalidId(httpExchange);
            return;
        }

        Task task = taskOptional.get();

        try {
            if (task.getId() > 0) {
                manager.update(task);
            } else {
                manager.add(task);
            }
            sendStatus(httpExchange, 201);
        } catch (HasTimeOverlapWithAnyException e) {
            System.out.println("Ошибка: " + e.getMessage());
            sendHasInteractions(httpExchange);
        }
    }

    void handlerDelete(HttpExchange httpExchange, int id) throws IOException {
        manager.deleteTaskById(id);
        sendStatus(httpExchange, 200);
    }

    protected void sendHasInteractions(HttpExchange exchange) throws IOException {
        byte[] responseBytes = "Not Acceptable".getBytes(DEFAULT_CHARSET);
        int responseCode = 406;

        send(exchange, responseBytes, responseCode);
    }

    protected void sendInternalServerError(HttpExchange exchange) throws IOException {
        byte[] responseBytes = "Internal Server Error".getBytes(DEFAULT_CHARSET);
        int responseCode = 500;

        send(exchange, responseBytes, responseCode);
    }

    protected void sendStatus(HttpExchange exchange, int responseCode) throws IOException {
        exchange.sendResponseHeaders(responseCode, 0);
        exchange.close();
    }

    private Optional<Task> parseTask(InputStream bodyInputStream) throws IOException {
        String jsonString = new String(bodyInputStream.readAllBytes(), DEFAULT_CHARSET);

        try {
            return Optional.of(gson.fromJson(jsonString, Task.class));
        } catch (JsonSyntaxException e) {
            return Optional.empty();
        }
    }
}
