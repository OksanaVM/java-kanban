package manager;
import task.Epic;
import task.SubTask;
import task.Task;

import java.util.ArrayList;

public class Manager {TaskManager taskManager = new TaskManager();
    EpicManager epicManager = new EpicManager();
    SubTaskManager subTaskManager = new SubTaskManager();


    //    Получение списка всех задач.
    public ArrayList<Task> findAllTasks() {
        return taskManager.findAll();
    }

    //    Получение списка всех эпиков.
    public ArrayList<Epic> findAllEpics() {
        return epicManager.findAll();
    }

    // Получение списка всех подзадач определённого эпика.
    public ArrayList<SubTask> findAllSubTasksOfEpic(Epic epic) {
        return subTaskManager.findAllOfEpic(epic);
    }

    // Получение подзадачи по идентификатору
    public SubTask findSubTaskById(Integer id) {
        return subTaskManager.findByIdSubTask(id);
    }

    // Получение задачи по идентификатору
    public Task findTaskById(Integer id) {
        return taskManager.findById(id);
    }

    // Получение эпика по идентификатору
    public Task findEpicById(Integer id) {
        return epicManager.findById(id);
    }

    // Добавление задачи.
    public Task createTask(Task task) {
        return taskManager.addTask(task);
    }

    // Добавление подзадачи.
    public SubTask createSubTask(SubTask subTask, Epic epic) {
        return subTaskManager.addSubtask(subTask, epic);
    }

    // Добавление Эпика.
    public Epic createEpic(Epic epic) {
        return epicManager.addEpic(epic);
    }
    // Обновление задачи.
    public Task updateTaskByID(Task task) {
        return taskManager.updateTask(task);
    }

    // Обновление подзадачи.
    public SubTask updateSubTaskByID(SubTask subTask) {
        return subTaskManager.updateSubTask(subTask);
    }

    // Обновление эпика.
    public Task updateEpicByID(Epic epic) {
        return epicManager.updateEpic(epic);
    }

    // Удаление всех задач.
    public void deleteAllTask() {
        taskManager.deleteAll();
    }

    // Удаление всех подзадач
    public void deleteAllSubTasks() {
        subTaskManager.deleteAllTasks();
    }

    // Удаление всех эпиков
    public void deleteAllEpics() {
        epicManager.deleteAllEpics();
    }

    // Удаление подзадач по ID.
    public void deleteSubTaskById(Integer id) {
        subTaskManager.deleteByIdTask(id);
    }

    // Удаление эпика по ID.
    public void deleteEpicById(Integer id) {
        epicManager.deleteByIdEpic(id);
    }

    // Удаление задачи по ID.
    public Task deleteTaskById(Integer id) {
        return taskManager.deleteById(id);
    }
}
