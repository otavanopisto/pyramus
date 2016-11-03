package fi.otavanopisto.pyramus.util.dataimport.scripting.api;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.List;

import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.CourseBaseVariableDAO;
import fi.otavanopisto.pyramus.dao.base.DefaultsDAO;
import fi.otavanopisto.pyramus.dao.base.SubjectDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseTypeDAO;
import fi.otavanopisto.pyramus.dao.modules.ModuleDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Subject;
import fi.otavanopisto.pyramus.domainmodel.courses.Course;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseState;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseType;
import fi.otavanopisto.pyramus.domainmodel.modules.Module;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.util.dataimport.scripting.InvalidScriptException;

public class CourseAPI {
  
  public CourseAPI(Long loggedUserId) {
    this.loggedUserId = loggedUserId;
  }

  public Long create(Long moduleId, Long typeId, String name, String nameExtension, String description, String subjectCode) throws InvalidScriptException {
    ModuleDAO moduleDAO = DAOFactory.getInstance().getModuleDAO();
    CourseDAO courseDAO = DAOFactory.getInstance().getCourseDAO();
    SubjectDAO subjectDAO = DAOFactory.getInstance().getSubjectDAO();
    StaffMemberDAO userDAO = DAOFactory.getInstance().getStaffMemberDAO();
    DefaultsDAO defaultsDAO = DAOFactory.getInstance().getDefaultsDAO();
    CourseTypeDAO courseTypeDAO = DAOFactory.getInstance().getCourseTypeDAO();
    
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

    CourseType type = typeId != null ? courseTypeDAO.findById(typeId) : null;

    // TODO: Add support for these:
    CourseState courseState = defaultsDAO.getDefaults().getInitialCourseState(); 
    Date beginDate = null;
    Date endDate = null;
    Double distanceTeachingDays = null;
    Double localTeachingDays = null;
    Double teachingHours = null;
    Double distanceTeachingHours = null;
    Double planningHours = null;
    Double assessingHours = null;
    Date enrolmentTimeEnd = null;
    BigDecimal courseFee = null;
    Currency courseFeeCurrency = null;
    
    return courseDAO.create(module, name, nameExtension, courseState, type, subject, module.getCourseNumber(), beginDate, endDate, 
        module.getCourseLength().getUnits(), module.getCourseLength().getUnit(), distanceTeachingDays, localTeachingDays, 
        teachingHours, distanceTeachingHours, planningHours, assessingHours, description, module.getMaxParticipantCount(), 
        courseFee, courseFeeCurrency, enrolmentTimeEnd, loggedUser).getId();
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
  
  public String getVariable(Long courseId, String key) throws InvalidScriptException {
    CourseDAO courseDAO = DAOFactory.getInstance().getCourseDAO();
    CourseBaseVariableDAO courseBaseVariableDAO = DAOFactory.getInstance().getCourseBaseVariableDAO();
    
    Course course = courseDAO.findById(courseId);
    if (course == null) {
      throw new InvalidScriptException(String.format("Course #%d could not be found", courseId));
    }
    
    return courseBaseVariableDAO.findByCourseAndVariableKey(course, key);
  }
  
  public void setVariable(Long courseId, String key, String value) throws InvalidScriptException {
    CourseDAO courseDAO = DAOFactory.getInstance().getCourseDAO();
    CourseBaseVariableDAO courseBaseVariableDAO = DAOFactory.getInstance().getCourseBaseVariableDAO();
  
    Course course = courseDAO.findById(courseId);
    if (course == null) {
      throw new InvalidScriptException(String.format("Course #%d could not be found", courseId));
    }
    
    courseBaseVariableDAO.setCourseVariable(course, key, value);
  }
  
  private Long loggedUserId;
}
