package fi.otavanopisto.pyramus.dao.projects;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
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

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.TSB;
import fi.otavanopisto.pyramus.domainmodel.base.CourseOptionality;
import fi.otavanopisto.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.otavanopisto.pyramus.domainmodel.base.Tag;
import fi.otavanopisto.pyramus.domainmodel.projects.Project;
import fi.otavanopisto.pyramus.domainmodel.projects.StudentProject;
import fi.otavanopisto.pyramus.domainmodel.projects.StudentProject_;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.students.Student_;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.persistence.search.SearchResult;

@Stateless
public class StudentProjectDAO extends PyramusEntityDAO<StudentProject> {

  public StudentProject create(Student student, String name, String description,
      Double optionalStudiesLength, EducationalTimeUnit optionalStudiesLengthTimeUnit, CourseOptionality optionality, User user, Project project) {
    EntityManager entityManager = getEntityManager();

    Date now = new Date(System.currentTimeMillis());

    StudentProject studentProject = new StudentProject();
    studentProject.setStudent(student);
    studentProject.setName(name);
    studentProject.setDescription(description);
    studentProject.getOptionalStudiesLength().setUnit(optionalStudiesLengthTimeUnit);
    studentProject.getOptionalStudiesLength().setUnits(optionalStudiesLength);
    studentProject.setOptionality(optionality);
    studentProject.setCreator(user);
    studentProject.setCreated(now);
    studentProject.setLastModifier(user);
    studentProject.setLastModified(now);
    studentProject.setProject(project);

    entityManager.persist(studentProject);

    return studentProject;
  }
  
  public StudentProject updateTags(StudentProject studentProject, Set<Tag> tags) {
    EntityManager entityManager = getEntityManager();
    
    studentProject.setTags(tags);
    
    entityManager.persist(studentProject);
    
    return studentProject;
  }

  public void update(StudentProject studentProject, String name, String description,
      Double optionalStudiesLength, EducationalTimeUnit optionalStudiesLengthTimeUnit, CourseOptionality optionality, User user) {
    EntityManager entityManager = getEntityManager();

    Date now = new Date(System.currentTimeMillis());

    studentProject.setName(name);
    studentProject.setDescription(description);
    studentProject.getOptionalStudiesLength().setUnit(optionalStudiesLengthTimeUnit);
    studentProject.getOptionalStudiesLength().setUnits(optionalStudiesLength);
    studentProject.setOptionality(optionality);
    studentProject.setLastModifier(user);
    studentProject.setLastModified(now);

    entityManager.persist(studentProject);
  }

  public void updateStudent(StudentProject studentProject, Student student, User modifier) {
    EntityManager entityManager = getEntityManager();

    Date now = new Date(System.currentTimeMillis());

    studentProject.setStudent(student);
    studentProject.setLastModifier(modifier);
    studentProject.setLastModified(now);

    entityManager.persist(studentProject);
  }

  @SuppressWarnings("unchecked")
  public SearchResult<StudentProject> searchStudentProjectsBasic(int resultsPerPage, int page, String projectText, String studentText) {
    int firstResult = page * resultsPerPage;

    StringBuilder queryBuilder = new StringBuilder();

    if (!StringUtils.isBlank(projectText)) {
      queryBuilder.append("+(");
      addTokenizedSearchCriteria(queryBuilder, "name", projectText, false);
      addTokenizedSearchCriteria(queryBuilder, "description", projectText, false);
      addTokenizedSearchCriteria(queryBuilder, "tags.text", projectText, false);
      queryBuilder.append(')');
    }

    if (!StringUtils.isBlank(studentText)) {
      queryBuilder.append("+(");
      addTokenizedSearchCriteria(queryBuilder, "student.fullName", studentText, false); 
      addTokenizedSearchCriteria(queryBuilder, "student.tags.text", studentText, false); 
      queryBuilder.append(')');
    }
    
    EntityManager entityManager = getEntityManager();
    FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);

