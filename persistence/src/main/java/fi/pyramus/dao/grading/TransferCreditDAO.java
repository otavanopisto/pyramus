package fi.pyramus.dao.grading;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.pyramus.dao.PyramusEntityDAO;
import fi.pyramus.domainmodel.base.CourseOptionality;
import fi.pyramus.domainmodel.base.EducationalLength;
import fi.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.pyramus.domainmodel.base.School;
import fi.pyramus.domainmodel.base.Subject;
import fi.pyramus.domainmodel.grading.Grade;
import fi.pyramus.domainmodel.grading.TransferCredit;
import fi.pyramus.domainmodel.grading.TransferCredit_;
import fi.pyramus.domainmodel.students.Student;
import fi.pyramus.domainmodel.users.User;

@Stateless
public class TransferCreditDAO extends PyramusEntityDAO<TransferCredit> {

  public TransferCredit create(String courseName, Integer courseNumber, Double courseLength, EducationalTimeUnit courseLengthUnit, School school, Subject subject, CourseOptionality optionality, Student student, User assessingUser, Grade grade, Date date, String verbalAssessment) {
    TransferCredit transferCredit = new TransferCredit();
    
    EducationalLength length = new EducationalLength();
    length.setUnits(courseLength);
    length.setUnit(courseLengthUnit);
    
    transferCredit.setAssessingUser(assessingUser);
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
    
    EntityManager entityManager = getEntityManager();
    entityManager.persist(length);
    entityManager.persist(transferCredit);
    
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
  
  public TransferCredit update(TransferCredit transferCredit, String courseName, Integer courseNumber, Double courseLength, EducationalTimeUnit courseLengthUnit, School school, Subject subject, CourseOptionality optionality, Student student, User assessingUser, Grade grade, Date date, String verbalAssessment) {
    EntityManager entityManager = getEntityManager();
    
    EducationalLength courseEducationalLength = transferCredit.getCourseLength();
    courseEducationalLength.setUnits(courseLength);
    courseEducationalLength.setUnit(courseLengthUnit);
    entityManager.persist(courseEducationalLength);
    
    transferCredit.setAssessingUser(assessingUser);
    transferCredit.setCourseName(courseName);
    transferCredit.setCourseNumber(courseNumber);
    transferCredit.setDate(date);
    transferCredit.setGrade(grade);
    transferCredit.setOptionality(optionality);
    transferCredit.setSchool(school);
    transferCredit.setStudent(student);
    transferCredit.setSubject(subject);
    transferCredit.setVerbalAssessment(verbalAssessment);
    entityManager.persist(transferCredit);
    
    return transferCredit;
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
  
}
