package fi.pyramus.views.students;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.I18N.Messages;
import fi.pyramus.breadcrumbs.Breadcrumbable;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.LanguageDAO;
import fi.pyramus.dao.base.MunicipalityDAO;
import fi.pyramus.dao.base.NationalityDAO;
import fi.pyramus.dao.base.StudyProgrammeDAO;
import fi.pyramus.domainmodel.base.Language;
import fi.pyramus.domainmodel.base.Municipality;
import fi.pyramus.domainmodel.base.Nationality;
import fi.pyramus.domainmodel.base.StudyProgramme;
import fi.pyramus.framework.PyramusViewController;
import fi.pyramus.framework.UserRole;
import fi.pyramus.util.StringAttributeComparator;

/**
 * ViewController to search for students.
 * 
 * @author antti.viljakainen
 */
public class SearchStudentsViewController extends PyramusViewController implements Breadcrumbable {

  /**
   * Returns roles that are allowed to use this resource.
   *  
   * @see fi.fi.pyramus.framework.PyramusViewController#getAllowedRoles()
   */
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

  /**
   * Processes the page request.
   * 
   * @param pageRequestContext Request context
   */
  public void process(PageRequestContext pageRequestContext) {
    StudyProgrammeDAO studyProgrammeDAO = DAOFactory.getInstance().getStudyProgrammeDAO();
    MunicipalityDAO municipalityDAO = DAOFactory.getInstance().getMunicipalityDAO();
    NationalityDAO nationalityDAO = DAOFactory.getInstance().getNationalityDAO();
    LanguageDAO languageDAO = DAOFactory.getInstance().getLanguageDAO();

    List<StudyProgramme> studyProgrammes = studyProgrammeDAO.listUnarchived();
    Collections.sort(studyProgrammes, new StringAttributeComparator("getName"));
    
    List<Nationality> nationalities = nationalityDAO.listUnarchived();
    Collections.sort(nationalities, new StringAttributeComparator("getName"));
    
    List<Municipality> municipalities = municipalityDAO.listUnarchived();
    Collections.sort(municipalities, new StringAttributeComparator("getName"));

    List<Language> languages = languageDAO.listUnarchived();
    Collections.sort(languages, new StringAttributeComparator("getName"));
    
    pageRequestContext.getRequest().setAttribute("nationalities", nationalities);
    pageRequestContext.getRequest().setAttribute("municipalities", municipalities);
    pageRequestContext.getRequest().setAttribute("languages", languages);
    pageRequestContext.getRequest().setAttribute("studyProgrammes", studyProgrammes);

    pageRequestContext.setIncludeJSP("/templates/students/searchstudents.jsp");
  }

  /**
   * Returns the localized name of this page. Used e.g. for breadcrumb navigation.
   * 
   * @param locale The locale to be used for the name
   * 
   * @return The localized name of this page
   */
  public String getName(Locale locale) {
    return Messages.getInstance().getText(locale, "students.searchStudents.pageTitle");
  }

}
