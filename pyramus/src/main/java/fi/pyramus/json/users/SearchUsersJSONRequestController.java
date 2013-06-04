package fi.pyramus.json.users;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.pyramus.I18N.Messages;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.users.UserDAO;
import fi.pyramus.domainmodel.users.Role;
import fi.pyramus.domainmodel.users.User;
import fi.pyramus.framework.JSONRequestController;
import fi.pyramus.framework.UserRole;
import fi.pyramus.persistence.search.SearchResult;

public class SearchUsersJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext requestContext) {
    UserDAO userDAO = DAOFactory.getInstance().getUserDAO();

    Integer resultsPerPage = requestContext.getInteger("maxResults");
    if (resultsPerPage == null) {
      resultsPerPage = 10;
    }

    Integer page = requestContext.getInteger("page");
    if (page == null) {
      page = 0;
    }
    
    String text = requestContext.getString("text");
    Role role = (Role) requestContext.getEnum("role", Role.class);

    SearchResult<User> searchResult = userDAO.searchUsers(resultsPerPage, page, text, text, text, text, role);
    List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
    List<User> users = searchResult.getResults();
    for (User user : users) {
      Map<String, Object> info = new HashMap<String, Object>();
      info.put("id", user.getId());
      info.put("firstName", user.getFirstName());
      info.put("lastName", user.getLastName());
      results.add(info);
    }

    String statusMessage = "";
    Locale locale = requestContext.getRequest().getLocale();
    if (searchResult.getTotalHitCount() > 0) {
      statusMessage = Messages.getInstance().getText(
          locale,
          "users.searchUsers.searchStatus",
          new Object[] { searchResult.getFirstResult() + 1, searchResult.getLastResult() + 1,
              searchResult.getTotalHitCount() });
    }
    else {
      statusMessage = Messages.getInstance().getText(locale, "students.searchStudents.searchStatusNoMatches");
    }
    requestContext.addResponseParameter("results", results);
    requestContext.addResponseParameter("statusMessage", statusMessage);
    requestContext.addResponseParameter("pages", searchResult.getPages());
    requestContext.addResponseParameter("page", searchResult.getPage());
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.GUEST, UserRole.USER, UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

}
