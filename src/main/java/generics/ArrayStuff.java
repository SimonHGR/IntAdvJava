package generics;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class ArrayStuff {

  public static <F, E extends F> F[] getAsArray(List<E> in, Class<F> c) {
//    E[] result = new E[in.size()];
//    E[] result = (E[])new Object[in.size()];
    F[] result = (F[])Array.newInstance(c, in.size());
    for (int idx = 0; idx < in.size(); idx++) {
      result[idx] = in.get(idx);
    }
    return result;
  }

  public static void main(String[] args) {
    List<String> ls = List.of("Fred", "Jim", "Sheila");

//    String [] sa = getAsArray(ls, String.class);
    CharSequence[] sa = getAsArray(ls, CharSequence.class);
    sa[0] = new StringBuilder("Frederick");

    System.out.println(Arrays.toString(sa));
  }
}
