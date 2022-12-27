import http.HttpTaskManagerClient;
import http.HttpTaskServer;
import http.KVServer;
import manager.TaskManager;
import task.Epic;
import task.Subtask;
import task.Task;
import task.TaskStatus;

import java.io.IOException;
import java.util.Collection;

public class Main_2 {

    static HttpTaskServer startHttpServer()
    {
        try {
            HttpTaskServer httpServer = new HttpTaskServer();
            httpServer.start();
            return httpServer;
        } catch (IOException | InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }

    static void checkEpicStatus() {
        HttpTaskServer httpTaskServer = startHttpServer();
        System.out.println("checkEpicStatus");
        TaskManager manager = new HttpTaskManagerClient();
        Epic epic1 = new Epic("Эпик №1", "Эпик");
        manager.addEpic(epic1);

// нет ни одной
        if (epic1.getStatus() != TaskStatus.NEW) {
            System.err.println("Неверный статус у эпика: " + epic1.getStatus() + ", ожидалось: " + TaskStatus.NEW);
        }

        Subtask subtask11 = new Subtask("Эпик1 Подзадача1", "Подзадача 1.1", TaskStatus.NEW, epic1.getId());
        manager.addSubtask(subtask11);
        Subtask subtask12 = new Subtask("Эпик1 Подзадача2", "Подзадача 2.1", TaskStatus.NEW, epic1.getId());
        manager.addSubtask(subtask12);
        Subtask subtask13 = new Subtask("Эпик1 Подзадача3", "Подзадача 3.1",
                TaskStatus.NEW, epic1.getId());
        manager.addSubtask(subtask13);

// N N N
        epic1 = manager.getEpic(epic1.getId());
        if (epic1.getStatus() != TaskStatus.NEW) {
            System.err.println("Неверный статус у эпика: " + epic1.getStatus() + ", ожидалось: " + TaskStatus.NEW);
        }

        subtask12.setStatus(TaskStatus.IN_PROGRESS);
        manager.updateSubtask(subtask12);

// N I_P N
        epic1 = manager.getEpic(epic1.getId());
        if (epic1.getStatus() != TaskStatus.IN_PROGRESS) {
            System.err.println("Неверный статус у эпика: " + epic1.getStatus() + ", ожидалось: " + TaskStatus.IN_PROGRESS);
        }

        subtask12.setStatus(TaskStatus.DONE);
        manager.updateSubtask(subtask12);

// N D N
        epic1 = manager.getEpic(epic1.getId());
        if (epic1.getStatus() != TaskStatus.IN_PROGRESS) {
            System.err.println("Неверный статус у эпика: " + epic1.getStatus() + ", ожидалось: " + TaskStatus.IN_PROGRESS);
        }

        subtask11.setStatus(TaskStatus.IN_PROGRESS);
        manager.updateSubtask(subtask11);

// I_P D N
        epic1 = manager.getEpic(epic1.getId());
        if (epic1.getStatus() != TaskStatus.IN_PROGRESS) {
            System.err.println("Неверный статус у эпика: " + epic1.getStatus() + ", ожидалось: " + TaskStatus.IN_PROGRESS);

        }

        subtask12.setStatus(TaskStatus.IN_PROGRESS);
        manager.updateSubtask(subtask12);
        subtask13.setStatus(TaskStatus.IN_PROGRESS);
        manager.updateSubtask(subtask13);

// I_P I_P I_P
        epic1 = manager.getEpic(epic1.getId());
        if (epic1.getStatus() != TaskStatus.IN_PROGRESS) {
            System.err.println("Неверный статус у эпика: " + epic1.getStatus() + ", ожидалось: " + TaskStatus.IN_PROGRESS);
        }

        subtask13.setStatus(TaskStatus.DONE);
        manager.updateSubtask(subtask13);

// I_P I_P D
        epic1 = manager.getEpic(epic1.getId());
        if (epic1.getStatus() != TaskStatus.IN_PROGRESS) {
            System.err.println("Неверный статус у эпика: " + epic1.getStatus() + ", ожидалось: " +
                    TaskStatus.IN_PROGRESS);
        }

        subtask11.setStatus(TaskStatus.DONE);
        manager.updateSubtask(subtask11);
        subtask12.setStatus(TaskStatus.DONE);
        manager.updateSubtask(subtask12);

// D D D
        epic1 = manager.getEpic(epic1.getId());
        if (epic1.getStatus() != TaskStatus.DONE) {
            System.err.println("Неверный статус у эпика: " + epic1.getStatus() + ", ожидалось: " + TaskStatus.DONE);
        }

        httpTaskServer.stop();
    }

    static void checkAdding()  {

        HttpTaskServer httpTaskServer = startHttpServer();
        System.out.println("checkAdding");
        TaskManager manager = new HttpTaskManagerClient();

        Task taskFirst = new Task("Поесть", "Принять пищу", TaskStatus.NEW);
        manager.addTask(taskFirst);
        Task task = manager.getTask(taskFirst.getId());
        if (task == null || !task.equals(taskFirst)) {
            System.err.println("Ошибка при добавлении задачи либо при запросе задачи");
        }
        Task taskSecond = new Task("Отдохнуть", "Поспать в тихий час", TaskStatus.NEW);
        manager.addTask(taskSecond);

        Collection<Task> collTasks = manager.getTasks();
        System.out.println("**************** Распечатывается список задач");
        System.out.println(collTasks);
        if (collTasks == null || collTasks.size() != 2) {
            System.err.println("Ошибка при запросе всех задач");
        }

        Epic epicFirst = new Epic("Поесть", "Принять пищу");
        manager.addEpic(epicFirst);
        Epic epic = manager.getEpic(epicFirst.getId());
        if (epic == null || !epic.equals(epicFirst)) {
            System.err.println("Ошибка при добавлении эпика либо при запросе эпика");
        }
        Epic epicSecond = new Epic("Отдохнуть", "Поспать в тихий час");
        manager.addEpic(epicSecond);

        Collection<Epic> collEpics = manager.getEpics();
        System.out.println("**************** Распечатывается список эпиков");
        System.out.println(collEpics);
        if (collEpics == null || collEpics.size() != 2) {
            System.err.println("Ошибка при запросе всех эпиков");
        }

        Subtask subtaskFirst = new Subtask("Поесть", "Принять пищу", TaskStatus.DONE, epicFirst.getId());
        manager.addSubtask(subtaskFirst);
        Subtask subtask = manager.getSubtask(subtaskFirst.getId());
        if (subtask == null || !subtask.equals(subtaskFirst)) {
            System.err.println("Ошибка при добавлении подзадачи либо при запросе подзадачи");
        }
        epic = manager.getEpic(epicFirst.getId());
        if (epic.getStatus() != TaskStatus.DONE) {
            System.err.println("Не пересчитывается статус эпика при добавлении подзадачи");
        }
        Subtask subtaskSecond = new Subtask("Отдохнуть", "Поспать в тихий час", TaskStatus.NEW);
        manager.addSubtask(subtaskSecond);

        Collection<Subtask> collSubtasks = manager.getSubtasks();
        System.out.println("**************** Распечатывается список подзадач");
        System.out.println(collSubtasks);
        if (collSubtasks == null || collSubtasks.size() != 2) {
            System.err.println("Ошибка при запросе всех подзадач");
        }

        httpTaskServer.stop();
    }

    static void checkUpdating()  {

        HttpTaskServer httpTaskServer = startHttpServer();
        System.out.println("checkUpdating");
        TaskManager manager = new HttpTaskManagerClient();

        Task taskFirst = new Task("Поесть", "Принять пищу", TaskStatus.NEW);
        manager.addTask(taskFirst);
        taskFirst.setName("Поесть спокойно");
        taskFirst.setDescription("Принять пищу не торопясь");
        taskFirst.setStatus(TaskStatus.IN_PROGRESS);
        manager.updateTask(taskFirst);
        Task task = manager.getTask(taskFirst.getId());
        if (!task.equals(taskFirst)) {
            System.err.println("Ошибка при обновлении задачи");
        }

        Epic epicFirst = new Epic("Поесть", "Принять пищу");
        manager.addEpic(epicFirst);
        epicFirst.setName("Поесть спокойно");
        epicFirst.setDescription("Принять пищу не торопясь");
        manager.updateEpic(epicFirst);
        Epic epic = manager.getEpic(epicFirst.getId());
        if (!epic.equals(epicFirst)) {
            System.err.println("Ошибка при обновлении эпика");
        }

        Subtask subtaskFirst = new Subtask("Поесть", "Принять пищу", TaskStatus.NEW, epicFirst.getId());
        manager.addSubtask(subtaskFirst);
        subtaskFirst.setName("Поесть спокойно");
        subtaskFirst.setDescription("Принять пищу не торопясь");
        subtaskFirst.setStatus(TaskStatus.IN_PROGRESS);
        manager.updateSubtask(subtaskFirst);
        Subtask subtask = manager.getSubtask(subtaskFirst.getId());
        if (!subtask.equals(subtaskFirst)) {
            System.err.println("Ошибка при обновлении подзадачи");
        }
        epic = manager.getEpic(epicFirst.getId());
        if (epic.getStatus() != TaskStatus.IN_PROGRESS) {
            System.err.println("Ошибка: при обновлении подзадачи не пересчитывается статус ее эпика");
        }

        httpTaskServer.stop();
    }

    static void checkGettingSubtasksOfEpic()  {

        HttpTaskServer httpTaskServer = startHttpServer();
        System.out.println("checkGettingSubtasksOfEpic");
        TaskManager manager = new HttpTaskManagerClient();

        Epic epic1 = new Epic("Эпик №1", "Эпик");
        manager.addEpic(epic1);
        Subtask subtask11 = new Subtask("Эпик1 Подзадача1", "Подзадача 1.1", TaskStatus.DONE, epic1.getId());
        manager.addSubtask(subtask11);
        Subtask subtask12 = new Subtask("Эпик1 Подзадача2", "Подзадача 2.1", TaskStatus.IN_PROGRESS, epic1.getId());
        manager.addSubtask(subtask12);
        Subtask subtask13 = new Subtask("Эпик1 Подзадача3", "Подзадача 3.1", TaskStatus.NEW, epic1.getId());
        manager.addSubtask(subtask13);

        Collection<Subtask> collSubtasks = manager.getEpicSubtasks(epic1.getId());
        if (collSubtasks == null || collSubtasks.size() != 3) {
            System.err.println("Ошибка при запросе списка подзадач определенного эпика");
        }

        if (!collSubtasks.contains(subtask11)) {
            System.err.println("Ошибка: список подзадач определенного эпика - некоррректный");
        }
        if (!collSubtasks.contains(subtask12)) {
            System.err.println("Ошибка: список подзадач определенного эпика - некоррректный");
        }
        if (!collSubtasks.contains(subtask13)) {
            System.err.println("Ошибка: список подзадач определенного эпика - некоррректный");
        }

        httpTaskServer.stop();
    }

    static void checkDeleting()  {

        HttpTaskServer httpTaskServer = startHttpServer();
        System.out.println("checkDeleting");
        TaskManager manager = new HttpTaskManagerClient();

        Task task1 = new Task("Поесть", "Принять пищу", TaskStatus.NEW);
        manager.addTask(task1);
        Task task2 = new Task("Отдохнуть", "Поспать в тихий час", TaskStatus.NEW);
        manager.addTask(task2);
        Task task3 = new Task("Позаниматься спортом", "Посетить спортзал", TaskStatus.NEW);
        manager.addTask(task3);
        manager.deleteTask(task2.getId());
        Collection<Task> collTasks = manager.getTasks();
        if (collTasks.size() != 2 || collTasks.contains(task2)) {
            System.err.println("Удаление задачи работает неверно");
        }

        Epic epic1 = new Epic("Эпик №1", "Эпик");
        manager.addEpic(epic1);
        Epic epic2 = new Epic("Эпик №2", "Эпик");
        manager.addEpic(epic2);
        Epic epic3 = new Epic("Эпик №3", "Эпик");
        manager.addEpic(epic3);
        Subtask subtask11 = new Subtask("Эпик1 Подзадача1", "Подзадача 1.1", TaskStatus.NEW, epic1.getId());
        manager.addSubtask(subtask11);
        Subtask subtask12 = new Subtask("Эпик1 Подзадача2", "Подзадача 2.1", TaskStatus.NEW, epic1.getId());
        manager.addSubtask(subtask12);
        Subtask subtask13 = new Subtask("Эпик1 Подзадача3", "Подзадача 3.1", TaskStatus.NEW, epic1.getId());
        manager.addSubtask(subtask13);
        Subtask subtask21 = new Subtask("Эпик2 Подзадача1", "Подзадача 2.1", TaskStatus.NEW, epic2.getId());
        manager.addSubtask(subtask21);
        Subtask subtask22 = new Subtask("Эпик2 Подзадача2", "Подзадача 2.2", TaskStatus.DONE, epic2.getId());
        manager.addSubtask(subtask22);
        Subtask subtask31 = new Subtask("Эпик3 Подзадача1", "Подзадача 3.1", TaskStatus.NEW, epic3.getId());
        manager.addSubtask(subtask31);

        manager.deleteEpic(epic1.getId());
        Collection<Epic> collEpics = manager.getEpics();
        if (collEpics.size() != 2 || collEpics.contains(epic1)) {
            System.err.println("Удаление эпика работает неверно");
        }
        Collection<Subtask> collSubs = manager.getSubtasks();
        if (collSubs.size() != 3 || collSubs.contains(subtask11) ||
                collSubs.contains(subtask12) || collSubs.contains(subtask13)) {
            System.err.println("Удаление подзадач эпика при удалении эпика работает неверно");
        }

////////////////////////////////////

        manager.deleteSubtask(subtask22.getId());
        collSubs = manager.getSubtasks();
        if (collSubs.size() != 2 || collSubs.contains(subtask22)) {
            System.err.println("Удаление подзадачи работает неверно");
        }

        Collection<Subtask> collSubtasksOfEpic = manager.getEpicSubtasks(epic2.getId());
        if (collSubtasksOfEpic.size() != 1 || collSubtasksOfEpic.contains(subtask22)) {
            System.err.println("Удаление подзадачи из эпика при ее удалении из менеджера работает неверно");

        }

        Epic epic = manager.getEpic(epic2.getId());
        if (epic.getStatus() != TaskStatus.NEW) {
            System.err.println("При удалении подзадачи не пересчитывается статус её эпика");
        }

        httpTaskServer.stop();
    }

    static void checkDeletingAll()  {
        System.out.println("checkDeletingAll");
        checkDeletingAllTasksAndEpics();
        checkDeletingAllSubTasks();
    }

    static void checkDeletingAllTasksAndEpics()  {

        HttpTaskServer httpTaskServer = startHttpServer();
        System.out.println("checkDeletingAllTasksAndEpics");
        TaskManager manager = new HttpTaskManagerClient();

        Task task1 = new Task("Поесть", "Принять пищу", TaskStatus.NEW);
        manager.addTask(task1);
        Task task2 = new Task("Отдохнуть", "Поспать в тихий час", TaskStatus.NEW);
        manager.addTask(task2);
        Task task3 = new Task("Позаниматься спортом", "Посетить спортзал", TaskStatus.NEW);
        manager.addTask(task3);

        manager.deleteTasks();
        Collection<Task> collTasks = manager.getTasks();
        if (collTasks == null || collTasks.size() != 0) {
            System.err.println("Удаление всех задач работает неверно");
        }

////////////////////////

        Epic epic1 = new Epic("Эпик №1", "Эпик");
        manager.addEpic(epic1);
        Epic epic2 = new Epic("Эпик №2", "Эпик");
        manager.addEpic(epic2);
        Epic epic3 = new Epic("Эпик №3", "Эпик");
        manager.addEpic(epic3);
        Subtask subtask11 = new Subtask("Эпик1 Подзадача1", "Подзадача 1.1", TaskStatus.NEW, epic1.getId());
        manager.addSubtask(subtask11);
        Subtask subtask12 = new Subtask("Эпик1 Подзадача2", "Подзадача 2.1",
                TaskStatus.NEW, epic1.getId());
        manager.addSubtask(subtask12);
        Subtask subtask13 = new Subtask("Эпик1 Подзадача3", "Подзадача 3.1", TaskStatus.NEW, epic1.getId());
        manager.addSubtask(subtask13);
        Subtask subtask21 = new Subtask("Эпик2 Подзадача1", "Подзадача 2.1", TaskStatus.NEW, epic2.getId());
        manager.addSubtask(subtask21);
        Subtask subtask22 = new Subtask("Эпик2 Подзадача2", "Подзадача 2.2", TaskStatus.DONE, epic2.getId());
        manager.addSubtask(subtask22);
        Subtask subtask31 = new Subtask("Эпик3 Подзадача1", "Подзадача 3.1", TaskStatus.NEW, epic3.getId());
        manager.addSubtask(subtask31);

        manager.deleteEpics();
        Collection<Epic> collEpics = manager.getEpics();
        if (collEpics == null || collEpics.size() != 0) {
            System.err.println("Удаление всех эпиков работает неверно");
        }
        Collection<Subtask> collSubs = manager.getSubtasks();
        if (collSubs == null || collSubs.size() != 0) {
            System.err.println("Удаление всех подзадач при удалении всех эпиков работает неверно");
        }

        httpTaskServer.stop();
    }

    static void checkDeletingAllSubTasks()  {

        HttpTaskServer httpTaskServer = startHttpServer();
        System.out.println("checkDeletingAllSubTasks");
        TaskManager manager = new HttpTaskManagerClient();

        Epic epic1 = new Epic("Эпик №1", "Эпик");
        manager.addEpic(epic1);
        Epic epic2 = new Epic("Эпик №2", "Эпик");
        manager.addEpic(epic2);
        Epic epic3 = new Epic("Эпик №3", "Эпик");
        manager.addEpic(epic3);
        Subtask subtask11 = new Subtask("Эпик1 Подзадача1", "Подзадача 1.1", TaskStatus.NEW, epic1.getId());
        manager.addSubtask(subtask11);
        Subtask subtask12 = new Subtask("Эпик1 Подзадача2", "Подзадача 2.1", TaskStatus.NEW, epic1.getId());
        manager.addSubtask(subtask12);
        Subtask subtask13 = new Subtask("Эпик1 Подзадача3", "Подзадача 3.1", TaskStatus.NEW, epic1.getId());
        manager.addSubtask(subtask13);
        Subtask subtask21 = new Subtask("Эпик2 Подзадача1", "Подзадача 2.1", TaskStatus.NEW, epic2.getId());
        manager.addSubtask(subtask21);
        Subtask subtask22 = new Subtask("Эпик2 Подзадача2", "Подзадача 2.2", TaskStatus.DONE, epic2.getId());
        manager.addSubtask(subtask22);
        Subtask subtask31 = new Subtask("Эпик3 Подзадача1", "Подзадача 3.1", TaskStatus.NEW, epic3.getId());
        manager.addSubtask(subtask31);
        manager.deleteSubtasks();
        Collection<Subtask> collSubs = manager.getSubtasks();
        if (collSubs == null || collSubs.size() != 0) {
            System.err.println("Удаление всех подзадач работает неверно");
        }

        Collection<Epic> collEpics = manager.getEpics();
        for (Epic epic : collEpics) {
            Collection<Subtask> collSubtasksOfEpic = manager.getEpicSubtasks(epic.getId());
            if (collSubtasksOfEpic == null || collSubtasksOfEpic.size() != 0) {
                System.err.println("Удаление подзадач из эпиков при удалении всех подзадач из менеджера работает неверно");
            }
        }

        httpTaskServer.stop();
    }

    static void checkHistory()  {

        HttpTaskServer httpTaskServer = startHttpServer();
        System.out.println("checkHistory");
        TaskManager manager = new HttpTaskManagerClient();

        Task taskFirst = new Task("Поесть", "Принять пищу", TaskStatus.NEW);
        manager.addTask(taskFirst);
        Task taskSecond = new Task("Отдохнуть", "Поспать в тихий час", TaskStatus.NEW);
        manager.addTask(taskSecond);
        Epic epic1 = new Epic("Эпик №1", "Эпик");
        manager.addEpic(epic1);
        Epic epic2 = new Epic("Эпик №2", "Эпик");
        manager.addEpic(epic2);
        Epic epic3 = new Epic("Эпик №3", "Эпик");
        manager.addEpic(epic3);
        Subtask subtask11 = new Subtask("Эпик1 Подзадача1", "Подзадача 1.1", TaskStatus.NEW, epic1.getId());
        manager.addSubtask(subtask11);
        Subtask subtask12 = new Subtask("Эпик1 Подзадача2", "Подзадача 2.1", TaskStatus.NEW, epic1.getId());
        manager.addSubtask(subtask12);
        Subtask subtask13 = new Subtask("Эпик1 Подзадача3", "Подзадача 3.1", TaskStatus.NEW, epic1.getId());
        manager.addSubtask(subtask13);
        Subtask subtask21 = new Subtask("Эпик2 Подзадача1", "Подзадача 2.1", TaskStatus.NEW, epic2.getId());
        manager.addSubtask(subtask21);
        Subtask subtask22 = new Subtask("Эпик2 Подзадача2", "Подзадача 2.2", TaskStatus.DONE, epic2.getId());
        manager.addSubtask(subtask22);

        System.out.println("История (ожидается: пусто):");
        for (Task task : manager.getHistory()) {
            System.out.println("- " + task);
        }

// делаем просмотры

        manager.getSubtask(subtask13.getId());
        manager.getSubtask(subtask11.getId());
        manager.getEpic(epic1.getId());
        manager.getTask(taskFirst.getId());
        manager.getTask(taskSecond.getId());

        System.out.println("История (ожидается: id 8, 6, 3, 1, 2):");

        for (Task task : manager.getHistory()) {
            System.out.println("- " + task);
        }

        manager.deleteTask(taskFirst.getId());

        System.out.println("История (ожидается: id 8, 6, 3, 2):");

        for (Task task : manager.getHistory()) {
            System.out.println("- " + task);
        }

        manager.getSubtask(subtask13.getId());
        manager.getTask(taskSecond.getId());
        manager.getSubtask(subtask12.getId());
        manager.getEpic(epic1.getId());
        manager.getEpic(epic2.getId());
        manager.getTask(taskSecond.getId());

        System.out.println("История (ожидается: id = 6, 8, 7, 3, 4, 2):");

        for (Task task : manager.getHistory()) {
            System.out.println("- " + task);
        }

        manager.deleteEpic(epic1.getId());

        System.out.println("История (ожидается: id = 4, 2):");

        for (Task task : manager.getHistory()) {
            System.out.println("- " + task);
        }

        httpTaskServer.stop();
    }

    public static void main(String[] args) {
        KVServer kvServer = null;
        HttpTaskServer httpTaskServer = null;
        HttpTaskServer newHttpTaskServer = null;
        try {
            kvServer = new KVServer();
            kvServer.start();

            checkEpicStatus();
            checkAdding();
            checkUpdating();
            checkGettingSubtasksOfEpic();
            checkDeleting();
            checkDeletingAll();
            checkHistory();

            // проверка save and load
            //////////////////////////////////////

            System.out.println("проверка save and load");

            httpTaskServer = new HttpTaskServer();
            httpTaskServer.start();

            TaskManager manager = new HttpTaskManagerClient();

            Task taskFirst = new Task("Поесть 1", "Принять пищу", TaskStatus.NEW);
            manager.addTask(taskFirst);
            Task taskSecond = new Task("Отдохнуть 1", "Поспать в тихий час", TaskStatus.NEW);
            manager.addTask(taskSecond);

            Epic epicFirst = new Epic("Поесть 2", "Принять пищу");
            manager.addEpic(epicFirst);
            Epic epicSecond = new Epic("Отдохнуть 2", "Поспать в тихий час");
            manager.addEpic(epicSecond);

            Subtask subtaskFirst = new Subtask("Поесть 3", "Принять пищу", TaskStatus.DONE, epicFirst.getId());
            manager.addSubtask(subtaskFirst);
            Subtask subtaskSecond = new Subtask("Отдохнуть 3", "Поспать в тихий час", TaskStatus.NEW, epicSecond.getId());
            manager.addSubtask(subtaskSecond);

            manager.getEpic(epicSecond.getId());
            manager.getTask(taskFirst.getId());
            manager.getSubtask(subtaskFirst.getId());
            manager.getEpic(epicSecond.getId());
            manager.getTask(taskSecond.getId());

            //////////////////////////

            String apiToken = httpTaskServer.getApiToken();
            httpTaskServer.stop();

            newHttpTaskServer = new HttpTaskServer(apiToken);  // новый сервер, который восстановит данные, используя ключ старого сервера
            newHttpTaskServer.start();

            System.out.println("getTasks");
            System.out.println(manager.getTasks());
            System.out.println();

            System.out.println("getEpics");
            System.out.println(manager.getEpics());
            System.out.println();

            System.out.println("getSubtasks");
            System.out.println(manager.getSubtasks());
            System.out.println();

            System.out.println("getHistory");
            System.out.println(manager.getHistory());
            System.out.println();

            newHttpTaskServer.stop();
            kvServer.stop();
        } catch (Exception ex) {
            System.out.println(ex.getClass() + " - " + ex.getMessage());
            ex.printStackTrace();
        }

    }

}