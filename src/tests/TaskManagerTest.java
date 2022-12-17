package tests;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import task.*;
import manager.TaskManager;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Iterator;

abstract class TaskManagerTest<T extends TaskManager> {

    T taskManager;

    TaskManagerTest(T manager) {
        taskManager = manager;
    }

    @Test
    void addTask() {
        Task task = new Task("Task", "Test desc...", TaskStatus.NEW);
        taskManager.addTask(task);

        assertNotNull(task.getId(), "Идентификатор после сохранения - null!");

        Task savedTask = taskManager.getTask(task.getId());

        assertNotNull(savedTask, "Задача не была добавлена");
        assertEquals(task, savedTask, "Задачи не совпадают");

        final Collection<Task> tasks = taskManager.getTasks();

        assertNotNull(tasks, "Коллекция всех задач - null");
        assertEquals(1, tasks.size(), "Неверное количество задач!");
        assertEquals(savedTask, tasks.iterator().next(), "Задачи не совпадают");
    }

    @Test
    void addEpic() {
        Epic epic = new Epic("Epic", "Epic desc...");
        taskManager.addEpic(epic);

        assertNotNull(epic.getId(), "Идентификатор после сохранения - null!");

        Epic savedEpic = taskManager.getEpic(epic.getId());

        assertNotNull(epic, "Эпик не был добавлен");
        assertEquals(epic, savedEpic, "Эпики не совпадают");

        final Collection<Epic> epics = taskManager.getEpics();

        assertNotNull(epics, "Коллекция всех эпиков - null");
        assertEquals(1, epics.size(), "Неверное количество эпиков!");
        assertEquals(savedEpic, epics.iterator().next(), "Эпики не совпадают");
    }

    @Test
    void addSubTask() {
        Subtask subtask = new Subtask("Subtask", "subtask desc...", TaskStatus.NEW);
        taskManager.addSubtask(subtask);

        assertNotNull(subtask.getId(), "Идентификатор после сохранения - null!");

        Subtask savedSubtask = taskManager.getSubtask(subtask.getId());

        assertNotNull(savedSubtask, "Подзадача не была добавлена");
        assertEquals(subtask, savedSubtask, "Подзадачи не совпадают");

        final Collection<Subtask> subtasks = taskManager.getSubtasks();

        assertNotNull(subtasks, "Подзадачи на возвращаются");
        assertEquals(1, subtasks.size(), "Неверное количество подзадач.");
        assertEquals(savedSubtask, subtasks.iterator().next(), "Подзадачи не совпадают.");
    }

    @Test
    void updateTask() {
        Task task = new Task("Task 1", "Task 1 desc", TaskStatus.NEW);
        taskManager.addTask(task);
        Task gotTask = taskManager.getTask(task.getId());

        assertNotNull(gotTask, "Задача не найдена по идентификатору");

        gotTask.setName("Task number 1");
        taskManager.updateTask(gotTask);

        Task task2 = taskManager.getTask(gotTask.getId());

        assertEquals(gotTask.getName(), task2.getName(), "Обновление не состоялось");
    }

    @Test
    void updateEpic() {
        Epic epic = new Epic("Epic 1", "epic 1 desc...");
        taskManager.addEpic(epic);
        Epic gotEpic = taskManager.getEpic(epic.getId());

        assertNotNull(gotEpic, "Эпик не найден по идентификатору");

        gotEpic.setName("epic number 1");
        taskManager.updateEpic(gotEpic);

        Epic epic2 = taskManager.getEpic(gotEpic.getId());

        assertEquals(gotEpic.getName(), epic2.getName(), "Обновление не состоялось");
    }

    @Test
    void updateSubtask() {
        Subtask subtask = new Subtask("SubTask 1", "SubTask 1 desc", TaskStatus.NEW);
        taskManager.addSubtask(subtask);
        Subtask gotSTask = taskManager.getSubtask(subtask.getId());

        assertNotNull(gotSTask, "Подзадача не найдена по идентификатору");

        gotSTask.setName("SubTask number 1");
        taskManager.updateSubtask(gotSTask);

        Subtask subtask2 = taskManager.getSubtask(gotSTask.getId());

        assertEquals(gotSTask.getName(), subtask2.getName(), "Обновление не состоялось");
    }

