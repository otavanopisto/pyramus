package fi.otavanopisto.pyramus.views.studentparents;

import java.util.Locale;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.I18N.Messages;
import fi.otavanopisto.pyramus.breadcrumbs.Breadcrumbable;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.students.StudentDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.framework.PyramusStatusCode;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.framework.UserUtils;

public class CreateStudentParentRegistrationViewController extends PyramusViewController implements Breadcrumbable {

  /**
   * Processes the page request by including the corresponding JSP page to the response. 
   * 
   * @param requestContext Page request context
   */
  public void process(PageRequestContext pageRequestContext) {
    StaffMemberDAO staffDAO = DAOFactory.getInstance().getStaffMemberDAO();
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();

    StaffMember loggedUser = staffDAO.findById(pageRequestContext.getLoggedUserId());
    
    Student student = studentDAO.findById(pageRequestContext.getLong("studentId"));
    
    // TODO permission ?

    if (!UserUtils.canAccessOrganization(loggedUser, student.getOrganization())) {
      throw new SmvcRuntimeException(PyramusStatusCode.UNAUTHORIZED, "Cannot access users' organization.");
    }
    
    pageRequestContext.getRequest().setAttribute("student", student);
    
    pageRequestContext.setIncludeJSP("/templates/studentparents/createstudentparentregistration.jsp");
  }

  public UserRole[] getAllowedRoles() {
    // TODO ?
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

  /**
   * Returns the localized name of this page. Used e.g. for breadcrumb navigation.
   * 
   * @param locale The locale to be used for the name
   * 
   * @return The localized name of this page
   */
  public String getName(Locale locale) {
    return Messages.getInstance().getText(locale, "studentparents.createStudentParentRegistration.pageTitle");
  }

}
