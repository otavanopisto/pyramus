package fi.otavanopisto.pyramus.dao.users;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.users.StudentParent;
import fi.otavanopisto.pyramus.domainmodel.users.StudentParentChild;
import fi.otavanopisto.pyramus.domainmodel.users.StudentParentChild_;

@Stateless
public class StudentParentChildDAO extends PyramusEntityDAO<StudentParentChild> {
  
  public StudentParentChild create(StudentParent parent, Student child) {
    StudentParentChild studentParentChild = new StudentParentChild();

    studentParentChild.setStudentParent(parent);
    studentParentChild.setStudent(child);
    
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

}
