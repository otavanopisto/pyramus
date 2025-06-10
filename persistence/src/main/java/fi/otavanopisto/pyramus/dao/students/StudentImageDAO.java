package fi.otavanopisto.pyramus.dao.students;

import java.util.Date;
import java.util.List;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.students.StudentImage;
import fi.otavanopisto.pyramus.domainmodel.students.StudentImage_;

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

  public List<Student> listPastStudentsWithImage(Date studentStudyEndThreshold, int maxResults) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<Student> criteria = criteriaBuilder.createQuery(Student.class);
    Root<StudentImage> root = criteria.from(StudentImage.class);
    Join<StudentImage, Student> student = root.join(StudentImage_.student);

    criteria.select(student).distinct(true);
    criteria.where(getPastStudentPredicate(criteriaBuilder, student, studentStudyEndThreshold));
    
    return entityManager.createQuery(criteria).setMaxResults(maxResults).getResultList();
  }
  
  @Override
  public void delete(StudentImage image) {
    super.delete(image);
  }
}
