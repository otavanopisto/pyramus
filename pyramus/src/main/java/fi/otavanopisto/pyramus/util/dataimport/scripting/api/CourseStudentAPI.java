package fi.otavanopisto.pyramus.util.dataimport.scripting.api;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Date;

import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.courses.CourseDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseStudentDAO;
import fi.otavanopisto.pyramus.dao.students.StudentDAO;
import fi.otavanopisto.pyramus.domainmodel.accommodation.Room;
import fi.otavanopisto.pyramus.domainmodel.base.CourseOptionality;
import fi.otavanopisto.pyramus.domainmodel.base.Defaults;
import fi.otavanopisto.pyramus.domainmodel.courses.Course;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseStudent;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.exception.DuplicateCourseStudentException;
import fi.otavanopisto.pyramus.util.dataimport.scripting.InvalidScriptException;

public class CourseStudentAPI {
  
  public CourseStudentAPI(Long loggedUserId) {
    this.loggedUserId = loggedUserId;
  }
  
  public Long create(Long courseId, Long studentId) throws InvalidScriptException {
    CourseDAO courseDAO = DAOFactory.getInstance().getCourseDAO();
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    CourseStudentDAO courseStudentDAO = DAOFactory.getInstance().getCourseStudentDAO();
    
    Defaults defaults = DAOFactory.getInstance().getDefaultsDAO().getDefaults();
    
    Course course = courseDAO.findById(courseId);
    if (course == null) {
      throw new InvalidScriptException("Course #" + courseId + " not found.");
    }
    
    Student student = studentDAO.findById(studentId);
    if (student == null) {
      throw new InvalidScriptException("Student #" + studentId + " not found.");
    }
    
    Room room = null;
    BigDecimal lodgingFee = null;
    Currency lodgingFeeCurrency = null;
    String organization = null;
    String additionalInfo = null;
    
    try {
      return courseStudentDAO.create(course, student, defaults.getInitialCourseEnrolmentType(), defaults.getInitialCourseParticipationType(), 
          new Date(), false, CourseOptionality.OPTIONAL, null, organization, additionalInfo, room, lodgingFee, lodgingFeeCurrency, Boolean.FALSE).getId();
    } catch (DuplicateCourseStudentException dcse) {
      throw new InvalidScriptException("Student #" + studentId + " has an already existing coursestudent.");
    }
  }
  
  public Long findIdByCourseAndStudent(Long courseId, Long studentId) throws InvalidScriptException {
    CourseDAO courseDAO = DAOFactory.getInstance().getCourseDAO();
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    CourseStudentDAO courseStudentDAO = DAOFactory.getInstance().getCourseStudentDAO();

    Course course = courseDAO.findById(courseId);
    if (course == null) {
      throw new InvalidScriptException("Course #" + courseId + " not found.");
    }
    
    Student student = studentDAO.findById(studentId);
    if (student == null) {
      throw new InvalidScriptException("Student #" + studentId + " not found.");
    }
    
    CourseStudent courseStudent = courseStudentDAO.findByCourseAndStudent(course, student);
    return courseStudent != null ? courseStudent.getId() : null;
  }
  
  @SuppressWarnings("unused")
  private Long loggedUserId;
}
