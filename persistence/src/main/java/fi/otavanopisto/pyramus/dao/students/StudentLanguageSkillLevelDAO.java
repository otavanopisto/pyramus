package fi.otavanopisto.pyramus.dao.students;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.grading.SpokenLanguageExamSkillLevel;
import fi.otavanopisto.pyramus.domainmodel.students.LanguageSkillType;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.students.StudentLanguageSkillLevel;
import fi.otavanopisto.pyramus.domainmodel.students.StudentLanguageSkillLevel_;

@Stateless
public class StudentLanguageSkillLevelDAO extends PyramusEntityDAO<StudentLanguageSkillLevel> {
 
  public StudentLanguageSkillLevel create(Student student, LanguageSkillType skillType, Date gradingDate, SpokenLanguageExamSkillLevel skillLevel) {
    EntityManager entityManager = getEntityManager();
    
    StudentLanguageSkillLevel studentLanguageSkillLevel = new StudentLanguageSkillLevel();
    studentLanguageSkillLevel.setStudent(student);
    studentLanguageSkillLevel.setGradingDate(gradingDate);
    studentLanguageSkillLevel.setSkillType(skillType);
    studentLanguageSkillLevel.setSkillLevel(skillLevel);
    
    entityManager.persist(studentLanguageSkillLevel);
    return studentLanguageSkillLevel;
  }
  
  public StudentLanguageSkillLevel update(StudentLanguageSkillLevel studentLanguageSkillLevel, LanguageSkillType skillType,Date gradingDate, SpokenLanguageExamSkillLevel skillLevel) {
    EntityManager entityManager = getEntityManager();
    
    studentLanguageSkillLevel.setSkillType(skillType);
    studentLanguageSkillLevel.setGradingDate(gradingDate);
    studentLanguageSkillLevel.setSkillLevel(skillLevel);
    
    entityManager.persist(studentLanguageSkillLevel);
    return studentLanguageSkillLevel;
  }
  
  public List<StudentLanguageSkillLevel> listByStudent(Student student) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<StudentLanguageSkillLevel> criteria = criteriaBuilder.createQuery(StudentLanguageSkillLevel.class);
    Root<StudentLanguageSkillLevel> root = criteria.from(StudentLanguageSkillLevel.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.equal(root.get(StudentLanguageSkillLevel_.student), student)
      );
    
    return entityManager.createQuery(criteria).getResultList();
  }

}
