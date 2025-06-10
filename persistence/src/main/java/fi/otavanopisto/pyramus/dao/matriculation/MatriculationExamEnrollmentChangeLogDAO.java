package fi.otavanopisto.pyramus.dao.matriculation;

import java.util.Date;
import java.util.List;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamEnrollment;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamEnrollmentChangeLog;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamEnrollmentChangeLog_;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.matriculation.MatriculationExamEnrollmentChangeLogType;
import fi.otavanopisto.pyramus.matriculation.MatriculationExamEnrollmentState;

@Stateless
public class MatriculationExamEnrollmentChangeLogDAO extends PyramusEntityDAO<MatriculationExamEnrollmentChangeLog> {

  public MatriculationExamEnrollmentChangeLog create(MatriculationExamEnrollment enrollment, User modifier, 
      MatriculationExamEnrollmentChangeLogType changeType, MatriculationExamEnrollmentState newState, String message) {
    MatriculationExamEnrollmentChangeLog logEntry = new MatriculationExamEnrollmentChangeLog();
    logEntry.setEnrollment(enrollment);
    logEntry.setModifier(modifier);
    logEntry.setTimestamp(new Date());
    logEntry.setChangeType(changeType);
    logEntry.setNewState(newState);
    logEntry.setMessage(message);
    return persist(logEntry);
  }
  
  public List<MatriculationExamEnrollmentChangeLog> listByEnrollment(MatriculationExamEnrollment enrollment) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<MatriculationExamEnrollmentChangeLog> criteria = criteriaBuilder.createQuery(MatriculationExamEnrollmentChangeLog.class);
    Root<MatriculationExamEnrollmentChangeLog> root = criteria.from(MatriculationExamEnrollmentChangeLog.class);
    criteria.select(root);
    criteria.where(
      criteriaBuilder.equal(root.get(MatriculationExamEnrollmentChangeLog_.enrollment), enrollment)
    );
    criteria.orderBy(
        criteriaBuilder.asc(root.get(MatriculationExamEnrollmentChangeLog_.timestamp)),
        criteriaBuilder.asc(root.get(MatriculationExamEnrollmentChangeLog_.id)));
    
    return entityManager.createQuery(criteria).getResultList();
  }

}