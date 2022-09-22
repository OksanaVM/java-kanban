package task;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Subtask> epicSubtasks = new ArrayList<>();

    public Epic(String title, String description) {
        super(title, description, "New");
        }

    public ArrayList<Subtask> getEpicSubtasks() {
        return epicSubtasks;
    }

    public void setEpicSubtasks(ArrayList<Subtask> epicSubtasks) {
        this.epicSubtasks = epicSubtasks;
    }
// метод добавления подзадач в Эпик
    public void addSubtask (Subtask subtask){
        epicSubtasks.add(subtask);
        subtask.setEpic(this);
        updateEpicStatus();
    }

    protected void updateEpicStatus() {

        if (getEpicSubtasks().size() == 0) {
            super.setStatus("NEW");
            return;
        }

        boolean allTaskIsNew = true;
        boolean allTaskIsDone = true;

        for (Subtask subtask : epicSubtasks) {
            String status = subtask.getStatus();
            if (!status.equals("NEW")) {
                allTaskIsNew = false;
            }
            if (!status.equals("DONE")) {
                allTaskIsDone = false;
            }
        }

        if (allTaskIsDone) {
           super.setStatus("DONE");
        } else if (allTaskIsNew) {
           super.setStatus("NEW");
        } else {
            super.setStatus("IN_PROGRESS");
        }
    }

// метод чтоб нельзя было поменять статус произвольно
    @Override
    public void setStatus(String status){
         }

    @Override
    public String toString() {
        return "Эпик{" +
                "№=" + getId()+
                ", Название='" + getTitle() + '\'' +
                ", Описание='" + getDescription() + '\'' +
                ", Статус='" + getStatus()+ '\'' +
                '}';
    }
}




