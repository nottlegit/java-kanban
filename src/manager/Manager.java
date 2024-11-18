package manager;
import java.util.ArrayList;
import java.util.HashMap;
import task.tracker.*;

public class Manager {
    private final HashMap<TaskStatus, HashMap<Integer, Object>> allTasks;

    public Manager() {
        this.allTasks = new HashMap<>();
        for (TaskStatus taskStatus : TaskStatus.values()) {
            allTasks.put(taskStatus, new HashMap<>());
        }
    }

    public void addTask(Object o) {
        allTasks.get(TaskStatus.NEW).put(o.hashCode(), o);
    }

    public ArrayList<String> getListTasks() {
        ArrayList<String> listTasks = new ArrayList<>();

        for (TaskStatus taskStatus : allTasks.keySet()) {
            listTasks.add(taskStatus.toString());
            for (Object o : allTasks.get(taskStatus).values()) {
                //listTasks.add(((Task) o).getTitle());
                listTasks.add(o.toString());
                if (o instanceof Epic) {
                    listTasks.addAll(getListEpicSubtasks(taskStatus, ((Epic) o).hashCode()));
                }
            }
        }
        return listTasks;
    }

    public ArrayList<String> getListEpicSubtasks(TaskStatus taskStatus, int id) {
        ArrayList<String> listEpicSubtasks = new ArrayList<>();

        HashMap<Integer, Object> hashMap = allTasks.get(taskStatus);

        if (hashMap.containsKey(id)) {
            Object object = hashMap.get(id);
            if (object instanceof Epic) {
                Epic epic = (Epic) object;
                for (Subtask subtask : epic.getSubtasks().values()) {
                    //listEpicSubtasks.add(subtask.getTitle());
                    listEpicSubtasks.add(subtask.toString());
                }
            }
        }
        return listEpicSubtasks;
    }
}
