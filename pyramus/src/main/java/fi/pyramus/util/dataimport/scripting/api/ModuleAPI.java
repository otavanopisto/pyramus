package fi.pyramus.util.dataimport.scripting.api;

import java.util.ArrayList;
import java.util.List;

import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.DefaultsDAO;
import fi.pyramus.dao.base.SubjectDAO;
import fi.pyramus.dao.modules.ModuleDAO;
import fi.pyramus.dao.users.StaffMemberDAO;
import fi.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.pyramus.domainmodel.base.Subject;
import fi.pyramus.domainmodel.modules.Module;
import fi.pyramus.domainmodel.users.User;
import fi.pyramus.util.dataimport.scripting.InvalidScriptException;

public class ModuleAPI {
  
  public ModuleAPI(Long loggedUserId) {
    this.loggedUserId = loggedUserId;
  }
  
  public Long create(String name, String description, Long maxParticipantCount, String subjectCode, Integer courseNumber) throws InvalidScriptException {
    ModuleDAO moduleDAO = DAOFactory.getInstance().getModuleDAO();
    SubjectDAO subjectDAO = DAOFactory.getInstance().getSubjectDAO();
    StaffMemberDAO userDAO = DAOFactory.getInstance().getStaffDAO();
    DefaultsDAO defaultsDAO = DAOFactory.getInstance().getDefaultsDAO();
    
    Subject subject = subjectDAO.findByCode(subjectCode);
    if (subject == null) {
      throw new InvalidScriptException("Subject by code '" + subjectCode + "' not found.");
    }

    User loggedUser = userDAO.findById(loggedUserId);
    if (loggedUser == null) {
      throw new InvalidScriptException("Logged user not found.");
    }
    
    EducationalTimeUnit timeUnit = defaultsDAO.getDefaults().getBaseTimeUnit();
    
    // TODO: moduleLength, timeUnit
    
    return moduleDAO.create(name, subject, courseNumber, 0d, timeUnit, description, maxParticipantCount, loggedUser).getId();
  }
  
  public Long[] listIdsBySubjectCodeAndCourseNumber(String subjectCode, Integer courseNumber) {
    ModuleDAO moduleDAO = DAOFactory.getInstance().getModuleDAO();
    SubjectDAO subjectDAO = DAOFactory.getInstance().getSubjectDAO();
    
    Subject subject = subjectDAO.findByCode(subjectCode);

    List<Long> result = new ArrayList<>();
    
    List<Module> modules = moduleDAO.listBySubjectAndCourseNumber(subject, courseNumber);
    for (Module module : modules) {
      result.add(module.getId());
    }
    
    return result.toArray(new Long[0]);
  }

  public Long[] listIdsBySubjectCode(String subjectCode) {
    ModuleDAO moduleDAO = DAOFactory.getInstance().getModuleDAO();
    SubjectDAO subjectDAO = DAOFactory.getInstance().getSubjectDAO();
    
    List<Long> result = new ArrayList<>();
    
    Subject subject = subjectDAO.findByCode(subjectCode);
    List<Module> modules = moduleDAO.listBySubject(subject);
    for (Module module : modules) {
      result.add(module.getId());
    }
    
    return result.toArray(new Long[0]);
  }
  
  private Long loggedUserId;
}
