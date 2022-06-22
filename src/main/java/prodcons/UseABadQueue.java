package prodcons;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

class BadQueue<T> {
  private Object rendezvous = new Object();
  private T[] data;
  private int count = 0;
  private int limit;

  public BadQueue(int size) {
//    data = new T[size];
//    data = Array.newInstance(MyClass.class???);
    data = (T[])new Object[size]; // example of "heap pollution"
    limit = size - 1;
  }

  public void put(T t) throws InterruptedException {
    synchronized (rendezvous) {
      while (count >= limit) {
        // tests for interrupts
        // releases the "key" (aka, monitor lock)
        // -- MUST BE "transactionally stable" at this point
        // releases the CPU to other task
        // Thread "goes to sleep with it's head on the 'pillow' of
        // the rendezvous object
        // regains the "key" BEFORE continuing
        rendezvous.wait();
      }
      data[count++] = t;
// unsafe functionally in multi-producer/consumer/CPU situations
//      rendezvous.notify();
// Non-scalable with large numbers of threads
      rendezvous.notifyAll();
    }
  }

  public T take() throws InterruptedException {
    synchronized (rendezvous) {
      while (count == 0) // MUST BE A LOOP (can wake up for wrong reasons)
        rendezvous.wait();
      T rv = data[0];
      System.arraycopy(data, 1, data, 0, --count);
      // shakes the "pillow" (rendezvous object)
      // potentially moving a waiting thread from
      // "blocked waiting for notification (pillow shaking)" to
      // "blocked waiting for "key"
//      rendezvous.notify();
      rendezvous.notifyAll();
      return rv;
    }
  }
}
class BQProducer implements Runnable {
  private BadQueue<int[]> queue;
  public BQProducer(BadQueue<int[]> queue) {
    this.queue = queue;
  }
  @Override
  public void run() {
    try {
      for (int idx = 0; idx < 10_000; idx++) {
        int[] data = {-1, idx}; // "transactionally unsound"
        if (idx < 5_000) {
          Thread.sleep(1);
        }
        data[0] = idx;
        if (idx == 5_000) {
          data[0] = -99;
        }
        queue.put(data);
        data = null;
      }
      System.out.println("Producer finished");
    } catch (InterruptedException ie) {
      System.out.println("Producer shut down on request");
    }
  }
}

class BQConsumer implements Runnable {
  private BadQueue<int[]> queue;
  public BQConsumer(BadQueue<int[]> queue) {
    this.queue = queue;
  }

  @Override
  public void run() {
    try {
      for (int idx = 0; idx < 10_000; idx++) {
        if (idx > 9_500) {
          Thread.sleep(1);
        }
        int[] data = queue.take();
        if (data[0] != data[1] || data[0] != idx) {
          System.out.println("**** Error at index " + idx +
              " data is " + Arrays.toString(data));
        }
      }
      System.out.println("Consumer finished normally");
    } catch (InterruptedException ie) {
      System.out.println("Consumer shut down on request");
    }
  }
}

public class UseABadQueue {
  public static void main(String[] args) throws InterruptedException {
//    BadQueue<String> bqs = new BadQueue<>(10);
//    bqs.put("Hello");
//    bqs.put("Bonjour");
//    System.out.println(bqs.take());
//    bqs.put("Guten Tag");
//    System.out.println(bqs.take());
//    System.out.println(bqs.take());
//
//    System.exit(0);

    BadQueue<int[]> q = new BadQueue<>(10);
    BQProducer prod = new BQProducer(q);
    BQConsumer cons = new BQConsumer(q);
    Thread pThread = new Thread(prod);
    Thread cThread = new Thread(cons);
    pThread.start();
    cThread.start();
    System.out.println("Prod and Cons started...");

    pThread.join();
    cThread.join();
    System.out.println("All finished. Main exiting.");
  }
}
