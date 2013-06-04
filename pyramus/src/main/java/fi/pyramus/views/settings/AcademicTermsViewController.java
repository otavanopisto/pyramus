package fi.pyramus.views.settings;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.I18N.Messages;
import fi.pyramus.breadcrumbs.Breadcrumbable;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.AcademicTermDAO;
import fi.pyramus.domainmodel.base.AcademicTerm;
import fi.pyramus.framework.PyramusViewController;
import fi.pyramus.framework.UserRole;

/**
 * The controller responsible of the Manage Academic Terms view of the application.
 * 
 * @see fi.pyramus.json.settings.CreateSubjectJSONRequestController
 */
public class AcademicTermsViewController extends PyramusViewController implements Breadcrumbable {

  /**
   * Processes the page request by including the corresponding JSP page to the response.
   * 
   * @param pageRequestContext Page request context
   */
  public void process(PageRequestContext pageRequestContext) {
    AcademicTermDAO academicTermDAO = DAOFactory.getInstance().getAcademicTermDAO();
    
    List<AcademicTerm> academicTerms = academicTermDAO.listUnarchived();
    Collections.sort(academicTerms, new Comparator<AcademicTerm>() {
      public int compare(AcademicTerm o1, AcademicTerm o2) {
        return o1.getStartDate() == null ? -1 : o2.getStartDate() == null ? 1 : o1.getStartDate().compareTo(o2.getStartDate());
      }
    });
    
    JSONArray jsonAcademicTerms = new JSONArray();
    for (AcademicTerm academicTerm : academicTerms) {
      JSONObject jsonAcademicTerm = new JSONObject();
      jsonAcademicTerm.put("name", academicTerm.getName());
      if (academicTerm.getStartDate() != null) {
        jsonAcademicTerm.put("startDate", academicTerm.getStartDate().getTime());
      }
      if (academicTerm.getEndDate() != null) {
        jsonAcademicTerm.put("endDate", academicTerm.getEndDate().getTime());
      }
      jsonAcademicTerm.put("id", academicTerm.getId());
      jsonAcademicTerms.add(jsonAcademicTerm);
    }

    this.setJsDataVariable(pageRequestContext, "academicTerms", jsonAcademicTerms.toString());
    pageRequestContext.setIncludeJSP("/templates/settings/academicterms.jsp");
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
    return Messages.getInstance().getText(locale, "settings.academicTerms.pageTitle");
  }

}
