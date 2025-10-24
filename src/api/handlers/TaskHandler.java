package api.handlers;

import com.sun.net.httpserver.HttpExchange;
import managers.TaskManager;
import managers.exceptions.NotFoundException;
import tasks.Task;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Optional;
//List<Task> listTask = gson.fromJson("", new TypeToken<List<Task>>(){}.getType());
public class TaskHandler extends BaseHandlers {

    public TaskHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String httpMethod = httpExchange.getRequestMethod();
        Optional<Integer> idOptional = getTheIdFromThePath(httpExchange);

        if (idOptional.isEmpty()) {
            if ("GET".equals(httpMethod)) {
                System.out.println(1);
                handlerGet(httpExchange);
                return;
            }
            sendInvalidId(httpExchange);
            return;
        }

        int id = idOptional.get();

        switch (httpMethod) {
            case "GET":
                handlerGetById(httpExchange, id);
                break;
            case "POST":
                handlerPost(httpExchange, id);
                break;
            case "DELETE":
                handlerDelete(httpExchange, id);
                break;
        }
    }

    protected void sendHasInteractions(HttpExchange exchange) throws IOException {
        String responseString = "Задача пересекается с уже существующими";
        int responseCode = 406;

        try (OutputStream os = exchange.getResponseBody()) {
            exchange.sendResponseHeaders(responseCode, 0);
            os.write(responseString.getBytes(DEFAULT_CHARSET));
        }
        exchange.close();
    }

    @Override
    void handlerGet(HttpExchange httpExchange) throws IOException {
        List<Task> tasks = manager.getListTasks();
        System.out.println(2);
        try {
            String json = gson.toJson(tasks);
            System.out.println(json);

            sendText(httpExchange, json, 200);
        } catch (IOException e) {
            System.out.println(e);
            throw new RuntimeException(e);
        } catch (RuntimeException e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }

    void handlerGetById(HttpExchange httpExchange, int id) throws IOException {
        try {
            Task task = manager.getTaskById(id);

            sendText(httpExchange, gson.toJson(task), 200);
        } catch (NotFoundException e) {
            sendNotFound(httpExchange);
        }
    }

    void handlerPost(HttpExchange httpExchange, int id) throws IOException {

    }

    void handlerDelete(HttpExchange httpExchange, int id) throws IOException {

    }
}
