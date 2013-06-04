package fi.pyramus.json.projects;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.projects.StudentProjectDAO;
import fi.pyramus.domainmodel.projects.StudentProject;
import fi.pyramus.framework.JSONRequestController;
import fi.pyramus.framework.UserRole;

public class ArchiveStudentProjectJSONRequestController extends JSONRequestController {
  
  public void process(JSONRequestContext requestContext) {
    StudentProjectDAO studentProjectDAO = DAOFactory.getInstance().getStudentProjectDAO();

    Long studentProjectId = requestContext.getLong("studentProjectId");
    StudentProject studentProject = studentProjectDAO.findById(studentProjectId);
    studentProjectDAO.archive(studentProject);
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

}