    @Test
    void getTask() {
        Task task = new Task("Task 1", "Task 1 description", TaskStatus.NEW);
        taskManager.addTask(task);

        Task savedTask = taskManager.getTask(task.getId());
        assertNotNull(savedTask, "Задача не возвращается");
        assertEquals(task, savedTask, "Задачи не совпадают");

        final Collection<Task> historyList = taskManager.getHistory();
        assertNotNull(historyList, "История - null!");
        assertEquals(1, historyList.size(), "Количество задач в истории неверное");
        assertEquals(savedTask, historyList.iterator().next(), "В историю добавилась не та задача");
    }

    @Test
    void getEpic() {
        Epic epic = new Epic("Epic 1", "Epic 1 description");
        taskManager.addEpic(epic);

        Epic savedEpic = taskManager.getEpic(epic.getId());
        assertNotNull(savedEpic, "Эпик не возвращается");
        assertEquals(epic, savedEpic, "Эпики не совпадают");

        final Collection<Task> historyList = taskManager.getHistory();
        assertNotNull(historyList, "История - null!");
        assertEquals(1, historyList.size(), "Количество эпиков в истории неверное");
        assertEquals(savedEpic, historyList.iterator().next(), "В историю добавился не тот эпик");
    }

    @Test
    void getSubtask() {
        Subtask subtask = new Subtask("Subtask 1", "Subtask 1 desc...", TaskStatus.NEW);
        taskManager.addSubtask(subtask);

        Subtask savedSubtask = taskManager.getSubtask(subtask.getId());
        assertNotNull(savedSubtask, "Подзадача не возвращается");
        assertEquals(subtask, savedSubtask, "Подзадачи не совпадают");

        final Collection<Task> historyList = taskManager.getHistory();
        assertNotNull(historyList, "История - null!");
        assertEquals(1, historyList.size(), "Количество подзадач в истории неверное");
        assertEquals(savedSubtask, historyList.iterator().next(), "В историю добавилась не та подзадача");
    }

    @Test
    void removeTask() {
        Task task = new Task("Task 1", "Task 1 description", TaskStatus.NEW);
        taskManager.addTask(task);
        taskManager.getTask(task.getId()); // добавилась в историю

        // проверяем, что задача удалилась из менеджера
        taskManager.deleteTask(task.getId());
        assertEquals(false, taskManager.getTasks().contains(task), "Задача не удалена");

        // удалилась из истории
        Collection<Task> history = taskManager.getHistory();
        assertFalse(history.contains(task), "Задача не удалена из истории");
    }

    @Test
    void removeEpic() {
        Epic epic = new Epic("Epic 1", "Epic 1 description...");
        taskManager.addEpic(epic);
        taskManager.getEpic(epic.getId()); //  в историю

        // ...из менеджера
        taskManager.deleteEpic(epic.getId());
        assertEquals(false, taskManager.getEpics().contains(epic), "эпик не удалён");

        // ...из истории
        Collection<Task> history = taskManager.getHistory();
        assertFalse(history.contains(epic), "Эпик не удалился из истории");
    }

    @Test
    void removeSubtask() {
        Subtask subtask = new Subtask("SubTask 1", "SubTask 1 description", TaskStatus.NEW);
        taskManager.addSubtask(subtask);
        taskManager.getSubtask(subtask.getId()); //  в историю

        // ... из менеджера
        taskManager.deleteSubtask(subtask.getId());
        assertEquals(false, taskManager.getSubtasks().contains(subtask), "Подзадача не удалена");

        // ... из истории
        Collection<Task> history = taskManager.getHistory();
        assertFalse(history.contains(subtask), "Подзадача не удалена из истории");
    }

    @Test
    void getTasks() {
        Collection<Task> allTasks = taskManager.getTasks();
        assertNotNull(allTasks, "Список задач - null!");
        assertEquals(0, allTasks.size(), "Неверное количество задач");

        Task task = new Task("Task 1", "Task 1 description", TaskStatus.NEW);
        taskManager.addTask(task);
        assertNotNull(allTasks, "Список всех задач - null!");
        assertEquals(1, allTasks.size(), "Количество задач неверное");

        Task task2 = new Task("t2", "t 2 desc", TaskStatus.NEW);
        taskManager.addTask(task2);
        assertNotNull(allTasks, "Список всех задач - null!");
        assertEquals(2, allTasks.size(), "Количество задач неверное");
        assertTrue(allTasks.contains(task), "Не те задачи!");
        assertTrue(allTasks.contains(task2), "Не те задачи!");
    }

