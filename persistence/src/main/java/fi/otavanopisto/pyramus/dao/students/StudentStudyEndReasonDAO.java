package fi.otavanopisto.pyramus.dao.students;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.students.StudentStudyEndReason;
import fi.otavanopisto.pyramus.domainmodel.students.StudentStudyEndReason_;


@Stateless
public class StudentStudyEndReasonDAO extends PyramusEntityDAO<StudentStudyEndReason> {
  
  public StudentStudyEndReason create(StudentStudyEndReason parentReason, String name) {
    EntityManager entityManager = getEntityManager(); 

    StudentStudyEndReason studentStudyEndReason = new StudentStudyEndReason();
    studentStudyEndReason.setName(name);
    if (parentReason != null) {
      parentReason.addChildEndReason(studentStudyEndReason);
      entityManager.persist(parentReason);
    }
    
    entityManager.persist(studentStudyEndReason);

    return studentStudyEndReason;
  }

  public List<StudentStudyEndReason> listByParentReason(StudentStudyEndReason parentReason) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<StudentStudyEndReason> criteria = criteriaBuilder.createQuery(StudentStudyEndReason.class);
    Root<StudentStudyEndReason> root = criteria.from(StudentStudyEndReason.class);
    criteria.select(root);
    if (parentReason == null) {
	    criteria.where(
	      criteriaBuilder.isNull(root.get(StudentStudyEndReason_.parentReason))
	    );
    } else {
    	criteria.where(
    	  criteriaBuilder.equal(root.get(StudentStudyEndReason_.parentReason), parentReason)
    	);
    }
    
    return entityManager.createQuery(criteria).getResultList();
  }
  
  public StudentStudyEndReason updateParentReason(StudentStudyEndReason studyEndReason, StudentStudyEndReason newParentReason) {
    EntityManager entityManager = getEntityManager(); 
    
    StudentStudyEndReason oldParentReason = studyEndReason.getParentReason();
    if (newParentReason == null) {
      if (oldParentReason == null) {
        return studyEndReason; // do nothing
      } else {
        oldParentReason.removeChildEndReason(studyEndReason);
        entityManager.persist(oldParentReason);
      }
    } else {
      if (oldParentReason != null) {
        oldParentReason.removeChildEndReason(studyEndReason);
        entityManager.persist(oldParentReason);
      }
      
      newParentReason.addChildEndReason(studyEndReason);
      entityManager.persist(newParentReason);
    }
    
    entityManager.persist(studyEndReason);
    return studyEndReason;
  }
  
  public StudentStudyEndReason updateName(StudentStudyEndReason studyEndReason, String name) {
    EntityManager entityManager = getEntityManager(); 
    
    studyEndReason.setName(name);
    entityManager.persist(studyEndReason);
    return studyEndReason;
  }
  
  public StudentStudyEndReason update(StudentStudyEndReason studyEndReason) {
    getEntityManager().persist(studyEndReason);
    return studyEndReason;
  }

  public void delete(StudentStudyEndReason studyEndReason) {
    EntityManager entityManager = getEntityManager(); 
    if (studyEndReason.getParentReason() != null) {
      StudentStudyEndReason oldParentReason = studyEndReason.getParentReason();
      oldParentReason.removeChildEndReason(studyEndReason);
      entityManager.persist(oldParentReason);
    }
    
    entityManager.remove(studyEndReason);
  }

}
