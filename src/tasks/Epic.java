package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> listSubtasks;

    public Epic(String title, String description, Status status, Duration duration, LocalDateTime localDateTime) {
        super(title, description, status, duration, localDateTime);
        this.listSubtasks = new ArrayList<>();
    }

    public Epic(int id, String title, String description, Status status) {
        super(id, title, description, status);
        this.listSubtasks = new ArrayList<>();
    }

    public Epic(Task task) {
        this(task.title, task.description, task.status, task.duration, task.localDateTime);
    }

    public ArrayList<Integer> getListSubtasks() {
        return listSubtasks;
    }

    public void setListSubtasks(ArrayList<Integer> listSubtasks) {
        this.listSubtasks = listSubtasks;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "listSubtasks=" + listSubtasks +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}
