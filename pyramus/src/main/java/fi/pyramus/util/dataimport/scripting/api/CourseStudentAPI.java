package fi.pyramus.util.dataimport.scripting.api;

import java.util.Date;

import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.courses.CourseDAO;
import fi.pyramus.dao.courses.CourseStudentDAO;
import fi.pyramus.dao.students.StudentDAO;
import fi.pyramus.domainmodel.base.CourseOptionality;
import fi.pyramus.domainmodel.base.Defaults;
import fi.pyramus.domainmodel.courses.Course;
import fi.pyramus.domainmodel.courses.CourseStudent;
import fi.pyramus.domainmodel.students.Student;
import fi.pyramus.util.dataimport.scripting.InvalidScriptException;

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
    
    return courseStudentDAO.create(course, student, defaults.getInitialCourseEnrolmentType(), defaults.getInitialCourseParticipationType(), 
        new Date(), false, CourseOptionality.OPTIONAL).getId();
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
