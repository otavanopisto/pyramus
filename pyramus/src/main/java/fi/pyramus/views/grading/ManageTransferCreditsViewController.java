package fi.pyramus.views.grading;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.I18N.Messages;
import fi.pyramus.breadcrumbs.Breadcrumbable;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.EducationalTimeUnitDAO;
import fi.pyramus.dao.grading.GradingScaleDAO;
import fi.pyramus.dao.grading.TransferCreditDAO;
import fi.pyramus.dao.grading.TransferCreditTemplateDAO;
import fi.pyramus.dao.students.StudentDAO;
import fi.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.pyramus.domainmodel.grading.GradingScale;
import fi.pyramus.domainmodel.grading.TransferCredit;
import fi.pyramus.domainmodel.grading.TransferCreditTemplate;
import fi.pyramus.domainmodel.students.Student;
import fi.pyramus.framework.PyramusViewController;
import fi.pyramus.framework.UserRole;
import fi.pyramus.util.StringAttributeComparator;

/**
 * The controller responsible of the Manage Transfer Credits view of the application.
 */
public class ManageTransferCreditsViewController extends PyramusViewController implements Breadcrumbable {

  /**
   * Processes the page request by including the corresponding JSP page to the response.
   * 
   * @param pageRequestContext Page request context
   */
  public void process(PageRequestContext pageRequestContext) {
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    TransferCreditDAO transferCreditDAO = DAOFactory.getInstance().getTransferCreditDAO();
    GradingScaleDAO gradingScaleDAO = DAOFactory.getInstance().getGradingScaleDAO();
    TransferCreditTemplateDAO transferCreditTemplateDAO = DAOFactory.getInstance().getTransferCreditTemplateDAO();
    EducationalTimeUnitDAO educationalTimeUnitDAO = DAOFactory.getInstance().getEducationalTimeUnitDAO();
    
    Long studentId = pageRequestContext.getLong("studentId");
    
    Student student = studentDAO.findById(studentId);
    List<TransferCredit> transferCredits = transferCreditDAO.listByStudent(student);
    List<GradingScale> gradingScales = gradingScaleDAO.listUnarchived();

    List<EducationalTimeUnit> timeUnits = educationalTimeUnitDAO.listUnarchived();
    Collections.sort(timeUnits, new StringAttributeComparator("getName"));

    List<TransferCreditTemplate> transferCreditTemplates = transferCreditTemplateDAO.listAll();

    Collections.sort(transferCredits, new StringAttributeComparator("getCourseName", true));
    
    pageRequestContext.getRequest().setAttribute("student", student);
    pageRequestContext.getRequest().setAttribute("transferCredits", transferCredits);
    pageRequestContext.getRequest().setAttribute("gradingScales", gradingScales);
    pageRequestContext.getRequest().setAttribute("timeUnits", timeUnits);
    pageRequestContext.getRequest().setAttribute("transferCreditTemplates", transferCreditTemplates);
    
    pageRequestContext.setIncludeJSP("/templates/grading/managetransfercredits.jsp");
  }

  /**
   * Returns the roles allowed to access this page.
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
    return Messages.getInstance().getText(locale, "grading.manageTransferCredits.pageTitle");
  }

}
