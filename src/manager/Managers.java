package manager;

// добавили

import http.HTTPTaskManager;
import http.KVServer;

import java.io.IOException;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getDefault(String serverAddress) throws IOException, InterruptedException {
        return new HTTPTaskManager(serverAddress);
    }
}