package task.tracker;

import java.util.ArrayList;

public class Epic extends Task {
    ArrayList<Integer> listSubtasks;

    public Epic(String title, String description, Status status) {
        super(title, description, status);
        this.listSubtasks = new ArrayList<>();
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
