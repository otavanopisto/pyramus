package fi.otavanopisto.pyramus.dao.courses;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Currency;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.accommodation.Room;
import fi.otavanopisto.pyramus.domainmodel.base.BillingDetails;
import fi.otavanopisto.pyramus.domainmodel.base.CourseOptionality;
import fi.otavanopisto.pyramus.domainmodel.courses.Course;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseEnrolmentType;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseParticipationType;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseStudent;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseStudent_;
import fi.otavanopisto.pyramus.domainmodel.courses.Course_;
import fi.otavanopisto.pyramus.domainmodel.modules.Module;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.students.Student_;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.events.CourseStudentArchivedEvent;
import fi.otavanopisto.pyramus.events.CourseStudentCreatedEvent;
import fi.otavanopisto.pyramus.events.CourseStudentUpdatedEvent;
import fi.otavanopisto.pyramus.exception.DuplicateCourseStudentException;

@Stateless
public class CourseStudentDAO extends PyramusEntityDAO<CourseStudent> {

  @Inject
  private Event<CourseStudentCreatedEvent> courseStudentCreatedEvent;

  @Inject
  private Event<CourseStudentUpdatedEvent> courseStudentUpdatedEvent;
  
  @Inject
  private Event<CourseStudentArchivedEvent> courseStudentArchivedEvent;

  /**
   * Adds a course student to the database.
   * 
   * @param course The course
   * @param student The student
   * @param courseEnrolmentType Student enrolment type
   * @param participationType Student participation type
   * @param enrolmentDate The enrolment date
   * @param optionality 
   * @param billingDetails 
   * @param archived 
   * @param room 
   * 
   * @return The created course student
   * @throws DuplicateCourseStudentException 
   */
  public CourseStudent create(Course course, Student student, CourseEnrolmentType courseEnrolmentType,
      CourseParticipationType participationType, Date enrolmentDate, Boolean lodging, CourseOptionality optionality, 
      BillingDetails billingDetails, String organization, String additionalInfo, Room room, BigDecimal lodgingFee, 
      Currency lodgingFeeCurrency, BigDecimal reservationFee, Currency reservationFeeCurrency, Boolean archived) throws DuplicateCourseStudentException {
    
    List<CourseStudent> courseStudents = listByCourseAndStudent(course, student);
    if (!courseStudents.isEmpty())
      throw new DuplicateCourseStudentException();
    
    CourseStudent courseStudent = new CourseStudent();
    
    courseStudent.setCourse(course);
    courseStudent.setArchived(archived);
    courseStudent.setBillingDetails(billingDetails);
    courseStudent.setCourseEnrolmentType(courseEnrolmentType);
    courseStudent.setEnrolmentTime(enrolmentDate);
    courseStudent.setParticipationType(participationType);
    courseStudent.setLodging(lodging);
    courseStudent.setOptionality(optionality);
    courseStudent.setStudent(student);
    courseStudent.setRoom(room);
    courseStudent.setLodgingFee(lodgingFee);
    courseStudent.setLodgingFeeCurrency(lodgingFeeCurrency);
    courseStudent.setReservationFee(reservationFee);
    courseStudent.setReservationFeeCurrency(reservationFeeCurrency);
    courseStudent.setOrganization(organization);
    courseStudent.setAdditionalInfo(additionalInfo);
    
    persist(courseStudent);
    
    courseStudentCreatedEvent.fire(new CourseStudentCreatedEvent(courseStudent.getId(), courseStudent.getCourse().getId(), courseStudent.getStudent().getId()));

    return courseStudent;
  }
  