    try {
      QueryParser parser = new QueryParser("", new StandardAnalyzer());
      String queryString = queryBuilder.toString();
      Query luceneQuery;

      if (StringUtils.isBlank(queryString)) {
        luceneQuery = new MatchAllDocsQuery();
      }
      else {
        luceneQuery = parser.parse(queryString);
      }

      FullTextQuery query = (FullTextQuery) fullTextEntityManager.createFullTextQuery(luceneQuery, StudentProject.class)
          .setSort(new Sort(new SortField[]{SortField.FIELD_SCORE, new SortField("nameSortable", SortField.Type.STRING)}))
          .setFirstResult(firstResult)
          .setMaxResults(resultsPerPage);

      query.enableFullTextFilter("ArchivedStudentProject").setParameter("archived", Boolean.FALSE);

      int hits = query.getResultSize();
      int pages = hits / resultsPerPage;
      if (hits % resultsPerPage > 0) {
        pages++;
      }

      int lastResult = Math.min(firstResult + resultsPerPage, hits) - 1;

      return new SearchResult<>(page, pages, hits, firstResult, lastResult, query.getResultList());

    }
    catch (ParseException e) {
      throw new PersistenceException(e);
    }
  }

  @SuppressWarnings("unchecked")
  public SearchResult<StudentProject> searchStudentProjects(int resultsPerPage, int page, String name, String tags, String description, String studentName, boolean filterArchived) {
    int firstResult = page * resultsPerPage;

    StringBuilder queryBuilder = new StringBuilder();

    if (!StringUtils.isBlank(name))
      addTokenizedSearchCriteria(queryBuilder, "name", name, true);
    if (!StringUtils.isBlank(description))
      addTokenizedSearchCriteria(queryBuilder, "description", description, true);
    if (!StringUtils.isBlank(tags))
      addTokenizedSearchCriteria(queryBuilder, "tags.text", tags, true);
    if (!StringUtils.isBlank(description))
      addTokenizedSearchCriteria(queryBuilder, "student.fullName", studentName, true); 

    EntityManager entityManager = getEntityManager();
    FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);

    try {
      QueryParser parser = new QueryParser("", new StandardAnalyzer());
      String queryString = queryBuilder.toString();
      Query luceneQuery;

      if (StringUtils.isBlank(queryString)) {
        luceneQuery = new MatchAllDocsQuery();
      }
      else {
        luceneQuery = parser.parse(queryString);
      }

      FullTextQuery query = (FullTextQuery) fullTextEntityManager.createFullTextQuery(luceneQuery, StudentProject.class)
          .setSort(new Sort(new SortField[]{SortField.FIELD_SCORE, new SortField("nameSortable", SortField.Type.STRING)}))
          .setFirstResult(firstResult)
          .setMaxResults(resultsPerPage);

      if (filterArchived) {
        query.enableFullTextFilter("ArchivedStudentProject").setParameter("archived", Boolean.FALSE);
      }

      int hits = query.getResultSize();
      int pages = hits / resultsPerPage;
      if (hits % resultsPerPage > 0) {
        pages++;
      }

      int lastResult = Math.min(firstResult + resultsPerPage, hits) - 1;

      return new SearchResult<>(page, pages, hits, firstResult, lastResult, query.getResultList());

    }
    catch (ParseException e) {
      throw new PersistenceException(e);
    }
  }

  public List<StudentProject> listBy(Student student, Project project, TSB archived) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<StudentProject> criteria = criteriaBuilder.createQuery(StudentProject.class);
    Root<StudentProject> root = criteria.from(StudentProject.class);
    
    List<Predicate> predicates = new ArrayList<>();
    predicates.add(criteriaBuilder.equal(root.get(StudentProject_.project), project));
    predicates.add(criteriaBuilder.equal(root.get(StudentProject_.student), student));
    if (archived.isBoolean()) {
      predicates.add(criteriaBuilder.equal(root.get(StudentProject_.archived), archived.booleanValue()));
    }
    
    criteria.select(root);
    criteria.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
    
    return entityManager.createQuery(criteria).getResultList();
  }

  public List<StudentProject> listByStudent(Student student) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<StudentProject> criteria = criteriaBuilder.createQuery(StudentProject.class);
    Root<StudentProject> root = criteria.from(StudentProject.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(StudentProject_.archived), Boolean.FALSE),
            criteriaBuilder.equal(root.get(StudentProject_.student), student)
        ));
    
    return entityManager.createQuery(criteria).getResultList();
  }

  public List<StudentProject> listByProject(Project project, int firstResult, int maxResults) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<StudentProject> criteria = criteriaBuilder.createQuery(StudentProject.class);
    Root<StudentProject> root = criteria.from(StudentProject.class);
    Join<StudentProject, Student> studentJoin = root.join(StudentProject_.student);
    
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(StudentProject_.archived), Boolean.FALSE),
            criteriaBuilder.equal(root.get(StudentProject_.project), project)
        ));

    criteria.orderBy(
        criteriaBuilder.asc(studentJoin.get(Student_.lastName)),
        criteriaBuilder.asc(studentJoin.get(Student_.firstName))
    );
    
    TypedQuery<StudentProject> query = entityManager.createQuery(criteria);
    query.setFirstResult(firstResult);
    query.setMaxResults(maxResults);
    return query.getResultList();
  }

  public Long countByProject(Project project) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Long> criteria = criteriaBuilder.createQuery(Long.class);
    Root<StudentProject> root = criteria.from(StudentProject.class);
    criteria.select(criteriaBuilder.count(root));
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(StudentProject_.archived), Boolean.FALSE),
            criteriaBuilder.equal(root.get(StudentProject_.project), project)
        ));
    
    return entityManager.createQuery(criteria).getSingleResult();
  }

}
