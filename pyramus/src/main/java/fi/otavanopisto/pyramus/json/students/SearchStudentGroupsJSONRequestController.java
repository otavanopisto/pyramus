package fi.otavanopisto.pyramus.json.students;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.I18N.Messages;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.students.StudentGroupDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.students.StudentGroup;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.persistence.search.SearchResult;

/**
 * The controller responsible of searching student groups.
 * 
 * @see fi.otavanopisto.pyramus.views.students.SearchStudentGroupsViewController
 */
public class SearchStudentGroupsJSONRequestController extends JSONRequestController {

  /**
   * Processes the request to search student groups.
   * 
   * @param jsonRequestContext The JSON request context
   */
  public void process(JSONRequestContext requestContext) {
    StaffMemberDAO userDAO = DAOFactory.getInstance().getStaffMemberDAO();
    StudentGroupDAO studentGroupDAO = DAOFactory.getInstance().getStudentGroupDAO();

    // Determine the number of results shown per page. If not defined, default to ten results per page

    Integer resultsPerPage = NumberUtils.createInteger(requestContext.getRequest().getParameter("maxResults"));
    if (resultsPerPage == null)
      resultsPerPage = 10;

    // Determine the result page to be shown. If not defined, default to the first page

    Integer page = NumberUtils.createInteger(requestContext.getRequest().getParameter("page"));
    if (page == null) {
      page = 0;
    }

    SearchResult<StudentGroup> searchResult;
    
    if ("advanced".equals(requestContext.getRequest().getParameter("activeTab"))) {
      String name = requestContext.getRequest().getParameter("name");
      String description = requestContext.getRequest().getParameter("description");
      
      String tags = requestContext.getString("tags");
      if (!StringUtils.isBlank(tags))
        tags = tags.replace(',', ' ');
      
      Long userId = requestContext.getLong("user");
      User user = null;
      if ((userId != null ? userId.intValue() : -1) > -1)
        user = userDAO.findById(userId);

      
      Date timeframeStart = null;
      String value = requestContext.getRequest().getParameter("timeframeStart");
      if (NumberUtils.isNumber(value)) {
        timeframeStart = new Date(NumberUtils.createLong(value));
      }

      Date timeframeEnd = null;
      value = requestContext.getRequest().getParameter("timeframeEnd");
      if (NumberUtils.isNumber(value)) {
        timeframeEnd = new Date(NumberUtils.createLong(value));
      }

      searchResult = studentGroupDAO.searchStudentGroups(resultsPerPage, page, name, tags, description, user, 
          timeframeStart, timeframeEnd, true);
    }
    else {
      String text = requestContext.getRequest().getParameter("text");
      searchResult = studentGroupDAO.searchStudentGroupsBasic(resultsPerPage, page, text); 
    }

    List<Map<String, Object>> results = new ArrayList<>();

    List<StudentGroup> studentGroups = searchResult.getResults();
    for (StudentGroup studentGroup : studentGroups) {
      Map<String, Object> info = new HashMap<>();
      info.put("id", studentGroup.getId());
      info.put("name", studentGroup.getName());
      if (studentGroup.getBeginDate() != null) {
        info.put("beginDate", studentGroup.getBeginDate().getTime());
      }
      results.add(info);
    }

    String statusMessage;
    Locale locale = requestContext.getRequest().getLocale();
    if (searchResult.getTotalHitCount() > 0) {
      statusMessage = Messages.getInstance().getText(
          locale,
          "students.searchStudentGroups.searchStatus",
          new Object[] { searchResult.getFirstResult() + 1, searchResult.getLastResult() + 1,
              searchResult.getTotalHitCount() });
    }
    else {
      statusMessage = Messages.getInstance().getText(locale, "students.searchStudentGroups.searchStatusNoMatches");
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
