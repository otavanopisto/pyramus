package fi.otavanopisto.pyramus.json.matriculation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.matriculation.MatriculationExamEnrollmentDAO;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamEnrollment;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class SearchEnrollmentsJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext requestContext) {
    MatriculationExamEnrollmentDAO dao = DAOFactory.getInstance().getMatriculationExamEnrollmentDAO();
    List<MatriculationExamEnrollment> enrollments = dao.listAll();
    
    List<Map<String, Object>> results = new ArrayList<>();
    for (MatriculationExamEnrollment enrollment : enrollments) {
      Map<String, Object> result = new HashMap<>();
      
      result.put("id", enrollment.getId());
      result.put("name", enrollment.getName());
      result.put("email", enrollment.getEmail());
      result.put("state", enrollment.getState());
      results.add(result);
    }
    
    requestContext.addResponseParameter("statusMessage", "");
    requestContext.addResponseParameter("results", results);
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR, UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER };
  }

}
