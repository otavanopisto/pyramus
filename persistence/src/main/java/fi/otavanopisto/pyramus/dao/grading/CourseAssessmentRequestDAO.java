package fi.otavanopisto.pyramus.dao.grading;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.courses.Course;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseStudent;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseStudent_;
import fi.otavanopisto.pyramus.domainmodel.grading.CourseAssessmentRequest;
import fi.otavanopisto.pyramus.domainmodel.grading.CourseAssessmentRequest_;
import fi.otavanopisto.pyramus.domainmodel.students.Student;

@Stateless
public class CourseAssessmentRequestDAO extends PyramusEntityDAO<CourseAssessmentRequest> {

  public CourseAssessmentRequest create(CourseStudent courseStudent, Date created, String requestText) {
    EntityManager entityManager = getEntityManager();

    CourseAssessmentRequest courseAssessmentRequest = new CourseAssessmentRequest();

    courseAssessmentRequest.setCourseStudent(courseStudent);
    courseAssessmentRequest.setCreated(created);
    courseAssessmentRequest.setRequestText(requestText);
    courseAssessmentRequest.setHandled(Boolean.FALSE);
    
    entityManager.persist(courseAssessmentRequest);
    
    return courseAssessmentRequest;
  }
  
  public CourseAssessmentRequest findLatestByCourseStudent(CourseStudent courseStudent) {
    List<CourseAssessmentRequest> requests = listByCourseStudent(courseStudent);
    requests.sort(Comparator.comparing(CourseAssessmentRequest::getCreated).reversed());
    return requests.isEmpty() ? null : requests.get(0);
  }
  
