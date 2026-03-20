package fi.otavanopisto.pyramus.dao.courses;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

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
import fi.otavanopisto.pyramus.dao.base.CourseBaseVariableKeyDAO;
import fi.otavanopisto.pyramus.domainmodel.base.CourseBase;
import fi.otavanopisto.pyramus.domainmodel.base.CourseBaseVariable;
import fi.otavanopisto.pyramus.domainmodel.base.CourseBaseVariableKey;
import fi.otavanopisto.pyramus.domainmodel.base.CourseBaseVariable_;
import fi.otavanopisto.pyramus.domainmodel.base.CourseModule;
import fi.otavanopisto.pyramus.domainmodel.base.CourseModule_;
import fi.otavanopisto.pyramus.domainmodel.base.Curriculum;
import fi.otavanopisto.pyramus.domainmodel.base.EducationSubtype;
import fi.otavanopisto.pyramus.domainmodel.base.EducationType;
import fi.otavanopisto.pyramus.domainmodel.base.Organization;
import fi.otavanopisto.pyramus.domainmodel.base.Subject;
import fi.otavanopisto.pyramus.domainmodel.base.Tag;
import fi.otavanopisto.pyramus.domainmodel.courses.Course;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseStaffMember;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseStaffMember_;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseState;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseType;
import fi.otavanopisto.pyramus.domainmodel.courses.Course_;
import fi.otavanopisto.pyramus.domainmodel.modules.Module;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.events.CourseArchivedEvent;
import fi.otavanopisto.pyramus.events.CourseCreatedEvent;
import fi.otavanopisto.pyramus.events.CourseUpdatedEvent;
import fi.otavanopisto.pyramus.persistence.search.SearchResult;
import fi.otavanopisto.pyramus.persistence.search.SearchTimeFilterMode;

@Stateless
public class CourseDAO extends PyramusEntityDAO<Course> {
  
  @Inject
  private Event<CourseCreatedEvent> courseCreatedEvent;

  @Inject
  private Event<CourseUpdatedEvent> courseUpdatedEvent;
  
  @Inject
  private Event<CourseArchivedEvent> courseArchivedEvent;
  
  /**
   * Creates a new course into the database.
   * 
   * @param module The module of the course
   * @param name Course name
   * @param subject Course subject
   * @param courseNumber Course number
   * @param beginDate Course begin date
   * @param endDate Course end date
   * @param signupStart Course sign up start date
   * @param signupEnd Course sign up end date 
   * @param courseLength Course length
   * @param description Course description
   * @param creatingUser Course owner
   * 
   * @return The created course
   */
  public Course create(Module module, Organization organization, String name, String nameExtension, CourseState state, CourseType type, 
      Date beginDate, Date endDate, Date signupStart, Date signupEnd,
      Double distanceTeachingDays, Double localTeachingDays, Double teachingHours, Double distanceTeachingHours, Double planningHours, 
      Double assessingHours, String description, Long maxParticipantCount, BigDecimal courseFee, Currency courseFeeCurrency, Date enrolmentTimeEnd, User creatingUser) {
    
    Date now = new Date(System.currentTimeMillis());

    Course course = new Course();
    course.setModule(module);
    course.setOrganization(organization);
    course.setName(name);
    course.setState(state);
    course.setType(type);
    course.setNameExtension(nameExtension);
    course.setDescription(description);
    course.setBeginDate(beginDate);
    course.setEndDate(endDate);
    course.setSignupStart(signupStart);
    course.setSignupEnd(signupEnd);
    course.setLocalTeachingDays(localTeachingDays);
    course.setDistanceTeachingDays(distanceTeachingDays);
    course.setTeachingHours(teachingHours);    
    course.setPlanningHours(planningHours);
    course.setAssessingHours(assessingHours);
    course.setMaxParticipantCount(maxParticipantCount);
    course.setEnrolmentTimeEnd(enrolmentTimeEnd);
    course.setDistanceTeachingHours(distanceTeachingHours);
    course.setCourseFee(courseFee);
    course.setCourseFeeCurrency(courseFeeCurrency);
    
    course.setCreator(creatingUser);
    course.setCreated(now);
    course.setLastModifier(creatingUser);
    course.setLastModified(now);

    persist(course);
    
    courseCreatedEvent.fire(new CourseCreatedEvent(course.getId()));

    return course;
  }

