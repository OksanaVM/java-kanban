import manager.Manager;
import task.Epic;
import task.Subtask;
import task.Task;


public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();
        Epic epic1 = new Epic("Эпик №1", "Подзадача");

        manager.addEpic(epic1);

        Subtask subtask11 = new Subtask("Эпик1 Подзадача1", "Подзадача 1.1", "DONE", epic1);
        manager.addSubtask(subtask11);
        Subtask subtask12 = new Subtask("Эпик1 Подзадача2", "Подзадача 2.1", "IN_PROGRESS", epic1);
        manager.addSubtask(subtask12);
        Subtask subtask13 = new Subtask("Эпик1 Подзадача3", "Подзадача 3.1", "NEW", epic1);
        manager.addSubtask(subtask13);
        Subtask subtask12New = new Subtask("Эпик1 Подзадача2 изменена", "Подзадача 2.2", "NEW", epic1);
        subtask12New.setId(12);
        manager.updateSubtask(subtask12New);

        Epic epic2 = new Epic("Эпик №2", "ЯндексПрактикум");
        manager.addEpic(epic2);

        Subtask subtask21 = new Subtask("Эпик2 Подзадача1", "Спринт № 1, 2, 3", "DONE", epic2);
        manager.addSubtask(subtask21);

        System.out.println("Эпик = " + manager.getEpicsList());
        System.out.println("Подзадача = " + manager.getSubtasksList());
    }
}
