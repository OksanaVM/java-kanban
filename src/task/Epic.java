package task;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> epicSubtaskIds;

    public Epic(String title, String description) {
        super(title, description, "NEW");
        epicSubtaskIds = new ArrayList<>();
    }
    
    public ArrayList<Integer> getEpicSubtasks() {
       return epicSubtaskIds;
    }



    public void setEpicSubtaskIds(ArrayList<Integer> epicSubtaskIds) {
        this.epicSubtaskIds = epicSubtaskIds;
    }

    @Override
    public String toString() {
                return "Эпик{" +
                "№=" + getId() +
                ", Название='" + getName() + '\'' +
                ", Описание='" + getDescription() + '\'' +
                ", Статус='" + getStatus() + '\'' +
                '}';
    }
}



