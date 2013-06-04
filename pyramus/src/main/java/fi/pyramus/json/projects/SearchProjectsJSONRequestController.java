package fi.pyramus.json.projects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.pyramus.I18N.Messages;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.projects.ProjectDAO;
import fi.pyramus.domainmodel.projects.Project;
import fi.pyramus.framework.JSONRequestController;
import fi.pyramus.framework.UserRole;
import fi.pyramus.persistence.search.SearchResult;

/**
 * The controller responsible of searching projects.
 * 
 * @see fi.pyramus.views.modules.SearchProjectsViewController
 */
public class SearchProjectsJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext requestContext) {
    ProjectDAO projectDAO = DAOFactory.getInstance().getProjectDAO();

    Integer resultsPerPage = NumberUtils.createInteger(requestContext.getRequest().getParameter("maxResults"));
    if (resultsPerPage == null) {
      resultsPerPage = 10;
    }

    Integer page = NumberUtils.createInteger(requestContext.getRequest().getParameter("page"));
    if (page == null) {
      page = 0;
    }

    // Gather the search terms

    String text = requestContext.getRequest().getParameter("text");

    // Search via the DAO object

    SearchResult<Project> searchResult = projectDAO.searchProjectsBasic(resultsPerPage, page, text);

    List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
    List<Project> projects = searchResult.getResults();
    for (Project project : projects) {
      Map<String, Object> projectInfo = new HashMap<String, Object>();
      projectInfo.put("id", project.getId());
      projectInfo.put("name", project.getName());
      results.add(projectInfo);
    }

    String statusMessage = "";
    Locale locale = requestContext.getRequest().getLocale();
    if (searchResult.getTotalHitCount() > 0) {
      statusMessage = Messages.getInstance().getText(
          locale,
          "projects.searchProjects.searchStatus",
          new Object[] { searchResult.getFirstResult() + 1, searchResult.getLastResult() + 1,
              searchResult.getTotalHitCount() });
    }
    else {
      statusMessage = Messages.getInstance().getText(locale, "projects.searchProjects.searchStatusNoMatches");
    }
    requestContext.addResponseParameter("results", results);
    requestContext.addResponseParameter("statusMessage", statusMessage);
    requestContext.addResponseParameter("pages", searchResult.getPages());
    requestContext.addResponseParameter("page", searchResult.getPage());
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.EVERYONE };
  }

}
