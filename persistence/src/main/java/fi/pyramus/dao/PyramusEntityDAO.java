package fi.pyramus.dao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;

public abstract class PyramusEntityDAO<T> extends GenericDAO<T> {

  @SuppressWarnings("unchecked")
  public List<T> listUnarchived(Integer firstResult, Integer maxResults) {
    EntityManager entityManager = getEntityManager();
    Class<?> genericTypeClass = getGenericTypeClass();
    Query query = entityManager.createQuery("select o from " + genericTypeClass.getName() + " o where archived=:archived");
    query.setParameter("archived", Boolean.FALSE);
    
    if (firstResult != null) {
      query.setFirstResult(firstResult);
    }
    
    if (maxResults != null) {
      query.setMaxResults(maxResults);
    }
    
    return query.getResultList();
  }
  
  public List<T> listUnarchived() {
    return listUnarchived(null, null);
  }
  
  public void forceReindex(T o) {
    FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(getEntityManager());
    fullTextEntityManager.index(o);
  }

  protected void addTokenizedSearchCriteria(StringBuilder queryBuilder, String fieldName, String value, boolean required, Float boost) {
    String inputText = value.replaceAll(" +", " ");
    String[] tokens = escapeSearchCriteria(inputText).split("[ ,]");
    
    for (String token : tokens) {
      if (!StringUtils.isBlank(token)) {
        queryBuilder.append(' ');
        if (required) {
          queryBuilder.append("+");
        }
        queryBuilder.append(fieldName).append(':').append(token);
        
        if (boost != null)
          queryBuilder.append("^").append(boost);
      }
    }
  }
  
  protected void addTokenizedSearchCriteria(StringBuilder queryBuilder, String fieldName, String value, boolean required) {
    addTokenizedSearchCriteria(queryBuilder, fieldName, value, required, null);
  }
  
  protected void addTokenizedSearchCriteria(StringBuilder queryBuilder, String fieldName1, String fieldName2, String value, boolean required) {
    String inputText = value.replaceAll(" +", " ");
    String[] tokens = escapeSearchCriteria(inputText).split("[ ,]");
    for (String token : tokens) {
      if (!StringUtils.isBlank(token)) {
        if (required) {
          queryBuilder.append("+");
        }
        queryBuilder.append('(').append(fieldName1).append(':').append(token).append(' ').append(fieldName2).append(':').append(token).append(')');
      }
    }
  }
  
  protected String getSearchDateInfinityHigh() {
    return DATERANGE_INFINITY_HIGH;
  }
  
  protected String getSearchDateInfinityLow() {
    return DATERANGE_INFINITY_LOW;
  }
  
  protected String getSearchFormattedDate(Date date) {
    return COURSE_SEARCH_DATE_FORMAT.format(date);
  }
  
  private String escapeSearchCriteria(String value) {
    return value
      .replaceAll("[\\:\\+\\-\\~\\(\\)\\{\\}\\[\\]\\^\\&\\|\\!\\\\]", "\\\\$0")
      .replaceAll("[*]{1,}", "\\*");
  }
  
  private static final String DATERANGE_INFINITY_LOW = "00000000";
  private static final String DATERANGE_INFINITY_HIGH = "99999999";
  private static final DateFormat COURSE_SEARCH_DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
}
