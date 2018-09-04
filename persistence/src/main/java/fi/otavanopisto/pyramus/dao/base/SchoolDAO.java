package fi.otavanopisto.pyramus.dao.base;

import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;

import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.base.BillingDetails;
import fi.otavanopisto.pyramus.domainmodel.base.ContactInfo;
import fi.otavanopisto.pyramus.domainmodel.base.School;
import fi.otavanopisto.pyramus.domainmodel.base.SchoolField;
import fi.otavanopisto.pyramus.domainmodel.base.SchoolVariable;
import fi.otavanopisto.pyramus.domainmodel.base.SchoolVariableKey;
import fi.otavanopisto.pyramus.domainmodel.base.SchoolVariable_;
import fi.otavanopisto.pyramus.domainmodel.base.School_;
import fi.otavanopisto.pyramus.domainmodel.base.Tag;
import fi.otavanopisto.pyramus.persistence.search.SearchResult;

@Stateless
public class SchoolDAO extends PyramusEntityDAO<School> {

  /**
   * Creates a new school.
   * 
   * @param code
   *          The school code
   * @param name
   *          The school name
   * @param schoolField
   * @param billingDetails 
   * 
   * @return The created school
   */
  public School create(String code, String name, SchoolField schoolField, BillingDetails billingDetails) {
    ContactInfo contactInfo = new ContactInfo();
    School school = new School();
    school.setCode(code);
    school.setName(name);
    school.setField(schoolField);
    school.setContactInfo(contactInfo);
    school.setBillingDetails(billingDetails);
    return persist(school);
  }
  
  public List<School> listByNameLowercase(String name) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<School> criteria = criteriaBuilder.createQuery(School.class);
    Root<School> root = criteria.from(School.class);
    criteria.select(root);
    criteria.where(
      criteriaBuilder.equal(
        criteriaBuilder.lower(root.get(School_.name)), name.toLowerCase())
    );
    
