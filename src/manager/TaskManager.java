package manager;

import task.Epic;
import task.Subtask;
import task.Task;
import task.TaskStatus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class TaskManager {// класс для объекта менеджер
    private int id; //хранение задач для Задач, Подзадач и Эпиков:
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, Subtask> subtasks;
    private HashMap<Integer, Epic> epics;

    public TaskManager() {
        id = 0;
        tasks = new HashMap<>();
        subtasks = new HashMap<>();
        epics = new HashMap<>();
    }

    public Collection<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public Collection<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    public Collection<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    // метод получения списка подзадач определенного эпика
    public Collection<Subtask> getEpicSubtasks(int epicId) {
        ArrayList<Subtask> subtasksOfEpic = new ArrayList<>();
        for (Subtask subtask : subtasks.values()) {
            if (epicId == subtask.getEpicId()) {
                subtasksOfEpic.add(subtask);
            }
        }
        return subtasksOfEpic;
    }

    // извлекаем task
    public Task getTask(int taskId) {
        return tasks.getOrDefault(taskId, null);
    }

    public Epic getEpic(int epicId) {
        return epics.getOrDefault(epicId, null);
    }

    public Subtask getSubtask(int subtaskId) {
        return subtasks.getOrDefault(subtaskId, null);
    }

    //Задачи: добавляем task
    public void addTask(Task task) {
        task.setId(++id);
        tasks.put(id, task);
    }

    //Эпики
    public void addEpic(Epic epic) {
        epic.setId(++id);
        epics.put(id, epic);
    }

    // подзадач
    public void addSubtask(Subtask subtask) {
        subtask.setId(++id);
        subtasks.put(id, subtask);
        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            epic.addSubtask(subtask);
            updateEpicStatus(epic);
        }
    }

    // храним task
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void updateEpic(Epic epic) {
        Epic oldEpic = epics.get(epic.getId());
        if (oldEpic != null) {
            oldEpic.setName(epic.getName());
            oldEpic.setDescription(epic.getDescription());
        }
    }

    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            updateEpicStatus(epic);
        }
    }

    // метод для удаления
    public void deleteTask(int taskId) {
        if (tasks.containsKey(taskId)) {
            tasks.remove(taskId);
        }
    }

    public void deleteEpic(int epicId) {
        if (epics.containsKey(epicId)) {
            Epic epic = epics.get(epicId);
            for (Integer subtaskId : epic.getEpicSubtasks()) {
                subtasks.remove(subtaskId);
            }
            epics.remove(epicId);
        }
    }

    public void deleteSubtask(int subtaskId) {
        Subtask subtask = subtasks.get(subtaskId);
        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            epic.getEpicSubtasks().remove(Integer.valueOf(subtaskId));
            if (subtasks.containsKey(subtaskId)) {
                subtasks.remove(subtaskId);
            }
            updateEpicStatus(epic);
        }
    }

    public void deleteTasks() {
        tasks.clear();
    }

    public void deleteEpics() {
        Collection<Epic> collEpics = getEpics();
        for (Epic epic : collEpics) {
            deleteEpic(epic.getId());
        }
    }

    public void deleteSubtasks() {
        Collection<Subtask> collSubtasks = getSubtasks();
        for (Subtask subtask : collSubtasks) {
            deleteSubtask(subtask.getId());
        }
    }

    private void updateEpicStatus(Epic epic) {
        if (epic.getEpicSubtasks().size() == 0) {
            epic.setStatus(TaskStatus.NEW);
            return;
        }

        boolean allTaskIsNew = true;
        boolean allTaskIsDone = true;

        for (Integer subtaskId : epic.getEpicSubtasks()) {
            Subtask subtask = subtasks.get(subtaskId);
            TaskStatus status = subtask.getStatus();
            if (!status.equals(TaskStatus.NEW)) {
                allTaskIsNew = false;
            }
            if (!status.equals(TaskStatus.DONE)) {
                allTaskIsDone = false;
            }
        }

        if (allTaskIsDone) {
            epic.setStatus(TaskStatus.DONE);
        } else if (allTaskIsNew) {
            epic.setStatus(TaskStatus.NEW);
        } else {
            epic.setStatus(TaskStatus.IN_PROGRESS);
        }
    }

}
