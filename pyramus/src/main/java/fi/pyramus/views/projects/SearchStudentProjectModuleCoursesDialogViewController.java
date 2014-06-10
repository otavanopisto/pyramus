package fi.pyramus.views.projects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.commons.lang.StringUtils;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.I18N.Messages;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.AcademicTermDAO;
import fi.pyramus.dao.courses.CourseDAO;
import fi.pyramus.dao.courses.CourseStudentDAO;
import fi.pyramus.dao.modules.ModuleDAO;
import fi.pyramus.dao.students.StudentDAO;
import fi.pyramus.domainmodel.base.AcademicTerm;
import fi.pyramus.domainmodel.courses.Course;
import fi.pyramus.domainmodel.courses.CourseParticipationType;
import fi.pyramus.domainmodel.courses.CourseStudent;
import fi.pyramus.domainmodel.modules.Module;
import fi.pyramus.domainmodel.students.Student;
import fi.pyramus.domainmodel.users.Role;
import fi.pyramus.framework.PyramusViewController;
import fi.pyramus.framework.UserRole;

/**
 * The controller responsible of the Search Modules dialog of the application.
 */
public class SearchStudentProjectModuleCoursesDialogViewController extends PyramusViewController {
  
  /**
   * Processes the page request by including the corresponding JSP page to the response.
   * 
   * @param pageRequestContext Page request context
   */
  @SuppressWarnings("unchecked")
  public void process(PageRequestContext pageRequestContext) {
    CourseDAO courseDAO = DAOFactory.getInstance().getCourseDAO();
    ModuleDAO moduleDAO = DAOFactory.getInstance().getModuleDAO();
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    CourseStudentDAO courseStudentDAO = DAOFactory.getInstance().getCourseStudentDAO();
    AcademicTermDAO academicTermDAO = DAOFactory.getInstance().getAcademicTermDAO();

    Long moduleId = pageRequestContext.getLong("moduleId");
    Long academicTermId = pageRequestContext.getLong("academicTermId");
    Long studentId = pageRequestContext.getLong("studentId");

    Module module = moduleDAO.findById(moduleId);
    AcademicTerm academicTerm = null;
    if (academicTermId != null && academicTermId >= 0)
      academicTerm = academicTermDAO.findById(academicTermId);
    Student student = studentDAO.findById(studentId);
    
    List<Course> courses = courseDAO.listByModule(module);
    
    Collections.sort(courses, new ReverseComparator(new Comparator<Course>() {
      @Override
      public int compare(Course o1, Course o2) {
        int result = compareDates(o1.getBeginDate(), o2.getBeginDate());
        if (result == 0) {
          result = compareDates(o1.getEndDate(), o2.getEndDate());
        }
        
        return result;
      }
      
      private int compareDates(Date d1, Date d2) {
        if (d1 == d2) {
          return 0;
        } else {
          if (d1 == null)
            return 1;
          else if (d2 == null)
            return -1;
          else {
            return d1.compareTo(d2);
          }
        }
      }
    }));
    
    List<StudentProjectModuleCourseBean> studentProjectModuleCourses = new ArrayList<StudentProjectModuleCourseBean>();
    int coursesInTimeFrame = 0; 
    for (Course course : courses) {
      boolean withinTimeFrame = false;
      
      if ((academicTerm != null) && (academicTerm.getStartDate() != null) && (academicTerm.getEndDate() != null) && (course.getBeginDate() != null) && (course.getEndDate() != null)) {
        withinTimeFrame = isWithinTimeFrame(academicTerm.getStartDate(), academicTerm.getEndDate(), course.getBeginDate(), course.getEndDate());
        if (withinTimeFrame)
          coursesInTimeFrame++;
      }
      
      CourseParticipationType courseParticipationType = null;
      CourseStudent courseStudent = courseStudentDAO.findByCourseAndStudent(course, student);
      if (courseStudent != null)
        courseParticipationType = courseStudent.getParticipationType();
      
      Long courseStudentCount = courseStudentDAO.countByCourse(course);
      Long maxCourseStudentCount = course.getMaxParticipantCount();
      
      StudentProjectModuleCourseBean studentProjectModuleCourseBean = new StudentProjectModuleCourseBean(course, courseParticipationType, withinTimeFrame, courseStudentCount, maxCourseStudentCount);
      studentProjectModuleCourses.add(studentProjectModuleCourseBean);
    }
    
    String message;
    
    if (courses.size() > 0) {
      message = Messages.getInstance().getText(pageRequestContext.getRequest().getLocale(), "projects.searchStudentProjectModuleCoursesDialog.coursesFound", new Object[] {
        courses.size(), coursesInTimeFrame
      });
    } else {
      message = Messages.getInstance().getText(pageRequestContext.getRequest().getLocale(), "projects.searchStudentProjectModuleCoursesDialog.noCoursesFound");
    }
    
    pageRequestContext.getRequest().setAttribute("message", message);
    pageRequestContext.getRequest().setAttribute("studentProjectModuleCourses", studentProjectModuleCourses);
    
    pageRequestContext.setIncludeJSP("/templates/projects/searchstudentprojectmodulecoursesdialog.jsp");
  }

  /**
   * Returns the roles allowed to access this page. Creating projects is available for users with
   * {@link Role#MANAGER} or {@link Role#ADMINISTRATOR} privileges.
   * 
   * @return The roles allowed to access this page
   */
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }
  
  private boolean isWithinTimeFrame(Date frameStart, Date frameEnd, Date timeStart, Date timeEnd) {
    long startTime = frameStart.getTime();
    long endTime = frameEnd.getTime();
    long t1 = timeStart.getTime();
    long t2 = timeEnd.getTime();

    return ((t1 <= endTime) && (t2 >= startTime));
  }

  public class StudentProjectModuleCourseBean {
    
    public StudentProjectModuleCourseBean(Course course, CourseParticipationType participationType, boolean withinTimeFrame, 
        Long courseStudentCount, Long maxCourseStudentCount) {
      this.course = course;
      this.withinTimeFrame = withinTimeFrame;
      this.participationType = participationType;
      this.courseStudentCount = courseStudentCount;
      this.maxCourseStudentCount = maxCourseStudentCount;
    }
    
    public Long getCourseId() {
      return course.getId();
    }
    
    public String getCourseName() {
      StringBuilder nameBuilder = new StringBuilder(course.getName());
      
      if (!StringUtils.isBlank(course.getNameExtension())) {
        nameBuilder
          .append(" (")
          .append(course.getNameExtension())
          .append(")");
      }
       
      return nameBuilder.toString();
    }
    
    public Date getCourseBeginDate() {
      return course.getBeginDate();
    }
    
    public Date getCourseEndDate() {
      return course.getEndDate();
    }
    
    public boolean isWithinTimeFrame() {
      return withinTimeFrame;
    }
    
    public CourseParticipationType getParticipationType() {
      return participationType;
    }
    
    public Long getMaxCourseStudentCount() {
      return maxCourseStudentCount;
    }

    public Long getCourseStudentCount() {
      return courseStudentCount;
    }

    private Course course;
    private CourseParticipationType participationType;
    private boolean withinTimeFrame;
    private final Long courseStudentCount;
    private final Long maxCourseStudentCount;
  }
}
