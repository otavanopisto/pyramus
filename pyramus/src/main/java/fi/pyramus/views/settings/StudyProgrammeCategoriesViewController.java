package fi.pyramus.views.settings;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.I18N.Messages;
import fi.pyramus.breadcrumbs.Breadcrumbable;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.EducationTypeDAO;
import fi.pyramus.dao.base.StudyProgrammeCategoryDAO;
import fi.pyramus.domainmodel.base.EducationType;
import fi.pyramus.domainmodel.base.StudyProgrammeCategory;
import fi.pyramus.framework.PyramusViewController;
import fi.pyramus.framework.UserRole;
import fi.pyramus.util.JSONArrayExtractor;
import fi.pyramus.util.StringAttributeComparator;

/**
 * The controller responsible of the Study Programme Categories view of the application.
 * 
 * @see fi.pyramus.json.settings.SaveStudyProgrammeCategoriesJSONRequestController
 */
public class StudyProgrammeCategoriesViewController extends PyramusViewController implements Breadcrumbable {

  /**
   * Processes the page request by including the corresponding JSP page to the response.
   * 
   * @param pageRequestContext Page request context
   */
  public void process(PageRequestContext pageRequestContext) {
    StudyProgrammeCategoryDAO studyProgrammeCategoryDAO = DAOFactory.getInstance().getStudyProgrammeCategoryDAO();
    EducationTypeDAO educationTypeDAO = DAOFactory.getInstance().getEducationTypeDAO();    

    List<EducationType> educationTypes = educationTypeDAO.listUnarchived();
    Collections.sort(educationTypes, new StringAttributeComparator("getName"));
    
    List<StudyProgrammeCategory> studyProgrammeCategories = studyProgrammeCategoryDAO.listUnarchived();
    JSONArray jsonStudyProgrammeCategories = new JSONArrayExtractor("name", "id").extract(studyProgrammeCategories);
    for (int i=0; i<jsonStudyProgrammeCategories.size(); i++) {
      JSONObject jsonStudyProgrammeCategory = jsonStudyProgrammeCategories.getJSONObject(i);
      if (studyProgrammeCategories.get(i).getEducationType() != null) {
        jsonStudyProgrammeCategory.put("educationTypeId", studyProgrammeCategories.get(i).getEducationType().getId());
      }
    }
    String jsonEducationTypes = new JSONArrayExtractor("name", "id").extractString(educationTypes);
    
    this.setJsDataVariable(pageRequestContext, "studyProgrammeCategories", jsonStudyProgrammeCategories.toString());
    this.setJsDataVariable(pageRequestContext, "educationTypes", jsonEducationTypes);

    pageRequestContext.setIncludeJSP("/templates/settings/studyprogrammecategories.jsp");
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
    return Messages.getInstance().getText(locale, "settings.studyProgrammeCategories.pageTitle");
  }

}
