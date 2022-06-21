package runnables;

class MyWorker implements Runnable {
  public boolean stop = false;
  @Override
  public void run() {
    System.out.println(Thread.currentThread().getName() +
        " starting...");

    while (! stop)
      ;

    System.out.println(Thread.currentThread().getName() +
        " stopping...");
  }
}
public class Stopper {
  public static void main(String[] args) throws Throwable {
    MyWorker myTask = new MyWorker();
    Thread t = new Thread(myTask, "Worker");
//    t.setDaemon(true);
    t.start();
    Thread.sleep(1_000);
    myTask.stop = true;
    System.out.println(Thread.currentThread().getName() +
        "--main method about to exit");
    System.out.println("stop flag is " + myTask.stop);
  }
}
