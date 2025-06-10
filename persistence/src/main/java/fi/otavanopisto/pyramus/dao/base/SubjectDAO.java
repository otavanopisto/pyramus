package fi.otavanopisto.pyramus.dao.base;

import java.util.List;

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

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.base.EducationType;
import fi.otavanopisto.pyramus.domainmodel.base.Subject;
import fi.otavanopisto.pyramus.domainmodel.base.Subject_;
import fi.otavanopisto.pyramus.persistence.search.SearchResult;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Stateless
public class SubjectDAO extends PyramusEntityDAO<Subject> {

  /**
   * Creates a new subject.
   * 
   * @param code
   *          The subject code
   * @param name
   *          The subject name
   * @param educationType
   * 
   * @return The created subject
   */
  public Subject create(String code, String name, EducationType educationType) {
    EntityManager entityManager = getEntityManager();
    Subject subject = new Subject();
    subject.setName(name);
    subject.setCode(code);
    subject.setEducationType(educationType);
    entityManager.persist(subject);
    return subject;
  }

  /**
   * Returns the subject corresponding to the given code.
   * 
   * @param code
   *          The subject code
   * 
   * @return The subject corresponding to the given code
   */
  public Subject findByCode(String code) {
    // TODO How to add a case sensitive restriction with Hibernate and MySQL?
//    List<Subject> subjects = s.createCriteria(Subject.class).add(Restrictions.eq("code", code)).list();

    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Subject> criteria = criteriaBuilder.createQuery(Subject.class);
    Root<Subject> root = criteria.from(Subject.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(Subject_.code), code),
            criteriaBuilder.equal(root.get(Subject_.archived), Boolean.FALSE)
        )
    );
    
    List<Subject> subjects = entityManager.createQuery(criteria).getResultList();
    
    for (Subject subject : subjects) {
      if (code.equals(subject.getCode()))
        return subject;
    }

    return null;
  }

  public Subject findByEducationTypeAndCode(EducationType educationType, String subjectCode) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Subject> criteria = criteriaBuilder.createQuery(Subject.class);
    Root<Subject> root = criteria.from(Subject.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(Subject_.educationType), educationType),
            criteriaBuilder.equal(root.get(Subject_.code), subjectCode),
            criteriaBuilder.equal(root.get(Subject_.archived), Boolean.FALSE)
        )
    );
    
    List<Subject> subjects = entityManager.createQuery(criteria).getResultList();
    
    // This is sort of a hack for case sensitive search, same as in findByCode
    for (Subject subject : subjects) {
      if (subjectCode.equals(subject.getCode())) {
        return subject;
      }
    }

    return null;
  }

  public List<Subject> listByEducationType(EducationType educationType) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Subject> criteria = criteriaBuilder.createQuery(Subject.class);
    Root<Subject> root = criteria.from(Subject.class);
    criteria.select(root);

    Predicate educationTypePredicate;
    if (educationType != null)
      educationTypePredicate = criteriaBuilder.equal(root.get(Subject_.educationType), educationType);
    else
      educationTypePredicate = criteriaBuilder.isNull(root.get(Subject_.educationType));
    
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(Subject_.archived), Boolean.FALSE),
            educationTypePredicate
        ));
    
    return entityManager.createQuery(criteria).getResultList();
  }

  public SearchResult<Subject> searchSubjectsBasic(int resultsPerPage, int page, String text) {

    int firstResult = page * resultsPerPage;

    StringBuilder queryBuilder = new StringBuilder();

    if (!StringUtils.isBlank(text)) {
      queryBuilder.append("+(");
      addTokenizedSearchCriteria(queryBuilder, "name", text, false);
      addTokenizedSearchCriteria(queryBuilder, "code", text, false);
      addTokenizedSearchCriteria(queryBuilder, "educationType.name", text, false);
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

      LuceneSearchResult<Subject> fetch = session
          .search(Subject.class)
          .extension(LuceneExtension.get())
          .where(f -> 
            f.bool()
              .must(f.fromLuceneQuery(luceneQuery))
              .filter(f.match().field("archived").matching(Boolean.FALSE))
          )
          .fetch(firstResult, resultsPerPage);
      return searchResults(fetch, page, firstResult, resultsPerPage);

    } catch (ParseException e) {
      throw new PersistenceException(e);
    }
  }

  /**
   * Updates the given subject with the given data.
   * 
   * @param subject
   *          The subject to be updated
   * @param code
   *          The subject code
   * @param name
   *          The subject name
   * @param educationType
   */
  public void update(Subject subject, String code, String name, EducationType educationType) {
    EntityManager entityManager = getEntityManager();
    subject.setCode(code);
    subject.setName(name);
    subject.setEducationType(educationType);
    entityManager.persist(subject);
  }

}
