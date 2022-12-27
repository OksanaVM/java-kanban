package manager;

import http.KVTaskClient;
import http.adapter.InstantAdapter;
import com.google.gson.*;
import exceptions.ManagerSaveException;
import task.*;
import java.io.IOException;
import java.time.Instant;

public class HttpTaskManager extends FileBackedTaskManager {

    private final String kvServerAddrress; // это адрес сервера KVServer, на котором хранится состояние менеджера
    private final KVTaskClient kvTaskClient;
    private String apiToken = null;

    private final Gson gson = new GsonBuilder().registerTypeAdapter(Instant.class, new InstantAdapter()).create();

    // создание менеджера с чистого листа
    public HttpTaskManager(String kvServerAddrress) throws IOException, InterruptedException {
        //super();
        this.kvServerAddrress = kvServerAddrress;
        kvTaskClient = new KVTaskClient(kvServerAddrress);
        apiToken = kvTaskClient.getApiToken();
    }

    // создание менеджера с ключом, который должен запросить у KVServer и восстановить свое состояние
    public HttpTaskManager(String kvServerAddrress, String apiToken) throws IOException, InterruptedException {
        //super();
        this.kvServerAddrress = kvServerAddrress;
        this.apiToken = apiToken;
        kvTaskClient = new KVTaskClient(kvServerAddrress, apiToken);
        String snapString = kvTaskClient.load(apiToken);
        ManagerSnapshot snap = gson.fromJson(snapString, ManagerSnapshot.class);
        load(snap);
    }

    @Override
    // сохранение состояния в моментальный снимок
    public void save() {
        if (needToSave) {
            ManagerSnapshot snapshot = new ManagerSnapshot();
            snapshot.setTasks(getTasks());
            snapshot.setEpics(getEpics());
            snapshot.setSubtasks(getSubtasks());
            snapshot.setPrioritizedTasks(getPrioritizedTasks());
            snapshot.setListHistory(getHistory());
            String snapString = gson.toJson(snapshot, ManagerSnapshot.class);
            try {
                kvTaskClient.put(kvTaskClient.getApiToken(), snapString);
            } catch (Exception ex) {
                System.out.println(ex);
                throw new ManagerSaveException("Ошибка: " + ex.getMessage());
            }
        }
    }

    // восстановление состояния из моментального снимка
    protected void load(ManagerSnapshot snap) {
        needToSave = false;

        for (Task t : snap.getTasks()) {
            addTask(t);
        }
        for (Epic e : snap.getEpics()) {
            addEpic(e);
        }
        for (Subtask s : snap.getSubtasks()) {
            addSubtask(s);
        }
        for(Task t : snap.getPrioritizedTasks()) {
            prioritizedTasks.add(t);
        }

        for(Task t : snap.getListHistory()) {
            getHistoryManager().add(t);
        }

        needToSave = true;
    }

    public String getApiToken() {
        return apiToken;
    }

}