package util;

import managers.*;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }
}
