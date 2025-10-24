package api.handlers;

import com.sun.net.httpserver.HttpExchange;
import managers.TaskManager;

public class EpicHandler extends TaskHandler {
    public EpicHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    void handlerGet(HttpExchange httpExchange) {

    }
    @Override
    void handlerGetById(HttpExchange httpExchange, int id) {

    }

    @Override
    void handlerPost(HttpExchange httpExchange) {

    }

    @Override
    void handlerDelete(HttpExchange httpExchange, int id) {

    }
}
