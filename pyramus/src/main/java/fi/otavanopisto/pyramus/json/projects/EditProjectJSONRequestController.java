package fi.otavanopisto.pyramus.json.projects;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibernate.StaleObjectStateException;

import fi.internetix.smvc.Severity;
import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.I18N.Messages;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.EducationalTimeUnitDAO;
import fi.otavanopisto.pyramus.dao.base.SubjectDAO;
import fi.otavanopisto.pyramus.dao.base.TagDAO;
import fi.otavanopisto.pyramus.dao.modules.ModuleDAO;
import fi.otavanopisto.pyramus.dao.projects.ProjectDAO;
import fi.otavanopisto.pyramus.dao.projects.ProjectModuleDAO;
import fi.otavanopisto.pyramus.dao.projects.ProjectSubjectCourseDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.otavanopisto.pyramus.domainmodel.base.Subject;
import fi.otavanopisto.pyramus.domainmodel.base.Tag;
import fi.otavanopisto.pyramus.domainmodel.modules.Module;
import fi.otavanopisto.pyramus.domainmodel.projects.Project;
import fi.otavanopisto.pyramus.domainmodel.projects.ProjectModule;
import fi.otavanopisto.pyramus.domainmodel.projects.ProjectModuleOptionality;
import fi.otavanopisto.pyramus.domainmodel.projects.ProjectSubjectCourse;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class EditProjectJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext jsonRequestContext) {
    StaffMemberDAO userDAO = DAOFactory.getInstance().getStaffMemberDAO();
    ModuleDAO moduleDAO = DAOFactory.getInstance().getModuleDAO();
    ProjectDAO projectDAO = DAOFactory.getInstance().getProjectDAO();
    ProjectModuleDAO projectModuleDAO = DAOFactory.getInstance().getProjectModuleDAO();
    EducationalTimeUnitDAO educationalTimeUnitDAO = DAOFactory.getInstance().getEducationalTimeUnitDAO();
    TagDAO tagDAO = DAOFactory.getInstance().getTagDAO();

    // Project

    Long projectId = jsonRequestContext.getLong("project");
    Project project = projectDAO.findById(projectId);
    
    // Version check
    Long version = jsonRequestContext.getLong("version"); 
    if (!project.getVersion().equals(version))
      throw new StaleObjectStateException(Project.class.getName(), project.getId());
    
    String name = jsonRequestContext.getRequest().getParameter("name");
    String description = jsonRequestContext.getRequest().getParameter("description");
    User user = userDAO.findById(jsonRequestContext.getLoggedUserId());
    Long optionalStudiesLengthTimeUnitId = jsonRequestContext.getLong("optionalStudiesLengthTimeUnit");
    EducationalTimeUnit optionalStudiesLengthTimeUnit = educationalTimeUnitDAO.findById(optionalStudiesLengthTimeUnitId);
    Double optionalStudiesLength = jsonRequestContext.getDouble("optionalStudiesLength");
    String tagsText = jsonRequestContext.getString("tags");
    
    Set<Tag> tagEntities = new HashSet<>();
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
    
    if (optionalStudiesLength == null) {
      Messages messages = Messages.getInstance();
      Locale locale = jsonRequestContext.getRequest().getLocale();

      jsonRequestContext.addMessage(Severity.ERROR, messages.getText(locale, "projects.editProject.projectOptionalStudiesLengthNotDefined"));
    }
    
    projectDAO.update(project, name, description, optionalStudiesLength, optionalStudiesLengthTimeUnit, user);

    // Tags

    projectDAO.updateTags(project, tagEntities);

    // Project modules

    Set<Long> existingIds = new HashSet<>();
    int rowCount = jsonRequestContext.getInteger("modulesTable.rowCount").intValue();
    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "modulesTable." + i;
      int optionality = new Integer(jsonRequestContext.getRequest().getParameter(colPrefix + ".optionality"))
          .intValue();
      Long projectModuleId = jsonRequestContext.getLong(colPrefix + ".projectModuleId");
      if (projectModuleId == -1) {
        Long moduleId = jsonRequestContext.getLong(colPrefix + ".moduleId");
        Module module = moduleDAO.findById(moduleId);
        projectModuleId = projectModuleDAO.create(project, module,
            ProjectModuleOptionality.getOptionality(optionality)).getId();
      }
      else {
        projectModuleDAO.update(projectModuleDAO.findById(projectModuleId), ProjectModuleOptionality
            .getOptionality(optionality));
      }
      existingIds.add(projectModuleId);
    }
    List<ProjectModule> projectModules = projectModuleDAO.listByProject(project);
    for (ProjectModule projectModule : projectModules) {
      if (!existingIds.contains(projectModule.getId())) {
        projectModuleDAO.delete(projectModule);
      }
    }

    handleSubjectCourses(jsonRequestContext, project);
    
    jsonRequestContext.setRedirectURL(jsonRequestContext.getReferer(true));
  }

  private void handleSubjectCourses(JSONRequestContext jsonRequestContext, Project project) {
    SubjectDAO subjectDAO = DAOFactory.getInstance().getSubjectDAO();
    ProjectSubjectCourseDAO projectSubjectCourseDAO = DAOFactory.getInstance().getProjectSubjectCourseDAO();

    Set<Long> existingIds = new HashSet<>();
    int rowCount = jsonRequestContext.getInteger("subjectCoursesTable.rowCount").intValue();
    for (int i = 0; i < rowCount; i++) {
      String colPrefix = "subjectCoursesTable." + i;
      int optionality = new Integer(jsonRequestContext.getRequest().getParameter(colPrefix + ".optionality"))
          .intValue();
      Long projectSubjectCourseId = jsonRequestContext.getLong(colPrefix + ".projectSubjectCourseId");
      if (projectSubjectCourseId == -1) {
        Long subjectId = jsonRequestContext.getLong(colPrefix + ".subjectId");
        Subject subject = subjectDAO.findById(subjectId);
        Integer courseNumber = jsonRequestContext.getInteger(colPrefix + ".courseNumber");
        projectSubjectCourseId = projectSubjectCourseDAO.create(project, subject, courseNumber,
            ProjectModuleOptionality.getOptionality(optionality)).getId();
      }
      else {
        projectSubjectCourseDAO.update(projectSubjectCourseDAO.findById(projectSubjectCourseId), ProjectModuleOptionality
            .getOptionality(optionality));
      }
      existingIds.add(projectSubjectCourseId);
    }
    
    List<ProjectSubjectCourse> projectSubjectCourses = projectSubjectCourseDAO.listByProject(project);
    for (ProjectSubjectCourse projectSubjectCourse : projectSubjectCourses) {
      if (!existingIds.contains(projectSubjectCourse.getId())) {
        projectSubjectCourseDAO.delete(projectSubjectCourse);
      }
    }
  }
  
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

}
