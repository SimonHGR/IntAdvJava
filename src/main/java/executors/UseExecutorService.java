package executors;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.*;

class MyTask implements Callable<String> {
  private static int nextJobId = 1;
  private int myJobId = nextJobId++;

  private static void delay() {
    try {
      Thread.sleep((int)(Math.random() * 2000) + 1000);
    } catch (InterruptedException ie) {}
  }

  @Override
  public String call() throws Exception {
    System.out.println(Thread.currentThread().getName()
        + " starting task " + myJobId);
    delay();
    String msg = Thread.currentThread().getName()
        + " completed task " + myJobId;
    System.out.println(msg);
    return msg;
  }
}

public class UseExecutorService {
  public static void main(String[] args) {
    ExecutorService es = Executors.newFixedThreadPool(2);
    final int TASK_COUNT = 6;
    List<Future<String>> handles = new ArrayList<>();
    for (int idx = 0; idx < TASK_COUNT; idx++) {
      handles.add(es.submit(new MyTask()));
    }

    // close off the input queue,
    // run existing tasks to completion
    // then kill the threads
    es.shutdown();

    // close off input queue
    // empty the input queue (unstarted tasks are abandoned)
    // send interrupt to cancel all running tasks
//    es.shutdownNow();

    System.out.println("All tasks submitted");
    while (handles.size() > 0) {
      Iterator<Future<String>> iter = handles.iterator();
      while (iter.hasNext()) {
        Future<String> handle = iter.next();
        if (handle.isDone()) {
          try {
            String rv = handle.get();
            System.out.println("Task returned: " + rv);
          } catch (InterruptedException e) {
            System.out.println("odd, main received an interrupt");;
          } catch (ExecutionException e) {
            System.out.println("Task terminated with exception "
              + e.getCause());;
          } catch (CancellationException ce) {
            System.out.println("oops, you had already canceled that task!");
          }
          iter.remove();
        }
      }
    }
    System.out.println("All tasks done");
  }
}
