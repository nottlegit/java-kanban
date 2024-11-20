import manager.TaskManager;
import task.tracker.Epic;
import task.tracker.Status;
import task.tracker.Subtask;
import task.tracker.Task;

public class Main {

    public static void main(String[] args) {
        TaskManager manager = new TaskManager();
        Task task1 = new Task("Ремонт", "Описание..", Status.DONE);
        Task task2 = new Task("Уборка", "Описание..", Status.NEW);
        Task task3 = new Task("Переезд", "Описание..", Status.NEW);

        System.out.println("Добавляем первые 2 задачи");
        manager.addTask(task1);
        manager.addTask(task2);
        System.out.println(manager.getListTasks());

        System.out.println("\nОбновляем 1ю задачу, вместо нее теперь 3я");
        task3.setId(task1.getId());
        manager.updateTask(task3);
        System.out.println(manager.getListTasks());

        System.out.println("\nПолучаем задачу по id");
        System.out.println(manager.getTaskById(task2.getId()));

        System.out.println("\nУдаляем 3ю задачу по id");
        manager.deleteTaskById(task3.getId());
        System.out.println(manager.getListTasks());

        System.out.println("\nУдаляем все задачи");
        manager.deleteAllTasks();
        System.out.println(manager.getListTasks());
        //---------------------------------------
        Epic epic1 = new Epic(task1);
        Epic epic2 = new Epic(task2);
        Epic epic3 = new Epic(task3);

        System.out.println("\nДобавляем Epics");
        manager.addEpic(epic1);
        System.out.println(manager.getListEpics());

        System.out.println("\nДобавляем subtask");
        Subtask subtask1 = new Subtask("Купить материалы", "Описание..", Status.NEW, epic1.getId());
        Subtask subtask2 = new Subtask("Нанять строителей", "Описание..", Status.NEW, epic1.getId());
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        System.out.println(manager.getListSubtasks());
        System.out.println("Subtasks с id 3 и 4 добавлены в эпик");
        System.out.println(manager.getListEpics());

        System.out.println("\nДобавляем эпики и удаляем другой по id");
        manager.addEpic(epic2);
        manager.addEpic(epic3);
        manager.deleteEpicById(epic1.getId());
        System.out.println(manager.getListEpics());
        System.out.println("Эпика 'Ремонт' нет, также удалены его подзадачи");
        System.out.println(manager.getListSubtasks());

        System.out.println("\nДобавляем новые подзадачи в менеджер");
        subtask1 = new Subtask("Subtask1", "Описание..", Status.NEW, epic2.getId());
        subtask2 = new Subtask("Subtask2", "Описание..", Status.NEW, epic2.getId());
        Subtask subtask3 = new Subtask("Subtask3", "Описание..", Status.NEW, epic3.getId());
        Subtask subtask4 = new Subtask("Subtask4", "Описание..", Status.NEW, epic3.getId());
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        manager.addSubtask(subtask3);
        manager.addSubtask(subtask4);
        System.out.println(manager.getListSubtasks());
        System.out.println(manager.getListEpics());

        System.out.printf("\nПолучить эпик по id=%s%n", epic2.getId());
        System.out.println(manager.getEpicById(epic2.getId()));

        System.out.println("\nПолучить список subtasks");
        System.out.println(manager.getListEpicSubtasksById(epic2.getId()));

        System.out.println("\nПолучить subtasks по id");
        System.out.println(manager.getSubtaskById(epic2.getId()));

        System.out.println("\nОбновление Эпика");
        epic2.setTitle("Epic2");
        manager.updateEpic(epic2);
        System.out.println(manager.getListEpics());

        System.out.println("\nОбновление подзадачи");
        subtask1.setStatus(Status.IN_PROGRESS);
        manager.updateSubtask(subtask1);
        System.out.println(manager.getListSubtasks());
        System.out.println(manager.getListEpics());

        System.out.printf("\nУдаление подзадачи по id=%s%n", subtask1.getId());
        manager.deleteSubtaskById(subtask1.getId());
        System.out.println(manager.getListSubtasks());
        System.out.println(manager.getListEpics());

        System.out.println("\nУдаление всех подзадач");
        manager.deleteAllSubtasks();
        System.out.println(manager.getListEpics());
        System.out.println(manager.getListSubtasks());

        System.out.println("\nУдаление всех эпиков");
        manager.deleteAllEpics();
        System.out.println(manager.getListEpics());


    }
}
