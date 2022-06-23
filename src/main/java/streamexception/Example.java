package streamexception;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

public class Example {

  public static Optional<Stream<String>> getStreamFromFile(String fn) {
    try {
      return Optional.of(Files.lines(Path.of(fn)));
    } catch (IOException ioe) {
//      throw new RuntimeException(ioe);
      System.err.println("uh oh!!! " + fn + " not found!");
      return Optional.empty();
    }
  }

  public static void main(String[] args) throws IOException {
//    Files.lines(Path.of("a.txt"))
//        .forEach(System.out::println);

//    Function<String, Stream<String>> fileMapper =
//        fn -> Files.lines(Path.of(fn));

//    try {
      Stream.of("a.txt", "b.txt", "c.txt")
//          .flatMap(fn -> getStreamFromFile(fn))
          .map(fn -> getStreamFromFile(fn))
          .peek(opt -> {
            if (opt.isEmpty()) {System.out.println("Hmm, something broke");}
          })
          .filter(opt -> opt.isPresent())
          .flatMap(opt -> opt.get())
          .forEach(System.out::println);
//    } catch (IOException ioe) {
//      System.out.println("Hmmm.");
//    }

//    Stream.of(Set.of("a", "b"), Set.of("c", "d", "e"))
////        .map(x -> new ArrayList(x))
//        .flatMap(x -> new ArrayList<>(x).stream())
//        .forEach(System.out::println);

//    FileReader fr = new FileReader("a.txt");
//    int c;
//    while ((c = fr.read()) != -2) { // OOPS, not -2, should be -1...
//      System.out.print((char)c);
//    }
  }
}
