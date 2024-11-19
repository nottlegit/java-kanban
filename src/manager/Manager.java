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

    public void addTask(Object object) {
        allTasks.get(TaskStatus.NEW).put(object.hashCode(), object);
    }

    public ArrayList<String> getListTasks() {
        ArrayList<String> listTasks = new ArrayList<>();

        for (TaskStatus taskStatus : allTasks.keySet()) {
            listTasks.add(taskStatus.toString());
            for (Object o : allTasks.get(taskStatus).values()) {
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
                    listEpicSubtasks.add(subtask.toString());
                }
            }
        }
        return listEpicSubtasks;
    }

    public void deleteAllTasks(TaskStatus taskstatus) {
        allTasks.put(taskstatus, new HashMap<>());
    }

    public Object getTaskById(TaskStatus taskStatus, Integer idTask) {
        return  allTasks.get(taskStatus).get(idTask);
    }

    public void updateTask(Object object) {
        this.addTask(object);
    }

    public void deleteById(TaskStatus taskStatus, Integer idTask) {
        allTasks.get(taskStatus).remove(idTask);
    }

    public void setStatusTask(TaskStatus newTaskStatus, Object object) {
        //TODO Работает некорректно
        if (object instanceof Subtask) {
            Subtask subtask = (Subtask) object;
            Epic epic = (Epic) allTasks.get(subtask.getTaskStatus()).get(subtask.getIdEpic());
            TaskStatus lastStatus = epic.getTaskStatus();
            Integer lastId = epic.hashCode();

            subtask.setTaskStatus(newTaskStatus);
            if (epic.isStatusTaskChanged()) {
                System.out.println(true);
                this.moveTask(epic, lastStatus, lastId);
            }
        } else if (object instanceof Epic) {
            return;
        } else {
            Task task = (Task) object;
            TaskStatus lastStatus = task.getTaskStatus();

            if (!newTaskStatus.equals(lastStatus)) {
                Integer lastId = task.hashCode();

                task.setTaskStatus(newTaskStatus);
                moveTask(task, lastStatus, lastId);
            }
        }
    }

    private void moveTask(Task task, TaskStatus lastStatus, Integer lastId) {
        allTasks.get(task.getTaskStatus()).put(task.hashCode(), task);
        this.deleteById(lastStatus, lastId);
    }

    public Subtask addSubtaskInEpic(Epic epic, String titleSubtask) {
        return epic.addNewSubtask(titleSubtask);
    }
}
