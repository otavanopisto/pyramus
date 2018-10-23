package fi.otavanopisto.pyramus.dao.matriculation;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamAttendance;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamAttendanceStatus;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamAttendance_;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamEnrollment;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamGrade;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamSubject;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamTerm;

@Stateless
public class MatriculationExamAttendanceDAO extends PyramusEntityDAO<MatriculationExamAttendance> {

  public MatriculationExamAttendance create(
    MatriculationExamEnrollment enrollment,
    MatriculationExamSubject subject,
    Boolean mandatory,
    Boolean repeat,
    Integer year,
    MatriculationExamTerm term,
    MatriculationExamAttendanceStatus status,
    MatriculationExamGrade grade
  ) {
    MatriculationExamAttendance attendance = new MatriculationExamAttendance();
    attendance.setEnrollment(enrollment);
    attendance.setSubject(subject);
    attendance.setMandatory(mandatory);
    attendance.setRetry(repeat);
    attendance.setYear(year);
    attendance.setTerm(term);
    attendance.setStatus(status);
    attendance.setGrade(grade);
    getEntityManager().persist(attendance);
    return attendance;
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

  public void deleteByEnrollment(
      MatriculationExamEnrollment enrollment
  ) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaDelete<MatriculationExamAttendance> delete = criteriaBuilder.createCriteriaDelete(MatriculationExamAttendance.class);
    Root<MatriculationExamAttendance> root = delete.from(MatriculationExamAttendance.class);
    delete.where(
      criteriaBuilder.equal(root.get(MatriculationExamAttendance_.enrollment), enrollment)
    );
    
    entityManager.createQuery(delete).executeUpdate();
  }
}