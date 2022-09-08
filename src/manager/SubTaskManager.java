package manager;
import task.Epic;
import task.SubTask;
import task.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class SubTaskManager {
    private Integer counterIDSubTasks = 0;
    private HashMap<Integer, SubTask> subTasks = new HashMap<>();

    EpicManager epicManager = new EpicManager();

    // Добавить подзадачу.
    public SubTask addSubtask(SubTask subTask, Epic epic) {
        final SubTask newTask = new SubTask(subTask.getName(), subTask.getDescription(), ++counterIDSubTasks, epic.getId());
        if (!subTasks.containsKey(newTask.getId()))
            subTasks.put(newTask.getId(), newTask);
        else {
            System.out.println("Задача с таким ID уже существует");
            return null;
        }
        return newTask;
    }

    // Получить подзадачу по ID
    public SubTask findByIdSubTask(Integer id) {
        return subTasks.get(id);
    }

    // Обновление подзадачи по ID
    public SubTask updateSubTask(SubTask task) {
        final SubTask originalTask = subTasks.get(task.getId());
        if (originalTask == null) {
            System.out.println("Задачи с таким ID не существует.");
            return null;
        }
        originalTask.setDescription(task.getDescription());
        originalTask.setName(task.getName());
        originalTask.setStatus(task.getStatus());
        refreshStatus(task);
        return originalTask;
    }

    // Удаление задачи по идентификатору.
    public Task deleteByIdTask(Integer id) {
        final Task deletedTask = subTasks.get(id);
        subTasks.remove(id);
        return deletedTask;
    }

    // Удаление всех задач.
    public void deleteAllTasks() {
        subTasks.clear();
    }

    // Получение списка всех подзадач определённого эпика.
    public ArrayList<SubTask> findAllOfEpic(Epic epic) {
        ArrayList<SubTask> subTasksOfEpic = new ArrayList<>();
        for (SubTask subTask : subTasks.values()) {
            if (epic.getId().equals(subTask.getEpicID())) {
                subTasksOfEpic.add(subTask);
            }
        }
        return subTasksOfEpic;
    }

    // Обновление статуса эпика в зависимости от статуса подзадач
    public void refreshStatus(SubTask task) {
        ArrayList<SubTask> subTasksOfEpic = new ArrayList<>();
        int counterNew = 1;
        int counterDone = 1;
        for (int i = 0; i < subTasks.size(); i++) {
            if (task.getEpicID().equals(subTasks.get(i).getEpicID())) {
                subTasksOfEpic.add(subTasks.get(i));
            }
        }
        for (SubTask subTask : subTasksOfEpic) {
            if (subTask.getStatus().equals("NEW")) {
                counterNew++;
            } else if (subTask.getStatus().equals("DONE")) {
                counterDone++;
            }
        }
        if (counterNew == subTasksOfEpic.size()) {
            epicManager.epics.get(task.getEpicID()).setStatus("NEW");
        } else if (counterDone == subTasksOfEpic.size()) {
            epicManager.epics.get(task.getEpicID()).setStatus("DONE");
        } else {
            epicManager.epics.get(task.getEpicID()).setStatus("IN_PROGRESS");
        }
    }
}
