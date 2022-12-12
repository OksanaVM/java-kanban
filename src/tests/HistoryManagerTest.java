package tests;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import task.Task;
import manager.*;
import task.TaskStatus;

import java.util.List;


class HistoryManagerTest {

    HistoryManager managerH = Managers.getDefaultHistory();
    TaskManager manager = Managers.getDefault();

    @Test
    void add() {
        Task task = new Task("task 1", "t desc 1", TaskStatus.NEW);
        manager.addTask(task);
        managerH.add(task);
        List<Task> list = managerH.getHistory();
        assertNotNull(list, "История - null!");
        assertEquals(1, list.size(), "Количество задач в истории неверное");
        assertEquals(task, list.get(0), "Задачи не совпадают");

        Task task2 = new Task("task 2", "t desc 2", TaskStatus.NEW);
        manager.addTask(task2);
        managerH.add(task2);
        list = managerH.getHistory();
        assertEquals(2, list.size(), "Количество задач в истории неверное");
        assertEquals(task, list.get(0), "Задачи не совпадают");
        assertEquals(task2, list.get(1), "Задачи не совпадают");
    }

    @Test
    void remove() {
        Task task1 = new Task("task 1", "task 1 desc", TaskStatus.NEW);
        Task task2 = new Task("task 2", "task 2 ...", TaskStatus.NEW);
        Task task3 = new Task("task 3", "task 3 ...", TaskStatus.NEW);

        manager.addTask(task1);
        manager.addTask(task2);
        manager.addTask(task3);

        managerH.add(task1);
        managerH.add(task2);
        managerH.add(task3);

        managerH.remove(task2.getId());
        List<Task> list = managerH.getHistory();
        assertEquals(2, list.size(), "Количество задач в истории неверное");
        assertEquals(task1, list.get(0), "Некорректно сработало удаление из истории");
        assertEquals(task3, list.get(1), "Некорректно сработало удаление из истории");
    }

    @Test
    void getHistory() {
        // пусто
        List<Task> list = managerH.getHistory();
        assertNotNull(list, "История - null!");
        assertEquals(0, list.size(), "История должна быть пуста");

        // дубли
        Task task1 = new Task("task 1", "task 1 description ...", TaskStatus.NEW);
        Task task2 = new Task("task 2", "task 2 ...", TaskStatus.NEW);
        Task task3 = new Task("task 3", "task 3", TaskStatus.NEW);

        manager.addTask(task1);
        manager.addTask(task2);
        manager.addTask(task3);

        managerH.add(task1);
        managerH.add(task2);
        managerH.add(task3);
        managerH.add(task1);

        list = managerH.getHistory();
        assertEquals(3, list.size(), "Количество задач в истории неверное");
        assertEquals(task2, list.get(0), "Неверная последовательность задач в истории");
        assertEquals(task3, list.get(1), "Неверная последовательность задач в истории");
        assertEquals(task1, list.get(2), "Неверная последовательность задач в истории");
    }

}