    @Test
    void getEpics() {
        Collection<Epic> allEpics = taskManager.getEpics();
        assertNotNull(allEpics, "Список эпиков - null!");
        assertEquals(0, allEpics.size(), "Неверное количество эпиков");

        Epic epic = new Epic("Epic 1", "Epic 1 description");
        taskManager.addEpic(epic);
        assertNotNull(allEpics, "Список всех эпиков - null!");
        assertEquals(1, allEpics.size(), "Количество эпиков неверное");

        taskManager.addEpic(new Epic("E2", "E 2 desc"));
        assertNotNull(allEpics, "Список всех эпиков - null!");
        assertEquals(2, allEpics.size(), "Количество эпиков неверное");
    }

    @Test
    void getSubTasks() {
        Collection<Subtask> allSubtasks = taskManager.getSubtasks();
        assertNotNull(allSubtasks, "Список подзадач - null!");
        assertEquals(0, allSubtasks.size(), "Неверное количество подзадач");

        Subtask subtask = new Subtask("subtask 1", "subtask 1 description", TaskStatus.NEW);
        taskManager.addSubtask(subtask);
        assertNotNull(allSubtasks, "Список всех подзадач - null!");
        assertEquals(1, allSubtasks.size(), "Количество подзадач неверное");

        taskManager.addSubtask(new Subtask("st2", "st 2 desc", TaskStatus.NEW));
        assertNotNull(allSubtasks, "Список всех подзадач - null!");
        assertEquals(2, allSubtasks.size(), "Количество подзадач неверное");
    }

    @Test
    void getPrioritizedTasks() {
        Instant ins = Instant.now();

        Task task1 = new Task("task 1", "", TaskStatus.NEW);
        task1.setStartTime(ins);
        task1.setDuration(30);

        Task task2 = new Task("task 2", "", TaskStatus.NEW);
        task2.setStartTime(ins.plus(60, ChronoUnit.MINUTES));
        task2.setDuration(30);

        Task task3 = new Task("task 3", "", TaskStatus.NEW);
        task3.setStartTime(ins.minus(60, ChronoUnit.MINUTES));
        task3.setDuration(30);

        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addTask(task3);

        Collection<Task> prTasks = taskManager.getPrioritizedTasks();
        Iterator<Task> iter = prTasks.iterator();
        Task task, next;
        if (iter.hasNext()) {
            task = iter.next();

            while (iter.hasNext()) {
                next = iter.next();
                assertTrue(task.compareTo(next) <= 0, "Задачи идут не по порядку времени начала");
                task = next;
            }
        }

    }

    @Test
    void deleteTasks() {
        taskManager.addTask(new Task("T 1", "desc", TaskStatus.NEW));
        taskManager.addTask(new Task("T 2", "desc", TaskStatus.NEW));
        taskManager.addTask(new Task("T 3", "desc", TaskStatus.NEW));

        taskManager.deleteTasks();
        assertNotNull(taskManager.getTasks(), "Коллекция задач - null!");
        assertEquals(0, taskManager.getTasks().size(), "Задачи не удалились");
    }

    @Test
    void deleteEpics() {
        taskManager.addEpic(new Epic("E 1", "desc"));
        taskManager.addEpic(new Epic("E 2", "desc"));
        taskManager.addEpic(new Epic("E 3", "desc"));

        taskManager.deleteEpics();
        assertNotNull(taskManager.getEpics(), "Коллекция эпиков - null!");
        assertEquals(0, taskManager.getEpics().size(), "Эпики не удалились");
    }

    @Test
    void deleteSubtasks() {
        taskManager.addSubtask(new Subtask("ST 1", "desc", TaskStatus.NEW));
        taskManager.addSubtask(new Subtask("ST 2", "desc", TaskStatus.NEW));
        taskManager.addSubtask(new Subtask("ST 3", "desc", TaskStatus.NEW));

        taskManager.deleteSubtasks();
        assertNotNull(taskManager.getSubtasks(), "Коллекция подзадач - null!");
        assertEquals(0, taskManager.getSubtasks().size(), "Подзадачи не удалились");
    }

