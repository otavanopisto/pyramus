package fi.otavanopisto.pyramus.dao.grading;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Subject;
import fi.otavanopisto.pyramus.domainmodel.courses.Course;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseStudent;
import fi.otavanopisto.pyramus.domainmodel.grading.CourseAssessment;
import fi.otavanopisto.pyramus.domainmodel.grading.Grade;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseStudent_;
import fi.otavanopisto.pyramus.domainmodel.courses.Course_;
import fi.otavanopisto.pyramus.domainmodel.grading.CourseAssessment_;

@Stateless
public class CourseAssessmentDAO extends PyramusEntityDAO<CourseAssessment> {

  public CourseAssessment create(CourseStudent courseStudent, StaffMember assessingUser, Grade grade, Date date, String verbalAssessment) {
    CourseAssessment courseAssessment = new CourseAssessment();
    courseAssessment.setAssessor(assessingUser);
    courseAssessment.setCourseStudent(courseStudent);
    courseAssessment.setDate(date);
    courseAssessment.setGrade(grade);
    courseAssessment.setVerbalAssessment(verbalAssessment);
    courseAssessment.setArchived(Boolean.FALSE);
    
    return persist(courseAssessment);
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
    
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(courseStudentJoin.get(CourseStudent_.student), student),
            criteriaBuilder.equal(root.get(CourseAssessment_.archived), Boolean.FALSE),
            criteriaBuilder.equal(courseStudentJoin.get(CourseStudent_.archived), Boolean.FALSE),
            criteriaBuilder.equal(courseJoin.get(Course_.archived), Boolean.FALSE),
            criteriaBuilder.equal(courseJoin.get(Course_.subject), subject)
        ));
    
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
  
  public CourseAssessment findByCourseStudent(CourseStudent courseStudent) {
    EntityManager entityManager = getEntityManager(); 
    
    // TODO Does not use archived
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<CourseAssessment> criteria = criteriaBuilder.createQuery(CourseAssessment.class);
    Root<CourseAssessment> root = criteria.from(CourseAssessment.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.equal(root.get(CourseAssessment_.courseStudent), courseStudent)
    );
    
    return getSingleResult(entityManager.createQuery(criteria));
  }

  public CourseAssessment update(CourseAssessment assessment, StaffMember assessingUser, Grade grade, Date assessmentDate, String verbalAssessment) {
    EntityManager entityManager = getEntityManager();

    assessment.setAssessor(assessingUser);
    assessment.setGrade(grade);
    assessment.setDate(assessmentDate);
    assessment.setVerbalAssessment(verbalAssessment);
    
    entityManager.persist(assessment);
    
    return assessment;
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
  
}
