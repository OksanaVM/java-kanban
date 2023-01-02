package http.handler;

import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import task.Epic;
import java.io.IOException;

public class EpicHandler extends AbstractHandler {

    private final TaskManager taskManager;

    public EpicHandler(TaskManager taskManager) {
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
                        response = gson.toJson(taskManager.getEpics());
                    } else {
                        try {
                            int id = Integer.parseInt(query.substring(query.indexOf("id=") + 3));
                            Epic epic = taskManager.getEpic(id);
                            if (epic != null) {
                                response = gson.toJson(epic);
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
                    String bodyRequest = new String(httpExchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
                    try {
                        Epic epic = gson.fromJson(bodyRequest, Epic.class);
                        if (epic.getSubtaskIds() == null) {
                            statusCode = 400;
                            response = "Для Эпика требуется передать параметр subtasks";
                        } else {
                            int id = epic.getId();
                            if (id != 0) {
                                taskManager.updateEpic(epic);
                                statusCode = 200;
                                response = gson.toJson(epic);
                            } else {
                                taskManager.addEpic(epic);
                                statusCode = 201;
                                response = gson.toJson(epic);
                            }
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
                        taskManager.deleteEpics();
                        statusCode = 204;
                    } else {
                        try {
                            int id = Integer.parseInt(query.substring(query.indexOf("id=") + 3));
                            taskManager.deleteEpic(id);
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
