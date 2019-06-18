package fi.otavanopisto.pyramus.dao.students;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
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
import fi.otavanopisto.pyramus.domainmodel.base.Tag;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.students.StudentGroup;
import fi.otavanopisto.pyramus.domainmodel.students.StudentGroupStudent;
import fi.otavanopisto.pyramus.domainmodel.students.StudentGroupStudent_;
import fi.otavanopisto.pyramus.domainmodel.students.StudentGroupUser;
import fi.otavanopisto.pyramus.domainmodel.students.StudentGroupUser_;
import fi.otavanopisto.pyramus.domainmodel.students.StudentGroup_;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.events.StudentGroupCreatedEvent;
import fi.otavanopisto.pyramus.events.StudentGroupUpdatedEvent;
import fi.otavanopisto.pyramus.persistence.search.SearchResult;

@Stateless
public class StudentGroupDAO extends PyramusEntityDAO<StudentGroup> {

  @Inject
  private Event<StudentGroupCreatedEvent> studentGroupCreatedEvent;
  
  @Inject
  private Event<StudentGroupUpdatedEvent> studentGroupUpdatedEvent;
  
//  @Inject
//  private Event<StudentGroupArchivedEvent> studentGroupRemovedEvent;
  
  public StudentGroup create(String name, String description, Date beginDate, User creatingUser, Boolean guidanceGroup) {
    EntityManager entityManager = getEntityManager();

    Date now = new Date(System.currentTimeMillis());
    
    StudentGroup studentGroup = new StudentGroup();
    studentGroup.setName(name);
    studentGroup.setDescription(description);
    studentGroup.setBeginDate(beginDate);

    studentGroup.setCreator(creatingUser);
    studentGroup.setCreated(now);
    studentGroup.setLastModifier(creatingUser);
    studentGroup.setLastModified(now);
    
    studentGroup.setGuidanceGroup(guidanceGroup);

    entityManager.persist(studentGroup);

    studentGroupCreatedEvent.fire(new StudentGroupCreatedEvent(studentGroup.getId()));
    
    return studentGroup;
  }

  public StudentGroup setStudentGroupTags(StudentGroup studentGroup, Set<Tag> tags) {
    EntityManager entityManager = getEntityManager();
    
    studentGroup.setTags(tags);
    
    entityManager.persist(studentGroup);
    
//    TODO: atm tag setting is done in conjunction with update so this would result in double messaging so it's disabled atm
//    studentGroupUpdatedEvent.fire(new StudentGroupUpdatedEvent(studentGroup.getId()));

    return studentGroup;
  }

  public StudentGroup update(StudentGroup studentGroup, String name, String description, Date beginDate, User updatingUser) {
    EntityManager entityManager = getEntityManager();

    studentGroup.setName(name);
    studentGroup.setDescription(description);
    studentGroup.setBeginDate(beginDate);

    studentGroup.setLastModified(new Date(System.currentTimeMillis()));
    studentGroup.setLastModifier(updatingUser);

    entityManager.persist(studentGroup);

    studentGroupUpdatedEvent.fire(new StudentGroupUpdatedEvent(studentGroup.getId()));

    return studentGroup;
  }
  
  public List<StudentGroup> listByStudent(Student student) {
    return listByStudent(student, null, null, null);
  }
  
  public List<StudentGroup> listByStudent(Student student, Integer firstResult, Integer maxResults, Boolean archived) {
    EntityManager entityManager = getEntityManager(); 

    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<StudentGroup> criteria = criteriaBuilder.createQuery(StudentGroup.class);
    Root<StudentGroupStudent> root = criteria.from(StudentGroupStudent.class);
    Join<StudentGroupStudent, StudentGroup> studentGroup = root.join(StudentGroupStudent_.studentGroup);
    
    List<Predicate> predicates = new ArrayList<Predicate>();
    predicates.add(criteriaBuilder.equal(root.get(StudentGroupStudent_.student), student));
    if (archived != null)
      predicates.add(criteriaBuilder.equal(studentGroup.get(StudentGroup_.archived), archived));
    
    criteria.select(root.get(StudentGroupStudent_.studentGroup));
    criteria.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
    
    TypedQuery<StudentGroup> query = entityManager.createQuery(criteria);
    
    if (firstResult != null) {
      query.setFirstResult(firstResult);
    }
   
    if (maxResults != null) {
      query.setMaxResults(maxResults);
    }
  
    return query.getResultList();
  }

  public List<StudentGroup> listByNameLowercaseAndArchived(String name, Boolean archived) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<StudentGroup> criteria = criteriaBuilder.createQuery(StudentGroup.class);
    Root<StudentGroup> root = criteria.from(StudentGroup.class);
    criteria.select(root);
    criteria.where(
      criteriaBuilder.and(
        criteriaBuilder.equal(criteriaBuilder.lower(root.get(StudentGroup_.name)), name.toLowerCase()),
        criteriaBuilder.equal(root.get(StudentGroup_.archived), archived)
      )
    );
    
