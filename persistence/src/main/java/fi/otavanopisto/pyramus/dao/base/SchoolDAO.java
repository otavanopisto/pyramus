package fi.otavanopisto.pyramus.dao.base;

import java.util.List;
import java.util.Set;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.hibernate.search.backend.lucene.LuceneExtension;
import org.hibernate.search.backend.lucene.search.query.LuceneSearchResult;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;

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
import fi.otavanopisto.pyramus.domainmodel.students.StudentGroup;
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
  public School create(String code, String name, SchoolField schoolField, StudentGroup studentGroup, BillingDetails billingDetails) {
    ContactInfo contactInfo = new ContactInfo();
    School school = new School();
    school.setCode(code);
    school.setName(name);
    school.setField(schoolField);
    school.setContactInfo(contactInfo);
    school.setBillingDetails(billingDetails);
    school.setStudentGroup(studentGroup);
    return persist(school);
  }
  
  public List<School> listByNameLowercaseAndArchived(String name, Boolean archived) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<School> criteria = criteriaBuilder.createQuery(School.class);
    Root<School> root = criteria.from(School.class);
    criteria.select(root);
    criteria.where(
      criteriaBuilder.and(
        criteriaBuilder.equal(criteriaBuilder.lower(root.get(School_.name)), name.toLowerCase()),
        criteriaBuilder.equal(root.get(School_.archived), archived)
      )
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
    SearchSession session = Search.session(entityManager);

    try {
      String queryString = queryBuilder.toString();
      Query luceneQuery;
      QueryParser parser = new QueryParser("", new StandardAnalyzer());
      if (StringUtils.isBlank(queryString)) {
        luceneQuery = new MatchAllDocsQuery();
      } else {
        luceneQuery = parser.parse(queryString);
      }

      LuceneSearchResult<School> fetch = session
          .search(School.class)
          .extension(LuceneExtension.get())
          .where(f -> 
            f.bool()
              .must(f.fromLuceneQuery(luceneQuery))
              .filter(f.match().field("archived").matching(Boolean.FALSE))
          )
          .sort(f -> 
              f.score()
              .then().field("nameSortable"))
          .fetch(firstResult, resultsPerPage);
      return searchResults(fetch, page, firstResult, resultsPerPage);
      
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
    SearchSession session = Search.session(entityManager);

    try {
      String queryString = queryBuilder.toString();
      Query luceneQuery;
      QueryParser parser = new QueryParser("", new StandardAnalyzer());
      if (StringUtils.isBlank(queryString)) {
        luceneQuery = new MatchAllDocsQuery();
      } else {
        luceneQuery = parser.parse(queryString);
      }

      LuceneSearchResult<School> fetch = session
        .search(School.class)
        .extension(LuceneExtension.get())
        .where(f -> f.bool().with(b -> {
          b.must(f.fromLuceneQuery(luceneQuery));

          if (filterArchived) {
            b.filter(f.match().field("archived").matching(Boolean.FALSE));
          }
        }))
        .sort(f -> 
            f.score()
            .then().field("nameSortable"))
        .fetch(firstResult, resultsPerPage);
      return searchResults(fetch, page, firstResult, resultsPerPage);
      
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
  public School update(School school, String code, String name, SchoolField schoolField, StudentGroup studentGroup) {
    EntityManager entityManager = getEntityManager();

    school.setCode(code);
    school.setName(name);
    school.setField(schoolField);
    school.setStudentGroup(studentGroup);
    entityManager.persist(school);
    
    return school;
  }
  
  public School updateContactInfo(School school, ContactInfo contactInfo) {
    school.setContactInfo(contactInfo);
    return persist(school);
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