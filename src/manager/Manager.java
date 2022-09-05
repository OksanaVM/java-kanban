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
    private ArrayList<Integer> task;


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
    public Task getTasks(int id) {
        return tasks.getOrDefault(id, null);
    }

    public List<Task> getTaskList() {
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
        epic.checkEpicStatus("NEW");
        epics.put(id, epic);
    }

    public void updateEpic(Epic epic) {
        epic.setEpicSubtaskIds(epics.get(epic.getId()).getEpicSubtasks());
        epics.put(epic.getId(), epic);
        updateEpicStatus(epic);
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
            for (Integer subtaskId : epic.getEpicSubtasks()) {
                subtasks.remove(subtaskId);
            }
            epic.setEpicSubtaskIds(new ArrayList<>());
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
        subtask.getEpic().getEpicSubtasks().add(id);
        updateEpicStatus(subtask.getEpic());
    }

    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        updateEpicStatus(subtask.getEpic());
    }

    public Subtask getSubtask(int id) {
        return subtasks.getOrDefault(id, null);
    }

    public List<Subtask> getSubtasksList() {
        return new ArrayList<>(subtasks.values());
    }

    public void deleteSubtask(int id) {
        if (subtasks.containsKey(id)) {
            Epic epic = subtasks.get(id).getEpic();
            epic.getEpicSubtasks().remove((Integer) id);
            updateEpicStatus(epic);
            subtasks.remove(id);
        }
    }

    public void deleteAllSubtask() {
        ArrayList<Epic> epicsForStatusUpdate = new ArrayList<>();
        for (Subtask subtask : subtasks.values()) {
            subtask.getEpic().setEpicSubtaskIds(new ArrayList<>());
            if (!epicsForStatusUpdate.contains(subtask.getEpic())) {
                epicsForStatusUpdate.add(subtask.getEpic());
            }
        }
        subtasks.clear();
        for (Epic epic : epicsForStatusUpdate) {
            epic.checkEpicStatus("NEW");
        }
    }

     ArrayList<Integer> getCompleteListOfSubTaskByEpicTask(Epic epic){
        return epic.getEpicSubtasks();
     }

    // статусы эпиков
    private void updateEpicStatus(Epic epic) {

        if (epic.getEpicSubtasks().size() == 0) {
            epic.checkEpicStatus("NEW");
            return;
        }

        boolean allTaskIsNew = true;
        boolean allTaskIsDone = true;

        for (Integer epicSubtaskId : epic.getEpicSubtasks()) {
            String status = subtasks.get(epicSubtaskId).getStatus();
            if (!status.equals("NEW")) {
                allTaskIsNew = false;
            }
            if (!status.equals("DONE")) {
                allTaskIsDone = false;
            }
        }

        if (allTaskIsDone) {
            epic.checkEpicStatus("DONE");
        } else if (allTaskIsNew) {
            epic.checkEpicStatus("NEW");
        } else {
            epic.checkEpicStatus("IN_PROGRESS");
        }

    }
}
