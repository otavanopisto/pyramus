package fi.otavanopisto.pyramus.dao.students;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.students.StudentLodgingPeriod;
import fi.otavanopisto.pyramus.domainmodel.students.StudentLodgingPeriod_;

@Stateless
public class StudentLodgingPeriodDAO extends PyramusEntityDAO<StudentLodgingPeriod> {

  public StudentLodgingPeriod create(Student student, Date begin, Date end) {
    StudentLodgingPeriod studentLodgingPeriod = new StudentLodgingPeriod();
    studentLodgingPeriod.setStudent(student);
    studentLodgingPeriod.setBegin(begin);
    studentLodgingPeriod.setEnd(end);
    
    return persist(studentLodgingPeriod);
  }
  
  public List<StudentLodgingPeriod> listByStudent(Student student) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<StudentLodgingPeriod> criteria = criteriaBuilder.createQuery(StudentLodgingPeriod.class);
    Root<StudentLodgingPeriod> root = criteria.from(StudentLodgingPeriod.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.equal(root.get(StudentLodgingPeriod_.student), student)
    );
    
    return entityManager.createQuery(criteria).getResultList();
  }

  public StudentLodgingPeriod update(StudentLodgingPeriod studentLodgingPeriod, Date begin, Date end) {
    studentLodgingPeriod.setBegin(begin);
    studentLodgingPeriod.setEnd(end);
    return persist(studentLodgingPeriod);
  }

  @Override
  public void delete(StudentLodgingPeriod e) {
    super.delete(e);
  }
}