    @Test
    void getSubtasksOfEpic() {
        Epic epic = new Epic("Epic", "desc");
        taskManager.addEpic(epic);
        Subtask sub1 = new Subtask("sub 1", "desc ", TaskStatus.NEW, epic.getId());
        Subtask sub2 = new Subtask("sub 2", "desc", TaskStatus.NEW, epic.getId());
        taskManager.addSubtask(sub1);
        taskManager.addSubtask(sub2);

        Collection<Subtask> collectionSubs = taskManager.getEpicSubtasks(epic.getId());
        assertEquals(2, collectionSubs.size(), "Количество подзадач в эпике неверное");
        assertEquals(true, collectionSubs.contains(sub1), "Не найдена подзадача в эпике");
        assertEquals(true, collectionSubs.contains(sub2), "Не найдена подзадача в эпике");
    }

    @Test
    void getHistory() {
        Task task1 = new Task("Task 1", "", TaskStatus.NEW);
        Task task2 = new Task("Task 2", "", TaskStatus.NEW);
        taskManager.addTask(task1);
        taskManager.addTask(task2);

        // добавились в историю
        taskManager.getTask(task1.getId());
        taskManager.getTask(task2.getId());

        Collection<Task> history = taskManager.getHistory();
        assertNotNull(history, "История - null!");
        assertEquals(2, history.size(), "Количество задач в истории неверное");
        assertTrue(history.contains(task1), "Отсутствует просмотр в истории");
        assertTrue(history.contains(task2), "Отсутствует просмотр в истории");
    }

    @Test
    void checkIntersectionsIntersection5minutes() {
        Instant ins = Instant.now();

        Task task1 = new Task("task 1", "", TaskStatus.NEW);
        task1.setStartTime(ins);
        task1.setDuration(30);

        Task task2 = new Task("task 2", "", TaskStatus.NEW);
        task2.setStartTime(ins.plusSeconds(25 * 60));
        task2.setDuration(40);

        taskManager.addTask(task1);
        try {
            taskManager.addTask(task2);
        } catch (Exception ex) {

        }

        Collection<Task> tasks = taskManager.getTasks();
        assertEquals(1, tasks.size(), "Добавилась задача с пересечением в 5 минут!");
    }

    @Test
    void checkIntersectionsStartTimeTheSame() {
        Instant ins = Instant.now();

        Task task1 = new Task("task 1", "", TaskStatus.NEW);
        task1.setStartTime(ins);
        task1.setDuration(0);

        Task task2 = new Task("task 2", "", TaskStatus.NEW);
        task2.setStartTime(ins);
        task2.setDuration(0);

        taskManager.addTask(task1);
        try {
            taskManager.addTask(task2);
        } catch (Exception ex) {

        }

        Collection<Task> tasks = taskManager.getTasks();
        assertEquals(1, tasks.size(), "Добавились задачи с одинаковым временем начала!");
    }

    @Test
    void checkIntersectionsOneEndsAnotherStarts() {
        Instant ins = Instant.now();

        Task task1 = new Task("task 1", "", TaskStatus.NEW);
        task1.setStartTime(ins);
        task1.setDuration(30);

        Task task2 = new Task("task 2", "", TaskStatus.NEW);
        task2.setStartTime(ins.plusSeconds(30*60));
        task2.setDuration(30);

        taskManager.addTask(task1);
        try {
            taskManager.addTask(task2);
        } catch (Exception ex) {

        }

        Collection<Task> tasks = taskManager.getTasks();
        assertEquals(2, tasks.size(), "Не добавилась задача, которая начинается, когда другая заканчивается!");

        taskManager.deleteTasks();

        taskManager.addTask(task2);
        try {
            taskManager.addTask(task1);
        } catch (Exception ex) {

        }

        tasks = taskManager.getTasks();
        assertEquals(2, tasks.size(), "Не добавилась задача, которая заканчивается, когда другая начинается!");
    }

    @Test
    void checkIntersectionsNoIntersection() {
        Instant ins = Instant.now();

        Task task1 = new Task("task 1", "", TaskStatus.NEW);
        task1.setStartTime(ins);
        task1.setDuration(2);

        Task task2 = new Task("task 2", "", TaskStatus.NEW);
        task2.setStartTime(ins.plusSeconds(5*60));
        task2.setDuration(4);

        taskManager.addTask(task1);
        try {
            taskManager.addTask(task2);
        } catch (Exception ex) {

        }

        Collection<Task> tasks = taskManager.getTasks();
        assertEquals(2, tasks.size(), "Не добавились задача, хотя пересечения нет!");
    }

}