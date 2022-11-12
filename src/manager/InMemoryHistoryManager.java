package manager;

import task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


public class InMemoryHistoryManager implements HistoryManager {

    public Node head;
    public Node tail;

    //InMemoryHistoryManager() {//
    // private LinkedList<Node<Task>> taskHistory;
    // private HashMap<Integer, Node<Task>> receivedTasks;
    // поле для хранения истории
    // private ArrayList<Task> listHistory = new ArrayList();//
    private LinkedList<Node> taskHistory;
    private HashMap<Integer, Node> receivedTasks;


    public InMemoryHistoryManager() {
        this.taskHistory = new LinkedList<>();

        this.receivedTasks = new HashMap<>(); //5/
    }

    public void linkLast(Task element) {  //5/ реализация двусвязного списка задач с методом linkLast
        final Node oldTail = tail;
        final Node newNode = new Node(oldTail, element, null);
        tail = newNode;
        receivedTasks.put(element.getId(), newNode);
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.next = newNode;
        }
    }

    public List<Task> getTasks() { //5/ реализация двусвязного списка задач с методом getTasks
        List<Task> tasks = new ArrayList<>();
        Node currentNode = head;
        while (!(currentNode == null)) {
            tasks.add(currentNode.data);
            currentNode = currentNode.next;
        }
        return tasks;
    }

    public void removeNode(Node node) {  //5/ метод void remove(int id) для удаления задачи из просмотра (принимает объект Node — узел связного списка и вырезает его)
        if (!(node == null)) {
            final Task data = node.data;
            final Node next = node.next;
            final Node prev = node.prev;
            node.data = null;

            if (head == node && tail == node) {
                head = null;
                tail = null;
            } else if (head == node && !(tail == node)) {
                head = next;
                head.prev = null;
            } else if (!(head == node) && tail == node) {
                tail = prev;
                tail.next = null;
            } else {
                prev.next = next;
                next.prev = prev;
            }
            node = null;
        }
    }

   /* @Override
    public void addTask(Task task) {
        listHistory.add(task); // добавляем в конец (согласно техническому заданию) самый свежий просмотр
// не храним более 10
        if (listHistory.size() > 10) {
            listHistory.remove(0); // самый первый (с индексом 0), давний просмотр, удаляем
        }
    }*/

    @Override
    public void addTask(Task task) {
        if (!(task == null)) {
            remove(task.getId());
            linkLast(task);
        }
    }

    @Override
    public void remove(int id) {
        removeNode(receivedTasks.get(id));
    }

    @Override
    public List<Task> getHistory() {
        //return listHistory;//
        return getTasks();
    }
}

class Node { //5/ отдельный класс Node для узла списка

    public Task data;
    public Node next;
    public Node prev;

    public Node(Node prev, Task data, Node next) {
        this.data = data;
        this.next = next;
        this.prev = prev;
    }
}

