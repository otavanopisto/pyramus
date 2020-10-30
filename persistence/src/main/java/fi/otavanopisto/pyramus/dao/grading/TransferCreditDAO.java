package fi.otavanopisto.pyramus.dao.grading;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.base.CourseOptionality;
import fi.otavanopisto.pyramus.domainmodel.base.Curriculum;
import fi.otavanopisto.pyramus.domainmodel.base.EducationalLength;
import fi.otavanopisto.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.otavanopisto.pyramus.domainmodel.base.School;
import fi.otavanopisto.pyramus.domainmodel.base.Subject;
import fi.otavanopisto.pyramus.domainmodel.grading.Grade;
import fi.otavanopisto.pyramus.domainmodel.grading.TransferCredit;
import fi.otavanopisto.pyramus.domainmodel.grading.TransferCreditFunding;
import fi.otavanopisto.pyramus.domainmodel.grading.TransferCredit_;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.events.TransferCreditEvent;
import fi.otavanopisto.pyramus.events.types.Created;
import fi.otavanopisto.pyramus.events.types.Removed;
import fi.otavanopisto.pyramus.events.types.Updated;

@Stateless
public class TransferCreditDAO extends PyramusEntityDAO<TransferCredit> {

  @Inject
  @Created
  private Event<TransferCreditEvent> transferCreditCreatedEvent;

  @Inject
  @Updated
  private Event<TransferCreditEvent> transferCreditUpdatedEvent;
  
  @Inject
  @Removed
  private Event<TransferCreditEvent> transferCreditRemovedEvent;
  
  public TransferCredit create(String courseName, Integer courseNumber, Double courseLength, EducationalTimeUnit courseLengthUnit, 
      School school, Subject subject, CourseOptionality optionality, Student student, StaffMember assessingUser, Grade grade, 
      Date date, String verbalAssessment, Curriculum curriculum, boolean offCurriculum, TransferCreditFunding funding) {
    TransferCredit transferCredit = new TransferCredit();
    
    EducationalLength length = new EducationalLength();
    length.setUnits(courseLength);
    length.setUnit(courseLengthUnit);
    
    transferCredit.setAssessor(assessingUser);
    transferCredit.setDate(date);
    transferCredit.setGrade(grade);
    transferCredit.setCourseLength(length);
    transferCredit.setCourseName(courseName);
    transferCredit.setCourseNumber(courseNumber);
    transferCredit.setSchool(school);
    transferCredit.setStudent(student);
    transferCredit.setSubject(subject);
    transferCredit.setOptionality(optionality);
    transferCredit.setVerbalAssessment(verbalAssessment);
    transferCredit.setCurriculum(curriculum);
    transferCredit.setOffCurriculum(offCurriculum);
    transferCredit.setFunding(funding);
    transferCredit.setArchived(Boolean.FALSE);
    
    EntityManager entityManager = getEntityManager();
    entityManager.persist(length);
    entityManager.persist(transferCredit);
    
    if (student != null)
      transferCreditCreatedEvent.fire(new TransferCreditEvent(student.getId(), transferCredit.getId()));
    
    return transferCredit;
  }
  