  /**
   * Updates a course to the database.
   * 
   * @param course The course to be updated
   * @param organization Course organization
   * @param name Course name
   * @param subject Course subject
   * @param courseNumber Course number
   * @param beginDate Course begin date
   * @param endDate Course end date
   * @param courseLength Course length 
   * @param courseLengthTimeUnit Course length unit
   * @param description Course description
   * @param user The user making the update, stored as the last modifier of the course
   */
  public void update(Course course, Organization organization, String name, String nameExtension, CourseState courseState, CourseType type,
      Date beginDate, Date endDate, Date signupStart, Date signupEnd,
      Double distanceTeachingDays, Double localTeachingDays, Double teachingHours, 
      Double distanceTeachingHours, Double planningHours, Double assessingHours, String description, Long maxParticipantCount, 
      Date enrolmentTimeEnd, User user) {
    EntityManager entityManager = getEntityManager();

    Date now = new Date(System.currentTimeMillis());
    
    course.setOrganization(organization);
    course.setName(name);
    course.setNameExtension(nameExtension);
    course.setState(courseState);
    course.setType(type);
    course.setDescription(description);
    course.setBeginDate(beginDate);
    course.setEndDate(endDate);
    course.setSignupStart(signupStart);
    course.setSignupEnd(signupEnd);
    course.setDistanceTeachingDays(distanceTeachingDays);
    course.setLocalTeachingDays(localTeachingDays);
    course.setTeachingHours(teachingHours);
    course.setPlanningHours(planningHours);
    course.setAssessingHours(assessingHours);
    course.setMaxParticipantCount(maxParticipantCount);
    course.setEnrolmentTimeEnd(enrolmentTimeEnd);
    course.setLastModifier(user);
    course.setLastModified(now);
    course.setDistanceTeachingHours(distanceTeachingHours);

    entityManager.persist(course);
    
    courseUpdatedEvent.fire(new CourseUpdatedEvent(course.getId()));
  }
  
  public Course updateCourseFee(Course course, BigDecimal courseFee, Currency courseFeeCurrency, User user) {
    course.setCourseFee(courseFee);
    course.setCourseFeeCurrency(courseFeeCurrency);

    persist(course);
    
    courseUpdatedEvent.fire(new CourseUpdatedEvent(course.getId()));
    
    return course;
  }

  public Course updateModule(Course course, Module module) {
    course.setModule(module);

    persist(course);
    
    courseUpdatedEvent.fire(new CourseUpdatedEvent(course.getId()));
    
    return course;
  }

  public Course updateCourseTemplate(Course course, boolean isCourseTemplate) {
    course.setCourseTemplate(isCourseTemplate);

    persist(course);
    
    courseUpdatedEvent.fire(new CourseUpdatedEvent(course.getId()));
    
    return course;
  }

  public Course updateCurriculums(Course course, Set<Curriculum> curriculums) {
    EntityManager entityManager = getEntityManager();

    course.setCurriculums(curriculums);

    entityManager.persist(course);

    courseUpdatedEvent.fire(new CourseUpdatedEvent(course.getId()));

    return course;
  }
  
  public Course setCourseTags(Course course, Set<Tag> tags) {
    EntityManager entityManager = getEntityManager();
    
    course.setTags(tags);
    
    entityManager.persist(course);
    
    courseUpdatedEvent.fire(new CourseUpdatedEvent(course.getId()));
    
    return course;
  }

