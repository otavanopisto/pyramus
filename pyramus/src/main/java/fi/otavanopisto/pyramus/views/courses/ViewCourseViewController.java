package fi.otavanopisto.pyramus.views.courses;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.I18N.Messages;
import fi.otavanopisto.pyramus.breadcrumbs.Breadcrumbable;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.courses.CourseComponentDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseDescriptionDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseStaffMemberDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseStudentDAO;
import fi.otavanopisto.pyramus.dao.grading.CourseAssessmentRequestDAO;
import fi.otavanopisto.pyramus.dao.reports.ReportDAO;
import fi.otavanopisto.pyramus.domainmodel.courses.Course;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseStaffMember;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseStudent;
import fi.otavanopisto.pyramus.domainmodel.grading.CourseAssessmentRequest;
import fi.otavanopisto.pyramus.domainmodel.reports.Report;
import fi.otavanopisto.pyramus.domainmodel.reports.ReportContextType;
import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.util.StringAttributeComparator;

/**
 * The controller responsible of the View Course view of the application.
 */
public class ViewCourseViewController extends PyramusViewController implements Breadcrumbable {

  /**
   * Processes the page request by including the corresponding JSP page to the response.
   * 
   * @param pageRequestContext Page request context
   */
  public void process(PageRequestContext pageRequestContext) {
    CourseDAO courseDAO = DAOFactory.getInstance().getCourseDAO();
    CourseDescriptionDAO descriptionDAO = DAOFactory.getInstance().getCourseDescriptionDAO();
    CourseStudentDAO courseStudentDAO = DAOFactory.getInstance().getCourseStudentDAO();
    CourseComponentDAO courseComponentDAO = DAOFactory.getInstance().getCourseComponentDAO();
    CourseStaffMemberDAO courseStaffMemberDAO = DAOFactory.getInstance().getCourseStaffMemberDAO();
    ReportDAO reportDAO = DAOFactory.getInstance().getReportDAO();
    CourseAssessmentRequestDAO courseAssessmentRequestDAO = DAOFactory.getInstance().getCourseAssessmentRequestDAO();
    
    // The course to be edited
    
    Course course = courseDAO.findById(pageRequestContext.getLong("course"));
    pageRequestContext.getRequest().setAttribute("course", course);

    Map<Long, CourseAssessmentRequest> courseAssessmentRequests = new HashMap<>();
    
    List<CourseStudent> courseStudents = courseStudentDAO.listByCourse(course);
    Collections.sort(courseStudents, new Comparator<CourseStudent>() {
      @Override
      public int compare(CourseStudent o1, CourseStudent o2) {
        int cmp = o1.getStudent().getLastName().compareToIgnoreCase(o2.getStudent().getLastName());
        if (cmp == 0)
          cmp = o1.getStudent().getFirstName().compareToIgnoreCase(o2.getStudent().getFirstName());
        return cmp;
      }
    });
    
    List<CourseStaffMember> courseUsers = courseStaffMemberDAO.listByCourse(course);
    Collections.sort(courseUsers, new Comparator<CourseStaffMember>() {
      @Override
      public int compare(CourseStaffMember o1, CourseStaffMember o2) {
        int cmp = o1.getStaffMember().getLastName().compareToIgnoreCase(o2.getStaffMember().getLastName());
        if (cmp == 0)
          cmp = o1.getStaffMember().getFirstName().compareToIgnoreCase(o2.getStaffMember().getFirstName());
        return cmp;
      }
    });

    JSONArray courseReportsJSON = new JSONArray();
    List<Report> courseReports = reportDAO.listByContextType(ReportContextType.Course);
    Collections.sort(courseReports, new StringAttributeComparator("getName"));
    
    for (Report report : courseReports) {
      JSONObject obj = new JSONObject();
      obj.put("id", report.getId().toString());
      obj.put("name", report.getName());
      courseReportsJSON.add(obj);
    }
    

    /**
     * Course Assessment Requests by Course Student
     */

    for (CourseStudent courseStudent : courseStudents) {
      List<CourseAssessmentRequest> courseAssessmentRequestsByCourseStudent = courseAssessmentRequestDAO.listByCourseStudent(courseStudent);

      Collections.sort(courseAssessmentRequestsByCourseStudent, new Comparator<CourseAssessmentRequest>() {
        @Override
        public int compare(CourseAssessmentRequest o1, CourseAssessmentRequest o2) {
          return o2.getCreated().compareTo(o1.getCreated());
        }
      });

      if (courseAssessmentRequestsByCourseStudent.size() > 0) {
        courseAssessmentRequests.put(courseStudent.getId(), courseAssessmentRequestsByCourseStudent.get(0));
      }
    }
    
    setJsDataVariable(pageRequestContext, "courseReports", courseReportsJSON.toString());
    
    pageRequestContext.getRequest().setAttribute("courseStudents", courseStudents);
    pageRequestContext.getRequest().setAttribute("courseUsers", courseUsers);
    pageRequestContext.getRequest().setAttribute("courseComponents", courseComponentDAO.listByCourse(course));
    pageRequestContext.getRequest().setAttribute("courseDescriptions", descriptionDAO.listByCourseBase(course));
    pageRequestContext.getRequest().setAttribute("courseAssessmentRequests", courseAssessmentRequests);
    
    pageRequestContext.setIncludeJSP("/templates/courses/viewcourse.jsp");
  }

  /**
   * Returns the roles allowed to access this page. Editing courses is available for users with
   * {@link Role#MANAGER} or {@link Role#ADMINISTRATOR} privileges.
   * 
   * @return The roles allowed to access this page
   */
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

  /**
   * Returns the localized name of this page. Used e.g. for breadcrumb navigation.
   * 
   * @param locale The locale to be used for the name
   * 
   * @return The localized name of this page
   */
  public String getName(Locale locale) {
    return Messages.getInstance().getText(locale, "courses.viewCourse.breadcrumb");
  }

}
