package streamexception;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

// Use vavr if you want to play with Either etc...
// Scala does this all rather more naturally
// I *think* Kotlin might too

class Either<S, F> {
  private S success;
  private F failure;

  private Either(S s, F f) {
    success = s;
    failure = f;
  }

  public static <S, F> Either<S, F> success(S s) {
    return new Either<>(s, null);
  }

  public static <S, F> Either<S, F> failure(F f) {
    return new Either<>(null, f);
  }

  public S getSuccess() {
    return success;
  }

  public F getFailure() {
    return failure;
  }

  public boolean isSuccess() {
    return failure == null;
  }

  public boolean isFailure() {
    return failure != null;
  }

  public Either<S, F> ifFailure(Consumer<F> op) {
    if (isFailure()) op.accept(failure);
    return this;
  }

  public Either<S, F> recover(Function<F, Either<S, F>>   op) {
    if (isFailure()) {
      return op.apply(failure);
    }
    return this;
  }
}

public class ExampleWithErrorReport {
  @FunctionalInterface
  interface ExFunction<A, R> {
    R apply(A a) throws Throwable;

    public static <A, R> Function<A, Either<R, Throwable>> wrap(streamexception.ExFunction<A, R> op) {
      return a -> {
        try {
          return Either.success(op.apply(a));
        } catch (Throwable t) {
          return Either.failure(t);
        }
      };
    }
  }

  public static void main(String[] args) throws IOException {

    Function<Throwable, Either<Stream<String>, Throwable>> recoveryFn =
        ExFunction.wrap(t -> Files.lines(Path.of("d.txt")));

    Stream.of("a.txt", "b.txt", "c.txt")
        .map(ExFunction.wrap(fn -> Files.lines(Path.of(fn))))
        .map(e -> e.ifFailure(System.out::println))
        .map(e -> e.recover(recoveryFn))
        .filter(e -> e.isSuccess())
        .flatMap(e -> e.getSuccess())
        .forEach(System.out::println);
  }
}
