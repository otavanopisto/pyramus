package fi.otavanopisto.pyramus.views.settings;

import java.util.List;
import java.util.Locale;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.I18N.Messages;
import fi.otavanopisto.pyramus.breadcrumbs.Breadcrumbable;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.StudyProgrammeCategoryDAO;
import fi.otavanopisto.pyramus.dao.base.StudyProgrammeDAO;
import fi.otavanopisto.pyramus.domainmodel.base.StudyProgramme;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.util.JSONArrayExtractor;

/**
 * The controller responsible of the Manage Fields of Education view of the application.
 * 
 * @see fi.otavanopisto.pyramus.json.settings.SaveEducationTypesJSONRequestController
 */
public class StudyProgrammesViewController extends PyramusViewController implements Breadcrumbable {

  /**
   * Processes the page request by including the corresponding JSP page to the response.
   * 
   * @param pageRequestContext Page request context
   */
  public void process(PageRequestContext pageRequestContext) {
    StudyProgrammeDAO studyProgrammeDAO = DAOFactory.getInstance().getStudyProgrammeDAO();
    StudyProgrammeCategoryDAO studyProgrammeCategoryDAO = DAOFactory.getInstance().getStudyProgrammeCategoryDAO();
    
    List<StudyProgramme> studyProgrammes = studyProgrammeDAO.listUnarchived();
    JSONArray jsonStudyProgrammes = new JSONArrayExtractor("name", "code", "id").extract(studyProgrammes);
    for (int i=0; i<jsonStudyProgrammes.size(); i++) {
      JSONObject jsonStudyProgrammeCategory = jsonStudyProgrammes.getJSONObject(i);
      if (studyProgrammes.get(i).getCategory() != null) {
        jsonStudyProgrammeCategory.put("categoryId", studyProgrammes.get(i).getCategory().getId());
      }
    }
    
    String jsonCategories = new JSONArrayExtractor("name", "id").extractString(studyProgrammeCategoryDAO.listUnarchived());
    
    this.setJsDataVariable(pageRequestContext, "studyProgrammes", jsonStudyProgrammes.toString());
    this.setJsDataVariable(pageRequestContext, "categories", jsonCategories);
    pageRequestContext.setIncludeJSP("/templates/settings/studyprogrammes.jsp");
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
    return Messages.getInstance().getText(locale, "settings.studyProgrammes.pageTitle");
  }

}
