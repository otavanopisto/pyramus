package fi.pyramus.dao.students;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.pyramus.dao.PyramusEntityDAO;
import fi.pyramus.domainmodel.students.Student;
import fi.pyramus.domainmodel.students.StudentImage;
import fi.pyramus.domainmodel.students.StudentImage_;

@Stateless
public class StudentImageDAO extends PyramusEntityDAO<StudentImage> {

  public StudentImage create(Student student, String contentType, byte[] data) {
    EntityManager em = getEntityManager();
    
    StudentImage studentImage = new StudentImage();
    studentImage.setStudent(student);
    studentImage.setContentType(contentType);
    studentImage.setData(data);
    
    em.persist(studentImage);

    return studentImage;
  }
  
  public StudentImage update(StudentImage studentImage, String contentType, byte[] data) {
    EntityManager em = getEntityManager();
    
    studentImage.setContentType(contentType);
    studentImage.setData(data);
    
    em.persist(studentImage);

    return studentImage;
  }

  /*
   * Student Image
   */
  
  public StudentImage findByStudent(Student student) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<StudentImage> criteria = criteriaBuilder.createQuery(StudentImage.class);
    Root<StudentImage> root = criteria.from(StudentImage.class);
    criteria.select(root);
    criteria.where(criteriaBuilder.equal(root.get(StudentImage_.student), student));
    
    return getSingleResult(entityManager.createQuery(criteria));
  }

  public Boolean findStudentHasImage(Student student) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Long> criteria = criteriaBuilder.createQuery(Long.class);
    Root<StudentImage> root = criteria.from(StudentImage.class);
    criteria.select(criteriaBuilder.count(root));
    criteria.where(
        criteriaBuilder.equal(root.get(StudentImage_.student), student)
    );
    
    return entityManager.createQuery(criteria).getSingleResult() > 0;
  }

  @Override
  public void delete(StudentImage image) {
    super.delete(image);
  }
}
