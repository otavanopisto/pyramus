package fi.otavanopisto.pyramus.dao.projects;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.otavanopisto.pyramus.dao.PyramusEntityDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Subject;
import fi.otavanopisto.pyramus.domainmodel.projects.Project;
import fi.otavanopisto.pyramus.domainmodel.projects.ProjectModuleOptionality;
import fi.otavanopisto.pyramus.domainmodel.projects.ProjectSubjectCourse;
import fi.otavanopisto.pyramus.domainmodel.projects.ProjectSubjectCourse_;

@Stateless
public class ProjectSubjectCourseDAO extends PyramusEntityDAO<ProjectSubjectCourse> {

  public ProjectSubjectCourse create(Project project, Subject subject, Integer courseNumber, ProjectModuleOptionality optionality) {
    EntityManager entityManager = getEntityManager();

    ProjectSubjectCourse projectSubjectCourse = new ProjectSubjectCourse();
    projectSubjectCourse.setProject(project);
    projectSubjectCourse.setSubject(subject);
    projectSubjectCourse.setCourseNumber(courseNumber);
    projectSubjectCourse.setOptionality(optionality);
    entityManager.persist(projectSubjectCourse);

    project.addProjectSubjectCourse(projectSubjectCourse);
    entityManager.persist(project);

    return projectSubjectCourse;
  }

  public void update(ProjectSubjectCourse projectSubjectCourse, ProjectModuleOptionality optionality) {
    EntityManager entityManager = getEntityManager();

    projectSubjectCourse.setOptionality(optionality);

    entityManager.persist(projectSubjectCourse);
  }

  @Override
  public void delete(ProjectSubjectCourse projectSubjectCourse) {
    if (projectSubjectCourse.getProject() != null) {
      projectSubjectCourse.getProject().removeProjectSubjectCourse(projectSubjectCourse);
    }
    super.delete(projectSubjectCourse);
  }

  public List<ProjectSubjectCourse> listByProject(Project project) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<ProjectSubjectCourse> criteria = criteriaBuilder.createQuery(ProjectSubjectCourse.class);
    Root<ProjectSubjectCourse> root = criteria.from(ProjectSubjectCourse.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.equal(root.get(ProjectSubjectCourse_.project), project)
    );
    
    return entityManager.createQuery(criteria).getResultList();
  }

}
