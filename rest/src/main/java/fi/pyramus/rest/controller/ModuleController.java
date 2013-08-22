package fi.pyramus.rest.controller;

import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import fi.pyramus.dao.modules.ModuleDAO;
import fi.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.pyramus.domainmodel.base.Subject;
import fi.pyramus.domainmodel.modules.Module;
import fi.pyramus.domainmodel.users.User;

@Dependent
@Stateless
public class ModuleController {
  @Inject
  private ModuleDAO moduleDAO;

  public Module createModule(String name, Subject subject, Integer courseNumber, Double moduleLength, EducationalTimeUnit moduleLengthTimeUnit,
      String description, Long maxParticipantCount, User user) {
    Module module = moduleDAO.create(name, subject, courseNumber, moduleLength, moduleLengthTimeUnit, description, maxParticipantCount, user);
    return module;
  }

  public List<Module> findModules() {
    List<Module> modules = moduleDAO.listAll();
    return modules;
  }

  public List<Module> findUnarchivedModules() {
    List<Module> modules = moduleDAO.listUnarchived();
    return modules;
  }

  public Module findModuleById(Long id) {
    Module module = moduleDAO.findById(id);
    return module;
  }

  public Module updateModule(Module module, String name, Subject subject, Integer courseNumber, Double length, EducationalTimeUnit lengthTimeUnit,
      String description, Long maxParticipantCount, User user) {
    Module moduleUpdated = moduleDAO.update(module, name, subject, courseNumber, length, lengthTimeUnit, description, maxParticipantCount, user);
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
}
