package fi.pyramus.json.projects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.projects.ProjectDAO;
import fi.pyramus.domainmodel.projects.Project;
import fi.pyramus.domainmodel.projects.ProjectModule;
import fi.pyramus.framework.JSONRequestController;
import fi.pyramus.framework.UserRole;

/**
 * The controller returning a list of all modules in a project. 
 */
public class GetProjectModulesJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext jsonRequestContext) {
    ProjectDAO projectDAO = DAOFactory.getInstance().getProjectDAO();

    Long projectId = NumberUtils.createLong(jsonRequestContext.getRequest().getParameter("project"));
    Project project = projectDAO.findById(projectId);

    List<Map<String, Object>> projectModules = new ArrayList<Map<String,Object>>();
    for (ProjectModule projectModule : project.getProjectModules()) {
      Map<String, Object> moduleInfo = new HashMap<String, Object>();
      moduleInfo.put("id", projectModule.getId());
      moduleInfo.put("moduleId", projectModule.getModule().getId());
      moduleInfo.put("name", projectModule.getModule().getName());
      moduleInfo.put("optionality", projectModule.getOptionality().getValue());
      projectModules.add(moduleInfo);
    }

    jsonRequestContext.addResponseParameter("projectModules", projectModules);
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR  };
  }

}
