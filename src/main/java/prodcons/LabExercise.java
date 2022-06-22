package prodcons;

import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

class Producer implements Runnable {
  // needs access to a *shared* BlockingQueue
  private BlockingQueue<int[]> queue;
  public Producer(BlockingQueue<int[]> queue) {
    this.queue = queue;
  }
  @Override
  public void run() {
    try {
      // create a series of data items--array of two int
      // for a range of indexes (e.g. 0-9999)
      for (int idx = 0; idx < 10_000; idx++) {
        // create the new array
        int[] data = {-1, idx}; // "transactionally unsound"
        if (idx < 5_000) {
          Thread.sleep(1);
        }
        // "in steps" (simulating passing through a "transactionally unsafe" point
        //   set the data to { index, index }
        data[0] = idx; // transactionally sound :)
        // put that item into the queue (what do we do after put?)

        // test the test:
        if (idx == 5_000) {
          data[0] = -99;
        }
        queue.put(data);
        data = null;
      }
      // print "finished"
      System.out.println("Producer finished");
    } catch (InterruptedException ie) {
      try {
        queue.put(new int[]{-1, -1});
        System.out.println("Producer shut down on request");
      } catch (InterruptedException e) {
        System.out.println("Yikes, putting poison pill was interrupted!!!");;
      }
    }
    // items should { 0, 0 } then { 1, 1 }
    // create (locally) {-1, 0} first...
    // then change it to {0, 0}, then put it.
    // and proceed with the incrementing index...
  }
}

class Consumer implements Runnable {
  // needs access to a *shared* BlockingQueue
  private BlockingQueue<int[]> queue;
  public Consumer(BlockingQueue<int[]> queue) {
    this.queue = queue;
  }

  @Override
  public void run() {
    try {
      // for the *same* range of indexes..
      for (int idx = 0; idx < 10_000; idx++) {
        if (idx > 9_500) {
          Thread.sleep(1);
        }
        // read the next item from the queue
        // take blocks :) IF you try to take on an empty queue...
        // you'll be wanting that interrupt to actually get out
        // of the take
        int[] data = queue.take();
        // queue.poll() can have a timeout
        // careful!! if (queue.size() > 0) queue.take() is NOT SAFE
        // ALSO: if you execute synchronized... the ONLY WAY you will
        // ever get that thread running again is when it obtains the lock

        if (data[0] == -1 && data[1] == -1) {
          System.out.println("Shutting down on poison pill");
          return;
        }
        // validate it -- two subscripts should be equal to the index
        // (how could you verify this test works)
        if (data[0] != data[1] || data[0] != idx) {
          System.out.println("**** Error at index " + idx +
              " data is " + Arrays.toString(data));
        }
      }
      // print finished
    } catch (InterruptedException ie) {
      System.out.println("Consumer shut down on request");
    }
  }

  // for the *same* range of indexes..
    // read the next item from the queue
    // validate it -- two subscripts should be equal to the index
    // (how could you verify this test works)
  // print finished
}

public class LabExercise {
  public static void main(String[] args) throws InterruptedException {
    BlockingQueue<int[]> q = new ArrayBlockingQueue<>(10);
    Producer prod = new Producer(q);
    Consumer cons = new Consumer(q);
    Thread pThread = new Thread(prod);
    Thread cThread = new Thread(cons);
    pThread.start();
    cThread.start();
    System.out.println("Prod and Cons started...");
    Thread.sleep(2_000);
    pThread.interrupt();

    pThread.join();
    cThread.join();
    System.out.println("All finished. Main exiting.");
  }
  // configure the above, with an actual ArrayBlockingQueue (length 10 is fine)
  // kick it off
  // let it run to completion
  // satisfy yourself that:
  // transactional issues are solid
  // timing issues are solid (can you prove that queue was sometimes
  //   full, sometimes empty, yet still worked properly
  // visibility is solid (look at documentation)
  // you understand it :)

  // Extra credit (hard)
  // how could you modify this idea to have multiple producers
  // multiple consumers, and still validate that all the right
  // right data was received
}