  /**
   * Lists all student's transfer credits excluding archived ones
   * 
   * @return list of all students transfer credits
   */
  public List<TransferCredit> listByStudent(Student student) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<TransferCredit> criteria = criteriaBuilder.createQuery(TransferCredit.class);
    Root<TransferCredit> root = criteria.from(TransferCredit.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(TransferCredit_.archived), Boolean.FALSE),
            criteriaBuilder.equal(root.get(TransferCredit_.student), student)
        ));
    
    return entityManager.createQuery(criteria).getResultList();
  }
  
  /**
   * Lists all student's transfer credits excluding archived ones
   * 
   * @return list of all students transfer credits
   */
  public List<TransferCredit> listByStudentAndSubject(Student student, Subject subject) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<TransferCredit> criteria = criteriaBuilder.createQuery(TransferCredit.class);
    Root<TransferCredit> root = criteria.from(TransferCredit.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(TransferCredit_.archived), Boolean.FALSE),
            criteriaBuilder.equal(root.get(TransferCredit_.student), student),
            criteriaBuilder.equal(root.get(TransferCredit_.subject), subject)
        ));
    
    return entityManager.createQuery(criteria).getResultList();
  }
  
  /**
   * Lists student's transfer credits by student, subject, curriculum and optionality.
   * 
   * Method exludes archived transfer credits
   * 
   * @param student student
   * @param subject subject
   * @param curriculum curriculum if null, curriculum is ignored
   * @param optionality optionality if null, optionality is ignored
   * @return list of student's transfer credits
   */
  public List<TransferCredit> listByStudentAndSubjectAndCurriculumAndOptionality(Student student, Subject subject, Curriculum curriculum, CourseOptionality optionality) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<TransferCredit> criteria = criteriaBuilder.createQuery(TransferCredit.class);
    Root<TransferCredit> root = criteria.from(TransferCredit.class);
    criteria.select(root);
    
    List<Predicate> predicates = new ArrayList<>();
    
    if (optionality != null) {
      predicates.add(criteriaBuilder.equal(root.get(TransferCredit_.optionality), optionality));
    }
    
    if (curriculum != null) {
      predicates.add(criteriaBuilder.equal(root.get(TransferCredit_.curriculum), curriculum));
    }

    predicates.add(criteriaBuilder.equal(root.get(TransferCredit_.archived), Boolean.FALSE));
    predicates.add(criteriaBuilder.equal(root.get(TransferCredit_.student), student));
    predicates.add(criteriaBuilder.equal(root.get(TransferCredit_.subject), subject));

    criteria.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
    
    return entityManager.createQuery(criteria).getResultList();
  }
  
  public TransferCredit update(TransferCredit transferCredit, String courseName, Integer courseNumber, Double courseLength, 
      EducationalTimeUnit courseLengthUnit, School school, Subject subject, CourseOptionality optionality, Student student, 
      StaffMember assessingUser, Grade grade, Date date, String verbalAssessment, Curriculum curriculum, boolean offCurriculum, 
      TransferCreditFunding funding) {
    EntityManager entityManager = getEntityManager();
    
    EducationalLength courseEducationalLength = transferCredit.getCourseLength();
    courseEducationalLength.setUnits(courseLength);
    courseEducationalLength.setUnit(courseLengthUnit);
    entityManager.persist(courseEducationalLength);
    
    transferCredit.setAssessor(assessingUser);
    transferCredit.setCourseName(courseName);
    transferCredit.setCourseNumber(courseNumber);
    transferCredit.setDate(date);
    transferCredit.setGrade(grade);
    transferCredit.setOptionality(optionality);
    transferCredit.setSchool(school);
    transferCredit.setStudent(student);
    transferCredit.setSubject(subject);
    transferCredit.setVerbalAssessment(verbalAssessment);
    transferCredit.setCurriculum(curriculum);
    transferCredit.setOffCurriculum(offCurriculum);
    transferCredit.setFunding(funding);
    entityManager.persist(transferCredit);
    
    if (student != null)
      transferCreditUpdatedEvent.fire(new TransferCreditEvent(student.getId(), transferCredit.getId()));
    
    return transferCredit;
  }

  public void archive(TransferCredit transferCredit) {
    super.archive(transferCredit);
    
    if (transferCredit.getStudent() != null)
      transferCreditRemovedEvent.fire(new TransferCreditEvent(transferCredit.getStudent().getId(), transferCredit.getId()));
  }
  
  public void unarchive(TransferCredit transferCredit) {
    super.unarchive(transferCredit);

    if (transferCredit.getStudent() != null)
      transferCreditUpdatedEvent.fire(new TransferCreditEvent(transferCredit.getStudent().getId(), transferCredit.getId()));
  }
  
  public Long countByStudent(Student student) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Long> criteria = criteriaBuilder.createQuery(Long.class);
    Root<TransferCredit> root = criteria.from(TransferCredit.class);
    
    criteria.select(criteriaBuilder.count(root));
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(TransferCredit_.student), student),
            criteriaBuilder.equal(root.get(TransferCredit_.archived), Boolean.FALSE)
        ));
    
    return entityManager.createQuery(criteria).getSingleResult();
  }

  public List<TransferCredit> listByStudentAndCurriculumAndOptionality(
      Student student,
      Curriculum curriculum,
      CourseOptionality optionality) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<TransferCredit> criteria = criteriaBuilder.createQuery(TransferCredit.class);
    Root<TransferCredit> root = criteria.from(TransferCredit.class);
    criteria.select(root);
    
    List<Predicate> predicates = new ArrayList<>();
    
    if (optionality != null) {
      predicates.add(criteriaBuilder.equal(root.get(TransferCredit_.optionality), optionality));
    }
    
    if (curriculum != null) {
      predicates.add(criteriaBuilder.equal(root.get(TransferCredit_.curriculum), curriculum));
    }

    predicates.add(criteriaBuilder.equal(root.get(TransferCredit_.archived), Boolean.FALSE));
    predicates.add(criteriaBuilder.equal(root.get(TransferCredit_.student), student));

    criteria.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
    
    return entityManager.createQuery(criteria).getResultList();
  }

}
