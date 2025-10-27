package api.handlers;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import managers.TaskManager;
import managers.exceptions.HasTimeOverlapWithAnyException;
import managers.exceptions.NotFoundException;
import tasks.Epic;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

public class EpicHandler extends TaskHandler {
    public EpicHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    void handlerGet(HttpExchange httpExchange) throws IOException {
        List<Epic> epics = manager.getListEpics();
        String json = gson.toJson(epics);

        sendText(httpExchange, json);
    }

    @Override
    void handlerGetById(HttpExchange httpExchange, int id) throws IOException {
        try {
            Epic epic = manager.getEpicById(id);

            sendText(httpExchange, gson.toJson(epic));
        } catch (NotFoundException e) {
            sendNotFound(httpExchange);
        }
    }

    @Override
    void handlerPost(HttpExchange httpExchange) throws IOException {
        Optional<Epic> epicOptional = parseEpic(httpExchange.getRequestBody());

        if(epicOptional.isEmpty()) {
            sendInvalidId(httpExchange);
            return;
        }
        Epic epic = epicOptional.get();

        try {
            if (epic.getId() > 0) {
                manager.update(epic);
            } else {
                manager.add(epic);
            }
            sendStatus(httpExchange, 201);
        } catch (HasTimeOverlapWithAnyException e) {
            System.out.println("Ошибка: " + e.getMessage());
            sendHasInteractions(httpExchange);
        }
    }

    @Override
    void handlerDelete(HttpExchange httpExchange, int id) throws IOException {
        manager.deleteEpicById(id);
        sendStatus(httpExchange, 200);
    }

    private Optional<Epic> parseEpic(InputStream bodyInputStream) throws IOException {
        String jsonString = new String(bodyInputStream.readAllBytes(), DEFAULT_CHARSET);

        try {
            return Optional.of(gson.fromJson(jsonString, Epic.class));
        } catch (JsonSyntaxException e) {
            return Optional.empty();
        }
    }
}
