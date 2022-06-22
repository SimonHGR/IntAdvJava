package runnables;

class MyCounter implements Runnable {
  public long count = 0;
  @Override
  public void run() {
    while(true) {
      try {
        Thread.sleep(1);
      } catch (InterruptedException ie) {
        System.out.println("Shutdown requested");
        return;
      }
//      if (Thread.interrupted()) {
//        // notice an interrupt by a "blocking" method throwing
//        // InterruptedException
//
//        // tidy up, complete partially completed work, then
//        // shut this thread down
//        return;
//      }
      for (int i = 0; i < 1_000_000_000; i++) {
        count++;
      }
    }
  }
}
public class Counter {
  public static void main(String[] args) throws Throwable {
    MyCounter mc = new MyCounter();
    Thread t1 = new Thread(mc);
    t1.start();
    Thread.sleep(1_000);
    // potentially set a bunch o'data to useful values
    // these would be visible to the interrupted thread
    // AFTER it has "noticed" the interrupt
    t1.interrupt();
//    t1.join();
    System.out.println("count is " + mc.count);
  }
}
