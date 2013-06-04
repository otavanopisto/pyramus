package fi.pyramus.json.settings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.pyramus.I18N.Messages;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.SchoolDAO;
import fi.pyramus.domainmodel.base.School;
import fi.pyramus.framework.JSONRequestController;
import fi.pyramus.framework.UserRole;
import fi.pyramus.persistence.search.SearchResult;

/**
 * The controller responsible of searching schools.
 * 
 * @see fi.pyramus.views.settings.SearchSchoolsViewController
 */
public class SearchSchoolsJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext requestContext) {
    SchoolDAO schoolDAO = DAOFactory.getInstance().getSchoolDAO();

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

    SearchResult<School> searchResult = schoolDAO.searchSchoolsBasic(resultsPerPage, page, text);

    List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
    List<School> schools = searchResult.getResults();
    for (School school : schools) {
      Map<String, Object> schoolInfo = new HashMap<String, Object>();
      schoolInfo.put("id", school.getId());
      schoolInfo.put("name", school.getName());
      schoolInfo.put("fieldName", school.getField() != null ? school.getField().getName() : "");
      results.add(schoolInfo);
    }

    String statusMessage = "";
    Locale locale = requestContext.getRequest().getLocale();
    if (searchResult.getTotalHitCount() > 0) {
      statusMessage = Messages.getInstance().getText(
          locale,
          "settings.searchSchools.searchStatus",
          new Object[] { searchResult.getFirstResult() + 1, searchResult.getLastResult() + 1,
              searchResult.getTotalHitCount() });
    }
    else {
      statusMessage = Messages.getInstance().getText(locale, "settings.searchSchools.searchStatusNoMatches");
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
