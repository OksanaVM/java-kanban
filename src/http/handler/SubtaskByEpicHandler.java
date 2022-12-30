package http.handler;

import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import java.io.IOException;

public class SubtaskByEpicHandler extends AbstractHandler {

    private final TaskManager taskManager;

    public SubtaskByEpicHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange httpExchange) {
        int statusCode = 400;
        String response;

        try {
            String method = httpExchange.getRequestMethod();
            String path = String.valueOf(httpExchange.getRequestURI());

            System.out.println("Обрабатывается запрос " + path + " с методом " + method);

            switch (method) {
                case "GET":
                    String query = httpExchange.getRequestURI().getQuery();
                    try {
                        int id = Integer.parseInt(query.substring(query.indexOf("id=") + 3));
                        statusCode = 200;
                        response = gson.toJson(taskManager.getEpicSubtasks(id));
                    } catch (StringIndexOutOfBoundsException | NullPointerException e) {
                        response = "В запросе отсутствует необходимый параметр - id";
                    } catch (NumberFormatException e) {
                        response = "Неверный формат id";
                    }
                    break;
                default:
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