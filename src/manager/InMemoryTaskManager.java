package manager;

import task.Epic;
import task.Subtask;
import task.Task;
import task.TaskStatus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

// класс для объекта менеджер

public class InMemoryTaskManager implements TaskManager {

    private int id; //хранение задач для Задач, Подзадач и Эпиков:
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, Subtask> subtasks;
    private HashMap<Integer, Epic> epics;

// добавили

    private HistoryManager historyManager = Managers.getDefaultHistory();

// изменили

    /*public*/ InMemoryTaskManager() {
        id = 0;
        tasks = new HashMap<>();
        subtasks = new HashMap<>();
        epics = new HashMap<>();
    }

    @Override

    public Collection<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override

    public Collection<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override

    public Collection<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

// метод получения списка подзадач определенного эпика

    @Override

    public Collection<Subtask> getEpicSubtasks(int epicId) {
        ArrayList<Subtask> subtasksOfEpic = new ArrayList<>();
// изменила (вследствие полученного замечания)
        Epic epic = epics.get(epicId);
        if (epic != null) {
            for (Integer subtaskId : epic.getEpicSubtasks()) {
                Subtask subtask = subtasks.get(subtaskId);
                if (subtask != null) {
                    subtasksOfEpic.add(subtask);
                }
            }
        }
        return subtasksOfEpic;
    }

// извлекаем task
    @Override

    public Task getTask(int taskId) {
// изменили
        Task task = tasks.getOrDefault(taskId, null);
        if (task != null) {
            historyManager.addTask(task);
        }
        return task;
    }

    @Override

    public Epic getEpic(int epicId) {
// изменили
        Epic epic = epics.getOrDefault(epicId, null);
        if (epic != null) {
            historyManager.addTask(epic);
        }
        return epic;
    }

    @Override

    public Subtask getSubtask(int subtaskId) {
// изменили
        Subtask subtask = subtasks.getOrDefault(subtaskId, null);
        if (subtask != null) {
            historyManager.addTask(subtask);
        }
        return subtask;
    }

//Задачи: добавляем task
    @Override

    public void addTask(Task task) {
        task.setId(++id);
        tasks.put(id, task);
    }

//Эпики

    @Override
    public void addEpic(Epic epic) {
        epic.setId(++id);
        epics.put(id, epic);
    }

// подзадач

    @Override

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

    @Override

    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override

    public void updateEpic(Epic epic) {
        Epic oldEpic = epics.get(epic.getId());
        if (oldEpic != null) {
            oldEpic.setName(epic.getName());
            oldEpic.setDescription(epic.getDescription());
        }
    }

    @Override

    public void updateSubtask(Subtask subtask) {

        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            updateEpicStatus(epic);
        }
    }

// метод для удаления

    @Override

    public void deleteTask(int taskId) {
        if (tasks.containsKey(taskId)) {
            tasks.remove(taskId);
        }
    }

    @Override

    public void deleteEpic(int epicId) {
        if (epics.containsKey(epicId)) {
            Epic epic = epics.get(epicId);
            for (Integer subtaskId : epic.getEpicSubtasks()) {
                subtasks.remove(subtaskId);
            }
            epics.remove(epicId);
        }
    }

    @Override

    public void deleteSubtask(int subtaskId) {
        Subtask subtask = subtasks.get(subtaskId);
        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
// изменили
            epic.removeSubtask(subtaskId);
            if (subtasks.containsKey(subtaskId)) {
                subtasks.remove(subtaskId);
            }
            updateEpicStatus(epic);
        }
    }

    @Override

    public void deleteTasks() {
        tasks.clear();

    }

    @Override

    public void deleteEpics() {
        Collection<Epic> collEpics = getEpics();
        for (Epic epic : collEpics) {
            deleteEpic(epic.getId());
        }
    }

    @Override

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

// добавили
    @Override

    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}