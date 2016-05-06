package fi.otavanopisto.pyramus.views.settings;

import java.util.Locale;

import org.apache.commons.lang.math.NumberUtils;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.I18N.Messages;
import fi.otavanopisto.pyramus.breadcrumbs.Breadcrumbable;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.grading.GradingScaleDAO;
import fi.otavanopisto.pyramus.domainmodel.grading.GradingScale;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.util.JSONArrayExtractor;

/**
 * The controller responsible of the Edit Grading Scale view of the application. 
 * 
 * @see fi.otavanopisto.pyramus.json.users.EditGradingScaleJSONRequestController
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
    return Messages.getInstance().getText(locale, "settings.editGradingScale.pageTitle");
  }

}
