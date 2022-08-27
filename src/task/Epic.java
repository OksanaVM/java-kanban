package task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class Epic extends Task {
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();

    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    public boolean checkStatusSubtasks(String status) {
        for (Subtask subtask : subtasks.values()) {
            if (!subtask.getStatus().equals(status)) {
                return false;
            }
        }
        return true;
    }

    public Collection<Subtask> getValuesSubtasks() {
        return subtasks.values();
    }

    public Subtask getIdSubtaskById(int idSubtask) {
        return subtasks.get(idSubtask);
    }

    public void removeAllSubtasks() {
        subtasks.clear();
    }

    public void removeIdSubtask(int idSubtask) {
        subtasks.remove(idSubtask);
    }

    public void updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", status='" + getStatus() + '\'' +
                ", subtasks=" + subtasks +
                '}';
    }
}
