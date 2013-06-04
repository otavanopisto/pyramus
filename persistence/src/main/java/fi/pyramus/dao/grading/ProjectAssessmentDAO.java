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
import fi.pyramus.domainmodel.users.User;

@Stateless
public class ProjectAssessmentDAO extends PyramusEntityDAO<ProjectAssessment> {

  public ProjectAssessment create(StudentProject studentProject, User assessingUser, Grade grade, Date date, String verbalAssessment) {
    EntityManager entityManager = getEntityManager();

    ProjectAssessment projectAssessment = new ProjectAssessment();
    projectAssessment.setAssessingUser(assessingUser);
    projectAssessment.setStudentProject(studentProject);
    projectAssessment.setDate(date);
    projectAssessment.setGrade(grade);
    projectAssessment.setVerbalAssessment(verbalAssessment);
    
    entityManager.persist(projectAssessment);
    
    return projectAssessment;
  }

  public List<ProjectAssessment> listByProject(StudentProject studentProject) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<ProjectAssessment> criteria = criteriaBuilder.createQuery(ProjectAssessment.class);
    Root<ProjectAssessment> root = criteria.from(ProjectAssessment.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.equal(root.get(ProjectAssessment_.studentProject), studentProject)
    );
    
    return entityManager.createQuery(criteria).getResultList();
  }

  public ProjectAssessment update(ProjectAssessment assessment, User assessingUser, Grade grade, Date assessmentDate, String verbalAssessment) {
    EntityManager entityManager = getEntityManager();

    assessment.setAssessingUser(assessingUser);
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
