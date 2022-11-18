package task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Epic extends Task {

    private ArrayList<Integer> subtaskIds = new ArrayList<>();

    public Epic() {
        super();
    }

    public Epic(String name, String description) {
        super(name, description, TaskStatus.NEW);
    }

    public List<Integer> getSubtaskIds() {
        return Collections.unmodifiableList(subtaskIds);
    }

    public void addSubtask(Subtask subtask) {
        subtaskIds.add(subtask.getId());
    }

    public void removeSubtask(Integer subtaskId) {
        subtaskIds.remove(subtaskId);
    }

    @Override
    public String toString() {
        String s = "";
        for (Integer subtaskId : subtaskIds) {
            if (!s.isEmpty()) {
                s += ", ";
            }
            s += subtaskId;
        }
        return "Эпик{" +
                "№=" + getId() +
                ", Название='" + getName() + '\'' +
                ", Описание='" + getDescription() + '\'' +
                ", Статус='" + getStatus() + '\'' +
                ", Подзадачи: id=" + s +
                '}';
    }
}