package generics;

public class Shopping {
  public static void main(String[] args) {
//    Pair<Shoe> pShoe = new Pair<>(new Shoe(9, "Black"), "Black");
    ShoppingPair<Shoe> pShoe = new ShoppingPair<>(
        new Shoe(10, "Blue"), new Shoe(10, "Black"));
    Shoe theLeft = pShoe.getLeft();

    if (pShoe.isMatched()) {
      System.out.println("good for sale!");
    } else {
      System.out.println("not a good match!");
    }
  }
}
