package parallelstream;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

class Average {
  private double sum;
  private long count;

  public Average(double sum, long count) {
    this.sum = sum;
    this.count = count;
  }

  public Average merge(Average other) {
    return new Average(this.sum + other.sum, this.count + other.count);
  }

  public double get() {
    return sum / count; // what about 0 / 0 ???
  }
}
public class AverageReduction {
  public static void main(String[] args) {
    final long COUNT = 1_000_000_000L;
    long start = System.nanoTime();
    new Random().doubles(COUNT, -Math.PI, Math.PI)
//    ThreadLocalRandom.current().doubles(COUNT, -Math.PI, Math.PI)
        .parallel()
        .mapToObj(x -> new Average(x, 1))
        .reduce((a1, a2) -> a1.merge(a2))
        .ifPresent/*OrElse*/(x -> System.out.println("Mean is " + x.get())/*,
            () -> System.out.println("No data!")*/);
    long time = System.nanoTime() - start;
    System.out.printf("Elapsed %7.5f, rate is %,7.3f per second\n",
        time / 1_000_000_000.0, 1_000_000_000.0 * COUNT / time);
  }
}
/*
JDK 17, 1 billion items
TLR, sequential, 14 seconds
Random, sequential, 14 seconds
TLR, parallel, 41 seconds
Random, parallel, 39 seconds

JDK 8, 1 billion items
TLR, sequential, 5 seconds
Random, sequential, 12 seconds
TLR, parallel, 4.4 seconds
Random, parallel, 43 seconds

 */
