package task;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> epicSubtasks = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description, TaskStatus.NEW);
    }

    public ArrayList<Integer> getEpicSubtasks() {
        return epicSubtasks;
    }

    public void setEpicSubtasks(ArrayList<Integer> epicSubtasks) {
        this.epicSubtasks = epicSubtasks;
    }

    // метод добавления подзадач в Эпик
    public void addSubtask(Subtask subtask) {
        epicSubtasks.add(subtask.getId());
    }

    @Override
    public String toString() {
        String s = "";
        for (Integer subtaskId : epicSubtasks) {
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




