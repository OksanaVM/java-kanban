package task;

public class Subtask extends Task {

    private Epic epic;

    public Subtask (String title, String description, String status) {
        super(title, description, status);
    }

    public Subtask (String title, String description, String status, Epic epic) {
        super(title, description, status);
        this.epic = epic;
    }

    public Epic getEpic() {
        return epic;
    }

    public void setEpic (Epic epic) {
        this.epic = epic;
    }

    public void setStatus (String status) {
        super.setStatus(status);
        if (epic != null) {
            epic.updateEpicStatus();
        }
    }

    @Override
    public String toString() {
        return "Подзадача{" +
                "№=" + getId()+
                ", Название='" + getTitle() + '\'' +
                ", Описание='" + getDescription() + '\'' +
                ", Статус='" + getStatus() + '\'' +
                '}';
    }

}
