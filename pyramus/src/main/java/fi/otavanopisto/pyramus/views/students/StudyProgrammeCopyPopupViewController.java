package fi.otavanopisto.pyramus.views.students;

import java.util.Locale;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.breadcrumbs.Breadcrumbable;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.grading.CourseAssessmentDAO;
import fi.otavanopisto.pyramus.dao.grading.CreditLinkDAO;
import fi.otavanopisto.pyramus.dao.grading.TransferCreditDAO;
import fi.otavanopisto.pyramus.dao.students.StudentDAO;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;

/**
 * ViewController for viewing student information within popup dialog.
 */
public class StudyProgrammeCopyPopupViewController extends PyramusViewController implements Breadcrumbable {

  /**
   * Returns allowed roles for this page. Everyone is allowed to use this view.
   * 
   * @return allowed roles
   */
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

  /**
   * Processes the page request.
   */
  public void process(PageRequestContext pageRequestContext) {
    TransferCreditDAO transferCreditDAO = DAOFactory.getInstance().getTransferCreditDAO();
    CourseAssessmentDAO courseAssessmentDAO = DAOFactory.getInstance().getCourseAssessmentDAO();
    CreditLinkDAO creditLinkDAO = DAOFactory.getInstance().getCreditLinkDAO();
    
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();

    Long studentId = pageRequestContext.getLong("student");

    Student student = studentDAO.findById(studentId);
    
    Long courseAssessmentCount = courseAssessmentDAO.countByStudent(student);
    Long transferCreditCount = transferCreditDAO.countByStudent(student);
    Long creditLinkCount = creditLinkDAO.countByStudent(student);
    
		pageRequestContext.getRequest().setAttribute("courseAssessmentCount", courseAssessmentCount);
    pageRequestContext.getRequest().setAttribute("transferCreditCount", transferCreditCount);
    pageRequestContext.getRequest().setAttribute("creditLinkCount", creditLinkCount);
  
    pageRequestContext.setIncludeJSP("/templates/students/studyprogrammecopypopup.jsp");
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