    return entityManager.createQuery(criteria).getResultList();
  }

  @SuppressWarnings("unchecked")
  public SearchResult<StudentGroup> searchStudentGroups(int resultsPerPage, int page, String name, 
      String tags, String description, User user, Date timeframeStart, Date timeframeEnd, 
      boolean filterArchived) {
    int firstResult = page * resultsPerPage;

    String timeframeS = null;
    if (timeframeStart != null)
      timeframeS = getSearchFormattedDate(timeframeStart);

    String timeframeE = null;
    if (timeframeEnd != null)
      timeframeE = getSearchFormattedDate(timeframeEnd);

    StringBuilder queryBuilder = new StringBuilder();

    if (!StringUtils.isBlank(name)) {
      addTokenizedSearchCriteria(queryBuilder, "name", name, true);
    }

    if (!StringUtils.isBlank(tags)) {
      addTokenizedSearchCriteria(queryBuilder, "tags.text", tags, true);
    }
    
    if (!StringUtils.isBlank(description)) {
      addTokenizedSearchCriteria(queryBuilder, "description", description, true);
    }
    
    if (user != null) {
      addTokenizedSearchCriteria(queryBuilder, "users.staffMember.id", user.getId().toString(), true);
    }
    
    if (timeframeS != null && timeframeE != null) {
      /**
       * (beginDate between timeframeStart - timeframeEnd or endDate between timeframeStart -
       * timeframeEnd) or (startDate less than timeframeStart and endDate more than
       * timeframeEnd)
       **/
      queryBuilder.append(" +(").append("(").append("beginDate:[").append(timeframeS).append(" TO ").append(
          timeframeE).append("]").append(")").append(")");
    }
    else if (timeframeS != null) {
      /** beginDate > timeframeStart **/
      queryBuilder.append(" +(").append("beginDate:[").append(timeframeS).append(" TO ").append(
          getSearchDateInfinityHigh()).append("]").append(")");
    }
    else if (timeframeE != null) {
      /** beginDate < timeframeEnd **/
      queryBuilder.append(" +(").append("beginDate:[").append(getSearchDateInfinityLow()).append(" TO ").append(
          timeframeE).append("]").append(")");
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

      FullTextQuery query = (FullTextQuery) fullTextEntityManager.createFullTextQuery(luceneQuery, StudentGroup.class)
          .setSort(new Sort(new SortField[]{SortField.FIELD_SCORE, new SortField("nameSortable", SortField.Type.STRING)}))
          .setFirstResult(firstResult)
          .setMaxResults(resultsPerPage);

      if (filterArchived)
        query.enableFullTextFilter("ArchivedStudentGroup").setParameter("archived", Boolean.FALSE);

      int hits = query.getResultSize();
      int pages = hits / resultsPerPage;
      if (hits % resultsPerPage > 0)
        pages++;

      int lastResult = Math.min(firstResult + resultsPerPage, hits) - 1;

      return new SearchResult<>(page, pages, hits, firstResult, lastResult, query.getResultList());
    }
    catch (ParseException e) {
      throw new PersistenceException(e);
    }
  }

  @SuppressWarnings("unchecked")
  public SearchResult<StudentGroup> searchStudentGroupsBasic(int resultsPerPage, int page, String text) {
    int firstResult = page * resultsPerPage;

    StringBuilder queryBuilder = new StringBuilder();
    if (!StringUtils.isBlank(text)) {
      queryBuilder.append("+(");
      addTokenizedSearchCriteria(queryBuilder, "name", text, false);
      addTokenizedSearchCriteria(queryBuilder, "tags.text", text, false);
      addTokenizedSearchCriteria(queryBuilder, "description", text, false);
      queryBuilder.append(")");
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

      FullTextQuery query = (FullTextQuery) fullTextEntityManager.createFullTextQuery(luceneQuery, StudentGroup.class)
          .setSort(new Sort(new SortField[] { SortField.FIELD_SCORE, new SortField("nameSortable", SortField.Type.STRING)}))
          .setFirstResult(firstResult)
          .setMaxResults(resultsPerPage);

      query.enableFullTextFilter("ArchivedStudentGroup").setParameter("archived", Boolean.FALSE);


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
  
  public StudentGroup updateGuidanceGroup(StudentGroup studentGroup, Boolean guidanceGroup) {
    studentGroup.setGuidanceGroup(guidanceGroup);
    return persist(studentGroup);
  }

  public List<StudentGroup> listByStaffMember(StaffMember staffMember, Integer firstResult, Integer maxResults, Boolean archived) {
    EntityManager entityManager = getEntityManager(); 

    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<StudentGroup> criteria = criteriaBuilder.createQuery(StudentGroup.class);
    Root<StudentGroupUser> root = criteria.from(StudentGroupUser.class);
    Join<StudentGroupUser, StudentGroup> studentGroup = root.join(StudentGroupUser_.studentGroup);

    List<Predicate> predicates = new ArrayList<Predicate>();
    predicates.add(criteriaBuilder.equal(root.get(StudentGroupUser_.staffMember), staffMember));
    if (archived != null)
      predicates.add(criteriaBuilder.equal(studentGroup.get(StudentGroup_.archived), archived));
    
    criteria.select(root.get(StudentGroupUser_.studentGroup));
    criteria.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
    
    TypedQuery<StudentGroup> query = entityManager.createQuery(criteria);
    
    if (firstResult != null) {
      query.setFirstResult(firstResult);
    }
   
    if (maxResults != null) {
      query.setMaxResults(maxResults);
    }
  
    return query.getResultList();
  }
  
}
