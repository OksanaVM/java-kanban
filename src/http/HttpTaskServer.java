package http;

import com.sun.net.httpserver.HttpServer;
import http.handlers.*;
import manager.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {

    private final HttpServer httpServer;
    private static final int PORT = 8080;
    private final String apiToken;

    public HttpTaskServer() throws IOException, InterruptedException {
        HTTPTaskManager taskManager = new HTTPTaskManager("http://localhost:" + KVServer.PORT);
        this.apiToken = taskManager.getApitoken();
        this.httpServer = HttpServer.create();
        initHandlers(taskManager);
    }

    public HttpTaskServer(String apiToken) throws IOException, InterruptedException {
        this.apiToken = apiToken;
        HTTPTaskManager taskManager = new HTTPTaskManager("http://localhost:" + KVServer.PORT, apiToken);
        this.httpServer = HttpServer.create();
        initHandlers(taskManager);
    }

    private void initHandlers(TaskManager taskManager)  throws IOException {
        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks/task/", new TaskHandler(taskManager));
        httpServer.createContext("/tasks/epic/", new EpicHandler(taskManager));
        httpServer.createContext("/tasks/subtask/", new SubtaskHandler(taskManager));
        httpServer.createContext("/tasks/subtask/epic/", new SubtaskByEpicHandler(taskManager));
        httpServer.createContext("/tasks/history/", new HistoryHandler(taskManager));
        httpServer.createContext("/tasks/", new TasksHandler(taskManager));
    }

    public void start() {
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(1);
    }

    public String getApiToken() {
        return apiToken;
    }

}
