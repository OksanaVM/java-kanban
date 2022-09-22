import jdk.swing.interop.SwingInterOpUtils;
import manager.Manager;
import task.Epic;
import task.Subtask;


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
       subtask12.setTitle("Эпик1 Подзадача2 изменена");
      subtask12.setDescription("Подзадача 2.2");
      subtask12.setStatus("NEW");
        manager.updateSubtask(subtask12);

        Epic epic2 = new Epic("Эпик №2", "ЯндексПрактикум");
        manager.addEpic(epic2);

        Subtask subtask21 = new Subtask("Эпик2 Подзадача1", "Спринт № 1, 2, 3", "DONE", epic2);
        manager.addSubtask(subtask21);
        System.out.println("Эпик = " + manager.getEpicsList());
        System.out.println("подзадача = " + manager.getSubtasksList());
        System.out.println(manager.getSubtaskListByEpic(epic1));
        manager.deleteAllEpics();
        System.out.println("Эпик = " + manager.getEpicsList());
    }
}
