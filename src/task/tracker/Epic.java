package task.tracker;
import java.util.HashMap;

public class Epic extends Task{
    private final HashMap<Integer, Subtask> subtasks = new HashMap<Integer, Subtask>();

    public Epic(String title) {
        super(title);
    }

    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    public void addNewSubtask(Subtask subtask) {
        subtasks.put(subtask.hashCode(), subtask);
    }
}
