package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> listSubtasks;
    private LocalDateTime endTime;

    public Epic(String title, String description, Status status) {
        super(title, description, status, Duration.ZERO, null);
        this.listSubtasks = new ArrayList<>();
    }

    public Epic(int id, String title, String description, Status status) {
        super(id, title, description, status, Duration.ZERO, null);
        this.listSubtasks = new ArrayList<>();
    }

    public Epic(Task task) {
        this(task.title, task.description, task.status);
    }

    public ArrayList<Integer> getListSubtasks() {
        return listSubtasks;
    }

    public void setListSubtasks(ArrayList<Integer> listSubtasks) {
        this.listSubtasks = listSubtasks;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "startTime=" + startTime +
                ", listSubtasks=" + listSubtasks +
                ", duration=" + duration +
                ", status=" + status +
                ", description='" + description + '\'' +
                ", title='" + title + '\'' +
                ", id=" + id +
                ", endTime=" + endTime +
                '}';
    }
}
