package fi.otavanopisto.pyramus.dao.matriculation;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamEnrollment;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamEnrollmentState;
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
    MatriculationExamEnrollmentState state
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
    Long nationalStudentNumber,
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
    enrollment.setNationalStudentNumber(nationalStudentNumber);
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
}
