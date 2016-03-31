package fi.otavanopisto.pyramus.json.students;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.projects.ProjectDAO;
import fi.otavanopisto.pyramus.dao.projects.StudentProjectDAO;
import fi.otavanopisto.pyramus.dao.projects.StudentProjectModuleDAO;
import fi.otavanopisto.pyramus.dao.students.StudentDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.base.CourseOptionality;
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

    User loggedUser = userDAO.findById(jsonRequestContext.getLoggedUserId());

    Long studentId = jsonRequestContext.getLong("studentId");
    Long projectId = jsonRequestContext.getLong("projectId");
    CourseOptionality projectOptionality = (CourseOptionality) jsonRequestContext.getEnum("optionality", CourseOptionality.class);

    Student student = studentDAO.findById(studentId);
    Project project = projectDAO.findById(projectId);

    StudentProject studentProject = studentProjectDAO.create(student, project.getName(), project.getDescription(), 
        project.getOptionalStudiesLength().getUnits(), project.getOptionalStudiesLength().getUnit(), projectOptionality, loggedUser, project);
    
    Set<Tag> tags = new HashSet<Tag>();
    for (Tag tag : project.getTags()) {
      tags.add(tag);
    }
    studentProjectDAO.updateTags(studentProject, tags);
    
    List<ProjectModule> projectModules = project.getProjectModules();
    for (ProjectModule projectModule : projectModules) {
      studentProjectModuleDAO.create(studentProject, projectModule.getModule(), null,
          CourseOptionality.getOptionality(projectModule.getOptionality().getValue()));
    }
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

}
