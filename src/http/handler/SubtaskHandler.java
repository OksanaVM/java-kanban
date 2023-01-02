package http.handler;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import task.Subtask;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class SubtaskHandler extends AbstractHandler {

    private final TaskManager taskManager;

    public SubtaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange httpExchange) {
        int statusCode;
        String response;

        try {
            String method = httpExchange.getRequestMethod();

            switch (method) {
                case "GET":
                    String query = httpExchange.getRequestURI().getQuery();
                    if (query == null) {
                        statusCode = 200;
                        response = gson.toJson(taskManager.getSubtasks());
                    } else {
                        try {
                            int id = Integer.parseInt(query.substring(query.indexOf("id=") + 3));
                            Subtask subtask = taskManager.getSubtask(id);
                            if (subtask != null) {
                                response = gson.toJson(subtask);
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
                    String bodyRequest = new String(httpExchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                    try {
                        Subtask subtask = gson.fromJson(bodyRequest, Subtask.class);
                        System.out.println("получен на сервере сабтаск " + subtask);
                        int id = subtask.getId();
                        if (id != 0) {
                            taskManager.updateSubtask(subtask);
                            statusCode = 200;
                            response = gson.toJson(subtask);
                        } else {
                            taskManager.addSubtask(subtask);
                            statusCode = 201;
                            response = gson.toJson(subtask);
                        }
                    } catch (JsonSyntaxException e) {
                        response = "Неверный формат запроса";
                        statusCode = 400;
                    }
                    break;
                case "DELETE":
                    response = "";
                    query = httpExchange.getRequestURI().getQuery();
                    if (query == null) {
                        taskManager.deleteSubtasks();
                        statusCode = 204;
                    } else {
                        try {
                            int id = Integer.parseInt(query.substring(query.indexOf("id=") + 3));
                            taskManager.deleteSubtask(id);
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
