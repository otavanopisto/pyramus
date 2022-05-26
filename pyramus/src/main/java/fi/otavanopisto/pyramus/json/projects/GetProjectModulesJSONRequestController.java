package fi.otavanopisto.pyramus.json.projects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.projects.ProjectDAO;
import fi.otavanopisto.pyramus.domainmodel.projects.Project;
import fi.otavanopisto.pyramus.domainmodel.projects.ProjectModule;
import fi.otavanopisto.pyramus.domainmodel.projects.ProjectSubjectCourse;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

/**
 * The controller returning a list of all modules in a project. 
 */
public class GetProjectModulesJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext jsonRequestContext) {
    ProjectDAO projectDAO = DAOFactory.getInstance().getProjectDAO();

    Long projectId = NumberUtils.createLong(jsonRequestContext.getRequest().getParameter("project"));
    Project project = projectDAO.findById(projectId);

    List<Map<String, Object>> projectModules = new ArrayList<>();
    for (ProjectModule projectModule : project.getProjectModules()) {
      Map<String, Object> moduleInfo = new HashMap<>();
      moduleInfo.put("id", projectModule.getId());
      moduleInfo.put("moduleId", projectModule.getModule().getId());
      moduleInfo.put("name", projectModule.getModule().getName());
      moduleInfo.put("optionality", projectModule.getOptionality().getValue());
      projectModules.add(moduleInfo);
    }

    List<Map<String, Object>> projectSubjectCourses = new ArrayList<>();
    for (ProjectSubjectCourse projectSubjectCourse : project.getProjectSubjectCourses()) {
      Map<String, Object> moduleInfo = new HashMap<>();
      moduleInfo.put("id", projectSubjectCourse.getId());
      moduleInfo.put("subjectId", projectSubjectCourse.getSubject().getId());
      moduleInfo.put("courseNumber", projectSubjectCourse.getCourseNumber());
      moduleInfo.put("optionality", projectSubjectCourse.getOptionality().getValue());
      projectSubjectCourses.add(moduleInfo);
    }

    jsonRequestContext.addResponseParameter("projectModules", projectModules);
    jsonRequestContext.addResponseParameter("projectSubjectCourses", projectSubjectCourses);
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

}
