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

    public Subtask addNewSubtask(String title) {
        Subtask subtask = new Subtask(title, this.hashCode());
        subtasks.put(subtask.hashCode(), subtask);
        return subtask;
    }

    public boolean isStatusTaskChanged() {
        int subtasksSize = subtasks.size();
        TaskStatus taskStatusNow = this.taskStatus;

        if (subtasksSize == 0) {
            if (taskStatusNow.equals(TaskStatus.NEW)){
                return false;
            }
            this.taskStatus = TaskStatus.NEW;
            return true;
        }

        int statusDoneCounter = 0;
        int statusInProgressCounter = 0;

        for (Subtask subtask : subtasks.values()) {
            switch (subtask.getTaskStatus()) {
                case IN_PROGRESS:
                    statusInProgressCounter++;
                    break;
                case DONE:
                    statusDoneCounter++;
                    break;
                default:
                    break;
            }
        }
        if (statusDoneCounter == subtasksSize) {
            if (taskStatusNow.equals(TaskStatus.DONE)){
                return false;
            }
            this.taskStatus = TaskStatus.DONE;
        } else if (statusInProgressCounter > 0) {
            if (taskStatusNow.equals(TaskStatus.IN_PROGRESS)){
                return false;
            }
            this.taskStatus = TaskStatus.IN_PROGRESS;
        } else {
            if (taskStatusNow.equals(TaskStatus.NEW)){
                return false;
            }
            this.taskStatus = TaskStatus.NEW;
        }
        return true;
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
