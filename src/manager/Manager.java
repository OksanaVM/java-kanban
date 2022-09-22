package manager;
import task.Epic;
import task.Subtask;
import task.Task;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Manager {// класс для объекта менеджер
    private int id; //хранение задач для Задач, Подзадач и Эпиков:
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, Subtask> subtasks;
    private HashMap<Integer, Epic> epics;

    public Manager() {
        id = 0;
        tasks = new HashMap<>();
        subtasks = new HashMap<>();
        epics = new HashMap<>();
    }

    //Задачи: добавляем task
    public void addTask(Task task) {
        task.setId(++id);
        tasks.put(id, task);
    }

    // храним task
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    // извлекаем task
    public Task getTask(int id) {
        return tasks.getOrDefault(id, null);
    }

    public List<Task> getTasksList() {
        return new ArrayList<>(tasks.values());
    }

    // метод для удаления
    public void deleteTask(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        }
    }

    public void deleteAllTasks() {
        tasks.clear();
    }

    //Эпики
    public void addEpic(Epic epic) {
        epic.setId(++id);
        epics.put(id, epic);
    }

    public void updateEpic(Epic epic) {
         epics.put(epic.getId(), epic);
       }

    public Epic getEpic(int id) {
        return epics.getOrDefault(id, null);
    }


    public List<Epic> getEpicsList() {
        return new ArrayList<>(epics.values());
    }

    public void deleteEpic(int id) {
        if (epics.containsKey(id)) {
            Epic epic = epics.get(id);
            epics.remove(id);
            for (Subtask subtask : epic.getEpicSubtasks()) {
                subtasks.remove(subtask.getId());
            }
        }
    }

    public void deleteAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    // подзадач
    public void addSubtask(Subtask subtask) {
        subtask.setId(++id);
        subtasks.put(id, subtask);
         }

    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
            }

    public Subtask getSubtask(int id) {
        return subtasks.getOrDefault(id, null);
    }

    public List<Subtask> getSubtasksList() {
        return new ArrayList<>(subtasks.values());
    }

    public void deleteSubtask(int id) {
        if (subtasks.containsKey(id)) {
            subtasks.remove(id);
        }
    }

    public void deleteAllSubtask() {
                subtasks.clear();
            }

   public List<Subtask> getSubtaskListByEpic(Epic epic) {
       ArrayList<Subtask> subtaskOfEpic = new ArrayList<>();
       for (Subtask subtask : subtasks.values()) {
           if (epic.getId().equals(subtask.getId())) {
               subtaskOfEpic.add(subtask);
           }
       }
       return subtaskOfEpic;
   }
}
