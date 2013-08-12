package fi.pyramus.rest.controller;

import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import fi.pyramus.dao.base.TagDAO;
import fi.pyramus.dao.projects.ProjectDAO;
import fi.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.pyramus.domainmodel.base.Tag;
import fi.pyramus.domainmodel.projects.Project;
import fi.pyramus.domainmodel.users.User;

@Dependent
@Stateless
public class ProjectController {
  @Inject
  private ProjectDAO projectDAO;
  @Inject
  private TagDAO tagDAO;

  public Project createProject(String name, String description, double optionalStudiesLength, EducationalTimeUnit optionalStudiesLengthTimeUnit, User user) {
    Project project = projectDAO.create(name, description, optionalStudiesLength, optionalStudiesLengthTimeUnit, user);
    return project;
  }
  
  public Tag createTag(Project project, String text) {
    Tag tag = tagDAO.create(text);
    project.addTag(tag);
    return tag;
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
  
  public Set<Tag> findTags(Project project) {
    Set<Tag> tags = project.getTags();
    return tags;
  }
  
  public Tag findTagById(Long id) {
    try {
      Tag tag = tagDAO.findById(id);
      return tag;
    } catch(NullPointerException e) {
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
