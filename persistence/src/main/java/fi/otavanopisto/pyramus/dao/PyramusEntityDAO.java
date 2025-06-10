package fi.otavanopisto.pyramus.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.hibernate.search.backend.lucene.search.query.LuceneSearchResult;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.hibernate.search.mapper.orm.work.SearchIndexingPlan;

import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.students.Student_;
import fi.otavanopisto.pyramus.persistence.search.SearchResult;

public abstract class PyramusEntityDAO<T> extends GenericDAO<T> {

  @SuppressWarnings("unchecked")
  public List<T> listByArchived(boolean archived, Integer firstResult, Integer maxResults) {
    EntityManager entityManager = getEntityManager();
    Class<?> genericTypeClass = getGenericTypeClass();
    Query query = entityManager.createQuery("select o from " + genericTypeClass.getSimpleName() + " o where archived=:archived");
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
    EntityManager em = getEntityManager();
    SearchSession searchSession = Search.session(em); 
    SearchIndexingPlan indexingPlan = searchSession.indexingPlan(); 
    indexingPlan.addOrUpdate(o);
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
    
    String[] tokens = escapeSearchCriteria(inputText).split("[ ,]");
    
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

  protected void addTokenizedSearchCriteriaEmail(StringBuilder queryBuilder, boolean required, String value, String ... fields) {
    String inputText = value.replaceAll(" +", " ");
    
      if (!StringUtils.isBlank(inputText)) {
        if (required) {
          queryBuilder.append("+");
        }
        
        queryBuilder.append('(');
        for (String field : fields) {
          queryBuilder.append(field).append(':').append(inputText).append(' ');
        }
        queryBuilder.append(')');
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
  
  protected Predicate getPastStudentPredicate(CriteriaBuilder criteriaBuilder, Path<Student> student, Date studentStudyEndThreshold) {
    return criteriaBuilder.and(
        criteriaBuilder.isNotNull(student.get(Student_.studyEndDate)),
        criteriaBuilder.lessThan(student.get(Student_.studyEndDate), studentStudyEndThreshold)
    );
  }
  
  protected <E> SearchResult<E> searchResults(LuceneSearchResult<E> fetch, long page, long firstResult, long resultsPerPage) {
    long hits = fetch.total().hitCount();
    long pages = hits / resultsPerPage;
    if (hits % resultsPerPage > 0) {
      pages++;
    }

    long lastResult = Math.min(firstResult + resultsPerPage, hits) - 1;

    return new SearchResult<>(page, pages, hits, firstResult, lastResult, fetch.hits());
  }
  
  private static final String DATERANGE_INFINITY_LOW = "00000000";
  private static final String DATERANGE_INFINITY_HIGH = "99999999";
}
