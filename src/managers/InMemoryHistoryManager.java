package managers;

import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private class Node<E> {
        E data;
        Node<E> next;
        Node<E> prev;

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
            remove(task);
        }
        linkLast(task);
    }

    private void linkLast(Task task) {
        final Node<Task> oldTail = tail;
        final Node<Task> newNode = new Node<>(tail, task, null);
        tail = newNode;
        if (oldTail == null)
            head = newNode;
        else
            oldTail.next = newNode;

        mapHistory.put(task.getId(), newNode);
    }

    @Override
    public List<Task> getHistory() {
        List<Task> listTask = new ArrayList<>();
        Node<Task> current = head;
        while (current != null) {
            listTask.add(current.data);
            current = current.next;
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
        if (task == null) {
            return;
        }

        Node<Task> node = mapHistory.remove(task.getId());
        if (node == null) {
            return;
        }

        unlink(node);
    }

    private void unlink(Node<Task> node) {
        final Node<Task> prev = node.prev;
        final Node<Task> next = node.next;

        if (prev == null) {
            head = next;
        } else {
            prev.next = next;
        }

        if (next == null) {
            tail = prev;
        } else {
            next.prev = prev;
        }
    }
}
