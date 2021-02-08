package fi.otavanopisto.pyramus.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.Predicate;

public class Predicates {

  public static Predicates newInstance() {
    return new Predicates();
  }
  
  public Predicates add(Predicate predicate) {
    predicates.add(predicate);
    return this;
  }
  
  public Predicate[] array() {
    return predicates.toArray(new Predicate[0]);
  }
  
  private List<Predicate> predicates = new ArrayList<>();
}
