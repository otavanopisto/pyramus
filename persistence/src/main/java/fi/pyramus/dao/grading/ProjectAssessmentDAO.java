package fi.pyramus.dao.grading;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.pyramus.dao.PyramusEntityDAO;
import fi.pyramus.domainmodel.grading.Grade;
import fi.pyramus.domainmodel.grading.ProjectAssessment;
import fi.pyramus.domainmodel.grading.ProjectAssessment_;
import fi.pyramus.domainmodel.projects.StudentProject;
import fi.pyramus.domainmodel.users.StaffMember;

@Stateless
public class ProjectAssessmentDAO extends PyramusEntityDAO<ProjectAssessment> {

  public ProjectAssessment create(StudentProject studentProject, StaffMember assessingUser, Grade grade, Date date, String verbalAssessment) {
    EntityManager entityManager = getEntityManager();

    ProjectAssessment projectAssessment = new ProjectAssessment();
    projectAssessment.setAssessor(assessingUser);
    projectAssessment.setStudentProject(studentProject);
    projectAssessment.setDate(date);
    projectAssessment.setGrade(grade);
    projectAssessment.setVerbalAssessment(verbalAssessment);
    
    entityManager.persist(projectAssessment);
    
    return projectAssessment;
  }

  public List<ProjectAssessment> listByProjectAndArchived(StudentProject studentProject, Boolean archived) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<ProjectAssessment> criteria = criteriaBuilder.createQuery(ProjectAssessment.class);
    Root<ProjectAssessment> root = criteria.from(ProjectAssessment.class);
    criteria.select(root);
    criteria.where(
      criteriaBuilder.and(
        criteriaBuilder.equal(root.get(ProjectAssessment_.studentProject), studentProject),
        criteriaBuilder.equal(root.get(ProjectAssessment_.archived), archived)
      )
    );
    
    return entityManager.createQuery(criteria).getResultList();
  }

  public ProjectAssessment update(ProjectAssessment assessment, StaffMember assessingUser, Grade grade, Date assessmentDate, String verbalAssessment) {
    EntityManager entityManager = getEntityManager();

    assessment.setAssessor(assessingUser);
    assessment.setGrade(grade);
    assessment.setDate(assessmentDate);
    assessment.setVerbalAssessment(verbalAssessment);
    
    entityManager.persist(assessment);
    
    return assessment;
  }

  @Override
  public void delete(ProjectAssessment projectAssessment) {
    super.delete(projectAssessment);
  }
}
