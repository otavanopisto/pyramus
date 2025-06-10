package fi.otavanopisto.pyramus.dao.students;

import java.util.Date;
import java.util.List;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.students.StudentStudyPeriod;
import fi.otavanopisto.pyramus.domainmodel.students.StudentStudyPeriodType;
import fi.otavanopisto.pyramus.domainmodel.students.StudentStudyPeriod_;

@Stateless
public class StudentStudyPeriodDAO extends PyramusEntityDAO<StudentStudyPeriod> {

  public StudentStudyPeriod create(Student student, Date begin, Date end, StudentStudyPeriodType periodType) {
    StudentStudyPeriod entity = new StudentStudyPeriod();
    entity.setStudent(student);
    entity.setBegin(begin);
    entity.setEnd(end);
    entity.setPeriodType(periodType);
    
    return persist(entity);
  }
  
  public List<StudentStudyPeriod> listByStudent(Student student) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<StudentStudyPeriod> criteria = criteriaBuilder.createQuery(StudentStudyPeriod.class);
    Root<StudentStudyPeriod> root = criteria.from(StudentStudyPeriod.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.equal(root.get(StudentStudyPeriod_.student), student)
    );
    
    return entityManager.createQuery(criteria).getResultList();
  }

  public StudentStudyPeriod update(StudentStudyPeriod entity, Date begin, Date end, StudentStudyPeriodType periodType) {
    entity.setBegin(begin);
    entity.setEnd(end);
    entity.setPeriodType(periodType);
    
    return persist(entity);
  }

  @Override
  public void delete(StudentStudyPeriod e) {
    super.delete(e);
  }
}
