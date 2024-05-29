package fi.otavanopisto.pyramus.dao.matriculation;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.grading.ProjectAssessment;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamAttendance;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamAttendance_;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamEnrollment;
import fi.otavanopisto.pyramus.matriculation.MatriculationExamAttendanceFunding;
import fi.otavanopisto.pyramus.matriculation.MatriculationExamAttendanceStatus;
import fi.otavanopisto.pyramus.matriculation.MatriculationExamGrade;
import fi.otavanopisto.pyramus.matriculation.MatriculationExamSubject;
import fi.otavanopisto.pyramus.matriculation.MatriculationExamTerm;

@Stateless
public class MatriculationExamAttendanceDAO extends PyramusEntityDAO<MatriculationExamAttendance> {

  public MatriculationExamAttendance create(
    MatriculationExamEnrollment enrollment,
    MatriculationExamSubject subject,
    Boolean mandatory,
    Boolean retry,
    Integer year,
    MatriculationExamTerm term,
    MatriculationExamAttendanceStatus status,
    MatriculationExamAttendanceFunding funding,
    MatriculationExamGrade grade
  ) {
    MatriculationExamAttendance attendance = new MatriculationExamAttendance();
    attendance.setEnrollment(enrollment);
    attendance.setSubject(subject);
    attendance.setMandatory(mandatory);
    attendance.setRetry(retry);
    attendance.setYear(year);
    attendance.setTerm(term);
    attendance.setStatus(status);
    attendance.setFunding(funding);
    attendance.setGrade(grade);
    return persist(attendance);
  }

  public List<MatriculationExamAttendance> listByEnrollment(
      MatriculationExamEnrollment enrollment
  ) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<MatriculationExamAttendance> criteria = criteriaBuilder.createQuery(MatriculationExamAttendance.class);
    Root<MatriculationExamAttendance> root = criteria.from(MatriculationExamAttendance.class);
    criteria.select(root);
    criteria.where(
      criteriaBuilder.equal(root.get(MatriculationExamAttendance_.enrollment), enrollment)
    );
    
    return entityManager.createQuery(criteria).getResultList();
  }

  public List<MatriculationExamAttendance> listByEnrollmentAndStatus(MatriculationExamEnrollment enrollment,
      MatriculationExamAttendanceStatus status) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<MatriculationExamAttendance> criteria = criteriaBuilder.createQuery(MatriculationExamAttendance.class);
    Root<MatriculationExamAttendance> root = criteria.from(MatriculationExamAttendance.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(MatriculationExamAttendance_.enrollment), enrollment),
            criteriaBuilder.equal(root.get(MatriculationExamAttendance_.status), status)
        )
    );
    
    return entityManager.createQuery(criteria).getResultList();
  }

  public List<MatriculationExamAttendance> listBy(Integer year, MatriculationExamTerm term, MatriculationExamAttendanceStatus status) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<MatriculationExamAttendance> criteria = criteriaBuilder.createQuery(MatriculationExamAttendance.class);
    Root<MatriculationExamAttendance> root = criteria.from(MatriculationExamAttendance.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(MatriculationExamAttendance_.year), year),
            criteriaBuilder.equal(root.get(MatriculationExamAttendance_.term), term),
            criteriaBuilder.equal(root.get(MatriculationExamAttendance_.status), status)
        )
    );
    
    return entityManager.createQuery(criteria).getResultList();
  }

  public MatriculationExamAttendance update(MatriculationExamAttendance attendance,
      MatriculationExamEnrollment enrollment,
      MatriculationExamSubject subject,
      Boolean mandatory,
      Boolean retry,
      Integer year,
      MatriculationExamTerm term,
      MatriculationExamAttendanceStatus status,
      MatriculationExamAttendanceFunding funding,
      MatriculationExamGrade grade) {
    attendance.setEnrollment(enrollment);
    attendance.setSubject(subject);
    attendance.setMandatory(mandatory);
    attendance.setRetry(retry);
    attendance.setYear(year);
    attendance.setTerm(term);
    attendance.setStatus(status);
    attendance.setFunding(funding);
    attendance.setGrade(grade);
    return persist(attendance);
  }

  public MatriculationExamAttendance updateProjectAssessment(MatriculationExamAttendance attendance, ProjectAssessment projectAssessment) {
    attendance.setProjectAssessment(projectAssessment);
    return persist(attendance);
  }
  
  public void delete(MatriculationExamAttendance matriculationExamAttendance) {
    super.delete(matriculationExamAttendance);
  }

}