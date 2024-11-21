package manager;

import task_tracker.Epic;
import task_tracker.Status;
import task_tracker.Subtask;
import task_tracker.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, Epic> epics;
    private HashMap<Integer, Subtask> subtasks;
    private int nextId;

    public TaskManager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
        this.nextId = 0;
    }

    public ArrayList<Task> getListTasks() {
        return new ArrayList<>(tasks.values());
    }

    public void deleteAllTasks() {
        tasks = new HashMap<>();
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public void addTask(Task task) {
        task.setId(nextId++);
        tasks.put(task.getId(), task);
    }

    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void deleteTaskById(int id) {
        tasks.remove(id);
    }

    public ArrayList<Epic> getListEpics() {
        return new ArrayList<>(epics.values());
    }

    public void deleteAllEpics() {
        epics = new HashMap<>();
    }

    public Epic getEpicById(int idEpic) {
        return epics.get(idEpic);
    }

    public void addEpic(Epic epic) {
        epic.setId(nextId++);
        epics.put(epic.getId(), epic);
        changeStatusEpic(epic);
    }

    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
        changeStatusEpic(epic);
    }

    public void deleteEpicById(int idEpic) {
        for (int idSubtask : epics.get(idEpic).getListSubtasks()) {
            subtasks.remove(idSubtask);
        }
        epics.remove(idEpic);
    }

    public ArrayList<Subtask> getListEpicSubtasksById(int idEpic) {
        ArrayList<Subtask> listSubtasks = new ArrayList<>();

        for (int idSubtasks : epics.get(idEpic).getListSubtasks()) {
            listSubtasks.add(subtasks.get(idSubtasks));
        }
        return listSubtasks;
    }

    public ArrayList<Subtask> getListSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public void deleteAllSubtasks() {
        for (Epic epic : epics.values()) {
            epic.setListSubtasks(new ArrayList<>());
        }
        subtasks = new HashMap<>();
    }

    public Subtask getSubtaskById(int idSubtasks) {
        return subtasks.get(idSubtasks);
    }

    public void addSubtask(Subtask subtask) {
        Epic epic = epics.get(subtask.getIdEpic());
        int idEpic = epic.getId();
        int idSubtask = nextId++;

        subtask.setId(idSubtask);
        subtasks.put(idSubtask, subtask);

        addIdSubtaskToEpic(idEpic, idSubtask);
        changeStatusEpic(epic);
    }

    public void updateSubtask(Subtask subtask) {
        Epic epic = epics.get(subtask.getIdEpic());

        subtasks.put(subtask.getId(), subtask);
        changeStatusEpic(epic);
    }

    public void deleteSubtaskById(int idSubtask) {
        Subtask subtask = subtasks.get(idSubtask);
        ArrayList<Integer> listSubtasks = epics.get(subtask.getIdEpic()).getListSubtasks();
        int indexToRemove = listSubtasks.indexOf(idSubtask);
        if (indexToRemove != -1) {
            listSubtasks.remove(indexToRemove);
        }
        subtasks.remove(idSubtask);
    }

    private void addIdSubtaskToEpic(int idEpic, int idSubtask) {
        epics.get(idEpic).getListSubtasks().add(idSubtask);
    }

    private void changeStatusEpic(Epic epic) {
        ArrayList<Integer> listSubtasks = epic.getListSubtasks();

        if (listSubtasks.isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }

        int statusNewCounter = 0;
        int statusDoneCounter = 0;
        int sizeListSubtasks = listSubtasks.size();

        for (Integer subtasksId : listSubtasks) {
            switch (subtasks.get(subtasksId).getStatus()) {
                case NEW:
                    statusNewCounter++;
                    break;
                case DONE:
                    statusDoneCounter++;
                    break;
                default:
                    break;
            }
        }
        if (statusDoneCounter == sizeListSubtasks) {
            epic.setStatus(Status.DONE);
        } else if (statusNewCounter == sizeListSubtasks) {
            epic.setStatus(Status.NEW);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }
}
