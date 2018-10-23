package fi.otavanopisto.pyramus.views.matriculation;

import java.util.Date;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.matriculation.MatriculationExamDAO;
import fi.otavanopisto.pyramus.domainmodel.matriculation.MatriculationExam;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class MatriculationExamSettingsViewController extends PyramusViewController {
  
  public void process(PageRequestContext pageRequestContext) {
    switch (pageRequestContext.getRequest().getMethod()) {
    case "GET":
      doGet(pageRequestContext);
      break;
    case "POST":
      doPost(pageRequestContext);
      break;
    }
  }
  
  private void doGet(PageRequestContext pageRequestContext) {
    MatriculationExamDAO dao = DAOFactory.getInstance().getMatriculationExamDAO();
    pageRequestContext.setIncludeJSP("/templates/matriculation/management-enrollment-settings.jsp");
    MatriculationExam exam = dao.get();
    if (exam != null) {
      pageRequestContext.getRequest().setAttribute("starts", exam.getStarts().getTime());
      pageRequestContext.getRequest().setAttribute("ends", exam.getEnds().getTime());
    }
  }
  
  private void doPost(PageRequestContext pageRequestContext) {
    MatriculationExamDAO dao = DAOFactory.getInstance().getMatriculationExamDAO();
    Date starts = pageRequestContext.getDate("starts");
    Date ends = pageRequestContext.getDate("ends");
    dao.createOrUpdate(starts, ends);
    pageRequestContext.setRedirectURL(pageRequestContext.getRequest().getRequestURI());
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR, UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER };
  }

}
