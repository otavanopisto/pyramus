package fi.otavanopisto.pyramus.json.applications;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.applications.ApplicationUtils;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.application.ApplicationDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.application.Application;
import fi.otavanopisto.pyramus.domainmodel.application.ApplicationState;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.persistence.search.SearchResult;

public class SearchApplicationsJSONRequestController extends JSONRequestController {

  private static final Logger logger = Logger.getLogger(SearchApplicationsJSONRequestController.class.getName());

  public void process(JSONRequestContext requestContext) {
    StaffMemberDAO staffMemberDAO = DAOFactory.getInstance().getStaffMemberDAO();
    StaffMember staffMember = staffMemberDAO.findById(requestContext.getLoggedUserId());
    
    ApplicationDAO applicationDAO = DAOFactory.getInstance().getApplicationDAO();

    Integer resultsPerPage = NumberUtils.createInteger(requestContext.getRequest().getParameter("maxResults"));
    if (resultsPerPage == null) {
      resultsPerPage = 10;
    }

    Integer page = NumberUtils.createInteger(requestContext.getRequest().getParameter("page"));
    if (page == null) {
      page = 0;
    }
    
    String applicantName = requestContext.getString("applicantName");
    Set<String> lines = new HashSet<>();
    String line = requestContext.getString("line");
    if (!StringUtils.isEmpty(line)) {
      if (!ApplicationUtils.hasLineAccess(staffMember, line)) {
        try {
          requestContext.getResponse().sendError(HttpServletResponse.SC_FORBIDDEN);
        }
        catch (IOException e) {
          logger.log(Level.SEVERE, "Unable to serve error response", e);
        }
        return;
      }
      lines.add(line);
    }
    else {
      lines = ApplicationUtils.listAccessibleLines(staffMember);
    }
    String stateStr = requestContext.getString("state");
    ApplicationState state = null;
    if (stateStr != null) {
      state = ApplicationState.valueOf(stateStr);
    }
    
    SearchResult<Application> searchResult = applicationDAO.searchApplications(resultsPerPage, page, applicantName, lines, state, Boolean.TRUE);

    List<Map<String, Object>> results = new ArrayList<>();
    List<Application> applications = searchResult.getResults();
    
    Collections.sort(applications, new Comparator<Application>() {
      @Override
      public int compare(Application o1, Application o2) {
        Date d1 = ApplicationUtils.getLatest(o1.getCreated(), o1.getApplicantLastModified(), o1.getLastModified());
        Date d2 = ApplicationUtils.getLatest(o2.getCreated(), o2.getApplicantLastModified(), o2.getLastModified());
        return d1 == null ? 1 : d2 == null ? -1 : d2.compareTo(d1);
      }
    });
    
    
    for (Application application : applications) {
      Map<String, Object> applicationInfo = new HashMap<>();
      applicationInfo.put("id", application.getId());
      applicationInfo.put("name", String.format("%s, %s", application.getLastName(), application.getFirstName()));
      applicationInfo.put("email", application.getEmail());
      applicationInfo.put("line", application.getLine());
      applicationInfo.put("state", application.getState());
      Date date = ApplicationUtils.getLatest(application.getCreated(), application.getApplicantLastModified(), application.getLastModified());
      applicationInfo.put("date", date == null ? null : date.getTime());
      applicationInfo.put("handler", application.getHandler() == null ? null : application.getHandler().getFullName());
      results.add(applicationInfo);
    }

    String statusMessage = searchResult.getTotalHitCount() > 0
        ? String.format("Näytetään %d - %d yhteensä %d osumasta",
            searchResult.getFirstResult() + 1,
            searchResult.getLastResult() + 1,
            searchResult.getTotalHitCount())
        : "Ei tuloksia";

    requestContext.addResponseParameter("results", results);
    requestContext.addResponseParameter("statusMessage", statusMessage);
    requestContext.addResponseParameter("pages", searchResult.getPages());
    requestContext.addResponseParameter("page", searchResult.getPage());
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR, UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER };
  }

}
