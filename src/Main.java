import api.HttpTaskServer;
import managers.TaskManager;
import util.Managers;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        TaskManager manager = Managers.getDefault();
        HttpTaskServer httpTaskServer = new HttpTaskServer(manager);

        httpTaskServer.startServer();
    }
}