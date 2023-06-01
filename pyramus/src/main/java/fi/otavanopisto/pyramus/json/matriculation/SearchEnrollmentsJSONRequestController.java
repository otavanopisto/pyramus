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
import fi.otavanopisto.pyramus.dao.matriculation.MatriculationExamDAO;
import fi.otavanopisto.pyramus.dao.matriculation.MatriculationExamEnrollmentDAO;
import fi.otavanopisto.pyramus.dao.matriculation.MatriculationExamEnrollmentDAO.MatriculationExamEnrollmentSorting;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExam;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamEnrollment;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExamEnrollmentState;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class SearchEnrollmentsJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext requestContext) {
    MatriculationExamDAO examDAO = DAOFactory.getInstance().getMatriculationExamDAO();
    MatriculationExamEnrollmentDAO dao = DAOFactory.getInstance().getMatriculationExamEnrollmentDAO();

    Integer resultsPerPage = NumberUtils.createInteger(requestContext.getRequest().getParameter("maxResults"));
    if (resultsPerPage == null) {
      resultsPerPage = 10;
    }

    Integer page = NumberUtils.createInteger(requestContext.getRequest().getParameter("page"));
    if (page == null) {
      page = 0;
    }
    
    Long examId = requestContext.getLong("examId");
    MatriculationExam exam = examId != null ? examDAO.findById(examId) : null;
    
    String nameQuery = requestContext.getString("name");
    Boolean below20courses = requestContext.getBoolean("below20courses");
    String stateStr = requestContext.getString("state");
    String sortingStr = requestContext.getString("sort");
    MatriculationExamEnrollmentState state = StringUtils.isNotBlank(stateStr) ? MatriculationExamEnrollmentState.valueOf(stateStr) : null;
    MatriculationExamEnrollmentSorting sorting = StringUtils.isNotBlank(sortingStr) ? MatriculationExamEnrollmentSorting.valueOf(sortingStr) : null;

    List<MatriculationExamEnrollment> enrollments = dao.listBy(nameQuery, exam, state, BooleanUtils.isTrue(below20courses), page * resultsPerPage, resultsPerPage, sorting);
    
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
