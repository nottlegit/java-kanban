package api.handlers;

import com.sun.net.httpserver.HttpExchange;
import managers.TaskManager;

public class HistoryHandler extends BaseHandlersTasks {


    public HistoryHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange httpExchange) {

    }

     @Override
     void handlerGet(HttpExchange httpExchange) {

     }
}
