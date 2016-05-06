package fi.otavanopisto.pyramus.json.projects;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.projects.ProjectDAO;
import fi.otavanopisto.pyramus.domainmodel.projects.Project;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class ArchiveProjectJSONRequestController extends JSONRequestController {
  
  public void process(JSONRequestContext requestContext) {
    ProjectDAO projectDAO = DAOFactory.getInstance().getProjectDAO();

    Long projectId = requestContext.getLong("projectId");
    Project project = projectDAO.findById(projectId);
    projectDAO.archive(project);
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

}
