package fi.otavanopisto.pyramus.dao.students;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Subject;
import fi.otavanopisto.pyramus.domainmodel.grading.Grade;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.students.StudentSubjectGrade;
import fi.otavanopisto.pyramus.domainmodel.students.StudentSubjectGrade_;

@Stateless
public class StudentSubjectGradeDAO extends PyramusEntityDAO<StudentSubjectGrade> {

  public StudentSubjectGrade create(Student student, Subject subject, Grade grade) {
    StudentSubjectGrade studentSubjectGrade = new StudentSubjectGrade();
    studentSubjectGrade.setStudent(student);
    studentSubjectGrade.setSubject(subject);
    studentSubjectGrade.setGrade(grade);
    
    return persist(studentSubjectGrade);
  }
  
  public List<StudentSubjectGrade> listByStudent(Student student) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<StudentSubjectGrade> criteria = criteriaBuilder.createQuery(StudentSubjectGrade.class);
    Root<StudentSubjectGrade> root = criteria.from(StudentSubjectGrade.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.equal(root.get(StudentSubjectGrade_.student), student)
    );
    
    return entityManager.createQuery(criteria).getResultList();
  }

  public StudentSubjectGrade findBy(Student student, Subject subject) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<StudentSubjectGrade> criteria = criteriaBuilder.createQuery(StudentSubjectGrade.class);
    Root<StudentSubjectGrade> root = criteria.from(StudentSubjectGrade.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.equal(root.get(StudentSubjectGrade_.student), student),
        criteriaBuilder.equal(root.get(StudentSubjectGrade_.subject), subject)
    );
    
    return getSingleResult(entityManager.createQuery(criteria));
  }
  
  public StudentSubjectGrade updateGrade(StudentSubjectGrade studentSubjectGrade, Grade grade) {
    studentSubjectGrade.setGrade(grade);
    return persist(studentSubjectGrade);
  }

  @Override
  public void delete(StudentSubjectGrade e) {
    super.delete(e);
  }

}
