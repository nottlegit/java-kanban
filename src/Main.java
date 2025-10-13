import managers.FileBackedTaskManager;
import managers.InMemoryTaskManager;
import managers.TaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;
import util.Managers;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;

public class Main {

    public static void main(String[] args) {
        TaskManager manager = new InMemoryTaskManager();
        Random random = new Random();
        LocalDateTime localDateTime = LocalDateTime.now();

        Task task1 = new Task("Ремонт", "Описание..",
                Status.DONE, Duration.ofMinutes(random.nextInt(100)), localDateTime.minus(Duration.ofDays(100)));
        Task task2 = new Task("Уборка", "Описание..",
                Status.NEW, Duration.ofMinutes(random.nextInt(100)), localDateTime.minus(Duration.ofDays(95)));
        Task task3 = new Task("Переезд", "Описание..", Status.NEW,
                Duration.ofMinutes(random.nextInt(100)), localDateTime.minus(Duration.ofDays(90)));

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
        Subtask subtask1 = new Subtask("Купить материалы", "Описание..", Status.NEW, epic1.getId(),
                Duration.ofMinutes(random.nextInt(100)), localDateTime.minus(Duration.ofDays(85)));
        Subtask subtask2 = new Subtask("Нанять строителей", "Описание..", Status.NEW, epic1.getId(),
                Duration.ofMinutes(random.nextInt(100)), localDateTime.minus(Duration.ofDays(80)));
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
        subtask1 = new Subtask("Subtask1", "Описание..", Status.NEW, epic2.getId(),
                Duration.ofMinutes(random.nextInt(100)), localDateTime.minus(Duration.ofDays(75)));
        subtask2 = new Subtask("Subtask2", "Описание..", Status.NEW, epic2.getId(),
                Duration.ofMinutes(random.nextInt(100)), localDateTime.minus(Duration.ofDays(70)));
        Subtask subtask3 = new Subtask("Subtask3", "Описание..", Status.NEW, epic3.getId(),
                Duration.ofMinutes(random.nextInt(100)), localDateTime.minus(Duration.ofDays(65)));
        Subtask subtask4 = new Subtask("Subtask4", "Описание..", Status.NEW, epic3.getId(),
                Duration.ofMinutes(random.nextInt(100)), localDateTime.minus(Duration.ofDays(60)));
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
        manager.getTaskById(task2.getId());
        manager.getEpicById(epic2.getId());
        manager.getTaskById(task3.getId());
        manager.getEpicById(epic3.getId());
        manager.getTaskById(task3.getId());
        manager.getEpicById(epic1.getId());
        manager.getTaskById(task1.getId());
        manager.getEpicById(epic1.getId());
        manager.getTaskById(task1.getId());
        manager.getEpicById(epic1.getId());
        manager.deleteEpicById(epic1.getId());
        System.out.println(manager.getHistory());
        System.out.println(manager.getHistory().size());
        System.out.println(manager.getListEpics());
        // Спринт 7
        System.out.println("--------------------------------------------------");
        System.out.println("Спринт 7");
        System.out.println("Проверка работоспособности нового менеджера задач");

        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager();

        Task task71 = new Task("Ремонт", "Описание..", Status.DONE,
                Duration.ofMinutes(random.nextInt(100)), localDateTime.minus(Duration.ofDays(55)));
        Task task72 = new Task("Уборка", "Описание..", Status.NEW,
                Duration.ofMinutes(random.nextInt(100)), localDateTime.minus(Duration.ofDays(50)));
        Task task73 = new Task("Переезд", "Описание..", Status.NEW,
                Duration.ofMinutes(random.nextInt(100)), localDateTime.minus(Duration.ofDays(45)));

        System.out.println("Добавляем первые задачи");
        fileBackedTaskManager.add(task71);
        fileBackedTaskManager.add(task72);
        fileBackedTaskManager.add(task73);
        fileBackedTaskManager.deleteTaskById(task72.getId());
        task72.setId(task1.getId());
        fileBackedTaskManager.update(task72);

        Epic epic71 = new Epic(task71);
        Epic epic72 = new Epic(task72);
        Epic epic73 = new Epic(task73);

        fileBackedTaskManager.add(epic71);
        fileBackedTaskManager.add(epic72);
        fileBackedTaskManager.add(epic73);
        fileBackedTaskManager.deleteEpicById(epic73.getId());

        Subtask subtask71 = new Subtask("Купить материалы", "Описание..", Status.NEW, epic71.getId(),
                Duration.ofMinutes(random.nextInt(100)), localDateTime.minus(Duration.ofDays(40)));
        Subtask subtask72 = new Subtask("Нанять строителей", "Описание..", Status.NEW, epic71.getId(),
                Duration.ofMinutes(random.nextInt(100)), localDateTime.minus(Duration.ofDays(35)));
        fileBackedTaskManager.add(subtask71);
        fileBackedTaskManager.add(subtask72);

        subtask71 = new Subtask("Subtask71", "Описание..", Status.NEW, epic72.getId(),
                Duration.ofMinutes(random.nextInt(100)), localDateTime.minus(Duration.ofDays(30)));
        subtask72 = new Subtask("Subtask72", "Описание..", Status.NEW, epic72.getId(),
                Duration.ofMinutes(random.nextInt(100)), localDateTime.minus(Duration.ofDays(25)));
        fileBackedTaskManager.add(subtask71);
        fileBackedTaskManager.add(subtask72);
        fileBackedTaskManager.deleteSubtaskById(subtask71.getId());

        System.out.println();
        System.out.println("Проверка восстановления состояния из файла");
        System.out.println("Состояние до \n");
        System.out.println(fileBackedTaskManager.getListTasks());
        System.out.println();
        System.out.println(fileBackedTaskManager.getListSubtasks());
        System.out.println();
        System.out.println(fileBackedTaskManager.getListEpics());

        FileBackedTaskManager newFileBackedTaskManager = fileBackedTaskManager.loadFromFile(
                new File("manager_status.csv")
        );

        System.out.println();
        System.out.println("Состояние после");
        System.out.println(newFileBackedTaskManager.getListTasks());
        System.out.println();
        System.out.println(newFileBackedTaskManager.getListSubtasks());
        System.out.println();
        System.out.println(newFileBackedTaskManager.getListEpics());


        System.out.println("---------------------------------------------");
        System.out.println("Спринт 8");

        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

        Task task81 = new Task("task81", "description",
                Status.NEW, Duration.ofMinutes(10), localDateTime.minus(Duration.ofDays(10)));
        Task task82 = new Task("task82", "description",
                Status.NEW, Duration.ofMinutes(10), localDateTime.minus(Duration.ofDays(1)));
        Epic epic81 = new Epic("epic81", "description", Status.NEW);
        Epic epic82 = new Epic("epic82", "description", Status.NEW);

        inMemoryTaskManager.add(task81);
        inMemoryTaskManager.add(task82);
        inMemoryTaskManager.add(epic81);
        inMemoryTaskManager.add(epic82);

        System.out.println("проверяем состояние эпика (времени выполнения нет)");
        System.out.println(inMemoryTaskManager.getListEpics());
        System.out.println();

        Subtask subtask81 = new Subtask("subtask81", "description",
                Status.NEW, 3, Duration.ofMinutes(1440), localDateTime.minus(Duration.ofDays(8)));
        Subtask subtask82 = new Subtask("subtask82", "description",
                Status.NEW, 3, Duration.ofMinutes(1440), localDateTime.minus(Duration.ofDays(2)));

        inMemoryTaskManager.add(subtask81);
        inMemoryTaskManager.add(subtask82);

        System.out.println("проверяем состояние эпика (должно появиться startTime && endTime");
        System.out.println(inMemoryTaskManager.getListEpics());
        System.out.println();

        Task task83 = new Task("task83", "description",
                Status.NEW,Duration.ofMinutes(10), localDateTime.minus(Duration.ofDays(1)));

        inMemoryTaskManager.add(task83);

        System.out.println("Задачи должны быть в правильном порядке");
        System.out.println("И task83 не добавилась");
        System.out.println(inMemoryTaskManager.getPrioritizedTasks());
        System.out.println(inMemoryTaskManager.getListTasks());
    }
}
