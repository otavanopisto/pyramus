package fi.pyramus.json.projects;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.projects.ProjectDAO;
import fi.pyramus.domainmodel.projects.Project;
import fi.pyramus.framework.JSONRequestController;
import fi.pyramus.framework.UserRole;

public class ArchiveProjectJSONRequestController extends JSONRequestController {
  
  public void process(JSONRequestContext requestContext) {
    ProjectDAO projectDAO = DAOFactory.getInstance().getProjectDAO();

    Long projectId = requestContext.getLong("projectId");
    Project project = projectDAO.findById(projectId);
    projectDAO.archive(project);
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

}
