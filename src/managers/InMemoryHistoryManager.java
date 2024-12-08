package managers;

import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final static int MAX_INDEX_LIST_HISTORY = 9;
    private int nextIdByHistory;
    private final List<Task> listHistory;

    public InMemoryHistoryManager() {
        this.listHistory = new ArrayList<>();
        this.nextIdByHistory = 0;
    }

    @Override
    public void add(Task task) {
        if (nextIdByHistory > MAX_INDEX_LIST_HISTORY) {
            nextIdByHistory = 0;
        }

        if (listHistory.size() > nextIdByHistory) {
            listHistory.set(nextIdByHistory, task);
        } else {
            listHistory.add(nextIdByHistory, task);
        }
        nextIdByHistory++;
    }

    @Override
    public List<Task> getListHistory() {
        return listHistory;
    }
}
