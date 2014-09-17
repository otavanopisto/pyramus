package fi.pyramus.util.dataimport.scripting.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.DefaultsDAO;
import fi.pyramus.dao.base.SubjectDAO;
import fi.pyramus.dao.courses.CourseDAO;
import fi.pyramus.dao.modules.ModuleDAO;
import fi.pyramus.dao.users.UserDAO;
import fi.pyramus.domainmodel.base.Subject;
import fi.pyramus.domainmodel.courses.Course;
import fi.pyramus.domainmodel.courses.CourseState;
import fi.pyramus.domainmodel.modules.Module;
import fi.pyramus.domainmodel.users.User;
import fi.pyramus.util.dataimport.scripting.InvalidScriptException;

public class CourseAPI {
  
  public CourseAPI(Long loggedUserId) {
    this.loggedUserId = loggedUserId;
  }
  
  public Long create(Long moduleId, String name, String nameExtension, String description, String subjectCode) throws InvalidScriptException {
    ModuleDAO moduleDAO = DAOFactory.getInstance().getModuleDAO();
    CourseDAO courseDAO = DAOFactory.getInstance().getCourseDAO();
    SubjectDAO subjectDAO = DAOFactory.getInstance().getSubjectDAO();
    UserDAO userDAO = DAOFactory.getInstance().getUserDAO();
    DefaultsDAO defaultsDAO = DAOFactory.getInstance().getDefaultsDAO();
    
    Module module = moduleDAO.findById(moduleId);
    if (module == null) {
      throw new InvalidScriptException("Module #" + moduleId + " not found.");
    }

    User loggedUser = userDAO.findById(loggedUserId);
    if (loggedUser == null) {
      throw new InvalidScriptException("Logged user not found.");
    }
    
    Subject subject = subjectDAO.findByCode(subjectCode);
    if (subject == null) {
      throw new InvalidScriptException("Subject by code '" + subjectCode + "' not found.");
    }
    
    // TODO: Add support for these:
    CourseState courseState = defaultsDAO.getDefaults().getInitialCourseState(); 
    Date beginDate = null;
    Date endDate = null;
    Double distanceTeachingDays = null;
    Double localTeachingDays = null;
    Double teachingHours = null;
    Double planningHours = null;
    Double assessingHours = null;
    Date enrolmentTimeEnd = null;
    
    return courseDAO.create(module, name, nameExtension, courseState, subject, module.getCourseNumber(), beginDate, endDate, 
        module.getCourseLength().getUnits(), module.getCourseLength().getUnit(), distanceTeachingDays, localTeachingDays, 
        teachingHours, planningHours, assessingHours, description, module.getMaxParticipantCount(), enrolmentTimeEnd, 
        loggedUser).getId();
  }
  
  public Long[] listIdsByModuleId(Long moduleId) throws InvalidScriptException {
    ModuleDAO moduleDAO = DAOFactory.getInstance().getModuleDAO();
    CourseDAO courseDAO = DAOFactory.getInstance().getCourseDAO();
    
    Module module = moduleDAO.findById(moduleId);
    if (module == null) {
      throw new InvalidScriptException("Module #" + moduleId + " not found.");
    }
    
    List<Long> result = new ArrayList<>();
    
    List<Course> courses = courseDAO.listByModule(module);
    for (Course course : courses) {
      result.add(course.getId());
    }
    
    return result.toArray(new Long[0]);
  }
  
  public Long[] listIds() throws InvalidScriptException {
    CourseDAO courseDAO = DAOFactory.getInstance().getCourseDAO();
    
    List<Long> result = new ArrayList<>();
    
    List<Course> courses = courseDAO.listUnarchived();
    for (Course course : courses) {
      result.add(course.getId());
    }
    
    return result.toArray(new Long[0]);
  }
  
  private Long loggedUserId;
}
