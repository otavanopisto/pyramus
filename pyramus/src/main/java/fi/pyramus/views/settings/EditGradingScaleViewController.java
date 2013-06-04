package fi.pyramus.views.settings;

import java.util.Locale;

import org.apache.commons.lang.math.NumberUtils;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.I18N.Messages;
import fi.pyramus.breadcrumbs.Breadcrumbable;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.grading.GradingScaleDAO;
import fi.pyramus.domainmodel.grading.GradingScale;
import fi.pyramus.framework.PyramusViewController;
import fi.pyramus.framework.UserRole;
import fi.pyramus.util.JSONArrayExtractor;

/**
 * The controller responsible of the Edit Grading Scale view of the application. 
 * 
 * @see fi.pyramus.json.users.EditGradingScaleJSONRequestController
 */
public class EditGradingScaleViewController extends PyramusViewController implements Breadcrumbable {

  /**
   * Processes the page request by including the corresponding JSP page to the response.
   * 
   * In order for the JSP page to build the view, gradingScale object is loaded in to "gradingScale" attribute
   * 
   * @param pageRequestContext Page request context
   */
  public void process(PageRequestContext pageRequestContext) {
    GradingScaleDAO gradingScaleDAO = DAOFactory.getInstance().getGradingScaleDAO();

    Long gradingScaleId = NumberUtils.createLong(pageRequestContext.getRequest().getParameter("gradingScaleId"));
    GradingScale gradingScale = gradingScaleDAO.findById(gradingScaleId);
    pageRequestContext.getRequest().setAttribute("gradingScale", gradingScale);
    
    String jsonGrades = new JSONArrayExtractor("passingGrade",
                                               "name",
                                               "qualification",
                                               "GPA",
                                               "description",
                                               "id").extractString(gradingScale.getGrades());
    this.setJsDataVariable(pageRequestContext, "grades", jsonGrades);
    
    pageRequestContext.setIncludeJSP("/templates/settings/editgradingscale.jsp");
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
    return Messages.getInstance().getText(locale, "settings.editGradingScale.pageTitle");
  }

}
