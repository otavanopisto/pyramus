package fi.otavanopisto.pyramus.persistence.search.filters;

import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.index.Term;
import org.apache.lucene.queries.TermsFilter;
import org.apache.lucene.search.Filter;
import org.hibernate.search.annotations.Factory;
import org.hibernate.search.annotations.Key;
import org.hibernate.search.filter.FilterKey;
import org.hibernate.search.filter.StandardFilterKey;

public class StudentIdFilterFactory {

  public void setStudentIds(List<Long> studentIds) {
    this.studentIds = studentIds;
  }

  @Factory
  public Filter getFilter() {
    List<Term> terms = new ArrayList<>();
    for (int i = 0; i < studentIds.size(); i++) {
      terms.add(new Term("users.id", studentIds.get(i).toString()));
    }
    return new TermsFilter(terms);
  }

  @Key
  public FilterKey getKey() {
    StandardFilterKey key = new StandardFilterKey();
    key.addParameter(this.studentIds);
    return key;
  }

  private List<Long> studentIds;
}