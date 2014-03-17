package fi.pyramus.views.students;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.breadcrumbs.Breadcrumbable;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.courses.CourseStudentDAO;
import fi.pyramus.dao.grading.CourseAssessmentRequestDAO;
import fi.pyramus.domainmodel.courses.CourseStudent;
import fi.pyramus.domainmodel.grading.CourseAssessmentRequest;
import fi.pyramus.framework.PyramusViewController;
import fi.pyramus.framework.UserRole;

public class StudentCourseAssessmentRequestsPopupViewController extends PyramusViewController implements Breadcrumbable {

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

  public void process(PageRequestContext pageRequestContext) {
    CourseAssessmentRequestDAO courseAssessmentRequestDAO = DAOFactory.getInstance().getCourseAssessmentRequestDAO();
    CourseStudentDAO courseStudentDAO = DAOFactory.getInstance().getCourseStudentDAO();
  
    Long courseStudentId = pageRequestContext.getLong("courseStudent");
    CourseStudent courseStudent = courseStudentDAO.findById(courseStudentId );
    List<CourseAssessmentRequest> courseAssessmentRequests = courseAssessmentRequestDAO.listByCourseStudent(courseStudent);
    
    Collections.sort(courseAssessmentRequests, new Comparator<CourseAssessmentRequest>() {
      @Override
      public int compare(CourseAssessmentRequest o1, CourseAssessmentRequest o2) {
        return o2.getCreated().compareTo(o1.getCreated());
      }
    });
    
    pageRequestContext.getRequest().setAttribute("courseStudent", courseStudent);
    pageRequestContext.getRequest().setAttribute("courseAssessmentRequests", courseAssessmentRequests);

    pageRequestContext.setIncludeJSP("/templates/students/studentcourseassessmentrequestspopup.jsp");
  }

  /**
   * Returns the localized name of this page. Used e.g. for breadcrumb navigation.
   * 
   * This view does not need a name because it's used as a content to popup dialog
   * 
   * @param locale The locale to be used for the name
   * 
   * @return The localized name of this page
   */
  public String getName(Locale locale) {
    return "";
  }

}
