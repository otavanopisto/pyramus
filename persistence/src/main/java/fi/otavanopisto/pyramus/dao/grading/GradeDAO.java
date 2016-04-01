package fi.otavanopisto.pyramus.dao.grading;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.grading.Grade;
import fi.otavanopisto.pyramus.domainmodel.grading.GradingScale;

@Stateless
public class GradeDAO extends PyramusEntityDAO<Grade> {

  /**
   * Creates new Grade
   * 
   * @param name grades's name
   * @param description description for grade
   * @param passingGrade indicates that grade is or is not a passing grade 
   * @param GPA grade points average or numeric representation of grade in grading system which don't use GPAs
   * @param qualification literal equivalent for grade (e.x. excellent)  
   *  
   * @return Grade
   */
  public Grade create(GradingScale gradingScale, String name, String description, Boolean passingGrade, Double GPA, String qualification) {
    EntityManager entityManager = getEntityManager();
    
    Grade grade = new Grade();
    grade.setName(name);
    grade.setDescription(description);
    grade.setGPA(GPA);
    grade.setPassingGrade(passingGrade);
    grade.setQualification(qualification);
    entityManager.persist(grade);
    
    gradingScale.addGrade(grade);
    
    entityManager.persist(gradingScale);
    
    return grade;
  }

  /**
   * Updates Grade
   * 
   * @param name grades's name
   * @param description description for grade
   * @param passingGrade indicates that grade is or is not a passing grade 
   * @param GPA grade points average or numeric representation of grade in grading system which don't use GPAs
   * @param qualification literal equivalent for grade (e.x. excellent)  
   *  
   * @return Grade
   */
  public Grade update(Grade grade, String name, String description, Boolean passingGrade, Double GPA, String qualification) {
    grade.setName(name);
    grade.setDescription(description);
    grade.setGPA(GPA);
    grade.setPassingGrade(passingGrade);
    grade.setQualification(qualification);
    EntityManager entityManager = getEntityManager();
    entityManager.persist(grade);
    
    return grade;
  }

  /**
   * Deletes a Grade
   * 
   * @param grade Grade to be deleted
   */
  @Override
  public void delete(Grade grade)  {
    super.delete(grade);
  }
}