  /**
   * Lists all student's course assessments excluding archived ones
   * 
   * @return list of all students course assessments
   */
  public List<CourseAssessmentRequest> listByCourseStudent(CourseStudent courseStudent) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<CourseAssessmentRequest> criteria = criteriaBuilder.createQuery(CourseAssessmentRequest.class);
    Root<CourseAssessmentRequest> root = criteria.from(CourseAssessmentRequest.class);
    
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(CourseAssessmentRequest_.courseStudent), courseStudent),
            criteriaBuilder.equal(root.get(CourseAssessmentRequest_.archived), Boolean.FALSE)
        ));
    
    return entityManager.createQuery(criteria).getResultList();
  }  

  public List<CourseAssessmentRequest> listByCourseStudentAndHandledAndArchivedBefore(CourseStudent courseStudent, Boolean handled, Boolean archived, Date beforeDate) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<CourseAssessmentRequest> criteria = criteriaBuilder.createQuery(CourseAssessmentRequest.class);
    Root<CourseAssessmentRequest> root = criteria.from(CourseAssessmentRequest.class);
    
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(CourseAssessmentRequest_.courseStudent), courseStudent),
            criteriaBuilder.lessThanOrEqualTo(root.get(CourseAssessmentRequest_.created), beforeDate),
            criteriaBuilder.equal(root.get(CourseAssessmentRequest_.handled), handled),
            criteriaBuilder.equal(root.get(CourseAssessmentRequest_.archived), archived)
        ));
    
    return entityManager.createQuery(criteria).getResultList();
  }  
  
  public List<CourseAssessmentRequest> listByCourseStudentAndHandledAndArchived(CourseStudent courseStudent, Boolean handled, Boolean archived) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<CourseAssessmentRequest> criteria = criteriaBuilder.createQuery(CourseAssessmentRequest.class);
    Root<CourseAssessmentRequest> root = criteria.from(CourseAssessmentRequest.class);
    
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(CourseAssessmentRequest_.courseStudent), courseStudent),
            criteriaBuilder.equal(root.get(CourseAssessmentRequest_.handled), handled),
            criteriaBuilder.equal(root.get(CourseAssessmentRequest_.archived), archived)
        ));
    
    return entityManager.createQuery(criteria).getResultList();
  }  
  
  /**
   * Lists all student's course assessments excluding archived ones
   * 
   * @return list of all students course assessments
   */
  public List<CourseAssessmentRequest> listByCourseAndStudent(Course course, Student student) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<CourseAssessmentRequest> criteria = criteriaBuilder.createQuery(CourseAssessmentRequest.class);
    Root<CourseAssessmentRequest> root = criteria.from(CourseAssessmentRequest.class);
    Join<CourseAssessmentRequest, CourseStudent> courseStudentJoin = root.join(CourseAssessmentRequest_.courseStudent);
    
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(courseStudentJoin.get(CourseStudent_.course), course),
            criteriaBuilder.equal(courseStudentJoin.get(CourseStudent_.student), student),
            criteriaBuilder.equal(root.get(CourseAssessmentRequest_.archived), Boolean.FALSE)
        ));
    
    return entityManager.createQuery(criteria).getResultList();
  }  
  
  public List<CourseAssessmentRequest> listByCourseAndStudentIncludingArchived(Course course, Student student) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<CourseAssessmentRequest> criteria = criteriaBuilder.createQuery(CourseAssessmentRequest.class);
    Root<CourseAssessmentRequest> root = criteria.from(CourseAssessmentRequest.class);
    Join<CourseAssessmentRequest, CourseStudent> courseStudentJoin = root.join(CourseAssessmentRequest_.courseStudent);
    
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(courseStudentJoin.get(CourseStudent_.course), course),
            criteriaBuilder.equal(courseStudentJoin.get(CourseStudent_.student), student)
        ));
    
    return entityManager.createQuery(criteria).getResultList();
  }  
  
  public CourseAssessmentRequest update(CourseAssessmentRequest courseAssessmentRequest, Date created, String requestText, Boolean archived, Boolean handled) {
    EntityManager entityManager = getEntityManager();

    courseAssessmentRequest.setCreated(created);
    courseAssessmentRequest.setRequestText(requestText);
    courseAssessmentRequest.setArchived(archived);
    courseAssessmentRequest.setHandled(handled);

    entityManager.persist(courseAssessmentRequest);
    
    return courseAssessmentRequest;
  }

  public CourseAssessmentRequest updateHandled(CourseAssessmentRequest courseAssessmentRequest, Boolean handled) {
    EntityManager entityManager = getEntityManager();

    courseAssessmentRequest.setHandled(handled);
    
    entityManager.persist(courseAssessmentRequest);
    
    return courseAssessmentRequest;
  }

  public List<CourseAssessmentRequest> listByCourse(Course course) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<CourseAssessmentRequest> criteria = criteriaBuilder.createQuery(CourseAssessmentRequest.class);
    Root<CourseAssessmentRequest> root = criteria.from(CourseAssessmentRequest.class);
    Join<CourseAssessmentRequest, CourseStudent> courseStudent = root.join(CourseAssessmentRequest_.courseStudent);
    
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(courseStudent.get(CourseStudent_.course), course),
            criteriaBuilder.equal(courseStudent.get(CourseStudent_.archived), Boolean.FALSE),            
            criteriaBuilder.equal(root.get(CourseAssessmentRequest_.archived), Boolean.FALSE)
        ));
    
    return entityManager.createQuery(criteria).getResultList();
  }

  public List<CourseAssessmentRequest> listByCourseAndHandled(Course course, Boolean handled) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<CourseAssessmentRequest> criteria = criteriaBuilder.createQuery(CourseAssessmentRequest.class);
    Root<CourseAssessmentRequest> root = criteria.from(CourseAssessmentRequest.class);
    Join<CourseAssessmentRequest, CourseStudent> courseStudent = root.join(CourseAssessmentRequest_.courseStudent);
    
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(courseStudent.get(CourseStudent_.course), course),
            criteriaBuilder.equal(courseStudent.get(CourseStudent_.archived), Boolean.FALSE),            
            criteriaBuilder.equal(root.get(CourseAssessmentRequest_.archived), Boolean.FALSE),
            criteriaBuilder.equal(root.get(CourseAssessmentRequest_.handled), handled)
        ));
    
    return entityManager.createQuery(criteria).getResultList();
  }

  public List<CourseAssessmentRequest> listByStudent(Student student) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<CourseAssessmentRequest> criteria = criteriaBuilder.createQuery(CourseAssessmentRequest.class);
    Root<CourseAssessmentRequest> root = criteria.from(CourseAssessmentRequest.class);
    Join<CourseAssessmentRequest, CourseStudent> courseStudent = root.join(CourseAssessmentRequest_.courseStudent);
    
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(courseStudent.get(CourseStudent_.student), student),
            criteriaBuilder.equal(root.get(CourseAssessmentRequest_.archived), Boolean.FALSE)
        ));
    
    return entityManager.createQuery(criteria).getResultList();
  }
  
}
