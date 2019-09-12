package fi.otavanopisto.pyramus.views.matriculation;

import java.util.List;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.matriculation.MatriculationExamDAO;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExam;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class BrowseMatriculationExamsViewController extends PyramusViewController {
  
  public void process(PageRequestContext pageRequestContext) {
    MatriculationExamDAO examDAO = DAOFactory.getInstance().getMatriculationExamDAO();
    
    List<MatriculationExam> exams = examDAO.listAll();
    
    JSONArray jsonMatriculationExams = new JSONArray();
    for (MatriculationExam exam : exams) {
      JSONObject obj = new JSONObject();
      obj.put("id", exam.getId());
      obj.put("examYear", exam.getExamYear());
      obj.put("examTerm", exam.getExamTerm());
      obj.put("starts", exam.getStarts() != null ? exam.getStarts().getTime() : null);
      obj.put("ends", exam.getEnds() != null ? exam.getEnds().getTime() : null);
      jsonMatriculationExams.add(obj);
    }
    setJsDataVariable(pageRequestContext, "matriculationExams", jsonMatriculationExams.toString());
    
    pageRequestContext.setIncludeJSP("/templates/matriculation/management-browse-exams.jsp");
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR, UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER };
  }

}
