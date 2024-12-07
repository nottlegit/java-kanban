package manager;

import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, Epic> epics;
    private HashMap<Integer, Subtask> subtasks;
    private int nextId;

    public InMemoryTaskManager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
        this.nextId = 0;
        this.nextIdByHistory = 0;
    }

    @Override
    public ArrayList<Task> getListTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public void deleteAllTasks() {
        tasks = new HashMap<>();
    }

    @Override
    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    @Override
    public void add(Task task) {
        task.setId(nextId++);
        tasks.put(task.getId(), task);
    }

    @Override
    public void update(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public void deleteTaskById(int id) {
        tasks.remove(id);
    }

    @Override
    public ArrayList<Epic> getListEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public void deleteAllEpics() {
        subtasks = new HashMap<>();
        epics = new HashMap<>();
    }

    @Override
    public Epic getEpicById(int idEpic) {
        return epics.get(idEpic);
    }

    @Override
    public void add(Epic epic) {
        epic.setId(nextId++);
        epics.put(epic.getId(), epic);
        changeStatusEpic(epic);
    }

    @Override
    public void update(Epic epic) {
        epics.put(epic.getId(), epic);
        changeStatusEpic(epic);
    }

    @Override
    public void deleteEpicById(int idEpic) {
        for (int idSubtask : epics.get(idEpic).getListSubtasks()) {
            subtasks.remove(idSubtask);
        }
        epics.remove(idEpic);
    }

    @Override
    public ArrayList<Subtask> getListEpicSubtasksById(int idEpic) {
        ArrayList<Subtask> listSubtasks = new ArrayList<>();

        for (int idSubtasks : epics.get(idEpic).getListSubtasks()) {
            listSubtasks.add(subtasks.get(idSubtasks));
        }
        return listSubtasks;
    }

    @Override
    public ArrayList<Subtask> getListSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void deleteAllSubtasks() {
        for (Epic epic : epics.values()) {
            epic.setListSubtasks(new ArrayList<>());
            changeStatusEpic(epic);
        }
        subtasks = new HashMap<>();
    }

    @Override
    public Subtask getSubtaskById(int idSubtasks) {
        return subtasks.get(idSubtasks);
    }

    @Override
    public void add(Subtask subtask) {
        Epic epic = epics.get(subtask.getIdEpic());
        int idEpic = epic.getId();
        int idSubtask = nextId++;

        subtask.setId(idSubtask);
        subtasks.put(idSubtask, subtask);

        addIdSubtaskToEpic(idEpic, idSubtask);
        changeStatusEpic(epic);
    }

    @Override
    public void update(Subtask subtask) {
        Epic epic = epics.get(subtask.getIdEpic());

        subtasks.put(subtask.getId(), subtask);
        changeStatusEpic(epic);
    }

    @Override
    public void deleteSubtaskById(int idSubtask) {
        Subtask subtask = subtasks.get(idSubtask);
        ArrayList<Integer> listSubtasks = epics.get(subtask.getIdEpic()).getListSubtasks();
        int indexToRemove = listSubtasks.indexOf(idSubtask);
        if (indexToRemove != -1) {
            listSubtasks.remove(indexToRemove);
        }
        changeStatusEpic(epics.get(subtask.getIdEpic()));
        subtasks.remove(idSubtask);
    }

    @Override
    public <T extends Task> List<T> getHistory() {
        return listHistory;
    }

    private <T extends Task> void updateHistory(T task) {
        if (nextIdByHistory == 9) {
            nextIdByHistory = 0;
        }
        listHistory.add(nextIdByHistory, task);
        nextIdByHistory++;
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
