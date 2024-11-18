import task.tracker.*;
import manager.Manager;

public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();
        Task task1 = new Task("Переезд");
        Task task2 = new Task("Переезд");
        Epic epic1 = new Epic("Сборка пк");
        Epic epic2 = new Epic("Переезд");
        epic1.addNewSubtask("Купить запчасти");

        manager.addTask(task1);
        manager.addTask(epic1);

        System.out.println(manager.getListTasks());

        System.out.println(task1.equals(epic2));

        System.out.println(task1.hashCode());
        System.out.println(task2.hashCode());
        System.out.println(epic1.hashCode());
        System.out.println(epic2.hashCode());

        System.out.println(manager.getListEpicSubtasks(TaskStatus.NEW, epic1.hashCode()));
    }
}
