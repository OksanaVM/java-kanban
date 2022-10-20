package task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Epic extends Task {

    private ArrayList<Integer> epicSubtasks = new ArrayList<>();
    public Epic(String name, String description) {
        super(name, description, TaskStatus.NEW);
    }

// изменила, чтобы не нарушать инкапсуляцию (согласно полученному замечанию)
    public List<Integer> getEpicSubtasks() {
        return Collections.unmodifiableList(epicSubtasks);
    }

// изменила. Убрала сеттер, чтобы не нарушать инкапсуляцию (согласно полученному замечанию). Можно совсем удалить этот сеттер.
/*
public void setEpicSubtasks(ArrayList<Integer> epicSubtasks) {
this.epicSubtasks = epicSubtasks;
}
*/

// метод добавления подзадачи в Эпик
    public void addSubtask(Subtask subtask) {
        epicSubtasks.add(subtask.getId());
    }

// добавили
// метод удаления подзадачи из эпика
    public void removeSubtask(Integer subtaskId) {
        epicSubtasks.remove(Integer.valueOf(subtaskId));
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