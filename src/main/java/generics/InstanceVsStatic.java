package generics;

import java.time.LocalDate;

class Pair<E> {
  private E left;
  private E right;

  public Pair(E left, E right) {
    this.left = left;
    this.right = right;
  }

  public <F, G> E getLeft() {
    return left;
  }

  public <F> void setLeft(E left) {
    this.left = left;
  }

//  public static void printType(E e) {
////    System.out.println(E.class);
//  }

  public E getRight() {
    return right;
  }

  public void setRight(E right) {
    this.right = right;
  }

  // assume this is in Stream<T>
//  public <U> Stream<U> map(Function<T, U>)
}

public class InstanceVsStatic {
  public static void main(String[] args) {
    Pair<String> ps = new Pair<String>("Hello", "Bonjour");
    Pair<LocalDate> pld = new Pair<LocalDate>(
        LocalDate.now(), LocalDate.now());
  }
}
