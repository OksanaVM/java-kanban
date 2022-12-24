package manager;
import exceptions.ManagerSaveException;
import exceptions.TaskNotFoundException;
import task.Epic;
import task.TaskStatus;
import task.Subtask;
import task.Task;

import java.io.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;
    protected boolean needToSave = true;

    public FileBackedTaskManager(File file) {
        super();
        this.file = file;
    }

    public FileBackedTaskManager() {
        super();
        file = null;
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);

        fileBackedTaskManager.needToSave = false;

        try {
            FileInputStream input = new FileInputStream(file);
            byte[] bytes = new byte[input.available()];
            input.read(bytes);
            String data = new String(bytes, "UTF-8");
            String[] lines = data.split("\r\n");
            String lineOfHistory = "";
            boolean isTitle = true;
            boolean itsTask = true;
            int maxId = 0;
            int id;
            for (String line : lines) {
                if (isTitle) {
                    isTitle = false;
                    continue;
                }
                if (line.isEmpty()) {
                    itsTask = false;
                    continue;
                }
                if (itsTask) {
                    TaskType taskType = TaskType.valueOf(line.split(",")[1]);
                    switch (taskType) {
                        case EPIC:
                            Epic epic = (Epic) fromString(line);
                            id = epic.getId();
                            if (id > maxId) {
                                maxId = id;
                            }
                            fileBackedTaskManager.addEpic(epic);

                            break;
                        case SUBTASK:
                            Subtask subtask = (Subtask) fromString(line);
                            id = subtask.getId();
                            if (id > maxId) {
                                maxId = id;
                            }
                            fileBackedTaskManager.addSubtask(subtask);

                            break;

                        case TASK:
                            Task task = fromString(line);

                            id = task.getId();
                            if (id > maxId) {
                                maxId = id;
                            }
                            fileBackedTaskManager.addTask(task);

                            break;
                    }
                } else {
                    lineOfHistory = line;
                }
            }
            fileBackedTaskManager.setId(maxId);
            List<Integer> ids = historyFromString(lineOfHistory);
            for (Integer taskId : ids) {
                try {
                    Task task = fileBackedTaskManager.getTaskAllKind(taskId);
                    fileBackedTaskManager.getHistoryManager().add(task);
                } catch (TaskNotFoundException ex) {
                    System.err.println(ex.getMessage());
                }
            }
        }
        catch (IOException ex) {
            throw new ManagerSaveException("Не удалось прочитать файл!");
        }

        fileBackedTaskManager.needToSave = true;
        return fileBackedTaskManager;
    }

    protected Task getTaskAllKind(int id) throws TaskNotFoundException {
        Task task = getTask(id);
        if (task != null) {
            return task;
        }

        Task epic = getEpic(id);
        if (epic != null) {
            return epic;
        }

        Task subtask = getSubtask(id);
        if (subtask != null) {
            return subtask;
        }

        throw new TaskNotFoundException("Не найдена задача произвольного типа по id!");
    }

    private static List<Integer> historyFromString(String value) {
        List<Integer> tasksId = new ArrayList<>();
        if (!value.trim().isEmpty()) {
            String[] idsString = value.split(",");
            for (String idString : idsString) {
                tasksId.add(Integer.valueOf(idString));
            }
        }
        return tasksId;
    }

    private static String toString(Task task) {
        TaskType taskType;
        if (task instanceof Epic) {
            taskType = TaskType.EPIC;
        } else if (task instanceof Subtask) {
            taskType = TaskType.SUBTASK;
        } else {
            taskType = TaskType.TASK;
        }
        String s = task.getId() + "," + taskType + "," + task.getName() + "," + task.getStatus() + "," + task.getDescription() + "," +
                (task.getStartTime() == null ? "" : task.getStartTime().getEpochSecond()) + "," +
                task.getDuration();
        if (task instanceof Subtask) {
            s += "," + ((Subtask)task).getEpicId();
        }

        return s;
    }

    private static Task fromString(String value) {
        String[] dataOfTask = value.split(",", 8);
        int id = Integer.valueOf(dataOfTask[0]);
        String name = dataOfTask[2];
        TaskStatus status = TaskStatus.valueOf(dataOfTask[3]);
        String description = dataOfTask[4];
        TaskType taskType = TaskType.valueOf(dataOfTask[1]);
        switch (taskType) {
            case TASK:
                Task task = new Task(id, name, description, status);
                task.setStartTime(dataOfTask[5].isEmpty() ? null : Instant.ofEpochSecond(Long.parseLong(dataOfTask[5])));
                task.setDuration(Double.parseDouble(dataOfTask[6]));
                return task;
            case EPIC:
                Epic epic = new Epic(id, name, description);
                return epic;
            case SUBTASK:
                String epicIdString = dataOfTask[7].trim();
                Subtask subtask = new Subtask(id, name, description, status, Integer.valueOf(epicIdString));
                subtask.setStartTime(dataOfTask[5].isEmpty() ? null : Instant.ofEpochSecond(Long.parseLong(dataOfTask[5])));
                subtask.setDuration(Double.parseDouble(dataOfTask[6]));
                return subtask;
            default:
                return null;
        }
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public Task getTask(int id) {
        Task task = super.getTask(id);
        save();
        return task;
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteTasks() {
        super.deleteTasks();
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = super.getEpic(id);
        save();
        return epic;
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void deleteEpics() {
        super.deleteEpics();
        save();
    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = super.getSubtask(id);
        save();
        return subtask;
    }

    @Override
    public void deleteSubtask(int id) {
        super.deleteSubtask(id);
        save();
    }

    @Override
    public void deleteSubtasks() {
        super.deleteSubtasks();
        save();
    }

    protected void save() {
        if (needToSave) {
            try (PrintWriter printWriter = new PrintWriter(file, "UTF-8")) {
                printWriter.println("id,type,name,status,description,startTime,duration,epic");

                for (Task task : getTasks()) {
                    printWriter.println(toString(task));
                }
                for (Epic epic : getEpics()) {
                    printWriter.println(toString(epic));
                }
                for (Subtask subtask : getSubtasks()) {
                    printWriter.println(toString(subtask));
                }

                List<Task> history = getHistory();
                if (!history.isEmpty()) {
                    printWriter.println();
                    printWriter.println(getHistoryManager().toString());
                }
            } catch (IOException ex) {
                throw new ManagerSaveException("Ошибка! Не удалось сохранить в файл.", ex);
            }
        }
    }

    public static void main(String[] args) {
        FileBackedTaskManager manager = new FileBackedTaskManager(new File("data.csv"));

        Task taskFirst = new Task("Поесть", "Принять пищу", TaskStatus.NEW);
        manager.addTask(taskFirst);

        Task taskSecond = new Task("Отдохнуть", "Поспать в тихий час", TaskStatus.NEW);
        manager.addTask(taskSecond);

        Epic epic1 = new Epic("Эпик №1", "Эпик");
        manager.addEpic(epic1);

        Epic epic2 = new Epic("Эпик №2", "Эпик");
        manager.addEpic(epic2);

        Epic epic3 = new Epic("Эпик №3", "Эпик");
        manager.addEpic(epic3);

        Subtask subtask11 = new Subtask("Эпик1 Подзадача1", "Подзадача 1.1", TaskStatus.NEW, epic1.getId());
        manager.addSubtask(subtask11);

        Subtask subtask12 = new Subtask("Эпик1 Подзадача2", "Подзадача 2.1",
                TaskStatus.NEW, epic1.getId());
        manager.addSubtask(subtask12);

        Subtask subtask13 = new Subtask("Эпик1 Подзадача3", "Подзадача 3.1",
                TaskStatus.NEW, epic1.getId());
        manager.addSubtask(subtask13);

        Subtask subtask21 = new Subtask("Эпик2 Подзадача1", "Подзадача 2.1",
                TaskStatus.NEW, epic2.getId());
        manager.addSubtask(subtask21);

        Subtask subtask22 = new Subtask("Эпик2 Подзадача2", "Подзадача 2.2",
                TaskStatus.DONE, epic2.getId());

        manager.addSubtask(subtask22);

        manager.getTask(taskFirst.getId());
        manager.getEpic(epic2.getId());
        manager.getSubtask(subtask11.getId());
        manager.getTask(taskFirst.getId());
        manager.getTask(taskSecond.getId());

        FileBackedTaskManager manager2 = FileBackedTaskManager.loadFromFile(new File("data.csv"));
        System.out.println("Tasks:");
        System.out.println(manager2.getTasks());
        System.out.println("Epics:");
        System.out.println(manager2.getEpics());
        System.out.println("Subtasks:");
        System.out.println(manager2.getSubtasks());
        System.out.println("History:");
        System.out.println(manager2.getHistory());
    }
}

