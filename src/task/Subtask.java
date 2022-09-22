package task;
import java.util.Objects;


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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Subtask)) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return Objects.equals(getEpic(), subtask.getId());
    }
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getId());
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
