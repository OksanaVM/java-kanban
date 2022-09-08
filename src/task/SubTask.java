package task;

public class SubTask extends Task {

    private Integer epicID;

    public SubTask(String name, String description, Integer id, Integer epicID) {
        super(name, description, id);
        this.epicID = epicID;
    }

    public Integer getEpicID() {
        return epicID;
    }

    public void setEpicID(Integer epicID) {
        this.epicID = epicID;
    }

    @Override
    public String toString() {
        return "task.SubTask{" +
                "epicID=" + epicID +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription()+ '\'' +
                ", id=" + getStatus()+
                ", status='" + status + '\'' +
                '}';
    }
}
