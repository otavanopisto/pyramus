package fi.pyramus.dao.projects;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import fi.pyramus.dao.PyramusEntityDAO;
import fi.pyramus.domainmodel.modules.Module;
import fi.pyramus.domainmodel.projects.Project;
import fi.pyramus.domainmodel.projects.ProjectModule;
import fi.pyramus.domainmodel.projects.ProjectModuleOptionality;
import fi.pyramus.domainmodel.projects.ProjectModule_;

@Stateless
public class ProjectModuleDAO extends PyramusEntityDAO<ProjectModule> {

  public ProjectModule create(Project project, Module module, ProjectModuleOptionality optionality) {
    EntityManager entityManager = getEntityManager();

    ProjectModule projectModule = new ProjectModule();
    projectModule.setProject(project);
    projectModule.setModule(module);
    projectModule.setOptionality(optionality);
    entityManager.persist(projectModule);

    project.addProjectModule(projectModule);
    entityManager.persist(project);

    return projectModule;
  }

  public void update(ProjectModule projectModule, ProjectModuleOptionality optionality) {
    EntityManager entityManager = getEntityManager();

    projectModule.setOptionality(optionality);

    entityManager.persist(projectModule);
  }

  @Override
  public void delete(ProjectModule projectModule) {
    if (projectModule.getProject() != null) {
      projectModule.getProject().removeProjectModule(projectModule);
    }
    super.delete(projectModule);
  }

  public List<ProjectModule> listByProject(Project project) {
    EntityManager entityManager = getEntityManager(); 
    
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<ProjectModule> criteria = criteriaBuilder.createQuery(ProjectModule.class);
    Root<ProjectModule> root = criteria.from(ProjectModule.class);
    criteria.select(root);
    criteria.where(
        criteriaBuilder.equal(root.get(ProjectModule_.project), project)
    );
    
    return entityManager.createQuery(criteria).getResultList();
  }

}
