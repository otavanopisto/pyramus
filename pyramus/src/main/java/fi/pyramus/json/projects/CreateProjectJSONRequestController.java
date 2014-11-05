package fi.pyramus.json.projects;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.EducationalTimeUnitDAO;
import fi.pyramus.dao.base.TagDAO;
import fi.pyramus.dao.modules.ModuleDAO;
import fi.pyramus.dao.projects.ProjectDAO;
import fi.pyramus.dao.projects.ProjectModuleDAO;
import fi.pyramus.dao.users.StaffMemberDAO;
import fi.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.pyramus.domainmodel.base.Tag;
import fi.pyramus.domainmodel.modules.Module;
import fi.pyramus.domainmodel.projects.Project;
import fi.pyramus.domainmodel.projects.ProjectModuleOptionality;
import fi.pyramus.domainmodel.users.User;
import fi.pyramus.framework.JSONRequestController;
import fi.pyramus.framework.UserRole;

public class CreateProjectJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext jsonRequestContext) {
    StaffMemberDAO userDAO = DAOFactory.getInstance().getStaffMemberDAO();
    ModuleDAO moduleDAO = DAOFactory.getInstance().getModuleDAO();
    ProjectDAO projectDAO = DAOFactory.getInstance().getProjectDAO();
    ProjectModuleDAO projectModuleDAO = DAOFactory.getInstance().getProjectModuleDAO();
    EducationalTimeUnitDAO educationalTimeUnitDAO = DAOFactory.getInstance().getEducationalTimeUnitDAO();
    TagDAO tagDAO = DAOFactory.getInstance().getTagDAO();

    // Project

    String name = jsonRequestContext.getRequest().getParameter("name");
    String description = jsonRequestContext.getRequest().getParameter("description");
    String tagsText = jsonRequestContext.getString("tags");
    
    Set<Tag> tagEntities = new HashSet<Tag>();
    if (!StringUtils.isBlank(tagsText)) {
      List<String> tags = Arrays.asList(tagsText.split("[\\ ,]"));
      for (String tag : tags) {
        if (!StringUtils.isBlank(tag)) {
          Tag tagEntity = tagDAO.findByText(tag.trim());
          if (tagEntity == null)
            tagEntity = tagDAO.create(tag);
          tagEntities.add(tagEntity);
        }
      }
    }
    
    User loggedUser = userDAO.findById(jsonRequestContext.getLoggedUserId());
    Long optionalStudiesLengthTimeUnitId = NumberUtils.createLong(jsonRequestContext.getRequest().getParameter(
        "optionalStudiesLengthTimeUnit"));
    EducationalTimeUnit optionalStudiesLengthTimeUnit = educationalTimeUnitDAO.findById(optionalStudiesLengthTimeUnitId);
    Double optionalStudiesLength = NumberUtils.createDouble(jsonRequestContext.getRequest().getParameter(
        "optionalStudiesLength"));
    Project project = projectDAO.create(name, description, optionalStudiesLength,
        optionalStudiesLengthTimeUnit, loggedUser);
    
    // Tags
    
    projectDAO.updateTags(project, tagEntities);

    // Project modules

    int rowCount = NumberUtils.createInteger(
        jsonRequestContext.getRequest().getParameter("modulesTable.rowCount")).intValue();
    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "modulesTable." + i;
      Long moduleId = NumberUtils.createLong(jsonRequestContext.getRequest().getParameter(colPrefix + ".moduleId"));
      Module module = moduleDAO.findById(moduleId);
      int optionality = new Integer(jsonRequestContext.getRequest().getParameter(colPrefix + ".optionality"))
          .intValue();
      projectModuleDAO.create(project, module, ProjectModuleOptionality.getOptionality(optionality));
    }
    
    String redirectURL = jsonRequestContext.getRequest().getContextPath() + "/projects/editproject.page?project=" + project.getId();
    String refererAnchor = jsonRequestContext.getRefererAnchor();
    
    if (!StringUtils.isBlank(refererAnchor))
      redirectURL += "#" + refererAnchor;

    jsonRequestContext.setRedirectURL(redirectURL);
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

}
