package generics;

import java.time.LocalDate;

// In "generics" we use extends to mean "is/must be assignment compatible with"
class Pair<E> {
  protected E left;
  protected E right;

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

//class ShoppingPair<E extends /*Object & */Sized, String> extends Pair<E> {
class ShoppingPair<E extends Sized & Colored> extends Pair<E> {
//  private String s = "Hello";
//  private java.lang.String s = "Hello";
  public ShoppingPair(E left, E right) {
    super(left, right);
  }

  public boolean isMatched() {
    return left.getSize() == right.getSize()
        && left.getColor().equals(right.getColor());
  }
}

public class InstanceVsStatic {
  public static void main(String[] args) {
    Pair<String> ps = new Pair<String>("Hello", "Bonjour");
    Pair<LocalDate> pld = new Pair<LocalDate>(
        LocalDate.now(), LocalDate.now());

    boolean b = true;
    var x =
        b ? "Hello" : 99;
    x.compareTo(null);
  }
}