    return entityManager.createQuery(criteria).getResultList();
  }

  /**
   * Returns a list of all schools having a school variable with the given key and value.
   * 
   * @param key
   *          The school variable key
   * @param value
   *          The school variable value
   * 
   * @return A list of all schools having a school variable with the given key and value
   */
  public List<School> listByVariable(String key, String value) {
    SchoolVariableKeyDAO schoolVariableKeyDAO = DAOFactory.getInstance().getSchoolVariableKeyDAO();
    SchoolVariableKey schoolVariableKey = schoolVariableKeyDAO.findVariableKey(key);

    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<School> criteria = criteriaBuilder.createQuery(School.class);
    Root<SchoolVariable> root = criteria.from(SchoolVariable.class);
    criteria.select(root.get(SchoolVariable_.school));
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(SchoolVariable_.key), schoolVariableKey),
            criteriaBuilder.equal(root.get(SchoolVariable_.value), value)
        ));
    
    return entityManager.createQuery(criteria).getResultList();
  }

  @SuppressWarnings("unchecked")
  public SearchResult<School> searchSchoolsBasic(int resultsPerPage, int page, String text) {

    int firstResult = page * resultsPerPage;

    StringBuilder queryBuilder = new StringBuilder();

    if (!StringUtils.isBlank(text)) {
      queryBuilder.append("+(");
      addTokenizedSearchCriteria(queryBuilder, "code", text, false);
      addTokenizedSearchCriteria(queryBuilder, "name", text, false);
      addTokenizedSearchCriteria(queryBuilder, "tags.text", text, false);
      queryBuilder.append(")");
    }

    EntityManager entityManager = getEntityManager();
    FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);

    try {
      String queryString = queryBuilder.toString();
      Query luceneQuery;
      QueryParser parser = new QueryParser("", new StandardAnalyzer());
      if (StringUtils.isBlank(queryString)) {
        luceneQuery = new MatchAllDocsQuery();
      } else {
        luceneQuery = parser.parse(queryString);
      }

      FullTextQuery query = (FullTextQuery) fullTextEntityManager.createFullTextQuery(luceneQuery, School.class)
          .setSort(new Sort(new SortField[] { SortField.FIELD_SCORE, new SortField("nameSortable", SortField.Type.STRING) })).setFirstResult(firstResult)
          .setMaxResults(resultsPerPage);
      query.enableFullTextFilter("ArchivedSchool").setParameter("archived", Boolean.FALSE);

      int hits = query.getResultSize();
      int pages = hits / resultsPerPage;
      if (hits % resultsPerPage > 0) {
        pages++;
      }

      int lastResult = Math.min(firstResult + resultsPerPage, hits) - 1;

      return new SearchResult<>(page, pages, hits, firstResult, lastResult, query.getResultList());

    } catch (ParseException e) {
      throw new PersistenceException(e);
    }
  }

  /**
   * Returns a list of schools matching the given search terms.
   * 
   * @param resultsPerPage
   *          The amount of schools per page
   * @param page
   *          The search results page
   * @param code
   *          The school code
   * @param name
   *          The school name
   * @param tags
   *          The schools tags
   * @param filterArchived
   *          <code>true</code> if archived schools should be omitted, otherwise <code>false</code>
   * 
   * @return A list of schools matching the given search terms
   */
  @SuppressWarnings("unchecked")
  public SearchResult<School> searchSchools(int resultsPerPage, int page, String code, String name, String tags, boolean filterArchived) {

    int firstResult = page * resultsPerPage;

    StringBuilder queryBuilder = new StringBuilder();
    if (!StringUtils.isBlank(code)) {
      addTokenizedSearchCriteria(queryBuilder, "code", code, false);
    }
    if (!StringUtils.isBlank(name)) {
      addTokenizedSearchCriteria(queryBuilder, "name", name, false);
    }
    if (!StringUtils.isBlank(tags)) {
      addTokenizedSearchCriteria(queryBuilder, "tags.text", tags, false);
    }

    EntityManager entityManager = getEntityManager();
    FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);

    try {
      String queryString = queryBuilder.toString();
      Query luceneQuery;
      QueryParser parser = new QueryParser("", new StandardAnalyzer());
      if (StringUtils.isBlank(queryString)) {
        luceneQuery = new MatchAllDocsQuery();
      } else {
        luceneQuery = parser.parse(queryString);
      }

      FullTextQuery query = (FullTextQuery) fullTextEntityManager.createFullTextQuery(luceneQuery, School.class)
          .setSort(new Sort(new SortField[] { SortField.FIELD_SCORE, new SortField("nameSortable", SortField.Type.STRING) })).setFirstResult(firstResult)
          .setMaxResults(resultsPerPage);

      if (filterArchived) {
        query.enableFullTextFilter("ArchivedSchool").setParameter("archived", Boolean.FALSE);
      }

      int hits = query.getResultSize();
      int pages = hits / resultsPerPage;
      if (hits % resultsPerPage > 0) {
        pages++;
      }

      int lastResult = Math.min(firstResult + resultsPerPage, hits) - 1;

      return new SearchResult<>(page, pages, hits, firstResult, lastResult, query.getResultList());

    } catch (ParseException e) {
      throw new PersistenceException(e);
    }
  }

  /**
   * Updates the given school with the given data.
   * 
   * @param school
   *          The school to be updated
   * @param code
   *          The school code
   * @param name
   *          The school name
   * @param schoolField
   * @return 
   */
  public School update(School school, String code, String name, SchoolField schoolField) {
    EntityManager entityManager = getEntityManager();

    school.setCode(code);
    school.setName(name);
    school.setField(schoolField);
    entityManager.persist(school);
    
    return school;
  }

  public School updateTags(School school, Set<Tag> tags) {
    EntityManager entityManager = getEntityManager();
    
    school.setTags(tags);

    entityManager.persist(school);

    return school;
  }

  public School updateBillingDetails(School school, BillingDetails billingDetails) {
    school.setBillingDetails(billingDetails);
    return persist(school);
  }
  
  public School addTag(School school, Tag tag) {
    school.addTag(tag);
    return persist(school);
  }

  public School removeTag(School school, Tag tag) {
    school.removeTag(tag);
    return persist(school);
  }

}