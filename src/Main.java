import manager.Manager;
import task.Epic;
import task.Subtask;
import task.Task;

import static task.Task.DONE_STATUS;
import static task.Task.IN_PROGRESS_STATUS;

public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();
        Task task1 = manager.createTask("Купить по списку 1", "Поехать в Ленту");
        Task task2 = manager.createTask("Купить по списку 2", "Поехать в Максидом");
        Epic epic1 = manager.createEpic("Приготовить завтрак", "Купить продукты к завтраку");
        Subtask subtask = manager.createSubtask(epic1.getId(), "Заказать роллы", "Роллы Филадельфия");
        Epic epic2 = manager.createEpic("Навести порядок в квартире", "Дедлайн суббота");
        Subtask subtask1 = manager.createSubtask(epic2.getId(), "Помыть собаку", "Записать к грумеру");
        Subtask subtask2 = manager.createSubtask(epic2.getId(), "Поздравить мужа", "Купить подарок");

        System.out.println(task1.toString());
        System.out.println(task2.toString());
        System.out.println(epic1.toString());
        System.out.println(subtask.toString());
        System.out.println(epic2.toString());
        System.out.println(subtask1.toString());
        System.out.println(subtask2.toString());

        task1.setStatus(IN_PROGRESS_STATUS);
        manager.updateTask(task1);
        System.out.println(task1.toString());

        task2.setStatus(DONE_STATUS);
        manager.updateTask(task2);
        System.out.println(task2.toString());

        manager.changeSubtaskStatus(epic1.getId(), subtask.getId(), IN_PROGRESS_STATUS);
        System.out.println(epic1.toString());

        manager.changeSubtaskStatus(epic2.getId(), subtask1.getId(), DONE_STATUS);
        manager.changeSubtaskStatus(epic2.getId(), subtask2.getId(), IN_PROGRESS_STATUS);
        System.out.println(epic2.toString());

        manager.removeIdEpic(epic1.getId());
        manager.removeIdTask(task1.getId());

        System.out.println(manager.getEpics());
        System.out.println(manager.getTasks());

    }
}
