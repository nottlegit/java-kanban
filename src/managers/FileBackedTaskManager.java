package managers;

import managers.exceptions.ManagerSaveException;
import tasks.*;

import java.io.*;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {
    private static final String FILENAME = "manager_status.csv";

    public FileBackedTaskManager() {
        super();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void add(Task task) {
        super.add(task);
        save();
    }

    @Override
    public void update(Task task) {
        super.update(task);
        save();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void add(Epic epic) {
        super.add(epic);
        save();
    }

    @Override
    public void update(Epic epic) {
        super.update(epic);
        save();
    }

    @Override
    public void deleteEpicById(int idEpic) {
        super.deleteEpicById(idEpic);
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public void add(Subtask subtask) {
        super.add(subtask);
        save();
    }

    @Override
    public void update(Subtask subtask) {
        super.update(subtask);
        save();
    }

    @Override
    public void deleteSubtaskById(int idSubtask) {
        super.deleteSubtaskById(idSubtask);
        save();
    }

    private void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILENAME))){
            writer.write("id,type,name,status,description,epic\n");

            for (Task task : getListTasks()) {
                writer.write(toStringTask(task));
            }

            for (Subtask subtask : getListSubtasks()) {
                writer.write(toStringTask(subtask));
            }

            for (Epic epic : getListEpics()) {
                writer.write(toStringTask(epic));
            }

        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка во время записи файла.");
        }
    }

    private String toStringTask(Task task) {
        TypesOfTasks type = TypesOfTasks.TASK;
        String name = "Task";
        String epic = "";
        StringBuilder str = new StringBuilder();


        if (task instanceof Epic) {
            type = TypesOfTasks.EPIC;
            name = "Epic";
        } else if (task instanceof Subtask) {
            type = TypesOfTasks.SUBTASK;
            name = "Subtask";
            Subtask subtask = (Subtask) task;
            epic = String.format("%d", subtask.getIdEpic());
        }

        str.append(String.format("%d,%S,%s,%s,%s,",
                task.getId(),
                type,
                name + task.getId(),
                task.getStatus(),
                task.getDescription()
        ));

        if (!epic.isEmpty()) {
            str.append(String.format("%s,", epic));
        }

        return str.append("\n").toString();
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            reader.readLine();
            while ((line = reader.readLine()) != null) {
                addTaskFromList(List.of(line.split(",")), fileBackedTaskManager);
            }
            fileBackedTaskManager.changeListSubtaskInEpics();
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка во время чтения файла.");
        }

        return fileBackedTaskManager;
    }

    private void changeListSubtaskInEpics() {
        for (Subtask subtask : getListSubtasks()) {
            Epic epic = getEpicById(subtask.getIdEpic());

            epic.getListSubtasks().add(subtask.getId());
        }
    }

    private void addFromFile(Task task) {
        nextId++;
        tasks.put(task.getId(), task);
    }

    private void addFromFile(Epic epic) {
        nextId++;
        epics.put(epic.getId(), epic);
    }

    private void addFromFile(Subtask subtask) {
        nextId++;
        subtasks.put(subtask.getId(), subtask);
    }

    private static void addTaskFromList(List<String> list, FileBackedTaskManager fileBackedTaskManager) {
        int id = Integer.parseInt(list.get(0));
        TypesOfTasks type = TypesOfTasks.valueOf(list.get(1));
        String description = list.get(4);
        Status status = Status.valueOf(list.get(3));

        switch (type) {
            case TASK:
                fileBackedTaskManager.addFromFile(new Task(id, "", description, status));
                break;
            case EPIC:
                fileBackedTaskManager.addFromFile(new Epic(id, "", description, status));
                break;
            case SUBTASK:
                int epicId = Integer.parseInt(list.get(5));
                fileBackedTaskManager.addFromFile(new Subtask(id, "", description, status, epicId));
                break;
            default:
                throw new IllegalArgumentException("Неизвестный тип задачи: " + type);
        }
    }
}
