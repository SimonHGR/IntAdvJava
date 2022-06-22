package runnables;

import java.util.concurrent.locks.ReentrantLock;

class MyCounter2 implements Runnable {
  public /*volatile*/ long count = 0;
  private Object rendezvous = new Object();
  ReentrantLock rl = new ReentrantLock();

  @Override
  public void run() {
    for (int i = 0; i < 1_000_000_000; i++) {
//      synchronized (rendezvous) {
      rl.lock();
      try {
        count++;
      }
      finally {
        rl.unlock();
      }
    }
  }
}

public class Counter2 {
  public static void main(String[] args) throws Throwable {
    MyCounter2 mc = new MyCounter2();
    Thread t1 = new Thread(mc);
    Thread t2 = new Thread(mc);

    long start = System.nanoTime();
    t1.start();
    t2.start();

    t1.join();
    t2.join();
    long time = System.nanoTime() - start;
    System.out.println("count is " + mc.count);
    System.out.printf("time taken: %7.4f\n", (time / 1_000_000_000.0));
  }
}
/*
Ubuntu 22.04 Linux
naked var 0.0929 s
volatile var 15.8771 s
synchronized 74.7545 s
ReentrantLock 43.0698 s

Semaphore
AtomicInteger
LongAdder
LongAccumulator
 */
