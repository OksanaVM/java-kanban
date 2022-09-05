package task;
import java.util.ArrayList;

public class Subtask extends Task {

    private Epic epic;

    public Subtask(String title, String description, String status) {
        super(title, description, status);
    }

    public Subtask(String title, String description, String status, Epic epic) {
        super(title, description, status);
        this.epic = epic;
    }

    public Epic getEpic() {
        return epic;
    }

    public void setEpic(Epic epic) {
        this.epic = epic;
    }



    @Override
    public String toString() {
        return "Подзадача{" +
                "№=" + getId()+
                ", Название='" + getName() + '\'' +
                ", Описание='" + getDescription() + '\'' +
                ", Статус='" + getStatus() + '\'' +
                '}';
    }

}
