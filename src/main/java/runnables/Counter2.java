package runnables;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.IntStream;

class MyCounter2 implements Runnable {
  public /*volatile*/ long count = 0;
  private Object rendezvous = new Object();
  ReentrantLock rl = new ReentrantLock();
  Semaphore sem = new Semaphore(1);

  public AtomicLong al = new AtomicLong(0);
  public LongAdder la = new LongAdder();
  public LongAccumulator lacc = new LongAccumulator(Long::sum, 0);
  @Override
  public void run() {
//    try {
      for (int i = 0; i < 1_000_000_000; i++) {
//      synchronized (rendezvous) {
//      rl.lock();
//        sem.acquire();
//        try {
//          count++;
//        al.incrementAndGet();
//        la.increment();
        lacc.accumulate(1);
//        } finally {
//          rl.unlock();
//          sem.release();
//        }
      }
//    } catch (InterruptedException ie) {
//      System.out.println("interrupt");
//    }
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
//    System.out.println("count is " + mc.count);
//    System.out.println("count is " + mc.al.get());
//    System.out.println("count is " + mc.la.sum());
    System.out.println("count is " + mc.lacc.get());
    System.out.printf("time taken: %7.4f\n", (time / 1_000_000_000.0));
  }
}
/*
Ubuntu 22.04 Linux
naked var 0.0929 s
volatile var 15.8771 s
synchronized 74.7545 s
ReentrantLock 43.0698 s

Semaphore 32.7307 s
AtomicInteger 16.2879 s
LongAdder 3.7131 s
LongAccumulator 3.7045 s
 */
