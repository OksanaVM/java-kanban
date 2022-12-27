package http.handler;
import http.adapter.InstantAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import manager.TaskManager;
import task.Task;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

public class TaskHandler implements HttpHandler {
    private final Gson gson = new GsonBuilder().registerTypeAdapter(Instant.class, new InstantAdapter()).create();
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private final TaskManager taskManager;

    public TaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        int statusCode;
        String response;
        String method = httpExchange.getRequestMethod();
        String path = String.valueOf(httpExchange.getRequestURI());

        System.out.println("Обрабатывается запрос " + path + " с методом " + method);

        switch (method) {
            case "GET":
                String query = httpExchange.getRequestURI().getQuery();
                System.out.println("query: " + query);
                if (query == null) {
                    statusCode = 200;
                    System.out.println("TASKS: " + taskManager.getTasks());
                    String jsonString = gson.toJson(taskManager.getTasks());
                    System.out.println("RESPONSE: " + jsonString);
                    response = gson.toJson(taskManager.getTasks());
                } else {
                    try {
                        int id = Integer.parseInt(query.substring(query.indexOf("id=") + 3));
                        Task task = taskManager.getTask(id);
                        if (task != null) {
                            response = gson.toJson(task);
                        } else {
                            response = "Задача с данным id не найдена";
                        }
                        statusCode = 200;
                    } catch (StringIndexOutOfBoundsException e) {
                        statusCode = 400;
                        response = "В запросе отсутствует необходимый параметр id";
                    } catch (NumberFormatException e) {
                        statusCode = 400;
                        response = "Неверный формат id";
                    }
                }
                break;
            case "POST":
                String bodyRequest = new String(
                        httpExchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                try {
                    Task task = gson.fromJson(bodyRequest, Task.class);
                    int id = task.getId();
                    if (taskManager.getTask(id) != null) {
                        taskManager.updateTask(task);
                        statusCode = 201;
                        response = "Задача с id=" + id + " обновлена";
                    }
                    else {
                        taskManager.addTask(task);
                        statusCode = 201;
                        response = "Создана задача с id=" + task.getId();
                    }
                } catch (JsonSyntaxException e) {
                    statusCode = 400;
                    response = "Неверный формат запроса";
                }
                break;
            case "DELETE":
                response = "";
                query = httpExchange.getRequestURI().getQuery();
                if (query == null) {
                    taskManager.deleteTasks();
                    statusCode = 204;
                } else {
                    try {
                        int id = Integer.parseInt(query.substring(query.indexOf("id=") + 3));
                        taskManager.deleteTask(id);
                        statusCode = 204;
                    } catch (StringIndexOutOfBoundsException e) {
                        statusCode = 400;
                        response = "В запросе отсутствует необходимый параметр id";
                    } catch (NumberFormatException e) {
                        statusCode = 400;
                        response = "Неверный формат id";
                    }
                }
                break;
            default:
                statusCode = 400;
                response = "Некорректный запрос";
        }

        byte[] bytes = response.getBytes(DEFAULT_CHARSET);
        httpExchange.getResponseHeaders().add("Content-Type", "application/json; charset=" + DEFAULT_CHARSET);
        httpExchange.sendResponseHeaders(statusCode, bytes.length);

        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(response.getBytes());
        }

        httpExchange.close();
    }
}
