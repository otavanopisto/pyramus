package fi.otavanopisto.pyramus.views.projects;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang.math.NumberUtils;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.I18N.Messages;
import fi.otavanopisto.pyramus.breadcrumbs.Breadcrumbable;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.EducationalTimeUnitDAO;
import fi.otavanopisto.pyramus.dao.base.SubjectDAO;
import fi.otavanopisto.pyramus.dao.projects.ProjectDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.otavanopisto.pyramus.domainmodel.base.Subject;
import fi.otavanopisto.pyramus.domainmodel.base.Tag;
import fi.otavanopisto.pyramus.domainmodel.projects.Project;
import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.util.StringAttributeComparator;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * The controller responsible of the Edit Project view of the application.
 */
public class ViewProjectViewController extends PyramusViewController implements Breadcrumbable {
  
  /**
   * Processes the page request by including the corresponding JSP page to the response.
   * 
   * @param pageRequestContext Page request context
   */
  public void process(PageRequestContext pageRequestContext) {
    StaffMemberDAO userDAO = DAOFactory.getInstance().getStaffMemberDAO();
    ProjectDAO projectDAO = DAOFactory.getInstance().getProjectDAO();
    EducationalTimeUnitDAO educationalTimeUnitDAO = DAOFactory.getInstance().getEducationalTimeUnitDAO();
    SubjectDAO subjectDAO = DAOFactory.getInstance().getSubjectDAO();

    Long projectId = NumberUtils.createLong(pageRequestContext.getRequest().getParameter("project"));
    Project project = projectDAO.findById(projectId);

    StringBuilder tagsBuilder = new StringBuilder();
    Iterator<Tag> tagIterator = project.getTags().iterator();
    while (tagIterator.hasNext()) {
      Tag tag = tagIterator.next();
      tagsBuilder.append(tag.getText());
      if (tagIterator.hasNext())
        tagsBuilder.append(' ');
    }
    
    List<EducationalTimeUnit> educationalTimeUnits = educationalTimeUnitDAO.listUnarchived();
    Collections.sort(educationalTimeUnits, new StringAttributeComparator("getName"));
    
    List<Subject> subjects = subjectDAO.listUnarchived();
    JSONArray subjectsJSON = new JSONArray();
    for (Subject subject : subjects) {
      JSONObject subjectJSON = new JSONObject();
      subjectJSON.put("id", subject.getId());
      subjectJSON.put("name", subject.getName());
      subjectJSON.put("code", subject.getCode());
      subjectJSON.put("educationTypeId", subject.getEducationType() != null ? subject.getEducationType().getId() : null);
      subjectJSON.put("educationTypeCode", subject.getEducationType() != null ? subject.getEducationType().getCode() : null);
      subjectJSON.put("educationTypeName", subject.getEducationType() != null ? subject.getEducationType().getName() : null);
      subjectsJSON.add(subjectJSON);
    }
    setJsDataVariable(pageRequestContext, "subjects", subjectsJSON.toString());
    
    pageRequestContext.getRequest().setAttribute("tags", tagsBuilder.toString());
    pageRequestContext.getRequest().setAttribute("project", project);
    pageRequestContext.getRequest().setAttribute("optionalStudiesLengthTimeUnits", educationalTimeUnits);
    pageRequestContext.getRequest().setAttribute("users", userDAO.listAll());

    pageRequestContext.setIncludeJSP("/templates/projects/viewproject.jsp");
  }

  /**
   * Returns the roles allowed to access this page. Editing projects is available for users with
   * {@link Role#MANAGER} or {@link Role#ADMINISTRATOR} privileges.
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
    return Messages.getInstance().getText(locale, "projects.viewProject.breadcrumb");
  }

}
