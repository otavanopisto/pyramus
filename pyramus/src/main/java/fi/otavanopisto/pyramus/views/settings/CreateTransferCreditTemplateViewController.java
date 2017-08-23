package fi.otavanopisto.pyramus.views.settings;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.I18N.Messages;
import fi.otavanopisto.pyramus.breadcrumbs.Breadcrumbable;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.CurriculumDAO;
import fi.otavanopisto.pyramus.dao.base.EducationalTimeUnitDAO;
import fi.otavanopisto.pyramus.dao.base.SchoolDAO;
import fi.otavanopisto.pyramus.dao.base.SubjectDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Curriculum;
import fi.otavanopisto.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.otavanopisto.pyramus.domainmodel.base.School;
import fi.otavanopisto.pyramus.domainmodel.base.Subject;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.util.JSONArrayExtractor;
import fi.otavanopisto.pyramus.util.StringAttributeComparator;

/**
 * The controller responsible of the Manage Transfer Credit Template view of the application.
 */
public class CreateTransferCreditTemplateViewController extends PyramusViewController implements Breadcrumbable {

  /**
   * Processes the page request by including the corresponding JSP page to the response.
   * 
   * @param pageRequestContext Page request context
   */
  public void process(PageRequestContext pageRequestContext) {
    SubjectDAO subjectDAO = DAOFactory.getInstance().getSubjectDAO();
    SchoolDAO schoolDAO = DAOFactory.getInstance().getSchoolDAO();
    EducationalTimeUnitDAO educationalTimeUnitDAO = DAOFactory.getInstance().getEducationalTimeUnitDAO();
    CurriculumDAO curriculumDAO = DAOFactory.getInstance().getCurriculumDAO();

    List<Subject> subjects = subjectDAO.listUnarchived();
    Collections.sort(subjects, new StringAttributeComparator("getName"));

    List<EducationalTimeUnit> timeUnits = educationalTimeUnitDAO.listUnarchived();
    Collections.sort(timeUnits, new StringAttributeComparator("getName"));

    List<School> schools = schoolDAO.listUnarchived();
    Collections.sort(schools, new StringAttributeComparator("getName"));
    
    String jsonTimeUnits = new JSONArrayExtractor("name", "id").extractString(timeUnits);
    JSONArray jsonSubjects = new JSONArrayExtractor("name", "code", "id").extract(subjects);
    for (int i = 0; i < jsonSubjects.size(); i++) {
      JSONObject jsonSubject = jsonSubjects.getJSONObject(i);
      if (subjects.get(i).getEducationType() != null) {
        jsonSubject.put("educationTypeName", subjects.get(i).getEducationType().getName());
      }
    }

    List<Curriculum> curriculums = curriculumDAO.listUnarchived();
    Collections.sort(curriculums, new StringAttributeComparator("getName"));
    String jsonCurriculums = new JSONArrayExtractor("name", "id").extractString(curriculums);
    setJsDataVariable(pageRequestContext, "curriculums", jsonCurriculums);
    
    this.setJsDataVariable(pageRequestContext, "subjects", jsonSubjects.toString());
    this.setJsDataVariable(pageRequestContext, "timeUnits", jsonTimeUnits);
    
    pageRequestContext.getRequest().setAttribute("curriculums", curriculums);
    pageRequestContext.setIncludeJSP("/templates/settings/createtransfercredittemplate.jsp");
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
    return Messages.getInstance().getText(locale, "settings.createTransferCreditTemplate.pageTitle");
  }

}
