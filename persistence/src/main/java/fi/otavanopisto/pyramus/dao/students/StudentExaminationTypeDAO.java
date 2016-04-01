package fi.otavanopisto.pyramus.dao.students;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.students.StudentExaminationType;

@Stateless
public class StudentExaminationTypeDAO extends PyramusEntityDAO<StudentExaminationType> {

  public StudentExaminationType create(String name) {
    EntityManager entityManager = getEntityManager();
    
    StudentExaminationType studentExaminationType = new StudentExaminationType();
    studentExaminationType.setName(name);
    
    entityManager.persist(studentExaminationType);
    return studentExaminationType;
  }
  
  public StudentExaminationType updateName(StudentExaminationType examinationType, String name) {
    EntityManager entityManager = getEntityManager();
    
    examinationType.setName(name);
    entityManager.persist(examinationType);
    return examinationType;
  }
}
