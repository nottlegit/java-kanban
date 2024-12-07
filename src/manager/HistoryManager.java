package manager;

import tasks.Task;

import java.util.List;

public interface HistoryManager {
    <T extends Task> void add(T task);

    <T extends Task> List<T> getListHistory();
}
