package generics.taxation;

import java.util.List;

public class TaxPrepCompany {
  public static void prepareTaxes(Taxable t) {}
  // co-variance
  public static void prepareBulkTaxes(List<? extends Taxable> lt) {
//  public static <E extends Taxable> void prepareBulkTaxes(List<E> lt) {
//    lt.add(new Charity());
//    lt.add(new Corporation());
//    lt.add(new Individual());
    for (Taxable t : lt) {
      prepareTaxes(t);
    }
  }

//  public static <Individual extends E> void collectIndividualClients(List<E> li) {
// contra-variance
  public static void collectIndividualClients(List<? super Individual> li) {
//    E newOne = new Individual();
    li.add(new Individual());
    li.add(new Individual());
  }

  public static void main(String[] args) {
    List<Taxable> clients = List.of(
        new Corporation(), new Individual(), new Charity(), new Corporation()
    );
    collectIndividualClients(clients);
    prepareBulkTaxes(clients);

    List<Individual> joesClients = List.of(
        new Individual(), new Individual()
    );

    collectIndividualClients(joesClients);

    // Liskov substitution (L in SOLID) says (very roughly)
    // IF a is to be assignment compatible with a type B
    // then a must substitute for "an instance of B" without
    // the potential to cause "surprises"

//    List<Taxable> lt2 = joesClients;
    prepareBulkTaxes(joesClients);
  }
}
