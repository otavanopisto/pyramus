package fi.otavanopisto.pyramus.dao.grading;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import jakarta.ejb.Stateless;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaBuilder.In;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.SetJoin;
import jakarta.persistence.criteria.Subquery;

import fi.otavanopisto.pyramus.dao.Predicates;
import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.TSB;
import fi.otavanopisto.pyramus.domainmodel.base.CourseBase;
import fi.otavanopisto.pyramus.domainmodel.base.CourseModule;
import fi.otavanopisto.pyramus.domainmodel.base.CourseModule_;
import fi.otavanopisto.pyramus.domainmodel.base.Curriculum;
import fi.otavanopisto.pyramus.domainmodel.base.Person;
import fi.otavanopisto.pyramus.domainmodel.base.Subject;
import fi.otavanopisto.pyramus.domainmodel.courses.Course;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseStudent;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseStudent_;
import fi.otavanopisto.pyramus.domainmodel.courses.Course_;
import fi.otavanopisto.pyramus.domainmodel.grading.CourseAssessment;
import fi.otavanopisto.pyramus.domainmodel.grading.CourseAssessment_;
import fi.otavanopisto.pyramus.domainmodel.grading.Grade;
import fi.otavanopisto.pyramus.domainmodel.grading.Grade_;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.students.Student_;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.events.CourseAssessmentEvent;
import fi.otavanopisto.pyramus.events.types.Created;
import fi.otavanopisto.pyramus.events.types.Removed;
import fi.otavanopisto.pyramus.events.types.Updated;

@Stateless
public class CourseAssessmentDAO extends PyramusEntityDAO<CourseAssessment> {

  @Inject
  @Created
  private Event<CourseAssessmentEvent> courseAssessmentCreatedEvent;

  @Inject
  @Updated
  private Event<CourseAssessmentEvent> courseAssessmentUpdatedEvent;
  
  @Inject
  @Removed
  private Event<CourseAssessmentEvent> courseAssessmentRemovedEvent;
  
  public CourseAssessment create(CourseStudent courseStudent, CourseModule courseModule, StaffMember assessingUser, Grade grade, Date date, String verbalAssessment) {
    CourseAssessment courseAssessment = new CourseAssessment();
    courseAssessment.setAssessor(assessingUser);
    courseAssessment.setCourseStudent(courseStudent);
    courseAssessment.setCourseModule(courseModule);
    courseAssessment.setDate(date);
    courseAssessment.setGrade(grade);
    courseAssessment.setVerbalAssessment(verbalAssessment);
    courseAssessment.setArchived(Boolean.FALSE);
    
    courseAssessment = persist(courseAssessment);
    
    if (courseAssessment.getStudent() != null)
      courseAssessmentCreatedEvent.fire(new CourseAssessmentEvent(courseAssessment.getStudent().getId(), courseAssessment.getId()));
    
    return courseAssessment;
  }
  
  public List<CourseAssessment> listByCourseIncludeArchived(Course course) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<CourseAssessment> criteria = criteriaBuilder.createQuery(CourseAssessment.class);
    Root<CourseAssessment> root = criteria.from(CourseAssessment.class);
    Join<CourseAssessment, CourseStudent> courseStudentJoin = root.join(CourseAssessment_.courseStudent);
    
    criteria.select(root);
    criteria.where(
        criteriaBuilder.equal(courseStudentJoin.get(CourseStudent_.course), course)
    );
    
