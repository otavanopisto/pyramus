package fi.pyramus.rest.controller;

import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import fi.pyramus.dao.projects.ProjectDAO;
import fi.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.pyramus.domainmodel.projects.Project;
import fi.pyramus.domainmodel.users.User;

@Dependent
@Stateless
public class ProjectController {
  @Inject
  private ProjectDAO projectDAO;

  public Project createProject(String name, String description, double optionalStudiesLength, EducationalTimeUnit optionalStudiesLengthTimeUnit, User user) {
    Project project = projectDAO.create(name, description, optionalStudiesLength, optionalStudiesLengthTimeUnit, user);
    return project;
  }

  public List<Project> findProjects() {
    List<Project> projects = projectDAO.listAll();
    return projects;
  }
  
  public List<Project> findUnarchivedProjects() {
    List<Project> projects = projectDAO.listUnarchived();
    return projects;
  }

  public Project findProjectById(Long id) {
    try {
      Project project = projectDAO.findById(id);
      return project;
    } catch (NullPointerException e) {
      return null;
    }
  }

  public Project updateProject(Project project, String name, String description, double optionalStudiesLength,
      EducationalTimeUnit optionalStudiesLengthTimeUnit, User user) {
    Project updatedProject = projectDAO.update(project, name, description, optionalStudiesLength, optionalStudiesLengthTimeUnit, user);
    return updatedProject;
  }

  public Project archiveProject(Project project) {
    projectDAO.archive(project);
    return project;
  }

  public Project unarchiveProject(Project project, User user) {
    projectDAO.unarchive(project, user);
    return project;
  }
}
