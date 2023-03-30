package fi.otavanopisto.pyramus.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;

public abstract class PyramusEntityDAO<T> extends GenericDAO<T> {

  @SuppressWarnings("unchecked")
  public List<T> listByArchived(boolean archived, Integer firstResult, Integer maxResults) {
    EntityManager entityManager = getEntityManager();
    Class<?> genericTypeClass = getGenericTypeClass();
    Query query = entityManager.createQuery("select o from " + genericTypeClass.getName() + " o where archived=:archived");
    query.setParameter("archived", archived);
    
    if (firstResult != null) {
      query.setFirstResult(firstResult);
    }
    
    if (maxResults != null) {
      query.setMaxResults(maxResults);
    }
    
    return query.getResultList();
  }
  
  public List<T> listUnarchived(Integer firstResult, Integer maxResults) {
    return listByArchived(false, firstResult, maxResults);
  }
  
  public List<T> listUnarchived() {
    return listByArchived(false, null, null);
  }
  
  public List<T> listArchived() {
    return listByArchived(true, null, null);
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
  
  protected void addTokenizedSearchCriteria(StringBuilder queryBuilder, String fieldName1, String fieldName2, String fieldName3, String value, boolean required) {
    String inputText = value.replaceAll(" +", " ");
    String[] tokens = escapeSearchCriteria(inputText).split("[ ,]");
    for (String token : tokens) {
      if (!StringUtils.isBlank(token)) {
        if (required) {
          queryBuilder.append("+");
        }
        queryBuilder.append('(').append(fieldName1).append(':').append(token).append(' ').append(fieldName2).append(':').append(token).append(' ').append(fieldName3).append(':').append(token).append(')');
      }
    }
  }
  
  protected void addTokenizedSearchCriteria(StringBuilder queryBuilder, boolean required, String value, String ... fields) {
    String inputText = value.replaceAll(" +", " ");
    String[] tokens = new String[] {inputText};
    
    if (!fields[0].contains("Emails")) {
      tokens = escapeSearchCriteria(inputText).split("[ ,]");
    }
    
    for (String token : tokens) {
      if (!StringUtils.isBlank(token)) {
        if (required) {
          queryBuilder.append("+");
        }
        
        queryBuilder.append('(');
        for (String field : fields) {
          queryBuilder.append(field).append(':').append(token).append(' ');
        }
        queryBuilder.append(')');
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
    return new SimpleDateFormat("yyyyMMdd").format(date);
  }
  
  private String escapeSearchCriteria(String value) {
    // QueryParser also escapes wildcard *, which we want as-is when it's a trailing character 
    if (StringUtils.endsWith(value, "*")) {
      return String.format("%s*", QueryParser.escape(value.substring(0, value.length() - 1)));
    }
    else {
      return QueryParser.escape(value);
    }
  }
  
  private static final String DATERANGE_INFINITY_LOW = "00000000";
  private static final String DATERANGE_INFINITY_HIGH = "99999999";
}
