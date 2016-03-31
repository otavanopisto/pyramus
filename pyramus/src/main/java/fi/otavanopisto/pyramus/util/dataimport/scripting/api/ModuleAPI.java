package fi.otavanopisto.pyramus.util.dataimport.scripting.api;

import java.util.ArrayList;
import java.util.List;

import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.DefaultsDAO;
import fi.otavanopisto.pyramus.dao.base.SubjectDAO;
import fi.otavanopisto.pyramus.dao.modules.ModuleDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.otavanopisto.pyramus.domainmodel.base.Subject;
import fi.otavanopisto.pyramus.domainmodel.modules.Module;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.util.dataimport.scripting.InvalidScriptException;

public class ModuleAPI {
  
  public ModuleAPI(Long loggedUserId) {
    this.loggedUserId = loggedUserId;
  }
  
  public Long create(String name, String description, Long maxParticipantCount, String subjectCode, Integer courseNumber) throws InvalidScriptException {
    ModuleDAO moduleDAO = DAOFactory.getInstance().getModuleDAO();
    SubjectDAO subjectDAO = DAOFactory.getInstance().getSubjectDAO();
    StaffMemberDAO userDAO = DAOFactory.getInstance().getStaffMemberDAO();
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
