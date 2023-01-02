package manager;

import task.*;

import java.util.Collection;
import java.util.List;

public class ManagerSnapshot {

    private Collection<Task> tasks;
    private Collection<Epic> epics;
    private Collection<Subtask> subtasks;
    private Collection<Task> prioritizedTasks;
    private List<Task> listHistory;

    public ManagerSnapshot() {

    }

    public Collection<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Collection<Task> tasks) {
        this.tasks = tasks;
    }

    public Collection<Epic> getEpics() {
        return epics;
    }

    public void setEpics(Collection<Epic> epics) {
        this.epics = epics;
    }

    public Collection<Subtask> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(Collection<Subtask> subtasks) {
        this.subtasks = subtasks;
    }

    public Collection<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }

    public void setPrioritizedTasks(Collection<Task> prioritizedTasks) {
        this.prioritizedTasks = prioritizedTasks;
    }

    public List<Task> getListHistory() {
        return listHistory;
    }

    public void setListHistory(List<Task> listHistory) {
        this.listHistory = listHistory;
    }
}
