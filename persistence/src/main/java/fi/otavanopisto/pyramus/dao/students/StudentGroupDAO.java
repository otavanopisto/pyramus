package fi.otavanopisto.pyramus.dao.students;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

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
import fi.otavanopisto.pyramus.domainmodel.base.ArchivableEntity;
import fi.otavanopisto.pyramus.domainmodel.base.Organization;
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
import fi.otavanopisto.pyramus.events.StudentGroupArchivedEvent;
import fi.otavanopisto.pyramus.events.StudentGroupCreatedEvent;
import fi.otavanopisto.pyramus.events.StudentGroupUpdatedEvent;
import fi.otavanopisto.pyramus.persistence.search.SearchResult;
import jakarta.ejb.Stateless;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

@Stateless
public class StudentGroupDAO extends PyramusEntityDAO<StudentGroup> {

  @Inject
  private Event<StudentGroupCreatedEvent> studentGroupCreatedEvent;
  
  @Inject
  private Event<StudentGroupUpdatedEvent> studentGroupUpdatedEvent;
  
  @Inject
  private Event<StudentGroupArchivedEvent> studentGroupRemovedEvent;
  
  public StudentGroup create(Organization organization, String name, String description, Date beginDate, User creatingUser, Boolean guidanceGroup) {
    EntityManager entityManager = getEntityManager();

    Date now = new Date(System.currentTimeMillis());
    
    StudentGroup studentGroup = new StudentGroup();
    studentGroup.setOrganization(organization);
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

  public StudentGroup update(StudentGroup studentGroup, Organization organization, String name, String description, Date beginDate, Boolean guidanceGroup, User updatingUser) {
    EntityManager entityManager = getEntityManager();

    studentGroup.setOrganization(organization);
    studentGroup.setName(name);
    studentGroup.setDescription(description);
    studentGroup.setBeginDate(beginDate);
    studentGroup.setGuidanceGroup(guidanceGroup);

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
  
  public List<StudentGroup> listByStudentAndUserAndGuidanceGroupAndArchived(Student student, StaffMember staffMember, Boolean guidanceGroup, Boolean archived) {
    EntityManager entityManager = getEntityManager(); 

    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<StudentGroup> criteria = criteriaBuilder.createQuery(StudentGroup.class);
    Root<StudentGroupStudent> root = criteria.from(StudentGroupStudent.class);
    Join<StudentGroupStudent, StudentGroup> studentGroup = root.join(StudentGroupStudent_.studentGroup);
    
    Subquery<StudentGroup> staffMemberGroupsQuery = criteria.subquery(StudentGroup.class);
    Root<StudentGroupUser> staffMemberRoot = staffMemberGroupsQuery.from(StudentGroupUser.class);
    staffMemberGroupsQuery.select(staffMemberRoot.get(StudentGroupUser_.studentGroup));
    staffMemberGroupsQuery.where(criteriaBuilder.equal(staffMemberRoot.get(StudentGroupUser_.staffMember), staffMember));

    List<Predicate> predicates = new ArrayList<Predicate>();
    predicates.add(criteriaBuilder.equal(root.get(StudentGroupStudent_.student), student));
    predicates.add(criteriaBuilder.equal(studentGroup.get(StudentGroup_.guidanceGroup), guidanceGroup));
    
    if (archived != null)
      predicates.add(criteriaBuilder.equal(studentGroup.get(StudentGroup_.archived), archived));
    
    predicates.add(root.get(StudentGroupStudent_.studentGroup).in(staffMemberGroupsQuery));
    
    criteria.select(root.get(StudentGroupStudent_.studentGroup));
    criteria.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
    
    TypedQuery<StudentGroup> query = entityManager.createQuery(criteria);
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

  public SearchResult<StudentGroup> searchStudentGroups(int resultsPerPage, int page, Organization organization, String name, 
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
    SearchSession session = Search.session(entityManager);

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

      LuceneSearchResult<StudentGroup> fetch = session
          .search(StudentGroup.class)
          .extension(LuceneExtension.get())
          .where(f -> f.bool().with(b -> {
            b.must(f.fromLuceneQuery(luceneQuery));

            if (user != null) {
              b.filter(f.match().field("users.staffMember.id").matching(user.getId()));
            }
            
            if (organization != null) {
              b.filter(f.match().field("organization.id").matching(organization.getId()));
            }
            
            if (filterArchived) {
              b.filter(f.match().field("archived").matching(Boolean.FALSE));
            }
          }))
          .sort(f -> 
              f.score()
              .then().field("name_sort"))
          .fetch(firstResult, resultsPerPage);
      return searchResults(fetch, page, firstResult, resultsPerPage);

    }
    catch (ParseException e) {
      throw new PersistenceException(e);
    }
  }

  public SearchResult<StudentGroup> searchStudentGroupsBasic(int resultsPerPage, int page, Organization organization, String text) {
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
    SearchSession session = Search.session(entityManager);

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

      LuceneSearchResult<StudentGroup> fetch = session
          .search(StudentGroup.class)
          .extension(LuceneExtension.get())
          .where(f -> f.bool().with(b -> {
            b.must(f.fromLuceneQuery(luceneQuery));

            if (organization != null) {
              b.filter(f.match().field("organization.id").matching(organization.getId()));
            }
            
            b.filter(f.match().field("archived").matching(Boolean.FALSE));
          }))
          .sort(f -> 
              f.score()
              .then().field("name_sort"))
          .fetch(firstResult, resultsPerPage);
      return searchResults(fetch, page, firstResult, resultsPerPage);

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

  public List<StudentGroup> listByOrganization(Organization organization, Integer firstResult, Integer maxResults, Boolean archived) {
    EntityManager entityManager = getEntityManager(); 

    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<StudentGroup> criteria = criteriaBuilder.createQuery(StudentGroup.class);
    Root<StudentGroup> root = criteria.from(StudentGroup.class);

    List<Predicate> predicates = new ArrayList<Predicate>();
    predicates.add(criteriaBuilder.equal(root.get(StudentGroup_.organization), organization));
    
    if (archived != null) {
      predicates.add(criteriaBuilder.equal(root.get(StudentGroup_.archived), archived));
    }
    
    criteria.select(root);
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

  @Override
  public void archive(ArchivableEntity entity, User modifier) {
    super.archive(entity, modifier);
    if (entity instanceof StudentGroup) {
      StudentGroup studentGroup = (StudentGroup) entity;
      studentGroupRemovedEvent.fire(new StudentGroupArchivedEvent(studentGroup.getId()));
    }
  }
  
}
