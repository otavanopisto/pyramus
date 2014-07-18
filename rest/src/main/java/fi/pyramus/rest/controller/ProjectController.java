package fi.pyramus.rest.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import fi.pyramus.dao.base.TagDAO;
import fi.pyramus.dao.projects.ProjectDAO;
import fi.pyramus.dao.projects.ProjectModuleDAO;
import fi.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.pyramus.domainmodel.base.Tag;
import fi.pyramus.domainmodel.modules.Module;
import fi.pyramus.domainmodel.projects.Project;
import fi.pyramus.domainmodel.projects.ProjectModule;
import fi.pyramus.domainmodel.projects.ProjectModuleOptionality;
import fi.pyramus.domainmodel.users.User;
import fi.pyramus.persistence.search.SearchResult;

@Dependent
@Stateless
public class ProjectController {
  
  @Inject
  private ProjectDAO projectDAO;
  
  @Inject
  private TagDAO tagDAO;
  
  @Inject
  private ProjectModuleDAO projectModuleDAO;
  
  /* Projects */

  public Project createProject(String name, String description, double optionalStudiesLength, EducationalTimeUnit optionalStudiesLengthTimeUnit, User user) {
    Project project = projectDAO.create(name, description, optionalStudiesLength, optionalStudiesLengthTimeUnit, user);
    return project;
  }

  public List<Project> listProjects() {
    List<Project> projects = projectDAO.listAll();
    return projects;
  }
  
  public List<Project> listUnarchivedProjects() {
    List<Project> projects = projectDAO.listUnarchived();
    return projects;
  }

  public Project findProjectById(Long id) {
    Project project = projectDAO.findById(id);
    return project;
  }

  public Project updateProject(Project project, String name, String description, double optionalStudiesLength,
      EducationalTimeUnit optionalStudiesLengthTimeUnit, User user) {
    Project updatedProject = projectDAO.update(project, name, description, optionalStudiesLength, optionalStudiesLengthTimeUnit, user);
    return updatedProject;
  }

  public Project archiveProject(Project project, User user) {
    projectDAO.archive(project, user);
    return project;
  }

  public Project unarchiveProject(Project project, User user) {
    projectDAO.unarchive(project, user);
    return project;
  }

  public void deleteProject(Project project) {
    projectDAO.delete(project);
  }
  
  public SearchResult<Project> searchProjects(int resultsPerPage, int page,String name, String description,  String tags, boolean filterArchived) {
    SearchResult<Project> projects = projectDAO.searchProjects(resultsPerPage, page, name, description, tags, filterArchived);
    return projects;
  }
  
  /* ProjectModule */
  
  public ProjectModule createProjectModule(Project project, Module module, ProjectModuleOptionality optionality) {
    ProjectModule projectModule = projectModuleDAO.create(project, module, optionality);
    return projectModule;
  }
  
  public List<ProjectModule> listProjectModules(Project project) {
    List<ProjectModule> modules = projectModuleDAO.listByProject(project);
    return modules;
  }
  
  public ProjectModule findProjectModuleById(Long id) {
    ProjectModule projectModule = projectModuleDAO.findById(id);
    return projectModule;
  }
  
  public ProjectModule updateProjectModule(ProjectModule projectModule, ProjectModuleOptionality optionality) {
    projectModuleDAO.update(projectModule, optionality);
    return projectModule;
  }
  
  public void deleteProjectModule(ProjectModule projectModule) {
    projectModuleDAO.delete(projectModule);
  }

  /* Tags */
  
  public Tag createProjectTag(Project project, String text) {
    Tag tag = tagDAO.findByText(text);
    if (tag == null) {
      tag = tagDAO.create(text);
    }
    project.addTag(tag);
    return tag;
  }
  
  public Project removeProjectTag(Project project, Tag tag) {
    return projectDAO.removeTag(project, tag);
  }

  public Project updateProjectTags(Project project, List<String> tags) {
    Set<String> newTags = new HashSet<>(tags);
    Set<Tag> projectTags = new HashSet<>(project.getTags());
    
    for (Tag projectTag : projectTags) {
      if (!newTags.contains(projectTag.getText())) {
        removeProjectTag(project, projectTag);
      }
        
      newTags.remove(projectTag.getText());
    }
    
    for (String newTag : newTags) {
      createProjectTag(project, newTag);
    }
    
    return project;
  }
}
