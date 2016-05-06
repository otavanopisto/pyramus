package fi.otavanopisto.pyramus.json.projects;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.projects.StudentProjectDAO;
import fi.otavanopisto.pyramus.domainmodel.projects.StudentProject;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class ArchiveStudentProjectJSONRequestController extends JSONRequestController {
  
  public void process(JSONRequestContext requestContext) {
    StudentProjectDAO studentProjectDAO = DAOFactory.getInstance().getStudentProjectDAO();

    Long studentProjectId = requestContext.getLong("studentProjectId");
    StudentProject studentProject = studentProjectDAO.findById(studentProjectId);
    studentProjectDAO.archive(studentProject);
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

}
