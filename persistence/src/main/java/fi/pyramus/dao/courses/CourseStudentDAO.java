package fi.pyramus.dao.courses;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

import fi.pyramus.dao.PyramusEntityDAO;
import fi.pyramus.domainmodel.base.CourseOptionality;
import fi.pyramus.domainmodel.courses.Course;
import fi.pyramus.domainmodel.courses.CourseEnrolmentType;
import fi.pyramus.domainmodel.courses.CourseParticipationType;
import fi.pyramus.domainmodel.courses.CourseStudent;
import fi.pyramus.domainmodel.courses.CourseStudent_;
import fi.pyramus.domainmodel.courses.Course_;
import fi.pyramus.domainmodel.modules.Module;
import fi.pyramus.domainmodel.students.Student;
import fi.pyramus.domainmodel.students.Student_;

@Stateless
public class CourseStudentDAO extends PyramusEntityDAO<CourseStudent> {

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
   * Adds a course student to the database.
   * 
   * @param course The course
   * @param student The student
   * @param courseEnrolmentType Student enrolment type
   * @param participationType Student participation type
   * @param enrolmentDate The enrolment date
   * @param optionality 
   * 
   * @return The created course student
   */
  public CourseStudent create(Course course, Student student, CourseEnrolmentType courseEnrolmentType,
      CourseParticipationType participationType, Date enrolmentDate, Boolean lodging, CourseOptionality optionality) {
    EntityManager entityManager = getEntityManager();

    CourseStudent courseStudent = new CourseStudent();
    courseStudent.setCourseEnrolmentType(courseEnrolmentType);
    courseStudent.setEnrolmentTime(enrolmentDate);
    courseStudent.setParticipationType(participationType);
    courseStudent.setLodging(lodging);
    courseStudent.setOptionality(optionality);
    courseStudent.setStudent(student);
    entityManager.persist(courseStudent);
    
    course.addCourseStudent(courseStudent);
    entityManager.persist(course);

    return courseStudent;
  }
  
  /**
   * Updates a course student to the database.
   * 
   * @param courseStudent The course student to be updated
   * @param courseEnrolmentType Student enrolment type
   * @param participationType Student participation type
   * @param enrolmentDate Student enrolment date
   * @param optionality 
   */
  public CourseStudent update(CourseStudent courseStudent, Student student, 
      CourseEnrolmentType courseEnrolmentType, CourseParticipationType participationType, 
      Date enrolmentDate, Boolean lodging, CourseOptionality optionality) {
    EntityManager entityManager = getEntityManager();

    courseStudent.setStudent(student);
    courseStudent.setCourseEnrolmentType(courseEnrolmentType);
    courseStudent.setEnrolmentTime(enrolmentDate);
    courseStudent.setParticipationType(participationType);
    courseStudent.setLodging(lodging);
    courseStudent.setOptionality(optionality);

    entityManager.persist(courseStudent);

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

  /**
   * Deletes the given course student from the database.
   * 
   * @param courseStudent The course student to be deleted
   */
  @Override
  public void delete(CourseStudent courseStudent) {
    EntityManager entityManager = getEntityManager();
    
    Course course = courseStudent.getCourse();
    if (course != null) {
      course.removeCourseStudent(courseStudent);
//      entityManager.persist(course);
    }
    
    entityManager.remove(courseStudent);
  }

}
