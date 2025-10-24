package managers;

import managers.exceptions.NotFoundException;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;
import util.Managers;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    protected TreeSet<Task> prioritizedTasks;
    protected HashMap<Integer, Task> tasks;
    protected HashMap<Integer, Epic> epics;
    protected HashMap<Integer, Subtask> subtasks;
    protected final HistoryManager historyManager;
    protected int nextId;

    public InMemoryTaskManager() {
        tasks = new HashMap<>();
        epics = new HashMap<>();
        subtasks = new HashMap<>();
        historyManager = Managers.getDefaultHistoryManager();
        nextId = 1;
        prioritizedTasks = new TreeSet<>(Comparator.comparing(
                Task::getStartTime,
                Comparator.nullsLast(Comparator.naturalOrder())
        ));
    }

    @Override
    public ArrayList<Task> getListTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public void deleteAllTasks() {
        deleteAllTasksInPrioritizedTasks();
        clearHistory(tasks);
        tasks = new HashMap<>();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        if (task == null) {
            throw new NotFoundException();
        }
        historyManager.add(task);
        return task;
    }

    @Override
    public void add(Task task) {
        if (isHasTimeOverlapWithAny(task)) {
            return;
        }
        task.setId(nextId++);
        tasks.put(task.getId(), task);
        addPrioritizedTasks(task);
    }

    @Override
    public void update(Task task) {
        if (isHasTimeOverlapWithAny(task)) {
            return;
        }
        tasks.put(task.getId(), task);
        updateInPrioritizedTasks(task);
    }

    @Override
    public void deleteTaskById(int id) {
        historyManager.remove(tasks.get(id));
        tasks.remove(id);
        deleteByIdInPrioritizedTasks(id);
    }

    @Override
    public ArrayList<Epic> getListEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public void deleteAllEpics() {
        deleteAllSubtasksInPrioritizedTasks();
        clearHistory(epics);
        clearHistory(subtasks);
        subtasks = new HashMap<>();
        epics = new HashMap<>();
    }

    @Override
    public Epic getEpicById(int idEpic) {
        Epic epic = epics.get(idEpic);
        historyManager.add(epic);
        return epic;
    }

    @Override
    public void add(Epic epic) {
        epic.setId(nextId++);
        epics.put(epic.getId(), epic);
        changeStatusEpic(epic);
        checkTheCompletionTimeEpic(epic);
    }

    @Override
    public void update(Epic epic) {
        epics.put(epic.getId(), epic);
        changeStatusEpic(epic);
        checkTheCompletionTimeEpic(epic);
    }

    @Override
    public void deleteEpicById(int idEpic) {
        epics.get(idEpic).getListSubtasks()
                        .forEach(idSubtask -> {
                            subtasks.remove(idSubtask);
                            historyManager.remove(subtasks.get(idSubtask));
                            deleteByIdInPrioritizedTasks(idSubtask);
                        });
        historyManager.remove(epics.get(idEpic));
        epics.remove(idEpic);
    }

    @Override
    public List<Subtask> getListEpicSubtasksById(int idEpic) {
        return epics.get(idEpic).getListSubtasks().stream()
                .filter(idSubtask -> subtasks.containsKey(idSubtask))
                .map(idSubtask -> subtasks.get(idSubtask))
                .collect(Collectors.toList());
    }

    @Override
    public ArrayList<Subtask> getListSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void deleteAllSubtasks() {
        getListEpics().forEach(epic -> {
            epic.setListSubtasks(new ArrayList<>());
            changeStatusEpic(epic);
            checkTheCompletionTimeEpic(epic);
        });

        clearHistory(subtasks);
        subtasks = new HashMap<>();
        deleteAllSubtasksInPrioritizedTasks();
    }

    @Override
    public Subtask getSubtaskById(int idSubtasks) {
        Subtask subtask = subtasks.get(idSubtasks);
        historyManager.add(subtask);
        return subtask;
    }

    @Override
    public void add(Subtask subtask) {
        if (isHasTimeOverlapWithAny(subtask)) {
            return;
        }
        Epic epic = epics.get(subtask.getIdEpic());
        int idEpic = epic.getId();
        int idSubtask = nextId++;

        subtask.setId(idSubtask);
        subtasks.put(idSubtask, subtask);

        addIdSubtaskToEpic(idEpic, idSubtask);
        changeStatusEpic(epic);
        checkTheCompletionTimeEpic(epic);
        addPrioritizedTasks(subtask);
    }

    @Override
    public void update(Subtask subtask) {
        if (isHasTimeOverlapWithAny(subtask)) {
            return;
        }
        Epic epic = epics.get(subtask.getIdEpic());

        subtasks.put(subtask.getId(), subtask);
        changeStatusEpic(epic);
        checkTheCompletionTimeEpic(epic);
        updateInPrioritizedTasks(subtask);
    }

    @Override
    public void deleteSubtaskById(int idSubtask) {
        Subtask subtask = subtasks.get(idSubtask);
        Epic epic = epics.get(subtask.getIdEpic());

        historyManager.remove(subtask);

        ArrayList<Integer> listSubtasks = epic.getListSubtasks();
        int indexToRemove = listSubtasks.indexOf(idSubtask);

        if (indexToRemove != -1) {
            listSubtasks.remove(indexToRemove);
        }
        changeStatusEpic(epic);
        checkTheCompletionTimeEpic(epic);
        subtasks.remove(idSubtask);
        deleteByIdInPrioritizedTasks(idSubtask);
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
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

        if (subtasks.isEmpty()) {
            return;
        }
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

    private <T extends Task> void clearHistory(HashMap<Integer, T> map) {
        for (T task : map.values()) {
            historyManager.remove(task);
        }
    }

    private void checkTheCompletionTimeEpic(Epic epic) {
        changeFirstTimeEpic(epic);
        changeDurationEpic(epic);
        changeEndTimeEpic(epic);
    }

    private void changeEndTimeEpic(Epic epic) {
        Optional<LocalDateTime> maxEndTime = epic.getListSubtasks().stream()
                .map(idSubtask -> subtasks.get(idSubtask))
                .filter(Objects::nonNull)
                .filter(subtask -> subtask.getStartTime() != null)
                .map(Subtask::getEndTime)
                .filter(Objects::nonNull)
                .max(LocalDateTime::compareTo);

        maxEndTime.ifPresent(epic::setEndTime);
    }

    private void changeDurationEpic(Epic epic) {
        Duration sumDuration =  epic.getListSubtasks().stream()
                .map(idSubtask -> subtasks.get(idSubtask))
                .filter(Objects::nonNull)
                .map(Subtask::getDuration)
                .filter(Objects::nonNull)
                .reduce(Duration.ZERO, Duration::plus);

        epic.setDuration(sumDuration);
    }

    private void changeFirstTimeEpic(Epic epic) {
        Optional<LocalDateTime> minEndTime = epic.getListSubtasks().stream()
                .map(idSubtask -> subtasks.get(idSubtask))
                .filter(Objects::nonNull)
                .map(Subtask::getStartTime)
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo);

        minEndTime.ifPresent(epic::setStartTime);
    }

    private void deleteAllTasksInPrioritizedTasks() {
        getListTasks().forEach(task -> prioritizedTasks.remove(task));
    }

    private void deleteAllSubtasksInPrioritizedTasks() {
        getListSubtasks().forEach(task -> prioritizedTasks.remove(task));
    }

    private void addPrioritizedTasks(Task task) {
        prioritizedTasks.add(task);
    }

    private void updateInPrioritizedTasks(Task task) {
        deleteByIdInPrioritizedTasks(task.getId());
        addPrioritizedTasks(task);
    }

    private void deleteByIdInPrioritizedTasks(int id) {
        prioritizedTasks.removeIf(t -> t.getId() == id);
    }

    private boolean isTheseTasksOverlapInTime(Task task1, Task task2) {
        if (task1.getEndTime() == null || task2.getEndTime() == null) {
            return false;
        }

        return task1.getStartTime().isBefore(task2.getEndTime()) &&
                task2.getStartTime().isBefore(task1.getEndTime());
    }

    private boolean isHasTimeOverlapWithAny(Task taskToCheck) {
        return getPrioritizedTasks().stream()
                .filter(task -> !task.equals(taskToCheck))
                .anyMatch(task -> isTheseTasksOverlapInTime(task, taskToCheck));
    }
}
