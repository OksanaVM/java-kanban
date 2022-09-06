package task;

import java.util.ArrayList;

public class Task {
    private int id;
    private String name;
    private String description;
    private String status;
    private ArrayList<Integer> tasks;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription (String description) {
        this.description = description;
    }

    public Task(String titleTask, String descriptionTask, String statusTask) {
        this.name = titleTask;
        this.description = descriptionTask;
        this.status = statusTask;
    }

    public int getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public String setStatus(String status) {
        return status;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ЗАДАЧА{" +
                "№=" + id +
                ", Название='" + name + '\'' +
                ", Описание='" + description + '\'' +
                ", Статус='" + status + '\'' +
                '}';
    }
}