  private List<CourseStudent> listByCourseAndStudent(Course course, Student student) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<CourseStudent> criteria = criteriaBuilder.createQuery(CourseStudent.class);
    Root<CourseStudent> root = criteria.from(CourseStudent.class);
    
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(CourseStudent_.course), course),
            criteriaBuilder.equal(root.get(CourseStudent_.student), student),
            criteriaBuilder.equal(root.get(CourseStudent_.archived), Boolean.FALSE)
        ));
    
    return entityManager.createQuery(criteria).getResultList();
  }
  
  public CourseStudent findByCourseAndStudent(Course course, Student student) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<CourseStudent> criteria = criteriaBuilder.createQuery(CourseStudent.class);
    Root<CourseStudent> root = criteria.from(CourseStudent.class);
    
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(CourseStudent_.course), course),
            criteriaBuilder.equal(root.get(CourseStudent_.student), student),
            criteriaBuilder.equal(root.get(CourseStudent_.archived), Boolean.FALSE)
        ));
    
    return getSingleResult(entityManager.createQuery(criteria));
  }
  
  /**
   * Updates a course student to the database.
   * 
   * @param courseStudent The course student to be updated
   * @param courseEnrolmentType Student enrolment type
   * @param participationType Student participation type
   * @param enrolmentDate Student enrolment date
   * @param optionality 
   * @throws DuplicateCourseStudentException 
   */
  public CourseStudent update(CourseStudent courseStudent, Student student, 
      CourseEnrolmentType courseEnrolmentType, CourseParticipationType participationType, 
      Date enrolmentDate, Boolean lodging, CourseOptionality optionality) throws DuplicateCourseStudentException {
    EntityManager entityManager = getEntityManager();

    List<CourseStudent> courseStudents = listByCourseAndStudent(courseStudent.getCourse(), student);
    if (!courseStudents.isEmpty()) {
      for (CourseStudent courseStudent2 : courseStudents) {
        if (courseStudent2.getId() != courseStudent.getId())
          throw new DuplicateCourseStudentException();
      }
    }
    
    courseStudent.setStudent(student);
    courseStudent.setCourseEnrolmentType(courseEnrolmentType);
    courseStudent.setEnrolmentTime(enrolmentDate);
    courseStudent.setParticipationType(participationType);
    courseStudent.setLodging(lodging);
    courseStudent.setOptionality(optionality);

    entityManager.persist(courseStudent);
    
    courseStudentUpdatedEvent.fire(new CourseStudentUpdatedEvent(courseStudent.getId(), courseStudent.getCourse().getId(), courseStudent.getStudent().getId()));

    return courseStudent;
  }
  
  public CourseStudent updateLodging(CourseStudent courseStudent, Boolean lodging) {
    courseStudent.setLodging(lodging);
    persist(courseStudent);
    
    courseStudentUpdatedEvent.fire(new CourseStudentUpdatedEvent(courseStudent.getId(), courseStudent.getCourse().getId(), courseStudent.getStudent().getId()));

    return courseStudent;
  }
  
  public CourseStudent updateBillingDetails(CourseStudent courseStudent, BillingDetails billingDetails) {
    courseStudent.setBillingDetails(billingDetails);
    persist(courseStudent);
    
    courseStudentUpdatedEvent.fire(new CourseStudentUpdatedEvent(courseStudent.getId(), courseStudent.getCourse().getId(), courseStudent.getStudent().getId()));

    return courseStudent;
  }
  
  public CourseStudent updateEnrolmentType(CourseStudent courseStudent, CourseEnrolmentType courseEnrolmentType) {
    courseStudent.setCourseEnrolmentType(courseEnrolmentType);
    persist(courseStudent);
    
    courseStudentUpdatedEvent.fire(new CourseStudentUpdatedEvent(courseStudent.getId(), courseStudent.getCourse().getId(), courseStudent.getStudent().getId()));

    return courseStudent;
  }
  
  public CourseStudent updateEnrolmentTime(CourseStudent courseStudent, Date enrolmentTime) {
    courseStudent.setEnrolmentTime(enrolmentTime);
    persist(courseStudent);
    
    courseStudentUpdatedEvent.fire(new CourseStudentUpdatedEvent(courseStudent.getId(), courseStudent.getCourse().getId(), courseStudent.getStudent().getId()));

    return courseStudent;
  }
  
  public CourseStudent updateOptionality(CourseStudent courseStudent, CourseOptionality optionality) {
    courseStudent.setOptionality(optionality);
    persist(courseStudent);
    
    courseStudentUpdatedEvent.fire(new CourseStudentUpdatedEvent(courseStudent.getId(), courseStudent.getCourse().getId(), courseStudent.getStudent().getId()));

    return courseStudent;
  }
  
  public CourseStudent updateParticipationType(CourseStudent courseStudent, CourseParticipationType participationType) {
    courseStudent.setParticipationType(participationType);
    persist(courseStudent);
    
    courseStudentUpdatedEvent.fire(new CourseStudentUpdatedEvent(courseStudent.getId(), courseStudent.getCourse().getId(), courseStudent.getStudent().getId()));

    return courseStudent;
  }

  /**
   * Returns a list of the students in the given course.
   * 
   * @param course The course
   * 
   * @return A list of the students in the given course
   */
  public List<CourseStudent> listByCourse(Course course) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<CourseStudent> criteria = criteriaBuilder.createQuery(CourseStudent.class);
    Root<CourseStudent> root = criteria.from(CourseStudent.class);
    Join<CourseStudent, Student> student = root.join(CourseStudent_.student);
    
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(CourseStudent_.course), course),
            criteriaBuilder.equal(root.get(CourseStudent_.archived), Boolean.FALSE),
            criteriaBuilder.equal(student.get(Student_.archived), Boolean.FALSE)
        ));
    
    return entityManager.createQuery(criteria).getResultList();
  }

  public List<CourseStudent> listByCourseAndParticipationTypes(Course course, List<CourseParticipationType> participationTypes) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<CourseStudent> criteria = criteriaBuilder.createQuery(CourseStudent.class);
    Root<CourseStudent> root = criteria.from(CourseStudent.class);
    Join<CourseStudent, Student> student = root.join(CourseStudent_.student);
    
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            root.get(CourseStudent_.participationType).in(participationTypes),
            criteriaBuilder.equal(root.get(CourseStudent_.course), course),
            criteriaBuilder.equal(root.get(CourseStudent_.archived), Boolean.FALSE),
            criteriaBuilder.equal(student.get(Student_.archived), Boolean.FALSE)
        ));
    
    return entityManager.createQuery(criteria).getResultList();
  }
  
  public List<CourseStudent> listByCourseIncludeArchived(Course course) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<CourseStudent> criteria = criteriaBuilder.createQuery(CourseStudent.class);
    Root<CourseStudent> root = criteria.from(CourseStudent.class);
    
    criteria.select(root);
    criteria.where(criteriaBuilder.equal(root.get(CourseStudent_.course), course));
    
    return entityManager.createQuery(criteria).getResultList();
  }

  public List<CourseStudent> listByCourseAndParticipationTypesIncludeArchived(Course course, List<CourseParticipationType> participationTypes) {
    if (participationTypes == null || participationTypes.isEmpty()) {
      return Collections.emptyList();
    }
    
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<CourseStudent> criteria = criteriaBuilder.createQuery(CourseStudent.class);
    Root<CourseStudent> root = criteria.from(CourseStudent.class);
    
    criteria.select(root);
    criteria.where(
      criteriaBuilder.and(
          root.get(CourseStudent_.participationType).in(participationTypes),
          criteriaBuilder.equal(root.get(CourseStudent_.course), course)
      ));
    
    return entityManager.createQuery(criteria).getResultList();
  }

  public Long countByCourse(Course course) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Long> criteria = criteriaBuilder.createQuery(Long.class);
    Root<CourseStudent> root = criteria.from(CourseStudent.class);
    Join<CourseStudent, Student> studentJoin = root.join(CourseStudent_.student);
    
    criteria.select(criteriaBuilder.count(root));
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(CourseStudent_.course), course),
            criteriaBuilder.equal(root.get(CourseStudent_.archived), Boolean.FALSE),
            criteriaBuilder.equal(studentJoin.get(Student_.archived), Boolean.FALSE)
        ));
    
    return entityManager.createQuery(criteria).getSingleResult();
  }

  public List<CourseStudent> listByModuleAndStudent(Module module, Student student) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<CourseStudent> criteria = criteriaBuilder.createQuery(CourseStudent.class);
    Root<CourseStudent> root = criteria.from(CourseStudent.class);
    Join<CourseStudent, Student> studentJoin = root.join(CourseStudent_.student);
    Join<CourseStudent, Course> courseJoin = root.join(CourseStudent_.course);
    
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(courseJoin.get(Course_.module), module),
            criteriaBuilder.equal(root.get(CourseStudent_.student), student),
            criteriaBuilder.equal(root.get(CourseStudent_.archived), Boolean.FALSE),
            criteriaBuilder.equal(courseJoin.get(Course_.archived), Boolean.FALSE),
            criteriaBuilder.equal(studentJoin.get(Student_.archived), Boolean.FALSE)
        ));
    
    return entityManager.createQuery(criteria).getResultList();
  }
  
  public List<CourseStudent> listByModulesAndStudent(List<Module> modules, Student student) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<CourseStudent> criteria = criteriaBuilder.createQuery(CourseStudent.class);
    Root<CourseStudent> root = criteria.from(CourseStudent.class);
    Join<CourseStudent, Student> studentJoin = root.join(CourseStudent_.student);
    Join<CourseStudent, Course> courseJoin = root.join(CourseStudent_.course);
    
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            courseJoin.get(Course_.module).in(modules),
            criteriaBuilder.equal(root.get(CourseStudent_.student), student),
            criteriaBuilder.equal(root.get(CourseStudent_.archived), Boolean.FALSE),
            criteriaBuilder.equal(courseJoin.get(Course_.archived), Boolean.FALSE),
            criteriaBuilder.equal(studentJoin.get(Student_.archived), Boolean.FALSE)
        ));
    
    return entityManager.createQuery(criteria).getResultList();
  }
  
  public List<CourseStudent> listByStudent(Student student) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<CourseStudent> criteria = criteriaBuilder.createQuery(CourseStudent.class);
    Root<CourseStudent> root = criteria.from(CourseStudent.class);
    Join<CourseStudent, Student> studentJoin = root.join(CourseStudent_.student);
    Join<CourseStudent, Course> courseJoin = root.join(CourseStudent_.course);
    
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(CourseStudent_.student), student),
            criteriaBuilder.equal(root.get(CourseStudent_.archived), Boolean.FALSE),
            criteriaBuilder.equal(courseJoin.get(Course_.archived), Boolean.FALSE),
            criteriaBuilder.equal(studentJoin.get(Student_.archived), Boolean.FALSE)
        ));
    
    return entityManager.createQuery(criteria).getResultList();
  }

  public CourseStudent updateRoom(CourseStudent courseStudent, Room room) {
    courseStudent.setRoom(room);
    return persist(courseStudent);
  }

  public CourseStudent updateRoomAdditionalInfo(CourseStudent courseStudent, String roomAdditionalInfo) {
    courseStudent.setRoomAdditionalInfo(roomAdditionalInfo);
    return persist(courseStudent);
  }

  public CourseStudent updateOrganization(CourseStudent courseStudent, String organization) {
    courseStudent.setOrganization(organization);
    return persist(courseStudent);
  }

  public CourseStudent updateAdditionalInfo(CourseStudent courseStudent, String additionalInfo) {
    courseStudent.setAdditionalInfo(additionalInfo);
    return persist(courseStudent);
  }

  public CourseStudent updateLodgingFee(CourseStudent courseStudent, BigDecimal lodgingFee, Currency lodgingFeeCurrency) {
    courseStudent.setLodgingFee(lodgingFee);
    courseStudent.setLodgingFeeCurrency(lodgingFeeCurrency);
    return persist(courseStudent);
  }

  public CourseStudent updateReservationFee(CourseStudent courseStudent, BigDecimal reservationFee, Currency reservationFeeCurrency) {
    courseStudent.setReservationFee(reservationFee);
    courseStudent.setReservationFeeCurrency(reservationFeeCurrency);
    return persist(courseStudent);
  }

  public void archive(CourseStudent courseStudent) {
    super.archive(courseStudent);
    courseStudentArchivedEvent.fire(new CourseStudentArchivedEvent(courseStudent.getId(), courseStudent.getCourse().getId(), courseStudent.getStudent().getId()));
  }
  
  public void archive(CourseStudent courseStudent, User user) {
    super.archive(courseStudent, user);
    courseStudentArchivedEvent.fire(new CourseStudentArchivedEvent(courseStudent.getId(), courseStudent.getCourse().getId(), courseStudent.getStudent().getId()));
  }
  
}
