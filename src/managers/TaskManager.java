package managers;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    ArrayList<Task> getListTasks();

    void deleteAllTasks();

    Task getTaskById(int id);

    void add(Task task);

    void update(Task task);

    void deleteTaskById(int id);

    ArrayList<Epic> getListEpics();

    void deleteAllEpics();

    Epic getEpicById(int idEpic);

    void add(Epic epic);

    void update(Epic epic);

    void deleteEpicById(int idEpic);

    ArrayList<Subtask> getListEpicSubtasksById(int idEpic);

    ArrayList<Subtask> getListSubtasks();

    void deleteAllSubtasks();

    Subtask getSubtaskById(int idSubtasks);

    void add(Subtask subtask);

    void update(Subtask subtask);

    void deleteSubtaskById(int idSubtask);

    <T extends Task> List<T> getHistory();
}
