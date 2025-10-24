package api.handlers;

import com.sun.net.httpserver.HttpExchange;
import managers.TaskManager;

public class PrioritizedHandler extends BaseHandlers {
    public PrioritizedHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange httpExchange) {

    }

    @Override
    void handlerGet(HttpExchange httpExchange) {

    }
}
