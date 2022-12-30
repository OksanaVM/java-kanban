package http.handler;

import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import java.io.IOException;

public class TasksHandler extends AbstractHandler {
    private final TaskManager taskManager;

    public TasksHandler(TaskManager taskManager) {
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
                    statusCode = 200;
                    response = gson.toJson(taskManager.getPrioritizedTasks());
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
