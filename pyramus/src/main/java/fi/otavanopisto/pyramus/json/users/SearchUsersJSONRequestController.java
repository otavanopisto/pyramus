package fi.otavanopisto.pyramus.json.users;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.I18N.Messages;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.persistence.search.SearchResult;

public class SearchUsersJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext requestContext) {
    StaffMemberDAO userDAO = DAOFactory.getInstance().getStaffMemberDAO();

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
    Set<Role> roles = null;
    
    if (role != null) {
      roles = Set.of(role);
    }

    SearchResult<StaffMember> searchResult = userDAO.searchUsers(resultsPerPage, page, text, text, text, text, roles);
    List<Map<String, Object>> results = new ArrayList<>();
    List<StaffMember> users = searchResult.getResults();
    for (User user : users) {
      Map<String, Object> info = new HashMap<>();
      info.put("id", user.getId());
      info.put("firstName", user.getFirstName());
      info.put("lastName", user.getLastName());
      results.add(info);
    }

    String statusMessage;
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
    return new UserRole[] { UserRole.GUEST, UserRole.USER, UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

}
