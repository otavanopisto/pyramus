package fi.otavanopisto.pyramus.dao.matriculation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.matriculation.DegreeType;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExam;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamAttendance;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamAttendanceStatus;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamAttendance_;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamEnrollment;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamEnrollmentDegreeStructure;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamEnrollmentState;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamEnrollment_;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamTerm;
import fi.otavanopisto.pyramus.domainmodel.matriculation.SchoolType;
import fi.otavanopisto.pyramus.domainmodel.students.Student;

@Stateless
public class MatriculationExamEnrollmentDAO extends PyramusEntityDAO<MatriculationExamEnrollment> {

  public MatriculationExamEnrollment create(
      MatriculationExam exam,
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
      DegreeType degreeType,
      int numMandatoryCourses,
      boolean restartExam,
      String location,
      String message,
      boolean canPublishName,
      Student student,
      MatriculationExamEnrollmentState state,
      MatriculationExamEnrollmentDegreeStructure degreeStructure,
      boolean approvedByGuider,
      Date enrollmentDate
  ) {
    MatriculationExamEnrollment result = new MatriculationExamEnrollment();

    result.setExam(exam);
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
    result.setDegreeType(degreeType);
    result.setNumMandatoryCourses(numMandatoryCourses);
    result.setRestartExam(restartExam);
    result.setLocation(location);
    result.setMessage(message);
    result.setCanPublishName(canPublishName);
    result.setStudent(student);
    result.setState(state);
    result.setDegreeStructure(degreeStructure);
    result.setApprovedByGuider(approvedByGuider);
    result.setEnrollmentDate(enrollmentDate);
    
    return persist(result);
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
    DegreeType degreeType,
    int numMandatoryCourses,
    boolean restartExam,
    String location,
    String message,
    boolean canPublishName,
    Student student,
    MatriculationExamEnrollmentState state,
    MatriculationExamEnrollmentDegreeStructure degreeStructure,
    boolean approvedByGuider
  ) {
    enrollment.setName(name);
    enrollment.setSsn(ssn);
    enrollment.setEmail(email);
    enrollment.setPhone(phone);
    enrollment.setAddress(address);
    enrollment.setPostalCode(postalCode);
    enrollment.setCity(city);
    enrollment.setGuider(guider);
    enrollment.setEnrollAs(enrollAs);
    enrollment.setDegreeType(degreeType);
    enrollment.setNumMandatoryCourses(numMandatoryCourses);
    enrollment.setRestartExam(restartExam);
    enrollment.setLocation(location);
    enrollment.setMessage(message);
    enrollment.setCanPublishName(canPublishName);
    enrollment.setStudent(student);
    enrollment.setState(state);
    enrollment.setDegreeStructure(degreeStructure);
    enrollment.setApprovedByGuider(approvedByGuider);
    
    return persist(enrollment);
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
  
  public List<MatriculationExamEnrollment> listBy(
      MatriculationExam exam, 
      MatriculationExamEnrollmentState state,
      boolean below20courses,
      int firstResult,
      int maxResults) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<MatriculationExamEnrollment> criteria = criteriaBuilder.createQuery(MatriculationExamEnrollment.class);
    Root<MatriculationExamEnrollment> root = criteria.from(MatriculationExamEnrollment.class);
    
    List<Predicate> predicates = new ArrayList<>();
    if (exam != null) {
      predicates.add(criteriaBuilder.equal(root.get(MatriculationExamEnrollment_.exam), exam));
    }
    if (state != null) {
      predicates.add(criteriaBuilder.equal(root.get(MatriculationExamEnrollment_.state), state));
    }
    if (below20courses) {
      predicates.add(criteriaBuilder.lessThan(root.get(MatriculationExamEnrollment_.numMandatoryCourses), 20));
    }
    
    criteria.select(root);
    
    if (!predicates.isEmpty()) {
      criteria.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
    }
    
    return entityManager
      .createQuery(criteria)
      .setFirstResult(firstResult)
      .setMaxResults(maxResults)
      .getResultList();
  }
    
  public List<MatriculationExamEnrollment> listDistinctByAttendanceTerms(MatriculationExam exam,
      MatriculationExamEnrollmentState enrollmentState, Integer year, MatriculationExamTerm term, 
      MatriculationExamAttendanceStatus attendanceStatus) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<MatriculationExamEnrollment> criteria = criteriaBuilder.createQuery(MatriculationExamEnrollment.class);
    Root<MatriculationExamAttendance> root = criteria.from(MatriculationExamAttendance.class);
    Join<MatriculationExamAttendance, MatriculationExamEnrollment> enrollment = root.join(MatriculationExamAttendance_.enrollment);
    
    criteria.select(root.get(MatriculationExamAttendance_.enrollment)).distinct(true);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(enrollment.get(MatriculationExamEnrollment_.exam), exam),
            criteriaBuilder.equal(enrollment.get(MatriculationExamEnrollment_.state), enrollmentState),
            criteriaBuilder.equal(root.get(MatriculationExamAttendance_.year), year),
            criteriaBuilder.equal(root.get(MatriculationExamAttendance_.term), term),
            criteriaBuilder.equal(root.get(MatriculationExamAttendance_.status), attendanceStatus)
        )
    );
    
    return entityManager
      .createQuery(criteria)
      .getResultList();
  }
  
  public MatriculationExamEnrollment findLatestByExamAndStudent(MatriculationExam exam, Student student) {
    EntityManager entityManager = getEntityManager(); 
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<MatriculationExamEnrollment> criteria = criteriaBuilder.createQuery(MatriculationExamEnrollment.class);
    Root<MatriculationExamEnrollment> root = criteria.from(MatriculationExamEnrollment.class);
    criteria.select(root);
    criteria.where(
      criteriaBuilder.and(
        criteriaBuilder.equal(root.get(MatriculationExamEnrollment_.exam), exam),
        criteriaBuilder.equal(root.get(MatriculationExamEnrollment_.student), student)
      )
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

  public MatriculationExamEnrollment updateCandidateNumber(MatriculationExamEnrollment enrollment, int candidateNumber) {
    enrollment.setCandidateNumber(candidateNumber);
    return persist(enrollment);
  }

}
