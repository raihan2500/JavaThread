package q2;

import java.util.concurrent.locks.*;

public class Question2 {

    private static class Node {
        int value;
        Node next;

        Node(int value) {
            this.value = value;
            this.next = null;
        }
    }

    private Node head;
    private Node tail;
    private final Lock lock = new ReentrantLock();
    private final Condition notEmpty = lock.newCondition();

    public Question2() {
        this.head = null;
        this.tail = null;
    }

    public void enqueue(int value) {
        lock.lock();
        try {
            Node newNode = new Node(value);
            if (tail != null) {
                tail.next = newNode;
            }
            tail = newNode;
            if (head == null) {
                head = newNode;
            }
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    public int dequeue() throws InterruptedException {
        lock.lock();
        try {
            while (head == null) {
                notEmpty.await();
            }
            int value = head.value;
            head = head.next;
            if (head == null) {
                tail = null;
            }
            return value;
        } finally {
            lock.unlock();
        }
    }

    public boolean isEmpty() {
        lock.lock();
        try {
            return head == null;
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        Question2 queue = new Question2();
        queue.enqueue(1);
        queue.enqueue(2);
        
        try {
            System.out.println(queue.dequeue());
            System.out.println(queue.dequeue());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
