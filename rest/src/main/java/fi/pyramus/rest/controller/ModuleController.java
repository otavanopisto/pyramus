package fi.pyramus.rest.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import fi.pyramus.dao.base.TagDAO;
import fi.pyramus.dao.courses.CourseDAO;
import fi.pyramus.dao.modules.ModuleComponentDAO;
import fi.pyramus.dao.modules.ModuleDAO;
import fi.pyramus.dao.projects.ProjectModuleDAO;
import fi.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.pyramus.domainmodel.base.Subject;
import fi.pyramus.domainmodel.base.Tag;
import fi.pyramus.domainmodel.courses.Course;
import fi.pyramus.domainmodel.modules.Module;
import fi.pyramus.domainmodel.modules.ModuleComponent;
import fi.pyramus.domainmodel.projects.Project;
import fi.pyramus.domainmodel.projects.ProjectModule;
import fi.pyramus.domainmodel.users.User;

@Dependent
@Stateless
public class ModuleController {
  
  @Inject
  private ModuleDAO moduleDAO;
  
  @Inject
  private ModuleComponentDAO moduleComponentDAO;
  
  @Inject
  private ProjectModuleDAO projectModuleDAO;
  
  @Inject
  private TagDAO tagDAO;
  
  @Inject
  private CourseDAO courseDAO;
  
  /* Module */

  public Module createModule(String name, Subject subject, Integer courseNumber, Double moduleLength, EducationalTimeUnit moduleLengthTimeUnit,
      String description, Long maxParticipantCount, User creator) {
    Module module = moduleDAO.create(name, subject, courseNumber, moduleLength, moduleLengthTimeUnit, description, maxParticipantCount, creator);
    return module;
  }

  public Module findModuleById(Long id) {
    Module module = moduleDAO.findById(id);
    return module;
  }

  public List<Module> listModules() {
    List<Module> modules = moduleDAO.listAll();
    return modules;
  }

  public List<Module> listUnarchivedModules() {
    List<Module> modules = moduleDAO.listUnarchived();
    return modules;
  }

  public Module updateModule(Module module, String name, Subject subject, Integer courseNumber, Double length, EducationalTimeUnit lengthTimeUnit,
      String description, Long maxParticipantCount, User modifier) {
    Module moduleUpdated = moduleDAO.update(module, name, subject, courseNumber, length, lengthTimeUnit, description, maxParticipantCount, modifier);
    return moduleUpdated;
  }

  public Module archiveModule(Module module, User user) {
    moduleDAO.archive(module, user);
    return module;
  }

  public Module unarchiveModule(Module module, User user) {
    moduleDAO.unarchive(module, user);
    return module;
  }
  
  public void deleteModule(Module module) {
    moduleDAO.delete(module);
  }
  
  /* Components */
  
  public ModuleComponent createModuleComponent(Module module, Double length, EducationalTimeUnit lengthTimeUnit, String name, String description) {
    return moduleComponentDAO.create(module, length, lengthTimeUnit, name, description);
  }
  
  public ModuleComponent findModuleComponentById(Long id) {
    return moduleComponentDAO.findById(id);
  }
  
  public List<ModuleComponent> listModuleComponentsByModule(Module module) {
    return moduleComponentDAO.listByModule(module);
  }
  
  public ModuleComponent updateModuleComponent(ModuleComponent moduleComponent, Double length, EducationalTimeUnit lengthTimeUnit, String name, String description) {
    return moduleComponentDAO.update(moduleComponent, length, lengthTimeUnit, name, description);
  }
  
  public ModuleComponent archiveModuleComponent(ModuleComponent moduleComponent, User user) {
    moduleComponentDAO.archive(moduleComponent, user);
    return moduleComponent;
  }
  
  public void deleteModuleComponent(ModuleComponent moduleComponent) {
    moduleComponentDAO.delete(moduleComponent);;
  }
  
  /* Tags */
  
  public Tag createModuleTag(Module module, String text) {
    Tag tag = tagDAO.findByText(text);
    if(tag == null) {
      tag = tagDAO.create(text);
    }
    
    module.addTag(tag);
    return tag;
  }

  public Module updateModuleTags(Module module, List<String> tags) {
    Set<String> newTags = new HashSet<>(tags);
    Set<Tag> moduleTags = new HashSet<>(module.getTags());
    
    for (Tag courseTag : moduleTags) {
      if (!newTags.contains(courseTag.getText())) {
        removeModuleTag(module, courseTag);
      }
        
      newTags.remove(courseTag.getText());
    }
    
    for (String newTag : newTags) {
      createModuleTag(module, newTag);
    }
    
    return module;
  }
  
  public void removeModuleTag(Module module, Tag tag) {
    module.removeTag(tag);
  }
  
  /* Courses */

  public List<Course> listCoursesByModule(Module module) {
    List<Course> courses = courseDAO.listByModule(module);
    return courses;
  }
  
  /* Projects */
  
  public List<Project> findProjectsByModule(Module module) {
    List<Project> projects = new ArrayList<Project>();
    List<ProjectModule> projectModules = projectModuleDAO.listAll();
    for (ProjectModule projectModule : projectModules) {
      if(projectModule.getModule().equals(module)) {
        projects.add(projectModule.getProject());
      }
    }
    return projects;
  }
  
}
