package fi.otavanopisto.pyramus.dao;

import java.util.Iterator;

import javax.persistence.TypedQuery;

public class Test<T> implements Iterable<T>, Iterator<T> {

  private TypedQuery<T> query;
  private int index = 0;
//  private int maxResults;

  public Test(TypedQuery<T> query) {
    this.query = query;
    query.setFirstResult(0);
    query.setMaxResults(1);
//    this.maxResults = query.
  }

  @Override
  public boolean hasNext() {
    return query.getSingleResult() != null;
  }

  @Override
  public T next() {
    T entity = query.getSingleResult();
    index++;
    query.setFirstResult(index);
    return entity;
  }

  @Override
  public Iterator<T> iterator() {
    return this;
  }

}
