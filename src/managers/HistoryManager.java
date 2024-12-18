package managers;

import tasks.Task;

import java.util.List;

public interface HistoryManager {
    void add(Task task);

    List<Task> getListHistory();

    void deleteTaskFromHistory(Task task);
}
