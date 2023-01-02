package http.handler;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import task.Task;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TaskHandler extends AbstractHandler {

    private final TaskManager taskManager;

    public TaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange httpExchange) {
        int statusCode;
        String response;

        try {
            String method = httpExchange.getRequestMethod();
            String path = String.valueOf(httpExchange.getRequestURI());

            System.out.println("Обрабатывается запрос " + path + " с методом " + method);

            switch (method) {
                case "GET":
                    String query = httpExchange.getRequestURI().getQuery();
                    System.out.println("query: " + query);
                    if (query == null) {
                        statusCode = 200;
                        //System.out.println("TASKS: " + taskManager.getTasks());
                        String jsonString = gson.toJson(taskManager.getTasks());
                        //System.out.println("RESPONSE: " + jsonString);
                        response = gson.toJson(taskManager.getTasks());
                    } else {
                        try {
                            int id = Integer.parseInt(query.substring(query.indexOf("id=") + 3));
                            Task task = taskManager.getTask(id);
                            if (task != null) {
                                response = gson.toJson(task);
                                statusCode = 200;
                            } else {
                                response = "null";
                                statusCode = 404;
                            }
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
                        if (id != 0) {
                            taskManager.updateTask(task);
                            statusCode = 200;
                            response = gson.toJson(task);
                        } else {
                            taskManager.addTask(task);
                            statusCode = 201;
                            response = gson.toJson(task);
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

            writeResponse(httpExchange, response, statusCode);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } finally {
            httpExchange.close();
        }
    }

}