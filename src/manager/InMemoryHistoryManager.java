package manager;

import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final static int MAX_INDEX_LIST_HISTORY = 9;
    private int nextIdByHistory;
    List<Task> listHistory;

    public InMemoryHistoryManager() {
        this.listHistory = new ArrayList<>();
        this.nextIdByHistory = 0;
    }

    @Override
    public  void  add(T task) {
        if (nextIdByHistory == MAX_INDEX_LIST_HISTORY) {
            nextIdByHistory = 0;
        }
        listHistory.add(nextIdByHistory, task);
    }

    @Override
    public <T extends Task> List<T> getListHistory() {
        return listHistory;
    }
}
