package task.tracker;

import java.util.Objects;

public class Task {
    protected  final String title;
    protected  TaskStatus taskStatus;

    public Task(String title) {
        this.title = title;
        this.taskStatus = TaskStatus.NEW;
    }

    public String getTitle() {
        return title;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        Task task = (Task) object;
        return Objects.equals(title, task.title) && taskStatus == task.taskStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, taskStatus);
    }

    @Override
    public String toString() {
        return "Task{" +
                "title='" + title + '\'' +
                ", taskStatus=" + taskStatus +
                '}';
    }
}
