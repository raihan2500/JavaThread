package q3;

import java.util.LinkedList;
import java.util.Random;

class Stack {
    private LinkedList<Integer> list = new LinkedList<>();
    private static final int MAX_SIZE = 10;

    public synchronized void push(int value) throws InterruptedException {
        while (list.size() == MAX_SIZE) {
            wait();
        }
        list.addFirst(value);
        notifyAll();
    }

    public synchronized int pop() throws InterruptedException {
        while (list.isEmpty()) {
            wait();
        }
        int value = list.removeFirst();
        notifyAll();
        return value;
    }
}

class Producer extends Thread {
    private Stack stack;
    private Random random = new Random();

    public Producer(Stack stack) {
        this.stack = stack;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 100; i++) {
                int value = random.nextInt(100) + 1;
                stack.push(value);
                System.out.println("Produced: " + value);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

class Consumer extends Thread {
    private Stack stack;

    public Consumer(Stack stack) {
        this.stack = stack;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 100; i++) {
                int value = stack.pop();
                System.out.println("Consumed: " + value);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

public class Question3 {
    public static void main(String[] args) {
        Stack stack = new Stack();
        Producer producer = new Producer(stack);
        Consumer consumer = new Consumer(stack);

        producer.start();
        consumer.start();

        try {
            producer.join();
            consumer.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
