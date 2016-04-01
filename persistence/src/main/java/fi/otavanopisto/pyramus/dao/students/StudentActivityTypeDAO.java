package fi.otavanopisto.pyramus.dao.students;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.students.StudentActivityType;

@Stateless
public class StudentActivityTypeDAO extends PyramusEntityDAO<StudentActivityType> {
 
  public StudentActivityType create(String name) {
    EntityManager entityManager = getEntityManager();
    
    StudentActivityType studentActivityType = new StudentActivityType();
    studentActivityType.setName(name);
    
    entityManager.persist(studentActivityType);
    return studentActivityType;
  }
  
  public StudentActivityType updateName(StudentActivityType studentActivityType, String name) {
    EntityManager entityManager = getEntityManager();
    
    studentActivityType.setName(name);
    entityManager.persist(studentActivityType);
    return studentActivityType;
  }
  
}
