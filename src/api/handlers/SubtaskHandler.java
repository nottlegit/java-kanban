package api.handlers;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import managers.TaskManager;
import managers.exceptions.HasTimeOverlapWithAnyException;
import managers.exceptions.NotFoundException;
import tasks.Subtask;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

public class SubtaskHandler extends TaskHandler {
    public SubtaskHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    void handlerGet(HttpExchange httpExchange) throws IOException {
        List<Subtask> tasks = manager.getListSubtasks();
        String json = gson.toJson(tasks);

        sendText(httpExchange, json, 200);
    }
    @Override
    void handlerGetById(HttpExchange httpExchange, int id) throws IOException {
        try {
            System.out.println(1);
            Subtask subtask = manager.getSubtaskById(id);

            sendText(httpExchange, gson.toJson(subtask), 200);
        } catch (NotFoundException e) {
            sendNotFound(httpExchange);
        }
    }

    @Override
    void handlerPost(HttpExchange httpExchange) throws IOException {
        Optional<Subtask> taskOptional = parseSubtask(httpExchange.getRequestBody());

        if(taskOptional.isEmpty()) {
            sendInvalidId(httpExchange);
            return;
        }
        Subtask subtask = taskOptional.get();

        try {
            if (subtask.getId() > 0) {
                manager.update(subtask);
            } else {
                manager.add(subtask);
            }
            sendStatus(httpExchange, 201);
        } catch (HasTimeOverlapWithAnyException e) {
            System.out.println("Ошибка: " + e.getMessage());
            sendHasInteractions(httpExchange);
        }
    }

    @Override
    void handlerDelete(HttpExchange httpExchange, int id) throws IOException {
        manager.deleteSubtaskById(id);
        sendStatus(httpExchange, 200);
    }

    private Optional<Subtask> parseSubtask(InputStream bodyInputStream) throws IOException {
        String jsonString = new String(bodyInputStream.readAllBytes(), DEFAULT_CHARSET);

        try {
            return Optional.of(gson.fromJson(jsonString, Subtask.class));
        } catch (JsonSyntaxException e) {
            return Optional.empty();
        }
    }
}
