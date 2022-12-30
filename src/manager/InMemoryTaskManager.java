package manager;

import task.Epic;
import task.Subtask;
import task.Task;
import task.TaskStatus;

import java.time.Instant;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private int id = 0;

    protected TreeSet<Task> prioritizedTasks = new TreeSet<>();

    public InMemoryTaskManager() {
        id = 0;
    }

    private boolean canTaskBeAdded(Task t) {
        boolean added = prioritizedTasks.add(t);
        if (!added) {
            return false;
        }

        if (t.getStartTime() != null) {
            NavigableSet<Task> headSet = prioritizedTasks.headSet(t, false);
            if (!headSet.isEmpty()) {
                Task previous = headSet.last();
                if (previous.getEndTime() != null && previous.getEndTime().isAfter(t.getStartTime())) {
                    prioritizedTasks.remove(t);
                    return false;
                }
            }

            NavigableSet<Task> tailSet = prioritizedTasks.tailSet(t, false);
            if (!tailSet.isEmpty()) {
                Task next = tailSet.first();
                if (next.getStartTime() != null && t.getEndTime().isAfter(next.getStartTime())) {
                    prioritizedTasks.remove(t);
                    return false;
                }
            }

            if (headSet.size() + tailSet.size() < prioritizedTasks.size() - 1) {
                prioritizedTasks.remove(t);
                return false;
            }
        }

        return true;
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

    @Override
    public Task getTask(int taskId) {
        Task task = tasks.get(taskId);
        historyManager.add(task);
        return task;
    }

    @Override
    public Epic getEpic(int epicId) {
        Epic epic = epics.getOrDefault(epicId, null);
        historyManager.add(epic);
        return epic;
    }

    @Override
    public Subtask getSubtask(int subtaskId) {
        Subtask subtask = subtasks.getOrDefault(subtaskId, null);
        historyManager.add(subtask);
        return subtask;
    }

    @Override
    public void addTask(Task task) {
        if (canTaskBeAdded(task)) {
            task.setId(++id);
            tasks.put(id, task);
        } else {
            throw new IllegalArgumentException("Задача " + task + " не добавлена, иначе будет пересечение");
        }
    }

    @Override
    public void addEpic(Epic epic) {
        epic.setId(++id);
        epics.put(id, epic);
    }

    @Override
    public void addSubtask(Subtask subtask) {
        if (canTaskBeAdded(subtask)) {
            subtask.setId(++id);
            subtasks.put(id, subtask);
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                epic.addSubtask(subtask);
                updateEpicStatus(epic);
                updateEpicTime(epic);
            }
        } else {
            throw new IllegalArgumentException("Подзадача " + subtask + " не добавлена, иначе будет пересечение");
        }
    }

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
            updateEpicTime(epic);
        }
    }

    @Override
    public void deleteTask(int taskId) {
        if (tasks.containsKey(taskId)) {
            Task task = tasks.get(taskId);
            prioritizedTasks.remove(task);
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
            updateEpicTime(epic);
        }
        if (subtasks.containsKey(subtaskId)) {
            prioritizedTasks.remove(subtask);
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
            if (subtask != null) {
                TaskStatus status = subtask.getStatus();
                if (!status.equals(TaskStatus.NEW)) {
                    allTaskIsNew = false;
                }
                if (!status.equals(TaskStatus.DONE)) {
                    allTaskIsDone = false;
                }
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

    private void updateEpicTime(Epic epic) {
        Collection<Subtask> subs = getEpicSubtasks(epic.getId());
        double duration = 0;
        Instant startTime = null;
        Instant endTime = null;
        for(Subtask sub : subs) {
            if (sub.getStartTime() != null) {
                if (startTime == null) {
                    startTime = sub.getStartTime();
                } else if (sub.getStartTime().isBefore(startTime)) {
                    startTime = sub.getStartTime();
                }

                if (endTime == null) {
                    endTime = sub.getEndTime();
                } else if (sub.getEndTime().isAfter(endTime)) {
                    endTime = sub.getEndTime();
                }

                duration += sub.getDuration();
            }
        }
        epic.setStartTime(startTime);
        epic.setEndTime(endTime);
        epic.setDuration(duration);
    }

    public Collection<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    protected void setId(int id) {
        this.id = id;
    }

}