package manager;

import task.Epic;
import task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final HashMap<Integer, Node> receivedTasks;
    private Node head;
    private Node tail;


    public InMemoryHistoryManager() {
        this.receivedTasks = new HashMap<>();
    }

    private void linkLast(Task element) {  /*реализация двусвязного списка задач с методом linkLast*/
        final Node newNode = new Node(tail, element, null);
        receivedTasks.put(element.getId(), newNode);
        if (head == null) {
            head = newNode;
        }
        if (tail != null) {
            tail.next = newNode;
        }
        tail = newNode;
    }

    private List<Task> getTasks() { /*реализация двусвязного списка задач с методом getTasks*/
        List<Task> tasks = new ArrayList<>();
        Node currentNode = head;
        while (currentNode != null) {
            tasks.add(currentNode.data);
            currentNode = currentNode.next;
        }
        return tasks;
    }

    private void removeNode(Node node) {  //5/ метод void remove(int id) для удаления задачи из просмотра (принимает объект Node — узел связного списка и вырезает его)
        if (node != null) {
            if (head == node && tail == node) {
                head = null;
                tail = null;
            } else if (head == node && tail != node) {
                head = node.next;
                head.prev = null;
            } else if (head != node && tail == node) {
                tail = node.prev;
                tail.next = null;
            } else {
                if (node.prev != null) {
                    node.prev.next = node.next;
                }
                if (node.next != null) {
                    node.next.prev = node.prev;
                }
            }
        }
    }

    @Override
    public void add(Task task) {
        if (task != null) {
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

class Node { //отдельный класс Node для узла списка//

    Task data;
    Node next;
    Node prev;

    Node(Node prev, Task data, Node next) {
        this.data = data;
        this.next = next;
        this.prev = prev;
    }
}

