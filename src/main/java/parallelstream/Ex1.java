package parallelstream;

import java.util.stream.IntStream;

public class Ex1 {
  private volatile static long counter = 0;
  public static void main(String[] args) {
    long count = IntStream.range(0, 100_000_000)
        // generally "side-effects" are bad in FP
//        .peek(System.out::println)
//        .map(x -> {
//          System.out.println("doubling...");
//          return x * 2;
//        })
        .filter(x -> true)
//        .filter(x -> x % 2 == 0)
        .count(); // can optimize out the entire stream process
//        .forEach(System.out::println);
//        .forEach(s -> System.out.println(s));
    System.out.println("there were " + count + " items");

//    long [] counter = { 0 };
//    long counter = 0;
    long count2 = IntStream.range(0, 100_000_000)
        .parallel()
//        .peek(x -> {long yy = counter[0]++;})
        .peek(x -> counter++)
        .filter(x -> true)
        .count(); // can optimize out the entire stream process

//    System.out.println(counter[0]);
    System.out.println(counter);
  }
}