    return entityManager.createQuery(criteria).getResultList();
  }  
  
  public List<CourseAssessment> listByCourseModule(CourseModule courseModule, TSB archived) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<CourseAssessment> criteria = criteriaBuilder.createQuery(CourseAssessment.class);
    Root<CourseAssessment> root = criteria.from(CourseAssessment.class);
    
    Predicates predicates = Predicates.newInstance()
        .add(criteriaBuilder.equal(root.get(CourseAssessment_.courseModule), courseModule))
        .add(criteriaBuilder.equal(root.get(CourseAssessment_.archived), archived.booleanValue()), archived.isBoolean());
    
    criteria.select(root);
    criteria.where(criteriaBuilder.and(predicates.array()));
    
    return entityManager.createQuery(criteria).getResultList();
  }  
  
  /**
   * Lists all student's course assessments excluding archived ones
   * 
   * @return list of all students course assessments
   */
  public List<CourseAssessment> listByStudent(Student student) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<CourseAssessment> criteria = criteriaBuilder.createQuery(CourseAssessment.class);
    Root<CourseAssessment> root = criteria.from(CourseAssessment.class);
    Join<CourseAssessment, CourseStudent> courseStudentJoin = root.join(CourseAssessment_.courseStudent);
    Join<CourseStudent, Course> courseJoin = courseStudentJoin.join(CourseStudent_.course);
    
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(courseStudentJoin.get(CourseStudent_.student), student),
            criteriaBuilder.equal(root.get(CourseAssessment_.archived), Boolean.FALSE),
            criteriaBuilder.equal(courseStudentJoin.get(CourseStudent_.archived), Boolean.FALSE),
            criteriaBuilder.equal(courseJoin.get(Course_.archived), Boolean.FALSE)
        ));
    
    return entityManager.createQuery(criteria).getResultList();
  }  
  
  /**
   * Lists all student's course assessments excluding archived ones
   * 
   * @return list of all students course assessments
   */
  public List<CourseAssessment> listByStudentAndSubject(Student student, Subject subject) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<CourseAssessment> criteria = criteriaBuilder.createQuery(CourseAssessment.class);
    Root<CourseAssessment> root = criteria.from(CourseAssessment.class);
    Join<CourseAssessment, CourseStudent> courseStudentJoin = root.join(CourseAssessment_.courseStudent);
    Join<CourseStudent, Course> courseJoin = courseStudentJoin.join(CourseStudent_.course);
    
    Subquery<CourseBase> courseModuleSubquery = criteria.subquery(CourseBase.class);
    Root<CourseModule> courseModuleRoot = courseModuleSubquery.from(CourseModule.class);
    courseModuleSubquery.select(courseModuleRoot.get(CourseModule_.course));
    courseModuleSubquery.where(criteriaBuilder.equal(courseModuleRoot.get(CourseModule_.subject), subject));

    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(courseStudentJoin.get(CourseStudent_.student), student),
            criteriaBuilder.equal(root.get(CourseAssessment_.archived), Boolean.FALSE),
            criteriaBuilder.equal(courseStudentJoin.get(CourseStudent_.archived), Boolean.FALSE),
            criteriaBuilder.equal(courseJoin.get(Course_.archived), Boolean.FALSE),
            courseJoin.in(courseModuleSubquery)
        ));
    
    return entityManager.createQuery(criteria).getResultList();
  }
  
  public List<CourseAssessment> listByStudentAndSubjectAndCurriculum(Student student, Subject subject, Curriculum curriculum) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<CourseAssessment> criteria = criteriaBuilder.createQuery(CourseAssessment.class);
    Root<CourseAssessment> root = criteria.from(CourseAssessment.class);
    Join<CourseAssessment, CourseStudent> courseStudentJoin = root.join(CourseAssessment_.courseStudent);
    Join<CourseStudent, Course> courseJoin = courseStudentJoin.join(CourseStudent_.course);

    Predicates predicates = Predicates.newInstance()
        .add(criteriaBuilder.equal(courseStudentJoin.get(CourseStudent_.student), student))
        .add(criteriaBuilder.equal(root.get(CourseAssessment_.archived), Boolean.FALSE))
        .add(criteriaBuilder.equal(courseStudentJoin.get(CourseStudent_.archived), Boolean.FALSE))
        .add(criteriaBuilder.equal(courseJoin.get(Course_.archived), Boolean.FALSE));
    
    if (subject != null) {
      Subquery<CourseBase> courseModuleSubquery = criteria.subquery(CourseBase.class);
      Root<CourseModule> courseModuleRoot = courseModuleSubquery.from(CourseModule.class);
      courseModuleSubquery.select(courseModuleRoot.get(CourseModule_.course));
      courseModuleSubquery.where(criteriaBuilder.equal(courseModuleRoot.get(CourseModule_.subject), subject));
      
      predicates.add(courseJoin.in(courseModuleSubquery));
    }
        
    if (curriculum != null) {
      SetJoin<Course, Curriculum> curriculumJoin = courseJoin.join(Course_.curriculums);
      predicates.add(criteriaBuilder.equal(curriculumJoin, curriculum));
    }
        
    criteria.select(root);
    criteria.where(criteriaBuilder.and(predicates.array()));
    
    return entityManager.createQuery(criteria).getResultList();
  }
  
  public List<CourseAssessment> listByStudentAndCourse(Student student, Course course) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<CourseAssessment> criteria = criteriaBuilder.createQuery(CourseAssessment.class);
    Root<CourseAssessment> root = criteria.from(CourseAssessment.class);
    Join<CourseAssessment, CourseStudent> courseStudentJoin = root.join(CourseAssessment_.courseStudent);
    Join<CourseStudent, Course> courseJoin = courseStudentJoin.join(CourseStudent_.course);
    
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(courseStudentJoin.get(CourseStudent_.student), student),
            criteriaBuilder.equal(courseStudentJoin.get(CourseStudent_.course), course),
            criteriaBuilder.equal(root.get(CourseAssessment_.archived), Boolean.FALSE),
            criteriaBuilder.equal(courseStudentJoin.get(CourseStudent_.archived), Boolean.FALSE),
            criteriaBuilder.equal(courseJoin.get(Course_.archived), Boolean.FALSE)
        ));
    
    return entityManager.createQuery(criteria).getResultList();
  }
  
  public List<CourseAssessment> listByStudentAndCourseModule(Student student, CourseModule courseModule) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<CourseAssessment> criteria = criteriaBuilder.createQuery(CourseAssessment.class);
    Root<CourseAssessment> root = criteria.from(CourseAssessment.class);
    Join<CourseAssessment, CourseStudent> courseStudentJoin = root.join(CourseAssessment_.courseStudent);
    Join<CourseStudent, Course> courseJoin = courseStudentJoin.join(CourseStudent_.course);
    
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(courseStudentJoin.get(CourseStudent_.student), student),
            criteriaBuilder.equal(root.get(CourseAssessment_.courseModule), courseModule),
            criteriaBuilder.equal(root.get(CourseAssessment_.archived), Boolean.FALSE),
            criteriaBuilder.equal(courseStudentJoin.get(CourseStudent_.archived), Boolean.FALSE),
            criteriaBuilder.equal(courseJoin.get(Course_.archived), Boolean.FALSE)
        ));
    
    return entityManager.createQuery(criteria).getResultList();
  }
  
  public List<CourseAssessment> listByUserAndCourseModules(User user, List<CourseModule> courseModules) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<CourseAssessment> criteria = criteriaBuilder.createQuery(CourseAssessment.class);
    Root<CourseAssessment> root = criteria.from(CourseAssessment.class);

    In<CourseModule> inClause = criteriaBuilder.in(root.get(CourseAssessment_.courseModule));
    for (CourseModule courseModule : courseModules) {
      inClause.value(courseModule);
    }
    
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            inClause,
            criteriaBuilder.equal(root.get(CourseAssessment_.assessor), user),
            criteriaBuilder.equal(root.get(CourseAssessment_.archived), Boolean.FALSE)
        ));
    
    return entityManager.createQuery(criteria).getResultList();
  }

  /**
   * This is deprecated because CourseAssessment is tied to both CourseStudent and CourseModule and
   * fetching with only CourseStudent can give misleading results. In some cases though it is enough 
   * information and as such should only be refactored later.
   */
  @Deprecated
  public CourseAssessment findLatestByCourseStudentAndArchived(CourseStudent courseStudent, Boolean archived) {
    List<CourseAssessment> courseAssessments = listByCourseStudentAndArchived(courseStudent, archived);
    courseAssessments.sort(Comparator.comparing(CourseAssessment::getDate).reversed());
    return courseAssessments.isEmpty() ? null : courseAssessments.get(0);
  }
  
  public CourseAssessment findLatestByCourseStudentAndCourseModuleAndArchived(CourseStudent courseStudent, CourseModule courseModule, Boolean archived) {
    List<CourseAssessment> courseAssessments = listByCourseStudentAndCourseModuleAndArchived(courseStudent, courseModule, archived);
    courseAssessments.sort(Comparator.comparing(CourseAssessment::getDate).thenComparing(CourseAssessment::getId).reversed());
    return courseAssessments.isEmpty() ? null : courseAssessments.get(0);
  }
  
  public List<CourseAssessment> listByCourseStudentAndArchived(CourseStudent courseStudent, Boolean archived) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<CourseAssessment> criteria = criteriaBuilder.createQuery(CourseAssessment.class);
    Root<CourseAssessment> root = criteria.from(CourseAssessment.class);
    criteria.select(root);
    criteria.where(
      criteriaBuilder.and(
        criteriaBuilder.equal(root.get(CourseAssessment_.courseStudent), courseStudent),
        criteriaBuilder.equal(root.get(CourseAssessment_.archived), archived)
      )
    );
    
    return entityManager.createQuery(criteria).getResultList();
  }

  public List<CourseAssessment> listByCourseStudentAndCourseModuleAndArchived(CourseStudent courseStudent, CourseModule courseModule, Boolean archived) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<CourseAssessment> criteria = criteriaBuilder.createQuery(CourseAssessment.class);
    Root<CourseAssessment> root = criteria.from(CourseAssessment.class);
    criteria.select(root);
    criteria.where(
      criteriaBuilder.and(
        criteriaBuilder.equal(root.get(CourseAssessment_.courseStudent), courseStudent),
        criteriaBuilder.equal(root.get(CourseAssessment_.courseModule), courseModule),
        criteriaBuilder.equal(root.get(CourseAssessment_.archived), archived)
      )
    );
    
    return entityManager.createQuery(criteria).getResultList();
  }

  public CourseAssessment update(CourseAssessment assessment, StaffMember assessingUser, Grade grade, Date assessmentDate, String verbalAssessment) {
    assessment.setAssessor(assessingUser);
    assessment.setGrade(grade);
    assessment.setDate(assessmentDate);
    assessment.setVerbalAssessment(verbalAssessment);
    
    assessment = persist(assessment);
    
    if (assessment.getStudent() != null)
      courseAssessmentUpdatedEvent.fire(new CourseAssessmentEvent(assessment.getStudent().getId(), assessment.getId()));
    
    return assessment;
  }
  
  public CourseAssessment archive(CourseAssessment courseAssessment) {
    super.archive(courseAssessment);
    if (courseAssessment.getStudent() != null) {
      courseAssessmentRemovedEvent.fire(new CourseAssessmentEvent(courseAssessment.getStudent().getId(), courseAssessment.getId()));
    }
    return courseAssessment;
  }

  public CourseAssessment unarchive(CourseAssessment courseAssessment) {
    super.unarchive(courseAssessment);
    if (courseAssessment.getStudent() != null) {
      // TODO If archive fires removed, should unarchive fire created rather than updated? 
      courseAssessmentUpdatedEvent.fire(new CourseAssessmentEvent(courseAssessment.getStudent().getId(), courseAssessment.getId()));
    }
    return courseAssessment;
  }
  
  public Long countByStudent(Student student) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Long> criteria = criteriaBuilder.createQuery(Long.class);
    Root<CourseAssessment> root = criteria.from(CourseAssessment.class);
    Join<CourseAssessment, CourseStudent> courseStudentJoin = root.join(CourseAssessment_.courseStudent);
    
    // TODO Archived course?
    criteria.select(criteriaBuilder.count(root));
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(courseStudentJoin.get(CourseStudent_.student), student),
            criteriaBuilder.equal(courseStudentJoin.get(CourseStudent_.archived), Boolean.FALSE),
            criteriaBuilder.equal(root.get(CourseAssessment_.archived), Boolean.FALSE)
        ));
    
    return entityManager.createQuery(criteria).getSingleResult();
  }

  public Long countCourseAssessments(Student student, Date timeIntervalStartDate, Date timeIntervalEndDate,
      Boolean passingGrade) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Long> criteria = criteriaBuilder.createQuery(Long.class);
    Root<CourseAssessment> root = criteria.from(CourseAssessment.class);
    Join<CourseAssessment, CourseStudent> courseStudentJoin = root.join(CourseAssessment_.courseStudent);
    Join<CourseStudent, Course> courseJoin = courseStudentJoin.join(CourseStudent_.course);
    
    List<Predicate> predicates = new ArrayList<>();
    predicates.add(criteriaBuilder.equal(courseStudentJoin.get(CourseStudent_.student), student));
    predicates.add(criteriaBuilder.equal(courseStudentJoin.get(CourseStudent_.archived), Boolean.FALSE));
    predicates.add(criteriaBuilder.equal(courseJoin.get(Course_.archived), Boolean.FALSE));
    predicates.add(criteriaBuilder.equal(root.get(CourseAssessment_.archived), Boolean.FALSE));

    if (timeIntervalStartDate != null)
      predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(CourseAssessment_.date), timeIntervalStartDate));
    if (timeIntervalEndDate != null)
      predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(CourseAssessment_.date), timeIntervalEndDate));
    
    if (passingGrade != null) {
      Join<CourseAssessment, Grade> gradeJoin = root.join(CourseAssessment_.grade);
      predicates.add(criteriaBuilder.equal(gradeJoin.get(Grade_.passingGrade), passingGrade));
    }
    
    criteria.select(criteriaBuilder.count(root));
    criteria.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
    
    return entityManager.createQuery(criteria).getSingleResult();
  }
  
  public Long countCourseAssessments(Person person, TSB passingGrade) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Long> criteria = criteriaBuilder.createQuery(Long.class);
    Root<CourseAssessment> root = criteria.from(CourseAssessment.class);
    Join<CourseAssessment, CourseStudent> courseStudentJoin = root.join(CourseAssessment_.courseStudent);
    Join<CourseStudent, Course> courseJoin = courseStudentJoin.join(CourseStudent_.course);
    Join<CourseStudent, Student> studentJoin = courseStudentJoin.join(CourseStudent_.student);
    
    List<Predicate> predicates = new ArrayList<>();
    predicates.add(criteriaBuilder.equal(studentJoin.get(Student_.person), person));
    predicates.add(criteriaBuilder.equal(studentJoin.get(Student_.archived), Boolean.FALSE));
    predicates.add(criteriaBuilder.equal(courseStudentJoin.get(CourseStudent_.archived), Boolean.FALSE));
    predicates.add(criteriaBuilder.equal(courseJoin.get(Course_.archived), Boolean.FALSE));
    predicates.add(criteriaBuilder.equal(root.get(CourseAssessment_.archived), Boolean.FALSE));

    if (passingGrade.isBoolean()) {
      Join<CourseAssessment, Grade> gradeJoin = root.join(CourseAssessment_.grade);
      predicates.add(criteriaBuilder.equal(gradeJoin.get(Grade_.passingGrade), passingGrade.booleanValue()));
    }
    
    criteria.select(criteriaBuilder.count(root));
    criteria.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
    
    return entityManager.createQuery(criteria).getSingleResult();
  }
  
}
