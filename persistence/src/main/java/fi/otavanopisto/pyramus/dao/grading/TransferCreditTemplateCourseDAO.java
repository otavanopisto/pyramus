package fi.otavanopisto.pyramus.dao.grading;

import java.util.List;

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

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.base.CourseOptionality;
import fi.otavanopisto.pyramus.domainmodel.base.Curriculum;
import fi.otavanopisto.pyramus.domainmodel.base.EducationalLength;
import fi.otavanopisto.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.otavanopisto.pyramus.domainmodel.base.Subject;
import fi.otavanopisto.pyramus.domainmodel.grading.TransferCreditTemplate;
import fi.otavanopisto.pyramus.domainmodel.grading.TransferCreditTemplateCourse;
import fi.otavanopisto.pyramus.domainmodel.grading.TransferCreditTemplateCourse_;
import fi.otavanopisto.pyramus.persistence.search.SearchResult;

@Stateless
public class TransferCreditTemplateCourseDAO extends PyramusEntityDAO<TransferCreditTemplateCourse> {

  public SearchResult<TransferCreditTemplateCourse> searchTransferCreditTemplateCoursesBasic(int resultsPerPage, int page, String text) {
    int firstResult = page * resultsPerPage;

    StringBuilder queryBuilder = new StringBuilder();

    if (!StringUtils.isBlank(text)) {
      queryBuilder.append("+(");
      addTokenizedSearchCriteria(queryBuilder, "courseName", text, false);
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

      LuceneSearchResult<TransferCreditTemplateCourse> fetch = session
          .search(TransferCreditTemplateCourse.class)
          .extension(LuceneExtension.get())
          .where(f -> f.fromLuceneQuery(luceneQuery))
          .fetch(firstResult, resultsPerPage);
      return searchResults(fetch, page, firstResult, resultsPerPage);

    } catch (ParseException e) {
      throw new PersistenceException(e);
    }
  }
  
  /* TransferCreditTemplateCourse */
  
  public TransferCreditTemplateCourse create(TransferCreditTemplate transferCreditTemplate, String courseName, Integer courseNumber, CourseOptionality optionality, Double courseLength, EducationalTimeUnit courseLengthUnit, Subject subject, Curriculum curriculum) {
    EntityManager entityManager = getEntityManager();
    
    EducationalLength courseEductionalLength = new EducationalLength();
    courseEductionalLength.setUnits(courseLength);
    courseEductionalLength.setUnit(courseLengthUnit);
    entityManager.persist(courseEductionalLength);
    
    TransferCreditTemplateCourse transferCreditTemplateCourse = new TransferCreditTemplateCourse();
    transferCreditTemplateCourse.setCourseLength(courseEductionalLength);
    transferCreditTemplateCourse.setCourseName(courseName);
    transferCreditTemplateCourse.setCourseNumber(courseNumber);
    transferCreditTemplateCourse.setOptionality(optionality);
    transferCreditTemplateCourse.setSubject(subject);
    transferCreditTemplateCourse.setCurriculum(curriculum);

    entityManager.persist(transferCreditTemplateCourse);
    
    transferCreditTemplate.addCourse(transferCreditTemplateCourse);

    entityManager.persist(transferCreditTemplate);
  
    return transferCreditTemplateCourse;
  }
  
  public TransferCreditTemplateCourse update(TransferCreditTemplateCourse transferCreditTemplateCourse, String courseName, Integer courseNumber, CourseOptionality optionality, Double courseLength, EducationalTimeUnit courseLengthUnit, Subject subject, Curriculum curriculum) {
    EntityManager entityManager = getEntityManager();
    
    EducationalLength educationalLength = transferCreditTemplateCourse.getCourseLength();
    educationalLength.setUnits(courseLength);
    educationalLength.setUnit(courseLengthUnit);
    entityManager.persist(educationalLength);
    
    transferCreditTemplateCourse.setCourseName(courseName);
    transferCreditTemplateCourse.setCourseNumber(courseNumber);
    transferCreditTemplateCourse.setOptionality(optionality);
    transferCreditTemplateCourse.setSubject(subject);
    transferCreditTemplateCourse.setCurriculum(curriculum);

    entityManager.persist(transferCreditTemplateCourse);
    
    return transferCreditTemplateCourse;
  }
  
  public List<TransferCreditTemplateCourse> listByTemplate(TransferCreditTemplate template) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<TransferCreditTemplateCourse> criteria = criteriaBuilder.createQuery(TransferCreditTemplateCourse.class);
    Root<TransferCreditTemplateCourse> root = criteria.from(TransferCreditTemplateCourse.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.equal(root.get(TransferCreditTemplateCourse_.transferCreditTemplate), template)
    );
    
    return entityManager.createQuery(criteria).getResultList();
  }
  
  @Override
  public void delete(TransferCreditTemplateCourse transferCreditTemplateCourse) {
    super.delete(transferCreditTemplateCourse);
  }

}
