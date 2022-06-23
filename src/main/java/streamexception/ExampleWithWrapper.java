package streamexception;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

@FunctionalInterface
interface ExFunction<A, R> {
  R apply(A a) throws Throwable;

  public static <A, R> Function<A, Optional<R>> wrap(ExFunction<A, R> op) {
    return a -> {
      try {
        return Optional.of(op.apply(a));
      } catch (Throwable t) {
        return Optional.empty();
      }
    };
  }
}

public class ExampleWithWrapper {
  public static void main(String[] args) throws IOException {
    Stream.of("a.txt", "b.txt", "c.txt")
        .map(ExFunction.wrap(fn -> Files.lines(Path.of(fn))))
        .peek(opt -> {
          if (opt.isEmpty()) {
            System.out.println("Hmm, something broke");
          }
        })
        .filter(opt -> opt.isPresent())
        .flatMap(opt -> opt.get())
        .forEach(System.out::println);
  }
}
