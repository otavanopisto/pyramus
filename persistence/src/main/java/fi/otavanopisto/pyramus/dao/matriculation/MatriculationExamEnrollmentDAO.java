package fi.otavanopisto.pyramus.dao.matriculation;

import java.sql.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamEnrollment;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamEnrollmentState;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamEnrollment_;
import fi.otavanopisto.pyramus.domainmodel.matriculation.SchoolType;
import fi.otavanopisto.pyramus.domainmodel.students.Student;

@Stateless
public class MatriculationExamEnrollmentDAO extends PyramusEntityDAO<MatriculationExamEnrollment> {

  public MatriculationExamEnrollment create(
    String name,
    String ssn,
    String email,
    String phone,
    String address,
    String postalCode,
    String city,
    Long nationalStudentNumber,
    String guider,
    SchoolType enrollAs,
    int numMandatoryCourses,
    boolean restartExam,
    String location,
    String message,
    boolean canPublishName,
    Student student,
    MatriculationExamEnrollmentState state,
    Date enrollmentDate
  ) {
    EntityManager entityManager = getEntityManager();
    MatriculationExamEnrollment result = new MatriculationExamEnrollment();

    result.setName(name);
    result.setSsn(ssn);
    result.setEmail(email);
    result.setPhone(phone);
    result.setAddress(address);
    result.setPostalCode(postalCode);
    result.setCity(city);
    result.setNationalStudentNumber(nationalStudentNumber);
    result.setGuider(guider);
    result.setEnrollAs(enrollAs);
    result.setNumMandatoryCourses(numMandatoryCourses);
    result.setRestartExam(restartExam);
    result.setLocation(location);
    result.setMessage(message);
    result.setCanPublishName(canPublishName);
    result.setStudent(student);
    result.setState(state);
    result.setEnrollmentDate(enrollmentDate);
    
    entityManager.persist(result);
    return result;
  }

  public MatriculationExamEnrollment update(
    MatriculationExamEnrollment enrollment,
    String name,
    String ssn,
    String email,
    String phone,
    String address,
    String postalCode,
    String city,
    String guider,
    SchoolType enrollAs,
    int numMandatoryCourses,
    boolean restartExam,
    String location,
    String message,
    boolean canPublishName,
    Student student,
    MatriculationExamEnrollmentState state
  ) {
    EntityManager entityManager = getEntityManager();

    enrollment.setName(name);
    enrollment.setSsn(ssn);
    enrollment.setEmail(email);
    enrollment.setPhone(phone);
    enrollment.setAddress(address);
    enrollment.setPostalCode(postalCode);
    enrollment.setCity(city);
    enrollment.setGuider(guider);
    enrollment.setEnrollAs(enrollAs);
    enrollment.setNumMandatoryCourses(numMandatoryCourses);
    enrollment.setRestartExam(restartExam);
    enrollment.setLocation(location);
    enrollment.setMessage(message);
    enrollment.setCanPublishName(canPublishName);
    enrollment.setStudent(student);
    enrollment.setState(state);
    
    entityManager.persist(enrollment);
    return enrollment;
  }
  
  public List<MatriculationExamEnrollment> listByState(
    MatriculationExamEnrollmentState state,
    int firstResult,
    int maxResults
  ) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<MatriculationExamEnrollment> criteria = criteriaBuilder.createQuery(MatriculationExamEnrollment.class);
    Root<MatriculationExamEnrollment> root = criteria.from(MatriculationExamEnrollment.class);
    criteria.select(root);
    criteria.where(
      criteriaBuilder.equal(root.get(MatriculationExamEnrollment_.state), state)
    );
    
    return entityManager
      .createQuery(criteria)
      .setFirstResult(firstResult)
      .setMaxResults(maxResults)
      .getResultList();
  }
  
  public MatriculationExamEnrollment findLatestByStudent(
      Student student
  ) {
    EntityManager entityManager = getEntityManager(); 
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<MatriculationExamEnrollment> criteria = criteriaBuilder.createQuery(MatriculationExamEnrollment.class);
    Root<MatriculationExamEnrollment> root = criteria.from(MatriculationExamEnrollment.class);
    criteria.select(root);
    criteria.where(
      criteriaBuilder.equal(root.get(MatriculationExamEnrollment_.student), student)
    );
    criteria.orderBy(criteriaBuilder.desc(root.get(MatriculationExamEnrollment_.enrollmentDate)));
    List<MatriculationExamEnrollment> resultList = 
        entityManager.createQuery(criteria).getResultList();
    if (resultList.size() == 0) {
      return null;
    } else {
      return resultList.get(0);
    }
  }

}
