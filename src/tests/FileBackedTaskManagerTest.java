package tests;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import task.*;
import manager.*;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;

class FileBackedTaskManagerTest extends TaskManagerTest {

    FileBackedTaskManagerTest() {
        super(new FileBackedTaskManager(new File("data.csv")));
    }

    @Test
    void testSaveAndLoad() {

        TaskManager manager = new FileBackedTaskManager(new File("data.csv"));

        Collection<Task> allTasks = manager.getTasks();
        assertNotNull(allTasks, "Список задач - null");
        assertEquals(0, allTasks.size(), "Количество задач неверное");

        Collection<Epic> allEpics = manager.getEpics();
        assertNotNull(allEpics, "Список эпиков - null");
        assertEquals(0, allEpics.size(), "Количество эпиков неверное");

        Collection<Subtask> allSubTasks = manager.getSubtasks();
        assertNotNull(allSubTasks, "Список подзадач - null");
        assertEquals(0, allSubTasks.size(), "Количество подзадач неверное");

        Collection<Task> history = manager.getHistory();
        assertNotNull(history, "История - null!");
        assertEquals(0, history.size(), "Количество задач в истории неверное");

        // *** есть список задач и есть история

        Task task1 = new Task("task 1", "1", TaskStatus.NEW);
        manager.addTask(task1);
        Task task2 = new Task("task 2", "2", TaskStatus.NEW);
        manager.addTask(task2);

        // эпик без подзадач
        Epic epic1 = new Epic("epic 1", "e desc 1");
        manager.addEpic(epic1);

        // эпик с подзадачами
        Epic epic2 = new Epic("epic 2", "e desc 2");
        manager.addEpic(epic2);
        Subtask sub1 = new Subtask("sub 1", "s desc 1", TaskStatus.NEW, epic2.getId());
        Subtask sub2 = new Subtask("sub 2", "s desc 2", TaskStatus.NEW, epic2.getId());
        manager.addSubtask(sub1);
        manager.addSubtask(sub2);

        // просмотры (для истории)
        manager.getTask(task1.getId());
        manager.getTask(task2.getId());
        manager.getEpic(epic1.getId());
        manager.getEpic(epic2.getId());
        manager.getSubtask(sub1.getId());
        manager.getSubtask(sub2.getId());

        // Проверяем восстановление

        TaskManager manager2 = FileBackedTaskManager.loadFromFile(new File("data.csv"));

        allTasks = manager2.getTasks();
        assertNotNull(allTasks, "Список задач - null");
        assertEquals(2, allTasks.size(), "Количество задач неверное");
        assertTrue(allTasks.contains(task1), "Задача пропала");
        assertTrue(allTasks.contains(task2), "Задача пропала");

        allEpics = manager2.getEpics();
        assertNotNull(allEpics, "Список эпиков - null!");
        assertEquals(2, allEpics.size(), "Количество эпиков неверное");
        assertTrue(allEpics.contains(epic1), "Эпик пропал");
        assertTrue(allEpics.contains(epic2), "Эпик пропал");

        allSubTasks = manager2.getSubtasks();
        assertNotNull(allSubTasks, "Список подзадач - null");
        assertEquals(2, allSubTasks.size(), "Количество подзадач неверное");
        assertTrue(allSubTasks.contains(sub1), "Подзадача пропала");
        assertTrue(allSubTasks.contains(sub2), "Подзадача пропала");

        // сохранение памяти эпика 2 о своих подзадачах
        Collection<Subtask> subs = manager2.getEpicSubtasks(epic2.getId());
        assertTrue(subs.contains(sub1), "Эпик потерял подзадачу");
        assertTrue(subs.contains(sub2), "Эпик потерял подзадачу");

        history = manager2.getHistory();
        assertNotNull(history, "История - null!");
        assertEquals(6, history.size(), "Количество просмотров в истории неверное");

        Iterator iter = history.iterator();
        assertEquals(task1, iter.next(), "Неверный порядок задач в истории");
        assertEquals(task2, iter.next(), "Неверный порядок задач в истории");
        assertEquals(epic1, iter.next(), "Неверный порядок задач в истории");
        assertEquals(epic2, iter.next(), "Неверный порядок задач в истории");
        assertEquals(sub1, iter.next(), "Неверный порядок задач в истории");
        assertEquals(sub2, iter.next(), "Неверный порядок задач в истории");
    }

}