package fi.otavanopisto.pyramus.json.matriculation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.matriculation.MatriculationExamEnrollmentDAO;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamEnrollment;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamEnrollmentState;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class SearchEnrollmentsJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext requestContext) {
    Integer resultsPerPage = NumberUtils.createInteger(requestContext.getRequest().getParameter("maxResults"));
    if (resultsPerPage == null) {
      resultsPerPage = 10;
    }

    Integer page = NumberUtils.createInteger(requestContext.getRequest().getParameter("page"));
    if (page == null) {
      page = 0;
    }
    
    Boolean below20courses = requestContext.getBoolean("below20courses");
    String stateStr = requestContext.getString("state");
    MatriculationExamEnrollmentState state = StringUtils.isNotBlank(stateStr) ? MatriculationExamEnrollmentState.valueOf(stateStr) : null;
    
    MatriculationExamEnrollmentDAO dao = DAOFactory.getInstance().getMatriculationExamEnrollmentDAO();
    List<MatriculationExamEnrollment> enrollments = dao.listBy(state, BooleanUtils.isTrue(below20courses), page * resultsPerPage, resultsPerPage);
    
    List<Map<String, Object>> results = new ArrayList<>();
    for (MatriculationExamEnrollment enrollment : enrollments) {
      Map<String, Object> result = new HashMap<>();
      
      result.put("id", enrollment.getId());
      result.put("name", enrollment.getName());
      result.put("email", enrollment.getEmail());
      result.put("state", enrollment.getState());
      result.put("numMandatoryCourses", enrollment.getNumMandatoryCourses());
      result.put("guider", enrollment.getGuider());
      result.put("approvedByGuider", enrollment.isApprovedByGuider());
      
      results.add(result);
    }
    
    long count = dao.count();
    
    requestContext.addResponseParameter("statusMessage", "");
    requestContext.addResponseParameter("results", results);
    requestContext.addResponseParameter("pages", Math.ceil((double)count / resultsPerPage));
    requestContext.addResponseParameter("page", page);
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR, UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER };
  }

}
