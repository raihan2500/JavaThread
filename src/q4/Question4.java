package q4;

import java.util.concurrent.Semaphore;

class ResourcePool {
    private final Semaphore semaphore;

    public ResourcePool(int numberOfResources) {
        semaphore = new Semaphore(numberOfResources);
    }

    public void acquire() throws InterruptedException {
        semaphore.acquire();
    }

    public void release() {
        semaphore.release();
    }
}

public class Question4 {
    public static void main(String[] args) {
        final int numberOfResources = 5;
        final int numberOfThreads = 20;
        ResourcePool resourcePool = new ResourcePool(numberOfResources);

        Runnable task = () -> {
            try {
                resourcePool.acquire();
                System.out.println(Thread.currentThread().getName() + " acquired a resource.");
                Thread.sleep((long) (Math.random() * 1000));
                System.out.println(Thread.currentThread().getName() + " released a resource.");
                resourcePool.release();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };

        for (int i = 0; i < numberOfThreads; i++) {
            new Thread(task).start();
        }
    }
}
