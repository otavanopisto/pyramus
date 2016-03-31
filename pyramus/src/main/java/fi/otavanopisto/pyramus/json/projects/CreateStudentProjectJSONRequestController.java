package fi.otavanopisto.pyramus.json.projects;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.I18N.Messages;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.DefaultsDAO;
import fi.otavanopisto.pyramus.dao.base.TagDAO;
import fi.otavanopisto.pyramus.dao.projects.ProjectDAO;
import fi.otavanopisto.pyramus.dao.projects.StudentProjectDAO;
import fi.otavanopisto.pyramus.dao.projects.StudentProjectModuleDAO;
import fi.otavanopisto.pyramus.dao.students.StudentDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.base.CourseOptionality;
import fi.otavanopisto.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.otavanopisto.pyramus.domainmodel.base.Tag;
import fi.otavanopisto.pyramus.domainmodel.projects.Project;
import fi.otavanopisto.pyramus.domainmodel.projects.ProjectModule;
import fi.otavanopisto.pyramus.domainmodel.projects.StudentProject;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class CreateStudentProjectJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext jsonRequestContext) {
    StaffMemberDAO userDAO = DAOFactory.getInstance().getStaffMemberDAO();
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    ProjectDAO projectDAO = DAOFactory.getInstance().getProjectDAO();
    StudentProjectDAO studentProjectDAO = DAOFactory.getInstance().getStudentProjectDAO();
    StudentProjectModuleDAO studentProjectModuleDAO = DAOFactory.getInstance().getStudentProjectModuleDAO();
    TagDAO tagDAO = DAOFactory.getInstance().getTagDAO();
    DefaultsDAO defaultsDAO = DAOFactory.getInstance().getDefaultsDAO();

    User loggedUser = userDAO.findById(jsonRequestContext.getLoggedUserId());

    Long studentId = NumberUtils.createLong(jsonRequestContext.getRequest().getParameter("studentId"));
    Long projectId = NumberUtils.createLong(jsonRequestContext.getRequest().getParameter("projectId"));
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
    
    Student student = studentDAO.findById(studentId);
    Project project = projectId == -1 ? null : projectDAO.findById(projectId);

    String name;
    String description;
    EducationalTimeUnit unit;
    Double units;

    if (project == null) {
      name = Messages.getInstance().getText(jsonRequestContext.getRequest().getLocale(),
          "projects.createStudentProject.newStudentProject");
      description = null;
      unit = defaultsDAO.getDefaults().getBaseTimeUnit();
      units = 0.0;
    } else {
      name = project.getName();
      description = project.getDescription();
      unit = project.getOptionalStudiesLength().getUnit();
      units = project.getOptionalStudiesLength().getUnits();
    }

    StudentProject studentProject = studentProjectDAO.create(student, name, description, units, unit, null, loggedUser, project);
    studentProjectDAO.updateTags(studentProject, tagEntities);

    if (project != null) {
      List<ProjectModule> projectModules = project.getProjectModules();
      for (ProjectModule projectModule : projectModules) {
        studentProjectModuleDAO.create(studentProject, projectModule.getModule(), null,
            CourseOptionality.getOptionality(projectModule.getOptionality().getValue()));
      }
    }
    
    String redirectURL = jsonRequestContext.getRequest().getContextPath() + "/projects/editstudentproject.page?studentproject=" + studentProject.getId();
    String refererAnchor = jsonRequestContext.getRefererAnchor();
    
    if (!StringUtils.isBlank(refererAnchor))
      redirectURL += "#" + refererAnchor;

    jsonRequestContext.setRedirectURL(redirectURL);
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

}
