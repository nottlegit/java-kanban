import managers.InMemoryHistoryManager;
import managers.InMemoryTaskManager;
import managers.TaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;
import util.Managers;

import java.util.List;
import java.util.Objects;

public class Main {

    public static void main(String[] args) {
        TaskManager manager = new InMemoryTaskManager();
        Task task1 = new Task("Ремонт", "Описание..", Status.DONE);
        Task task2 = new Task("Уборка", "Описание..", Status.NEW);
        Task task3 = new Task("Переезд", "Описание..", Status.NEW);

        System.out.println("Добавляем первые 2 задачи");
        manager.add(task1);
        manager.add(task2);
        System.out.println(manager.getListTasks());

        System.out.println("\nОбновляем 1ю задачу, вместо нее теперь 3я");
        task3.setId(task1.getId());
        manager.update(task3);
        System.out.println(manager.getListTasks());

        System.out.println("\nПолучаем задачу по id");
        System.out.println(manager.getTaskById(task2.getId()));

        System.out.println("\nУдаляем 3ю задачу по id");
        manager.deleteTaskById(task3.getId());
        System.out.println(manager.getListTasks());

        System.out.println("\nУдаляем все задачи");
        manager.deleteAllTasks();
        System.out.println(manager.getListTasks());

        Epic epic1 = new Epic(task1);
        Epic epic2 = new Epic(task2);
        Epic epic3 = new Epic(task3);

        System.out.println("\nДобавляем Epics");
        manager.add(epic1);
        System.out.println(manager.getListEpics());

        System.out.println("\nДобавляем subtask");
        Subtask subtask1 = new Subtask("Купить материалы", "Описание..", Status.NEW, epic1.getId());
        Subtask subtask2 = new Subtask("Нанять строителей", "Описание..", Status.NEW, epic1.getId());
        manager.add(subtask1);
        manager.add(subtask2);
        System.out.println(manager.getListSubtasks());
        System.out.println("Subtasks с id 3 и 4 добавлены в эпик");
        System.out.println(manager.getListEpics());

        System.out.println("\nДобавляем эпики и удаляем другой по id");
        manager.add(epic2);
        manager.add(epic3);
        manager.deleteEpicById(epic1.getId());
        System.out.println(manager.getListEpics());
        System.out.println("Эпика 'Ремонт' нет, также удалены его подзадачи");
        System.out.println(manager.getListSubtasks());

        System.out.println("\nДобавляем новые подзадачи в менеджер");
        subtask1 = new Subtask("Subtask1", "Описание..", Status.NEW, epic2.getId());
        subtask2 = new Subtask("Subtask2", "Описание..", Status.NEW, epic2.getId());
        Subtask subtask3 = new Subtask("Subtask3", "Описание..", Status.NEW, epic3.getId());
        Subtask subtask4 = new Subtask("Subtask4", "Описание..", Status.NEW, epic3.getId());
        manager.add(subtask1);
        manager.add(subtask2);
        manager.add(subtask3);
        manager.add(subtask4);
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
        manager.update(epic2);
        System.out.println(manager.getListEpics());

        System.out.println("\nОбновление подзадачи");
        subtask1.setStatus(Status.IN_PROGRESS);
        manager.update(subtask1);
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
        manager.add(subtask1);
        manager.add(subtask2);
        manager.add(subtask3);
        manager.add(subtask4);
        manager.deleteAllEpics();
        System.out.println(manager.getListEpics());
        System.out.println(manager.getListSubtasks());
        // sprint5
        System.out.println("---------------------------");
        manager = Managers.getDefault();
        manager.add(task1);
        manager.add(task2);
        manager.add(task3);
        manager.add(epic1);
        manager.add(epic2);
        manager.add(epic3);

        manager.getTaskById(task1.getId());
        manager.getEpicById(epic1.getId());
        manager.getTaskById(task1.getId());
        manager.getEpicById(epic1.getId());
        manager.getTaskById(task1.getId());
        manager.getEpicById(epic1.getId());
        manager.getTaskById(task1.getId());
        manager.getEpicById(epic1.getId());
        manager.getTaskById(task1.getId());
        manager.getEpicById(epic1.getId());
        manager.getTaskById(task1.getId());
        manager.getEpicById(epic1.getId());
        manager.deleteEpicById(epic1.getId());
        System.out.println(manager.getHistory());
        System.out.println(manager.getHistory().size());

        System.out.println("////////////////");
        System.out.println("////////////////");
        System.out.println();

        InMemoryHistoryManager manager1 = new InMemoryHistoryManager();

        Task task11 = new Task("11", "Описание..", Status.DONE);
        Task task12 = new Task("12", "Описание..", Status.DONE);
        Task task13 = new Task("13", "Описание..", Status.DONE);

        task11.setId(11);
        task12.setId(12);
        task13.setId(13);

        manager1.add(task11);
        manager1.add(task12);
        manager1.add(task13);
        manager1.add(task13);

        System.out.println(manager1.getHistory());
        System.out.println(manager1.getListNodes());


        System.out.println("33333333");
        manager1.printNodes();
    }
}
