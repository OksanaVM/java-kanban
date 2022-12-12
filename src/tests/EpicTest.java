package tests;

import static org.junit.jupiter.api.Assertions.*;

import manager.Managers;
import manager.TaskManager;
import org.testng.annotations.Test;

import task.*;

public class EpicTest {

    @Test
    public void checkStatus() {
        TaskManager manager = Managers.getDefault();

        Epic epic = new Epic("Epic name", "Epic...");

        TaskStatus expected = TaskStatus.NEW;
        TaskStatus actual = epic.getStatus();
        assertEquals(expected, actual, "У эпика нет подзадач. Ожидаем статус: " + expected + ", имеем: " + actual);

        manager.addEpic(epic);

        Subtask sub1 = new Subtask("Subtask 1", "desc 1", TaskStatus.NEW, epic.getId());
        Subtask sub2 = new Subtask("SubTask 2", "desc 2", TaskStatus.NEW, epic.getId());
        Subtask sub3 = new Subtask("Subtask 3", "desc 3", TaskStatus.NEW, epic.getId());
        manager.addSubtask(sub1);
        manager.addSubtask(sub2);
        manager.addSubtask(sub3);

        actual = epic.getStatus();
        assertEquals(expected, actual, "Все подзадачи новые. Ожидаем статус: " + expected + ", имеем: " + actual);

        sub3.setStatus(TaskStatus.IN_PROGRESS);
        manager.updateSubtask(sub3);
        expected = TaskStatus.IN_PROGRESS;
        actual = epic.getStatus();
        assertEquals(expected, actual, "Подзадачи новые и в процессе. Ожидаем статус: " + expected + ", имеем: " + actual);

        sub3.setStatus(TaskStatus.DONE);
        manager.updateSubtask(sub3);
        actual = epic.getStatus();
        assertEquals(expected, actual, "Подзадачи новые и завершённые. Ожидаем статус: " + expected + ", имеем: " + actual);

        sub1.setStatus(TaskStatus.IN_PROGRESS);
        manager.updateSubtask(sub1);
        actual = epic.getStatus();
        assertEquals(expected, actual, "Подзадачи новые, в процессе и завершённые. Ожидаем статус: " + expected + ", имеем: " + actual);

        sub2.setStatus(TaskStatus.IN_PROGRESS);
        manager.updateSubtask(sub2);
        actual = epic.getStatus();
        assertEquals(expected, actual, "Подзадачи в процессе и завершённые. Ожидаем статус: " + expected + ", имеем: " + actual);

        sub1.setStatus(TaskStatus.DONE);
        manager.updateSubtask(sub1);
        sub2.setStatus(TaskStatus.DONE);
        manager.updateSubtask(sub2);
        expected = TaskStatus.DONE;
        actual = epic.getStatus();
        assertEquals(expected, actual, "Подзадачи все завершены. Ожидаем статус: " + expected + ", имеем: " + actual);
    }

}