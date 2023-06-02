package fi.otavanopisto.pyramus.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.Predicate;

public class Predicates {

  public static Predicates newInstance() {
    return new Predicates();
  }

  /**
   * Adds a predicate to the list
   * 
   * @param predicate
   * @return this list
   */
  public Predicates add(Predicate predicate) {
    predicates.add(predicate);
    return this;
  }
  
  /**
   * Adds a predicate to the list if condition is true
   * 
   * @param predicate
   * @param condition
   * @return this list
   */
  public Predicates add(Predicate predicate, boolean condition) {
    if (condition) {
      predicates.add(predicate);
    }
    return this;
  }
  
  public boolean isEmpty() {
    return predicates.isEmpty();
  }
  
  public int size() {
    return predicates.size();
  }
  
  public Predicate[] array() {
    return predicates.toArray(new Predicate[0]);
  }
  
  private List<Predicate> predicates = new ArrayList<>();
}
