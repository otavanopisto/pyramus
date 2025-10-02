package fi.otavanopisto.pyramus.dao.users;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.users.StudentParent;
import fi.otavanopisto.pyramus.domainmodel.users.StudentParentChild;
import fi.otavanopisto.pyramus.domainmodel.users.StudentParentChild_;

@Stateless
public class StudentParentChildDAO extends PyramusEntityDAO<StudentParentChild> {
  
  public StudentParentChild create(StudentParent parent, Student child, boolean continuedViewPermission) {
    StudentParentChild studentParentChild = new StudentParentChild();

    studentParentChild.setStudentParent(parent);
    studentParentChild.setStudent(child);
    studentParentChild.setContinuedViewPermission(continuedViewPermission);
    
    return persist(studentParentChild);
  }

  public StudentParentChild findBy(StudentParent studentParent, Student student) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<StudentParentChild> criteria = criteriaBuilder.createQuery(StudentParentChild.class);
    Root<StudentParentChild> root = criteria.from(StudentParentChild.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.and(
            criteriaBuilder.equal(root.get(StudentParentChild_.studentParent), studentParent),
            criteriaBuilder.equal(root.get(StudentParentChild_.student), student)
        )
    );
    
    return getSingleResult(entityManager.createQuery(criteria));
  }

  public List<StudentParentChild> listBy(Student student) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<StudentParentChild> criteria = criteriaBuilder.createQuery(StudentParentChild.class);
    Root<StudentParentChild> root = criteria.from(StudentParentChild.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.equal(root.get(StudentParentChild_.student), student)
    );
    
    return entityManager.createQuery(criteria).getResultList();
  }

  public StudentParentChild updateContinuedViewPermission(StudentParentChild studentParentChild,
      boolean continuedViewPermission) {
    studentParentChild.setContinuedViewPermission(continuedViewPermission);
    return persist(studentParentChild);
  }

}
