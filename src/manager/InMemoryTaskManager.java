package manager;

import task.Epic;
import task.Subtask;
import task.Task;
import task.TaskStatus;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    // добавили
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private int id = 0; //хранение задач для Задач, Подзадач и Эпиков:

// изменили

    InMemoryTaskManager() {
        id = 0;
    }

    @Override
    public Collection<Task> getTasks() {
        return Collections.unmodifiableCollection(tasks.values());
    }

    @Override
    public Collection<Epic> getEpics() {
        return Collections.unmodifiableCollection(epics.values());
    }

    @Override
    public Collection<Subtask> getSubtasks() {
        return Collections.unmodifiableCollection(subtasks.values());
    }

    // метод получения списка подзадач определенного эпика
    @Override
    public Collection<Subtask> getEpicSubtasks(int epicId) {
        ArrayList<Subtask> subtasksOfEpic = new ArrayList<>();
        Epic epic = epics.get(epicId);
        if (epic != null) {
            for (Integer subtaskId : epic.getSubtaskIds()) {
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
        Task task = tasks.get(taskId);
        historyManager.add(task);
        return task;
    }

    // изменили
    @Override
    public Epic getEpic(int epicId) {
        Epic epic = epics.getOrDefault(epicId, null);
        historyManager.add(epic);
        return epic;
    }

    // изменили
    @Override
    public Subtask getSubtask(int subtaskId) {
        Subtask subtask = subtasks.getOrDefault(subtaskId, null);
        historyManager.add(subtask);
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
            historyManager.remove(taskId);
        }
    }

    @Override
    public void deleteEpic(int epicId) {
        if (epics.containsKey(epicId)) {
            Epic epic = epics.get(epicId);
            for (Integer subtaskId : epic.getSubtaskIds()) {
                subtasks.remove(subtaskId);
                historyManager.remove(subtaskId);
            }
            epics.remove(epicId);
            historyManager.remove(epicId);
        }
    }

    @Override
    public void deleteSubtask(int subtaskId) {
        Subtask subtask = subtasks.get(subtaskId);
        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            epic.removeSubtask(subtaskId);
            updateEpicStatus(epic);
        }
        if (subtasks.containsKey(subtaskId)) {
            subtasks.remove(subtaskId);
            historyManager.remove(subtaskId);
        }
    }

    @Override
    public void deleteTasks() {
        Collection<Task> collTasks = new ArrayList<>(tasks.values());
        for (Task task : collTasks) {
            deleteTask(task.getId());
        }
    }

    @Override
    public void deleteEpics() {
        Collection<Epic> collEpics = new ArrayList<>(epics.values());
        for (Epic epic : collEpics) {
            deleteEpic(epic.getId());
        }
    }

    @Override
    public void deleteSubtasks() {
        Collection<Subtask> collSubtasks = new ArrayList<>(subtasks.values());
        for (Subtask subtask : collSubtasks) {
            deleteSubtask(subtask.getId());
        }
    }

    private void updateEpicStatus(Epic epic) {
        if (epic.getSubtaskIds().size() == 0) {
            epic.setStatus(TaskStatus.NEW);
            return;
        }

        boolean allTaskIsNew = true;
        boolean allTaskIsDone = true;

        for (Integer subtaskId : epic.getSubtaskIds()) {
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