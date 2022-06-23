package classes.parallelstream;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

class AverageMut {
  private double sum;
  private long count;

  public AverageMut(double sum, long count) {
    this.sum = sum;
    this.count = count;
  }

  public void include(double d) {
    this.sum += d;
    this.count++;
  }
  public void merge(AverageMut other) {
    this.sum += other.sum;
    this.count += other.count;
  }

  public Optional<Double> get() {
    if (count > 0) {
      return Optional.of(sum / count);
    } else {
      return Optional.empty();
    }
  }
}
public class AverageCollection {
  public static void main(String[] args) {
    final long COUNT = 1_000_000_000L;
    long start = System.nanoTime();
//    new Random().doubles(COUNT, -Math.PI, Math.PI)
    ThreadLocalRandom.current().doubles(COUNT, -Math.PI, Math.PI)
//        .parallel()
        .collect(
            () -> new AverageMut(0, 0),
            (a, d) -> a.include(d),
            (a1, a2) -> a1.merge(a2)
        )
        .get()
        .ifPresentOrElse(x -> System.out.println("Mean is " + x),
            () -> System.out.println("No data!"));
    long time = System.nanoTime() - start;
    System.out.printf("Elapsed %7.5f, rate is %,7.3f per second\n",
        time / 1_000_000_000.0, 1_000_000_000.0 * COUNT / time);
  }
}
/*
Collect!!!
JDK 17, 1 billion items
TLR, sequential, 9
Random, sequential, 9
TLR, parallel, 37.08521
Random, parallel, 40.92140

JDK 8, 1 billion items
TLR, sequential, 9
Random, sequential,
TLR, parallel,
Random, parallel,

 */