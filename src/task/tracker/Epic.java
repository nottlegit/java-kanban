package task.tracker;
import java.util.HashMap;
import java.util.Objects;

public class Epic extends Task{
    private final HashMap<Integer, Subtask> subtasks = new HashMap<Integer, Subtask>();

    public Epic(String title) {
        super(title);
    }

    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    public void addNewSubtask(String title) {
        Subtask subtask = new Subtask(title, this.hashCode());
        subtasks.put(subtask.hashCode(), subtask);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtasks);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subtasks=" + subtasks +
                ", title='" + title + '\'' +
                ", taskStatus=" + taskStatus +
                '}';
    }
}
