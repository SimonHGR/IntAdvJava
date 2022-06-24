package executors;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

class ThreadPool {
  private BlockingQueue<Runnable> input = new ArrayBlockingQueue<>(10);
  {
    for (int i = 0; i < 2; i++) {
      new Thread(() -> {
        while (true) {
          try {
            input.take().run();
          } catch (InterruptedException e) {
            System.out.println("Shutting down worker thread!");
            return;
          }
        }
      }).start();
    }
  }
  public void execute(Runnable r) throws InterruptedException {
    input.put(r);
  }
}

public class UseThreadPool {
  public static void delay() {
    try {
      Thread.sleep((int)(Math.random() * 2000) + 1000);
    } catch (InterruptedException ie) {}
  }

  public static void main(String[] args) throws Throwable {
    ThreadPool tp = new ThreadPool();
    tp.execute(() -> {
      System.out.println(Thread.currentThread().getName() + " starting task 1");
      delay();
      System.out.println(Thread.currentThread().getName() + " completed task 1");
    });
    tp.execute(() -> {
      System.out.println(Thread.currentThread().getName() + " starting task 2");
      delay();
      System.out.println(Thread.currentThread().getName() + " completed task 2");
    });
    tp.execute(() -> {
      System.out.println(Thread.currentThread().getName() + " starting task 3");
      delay();
      System.out.println(Thread.currentThread().getName() + " completed task 3");
    });
    tp.execute(() -> {
      System.out.println(Thread.currentThread().getName() + " starting task 4");
      delay();
      System.out.println(Thread.currentThread().getName() + " completed task 4");
    });
    tp.execute(() -> {
      System.out.println(Thread.currentThread().getName() + " starting task 5");
      delay();
      System.out.println(Thread.currentThread().getName() + " completed task 5");
    });
  }
}
