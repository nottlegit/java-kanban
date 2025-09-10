package managers;

import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    class Node<E> {
        public E data;
        public Node<E> next;
        public Node<E> prev;

        public Node(Node<E> prev, E data, Node<E> next) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "data=" + data +
                    '}';
        }
    }

    private Map<Integer, Node<Task>> mapHistory;
    private Node<Task> head;
    private Node<Task> tail;

    public InMemoryHistoryManager() {
        this.mapHistory = new HashMap<>();
        head = null;
        tail = null;
    }

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        if (mapHistory.containsKey(task.getId())) {
            remove(task.getId());
        }

        final Node<Task> oldTail = tail;
        final Node<Task> newNode = new Node<>(tail, task, null);
        tail = newNode;
        if (oldTail == null)
            head = newNode;
        else
            oldTail.next = newNode;

        mapHistory.put(task.getId(), newNode);

    }

    private void remove(int id) {
        Node<Task> node = mapHistory.get(id);
        Node<Task> prev = node.prev;
        Node<Task> next = node.next;

        if (prev == null && next == null) {
            this.head = null;
            this.tail = null;
            mapHistory.remove(id);
            return;
        }

        if (prev == null) {
            next.prev = null;
            this.head = next;
            mapHistory.remove(id);
            return;
        } else if (next == null) {
            this.tail = prev;
            prev.next = null;
            mapHistory.remove(id);
            return;
        }

        prev.next = next;
        next.prev = prev;
        mapHistory.remove(id);
    }

    @Override
    public List<Task> getHistory() {
        List<Task> listTask = new ArrayList<>();

        for (Node<Task> node : mapHistory.values()) {
            listTask.add(node.data);
        }
        return listTask;
    }

    public List<Node<Task>> getListNodes() {
        return new ArrayList<>(mapHistory.values());
    }

    public void printNodes() {
        for (Node<Task> node : mapHistory.values()) {
            String message = "data: " + node.data
                    + " prev: " + node.prev
                    + " next: " + node.next;
            System.out.println(message);
        }
    }

    @Override
    public void remove(Task task) {

    }
}
