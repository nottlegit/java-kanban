package managers;

import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final static int MAX_SIZE_LIST_HISTORY = 10;
    private List<Task> listHistory;

    public InMemoryHistoryManager() {
        this.listHistory = new ArrayList<>();
    }

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        if (listHistory.size() == MAX_SIZE_LIST_HISTORY) {
            listHistory.removeFirst();
        }
        listHistory.add(task);
    }

    @Override
    public List<Task> getListHistory() {
        return listHistory;
    }

    @Override
    public void deleteTaskFromHistory(Task task) {
        if (listHistory.isEmpty()) {
            return;
        }
        List<Task> newListHistory = new ArrayList<>();

        for (Task taskInList : listHistory) {
            if (taskInList.equals(task)) {
                continue;
            }
            newListHistory.add(taskInList);
        }
        listHistory = newListHistory;
    }
}
