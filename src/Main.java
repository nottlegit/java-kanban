import task.tracker.*;
import manager.Manager;

public class Main {

    public static void main(String[] args) {
        //TODO нужно заменить ключ в мапе с hashCode UUID - сформированный для каждого объектах см. телегу
        Manager manager = new Manager();
        Task task1 = new Task("Переезд!!");
        Task task2 = new Task("Переезд");
        Epic epic1 = new Epic("Сборка пк");
        Epic epic2 = new Epic("Переезд");


        Subtask subtask1 = manager.addSubtaskInEpic(epic1, "Купить запчасти");
        Subtask subtask2 = manager.addSubtaskInEpic(epic1, "Купить запчасти");
        Subtask subtask3 = manager.addSubtaskInEpic(epic1, "Купить моник");

        manager.addTask(task1);
        manager.addTask(epic1);
        //manager.deleteById(task1.getTaskStatus(),task1.hashCode());
        //System.out.println(manager.getListTasks());

        System.out.println(task1.equals(epic2));

        System.out.println(task1.hashCode());
        System.out.println(task2.hashCode());
        System.out.println(epic1.hashCode());
        System.out.println(epic2.hashCode());

        //System.out.println(manager.getListEpicSubtasks(TaskStatus.NEW, epic1.hashCode()));
        //System.out.println(manager.getTaskById(TaskStatus.NEW, -280801830));

        manager.setStatusTask(TaskStatus.DONE, subtask1);
        //manager.setStatusTask(TaskStatus.IN_PROGRESS, task1);
        System.out.println(manager.getListTasks());


    }
}
