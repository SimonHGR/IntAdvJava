package streamexception;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;

public class ExampleWithWrapper {

  public static Optional<Stream<String>> getStreamFromFile(String fn) {
    try {
      return Optional.of(Files.lines(Path.of(fn)));
    } catch (IOException ioe) {
      System.err.println("uh oh!!! " + fn + " not found!");
      return Optional.empty();
    }
  }

  public static void main(String[] args) throws IOException {
    Stream.of("a.txt", "b.txt", "c.txt")
        .map(fn -> getStreamFromFile(fn))
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
