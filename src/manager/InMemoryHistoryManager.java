package manager;

import task.Task;
import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

// поле для хранения истории
    private ArrayList<Task> listHistory = new ArrayList();
    InMemoryHistoryManager() {

    }

    @Override

    public void addTask(Task task) {
        listHistory.add(task); // добавляем в конец (согласно техническому заданию) самый свежий просмотр

// не храним более 10
        if (listHistory.size() > 10) {
            listHistory.remove(0); // самый первый (с индексом 0), давний просмотр, удаляем
        }
    }

    @Override

    public List<Task> getHistory() {
        return listHistory;
    }
}