  public List<Course> listByCourseVariable(String key, String value) {
    CourseBaseVariableKeyDAO variableKeyDAO = DAOFactory.getInstance().getCourseBaseVariableKeyDAO();

    CourseBaseVariableKey courseBaseVariableKey = variableKeyDAO.findByVariableKey(key);
    
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Course> criteria = criteriaBuilder.createQuery(Course.class);
    Root<CourseBaseVariable> variable = criteria.from(CourseBaseVariable.class);
    Root<Course> course = criteria.from(Course.class);

    criteria.select(course);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(course, variable.get(CourseBaseVariable_.courseBase)),
            criteriaBuilder.equal(course.get(Course_.archived), Boolean.FALSE),
            criteriaBuilder.equal(variable.get(CourseBaseVariable_.key), courseBaseVariableKey),
            criteriaBuilder.equal(variable.get(CourseBaseVariable_.value), value)
        ));
    
    return entityManager.createQuery(criteria).getResultList();
  }
  
  public List<Course> listByStaffMember(StaffMember staffMember) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Course> criteria = criteriaBuilder.createQuery(Course.class);
    Root<CourseStaffMember> staffRoot = criteria.from(CourseStaffMember.class);
    Join<CourseStaffMember, Course> courseJoin = staffRoot.join(CourseStaffMember_.course);
    
    criteria.select(staffRoot.get(CourseStaffMember_.course));
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(courseJoin.get(Course_.archived), Boolean.FALSE),
            criteriaBuilder.equal(staffRoot.get(CourseStaffMember_.staffMember), staffMember)
        ));
    
    return entityManager.createQuery(criteria).getResultList();
  }

  public List<Course> listByModule(Module module) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Course> criteria = criteriaBuilder.createQuery(Course.class);
    Root<Course> root = criteria.from(Course.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(Course_.archived), Boolean.FALSE),
            criteriaBuilder.equal(root.get(Course_.module), module)
        ));
    
    return entityManager.createQuery(criteria).getResultList();
  }

  public List<Course> listBySubject(Subject subject) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Course> criteria = criteriaBuilder.createQuery(Course.class);
    Root<Course> courseRoot = criteria.from(Course.class);
    
    Subquery<CourseBase> courseModuleSubquery = criteria.subquery(CourseBase.class);
    Root<CourseModule> courseModuleRoot = courseModuleSubquery.from(CourseModule.class);
    courseModuleSubquery.select(courseModuleRoot.get(CourseModule_.course));
    courseModuleSubquery.where(criteriaBuilder.equal(courseModuleRoot.get(CourseModule_.subject), subject));
    
    criteria.select(courseRoot);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(courseRoot.get(Course_.archived), Boolean.FALSE),
            courseRoot.in(courseModuleSubquery)
        ));
    
    return entityManager.createQuery(criteria).getResultList();
  }

  @SuppressWarnings("unchecked")
  public SearchResult<Course> searchCoursesBasic(int resultsPerPage, int page, String text, boolean filterArchived) {
    int firstResult = page * resultsPerPage;

    StringBuilder queryBuilder = new StringBuilder();

    if (!StringUtils.isBlank(text)) {
      queryBuilder.append("+(");
      addTokenizedSearchCriteria(queryBuilder, "name", text, false);
      addTokenizedSearchCriteria(queryBuilder, "description", text, false);
      addTokenizedSearchCriteria(queryBuilder, "nameExtension", text, false);
      addTokenizedSearchCriteria(queryBuilder, "courseComponents.name", text, false);
      addTokenizedSearchCriteria(queryBuilder, "courseComponents.description", text, false);
      addTokenizedSearchCriteria(queryBuilder, "tags.text", text, false);
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
      
      FullTextQuery query = (FullTextQuery) fullTextEntityManager.createFullTextQuery(luceneQuery, Course.class)
          .setSort(new Sort(new SortField[]{SortField.FIELD_SCORE, new SortField("nameSortable", SortField.Type.STRING)}))
          .setFirstResult(firstResult)
          .setMaxResults(resultsPerPage);

      if (filterArchived) {
        query.enableFullTextFilter("ArchivedCourse").setParameter("archived", Boolean.FALSE);
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

  public SearchResult<Course> searchCourses(int resultsPerPage, int page, String name, String tags, String nameExtension,
      String description, CourseState courseState, Subject subject, SearchTimeFilterMode timeFilterMode,
      Date timeframeStart, Date timeframeEnd, boolean filterArchived, CourseTemplateFilter courseTemplateFilter) {
    return searchCourses(resultsPerPage, page, name, tags, nameExtension, description, courseState, subject, timeFilterMode,
        timeframeStart, timeframeEnd, null, null, filterArchived, courseTemplateFilter);
  }
  
  @SuppressWarnings("unchecked")
  public SearchResult<Course> searchCourses(int resultsPerPage, int page, String name, String tags, String nameExtension,
      String description, CourseState courseState, Subject subject, SearchTimeFilterMode timeFilterMode,
      Date timeframeStart, Date timeframeEnd, EducationType educationType, EducationSubtype educationSubtype, boolean filterArchived,
      CourseTemplateFilter courseTemplateFilter) {
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

    if (!StringUtils.isBlank(nameExtension)) {
      addTokenizedSearchCriteria(queryBuilder, "nameExtension", nameExtension, true);
    }

    if (!StringUtils.isBlank(description)) {
      addTokenizedSearchCriteria(queryBuilder, "description", description, true);
    }
    
    if (courseState != null) {
      addTokenizedSearchCriteria(queryBuilder, "state.id", courseState.getId().toString(), true);
    }

    if (subject != null) {
      addTokenizedSearchCriteria(queryBuilder, "courseModules.subject.id", subject.getId().toString(), true);
    }

    if (educationType != null)
      addTokenizedSearchCriteria(queryBuilder, "courseEducationTypes.educationType.id", educationType.getId().toString(), true);

    if (educationSubtype != null)
      addTokenizedSearchCriteria(queryBuilder, "courseEducationTypes.courseEducationSubtypes.educationSubtype.id", educationSubtype.getId().toString(), true);
    
    if (timeframeS != null && timeframeE != null) {
      switch (timeFilterMode) {
      case EXCLUSIVE:
        /** beginDate > timeframeStart and endDate < timeframeEnd **/
        queryBuilder.append(" +(").append("+beginDate:[").append(timeframeS).append(" TO ").append(
            getSearchDateInfinityHigh()).append("]").append("+endDate:[").append(getSearchDateInfinityLow()).append(" TO ")
            .append(timeframeE).append("]").append(")");
        break;
      case INCLUSIVE:
        /**
         * (beginDate between timeframeStart - timeframeEnd or endDate between timeframeStart -
         * timeframeEnd) or (startDate less than timeframeStart and endDate more than
         * timeframeEnd)
         **/
        queryBuilder.append(" +(").append("(").append("beginDate:[").append(timeframeS).append(" TO ").append(
            timeframeE).append("] ").append("endDate:[").append(timeframeS).append(" TO ").append(timeframeE).append(
            "]").append(") OR (").append("beginDate:[").append(getSearchDateInfinityLow()).append(" TO ").append(
            timeframeS).append("] AND ").append("endDate:[").append(timeframeE).append(" TO ").append(
                getSearchDateInfinityHigh()).append("]").append(")").append(")");
        break;
      default:
        break;
      }
    }
    else if (timeframeS != null) {
      switch (timeFilterMode) {
      case EXCLUSIVE:
        /** beginDate > timeframeStart **/
        queryBuilder.append(" +(").append("+beginDate:[").append(timeframeS).append(" TO ").append(
            getSearchDateInfinityHigh()).append("]").append(")");
        break;
      case INCLUSIVE:
        /** beginDate > timeframeStart or endDate > timeframeStart **/
        queryBuilder.append(" +(").append("beginDate:[").append(timeframeS).append(" TO ").append(
            getSearchDateInfinityHigh()).append("]").append("endDate:[").append(timeframeS).append(" TO ").append(
                getSearchDateInfinityHigh()).append("]").append(")");
        break;
      default:
        break;
      }
    }
    else if (timeframeE != null) {
      switch (timeFilterMode) {
      case EXCLUSIVE:
        /** endDate < timeframeEnd **/
        queryBuilder.append(" +(").append("+endDate:[").append(getSearchDateInfinityLow()).append(" TO ").append(
            timeframeE).append("]").append(")");
        break;
      case INCLUSIVE:
        /** beginDate < timeframeEnd or endDate < timeframeEnd **/
        queryBuilder.append(" +(").append("beginDate:[").append(getSearchDateInfinityLow()).append(" TO ").append(
            timeframeE).append("] ").append("endDate:[").append(getSearchDateInfinityLow()).append(" TO ").append(
            timeframeE).append("]").append(")");
        break;
      default:
        break;
      }
    }

    switch (courseTemplateFilter) {
      case LIST_COURSES:
        addTokenizedSearchCriteria(queryBuilder, "courseTemplate", Boolean.FALSE.toString(), true);
      break;
      case LIST_TEMPLATES:
        addTokenizedSearchCriteria(queryBuilder, "courseTemplate", Boolean.TRUE.toString(), true);
      break;
      case LIST_ALL:
        // No restrictions
      break;
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

      FullTextQuery query = (FullTextQuery) fullTextEntityManager.createFullTextQuery(luceneQuery, Course.class)
          .setSort(new Sort(new SortField[]{SortField.FIELD_SCORE, new SortField("nameSortable", SortField.Type.STRING)}))
          .setFirstResult(firstResult)
          .setMaxResults(resultsPerPage);

      if (filterArchived)
        query.enableFullTextFilter("ArchivedCourse").setParameter("archived", Boolean.FALSE);

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
  
  public void archive(Course course) {
    super.archive(course);
    courseArchivedEvent.fire(new CourseArchivedEvent(course.getId()));
  }
  
  public void archive(Course course, User user) {
    super.archive(course, user);
    courseArchivedEvent.fire(new CourseArchivedEvent(course.getId()));
  }

  public enum CourseTemplateFilter {
    LIST_ALL,
    LIST_COURSES,
    LIST_TEMPLATES
  }
}
