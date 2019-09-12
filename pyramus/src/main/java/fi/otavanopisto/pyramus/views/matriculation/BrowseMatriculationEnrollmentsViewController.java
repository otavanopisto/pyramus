package fi.otavanopisto.pyramus.views.matriculation;

import java.util.List;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.matriculation.MatriculationExamDAO;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExam;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class BrowseMatriculationEnrollmentsViewController extends PyramusViewController {
  
  public void process(PageRequestContext pageRequestContext) {
    MatriculationExamDAO matriculationExamDAO = DAOFactory.getInstance().getMatriculationExamDAO();
    
    List<MatriculationExam> exams = matriculationExamDAO.listAll();
    pageRequestContext.getRequest().setAttribute("exams", exams);
    
    pageRequestContext.setIncludeJSP("/templates/matriculation/management-browse-enrollments.jsp");
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR, UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER };
  }

}
