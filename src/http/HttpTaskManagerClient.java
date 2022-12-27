package http;

import http.adapter.InstantAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import manager.TaskManager;
import task.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.Collection;
import java.util.List;

/*  наш клиентский имитация-менеджер, на самом деле отправляющий данные по http серверу HttpTaskServer,
где работает настоящий менеджер - HTTPTaskManager */

public class HttpTaskManagerClient implements TaskManager {

    private HttpClient httpClient = HttpClient.newHttpClient();
    private String addressOfServer = "http://localhost:8080";
    private Gson gson = new GsonBuilder().registerTypeAdapter(Instant.class, new InstantAdapter()).create();

    @Override
    public Collection<Task> getTasks() {
        try {
            URI url = URI.create(addressOfServer + "/tasks/task/");
            HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            String answer = response.body();
            return gson.fromJson(answer, new TypeToken<Collection<Task>>(){}.getType());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Collection<Epic> getEpics() {
        try {
            URI url = URI.create(addressOfServer + "/tasks/epic/");
            HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            String answer = response.body();
            return gson.fromJson(answer, new TypeToken<Collection<Epic>>(){}.getType());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Collection<Subtask> getSubtasks() {
        try {
            URI url = URI.create(addressOfServer + "/tasks/subtask/");
            HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            String answer = response.body();
            return gson.fromJson(answer, new TypeToken<Collection<Subtask>>(){}.getType());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Collection<Subtask> getEpicSubtasks(int epicId) {
        try {
            URI url = URI.create(addressOfServer + "/tasks/subtask/epic/?id=" + epicId);
            HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            String answer = response.body();
            return gson.fromJson(answer, new TypeToken<List<Subtask>>(){}.getType());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Task getTask(int id) {
        try {
            URI url = URI.create(addressOfServer + "/tasks/task/?id=" + id);
            HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            String answer = response.body();
            return gson.fromJson(answer, Task.class);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Epic getEpic(int id) {
        try {
            URI url = URI.create(addressOfServer + "/tasks/epic/?id=" + id);
            HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            String answer = response.body();
            return gson.fromJson(answer, Epic.class);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Subtask getSubtask(int id) {
        try {
            URI url = URI.create(addressOfServer + "/tasks/subtask/?id=" + id);
            HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            String answer = response.body();
            Subtask s = gson.fromJson(answer, Subtask.class);
            return s;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void addTask(Task task) {
        try {
            URI url = URI.create(addressOfServer + "/tasks/task/");
            final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(gson.toJson(task));
            HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            String resp = response.body();
            resp = resp.substring(resp.indexOf("id=") + 3);
            int id = Integer.parseInt(resp);
            task.setId(id);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void addEpic(Epic epic) {
        try {
            URI url = URI.create(addressOfServer + "/tasks/epic/");
            final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(gson.toJson(epic, Epic.class));
            HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            String resp = response.body();
            resp = resp.substring(resp.indexOf("id=") + 3);
            int id = Integer.parseInt(resp);
            epic.setId(id);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void addSubtask(Subtask subtask) {
        try {
            URI url = URI.create(addressOfServer + "/tasks/subtask/");
            final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(gson.toJson(subtask, Subtask.class));
            HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            String resp = response.body();
            resp = resp.substring(resp.indexOf("id=") + 3);
            int id = Integer.parseInt(resp);
            subtask.setId(id);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void updateTask(Task task) {
        try {
            URI url = URI.create(addressOfServer + "/tasks/task/");
            final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(gson.toJson(task));
            HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        try {
            URI url = URI.create(addressOfServer + "/tasks/epic/");
            final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(gson.toJson(epic));
            HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        try {
            URI url = URI.create(addressOfServer + "/tasks/subtask/");
            final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(gson.toJson(subtask));
            HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void deleteTask(int id) {
        try {
            URI url = URI.create(addressOfServer + "/tasks/task/?id=" + id);
            HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void deleteEpic(int id) {
        try {
            URI url = URI.create(addressOfServer + "/tasks/epic/?id=" + id);
            HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void deleteSubtask(int id) {
        try {
            URI url = URI.create(addressOfServer + "/tasks/subtask/?id=" + id);
            HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void deleteTasks() {
        try {
            URI url = URI.create(addressOfServer + "/tasks/task/");
            HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void deleteEpics() {
        try {
            URI url = URI.create(addressOfServer + "/tasks/epic/");
            HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void deleteSubtasks() {
        try {
            URI url = URI.create(addressOfServer + "/tasks/subtask/");
            HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Collection<Task> getPrioritizedTasks() {
        try {
            URI url = URI.create(addressOfServer + "/tasks/");
            HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            String answer = response.body();
            return gson.fromJson(answer, new TypeToken<Collection<Task>>(){}.getType());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Collection<Task> getHistory() {
        try {
            URI url = URI.create(addressOfServer + "/tasks/history/");
            HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            String answer = response.body();
            return gson.fromJson(answer, new TypeToken<List<Task>>(){}.getType());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}
