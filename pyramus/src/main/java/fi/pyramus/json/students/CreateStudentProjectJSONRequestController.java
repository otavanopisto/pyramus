package fi.pyramus.json.students;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.projects.ProjectDAO;
import fi.pyramus.dao.projects.StudentProjectDAO;
import fi.pyramus.dao.projects.StudentProjectModuleDAO;
import fi.pyramus.dao.students.StudentDAO;
import fi.pyramus.dao.users.UserDAO;
import fi.pyramus.domainmodel.base.CourseOptionality;
import fi.pyramus.domainmodel.base.Tag;
import fi.pyramus.domainmodel.projects.Project;
import fi.pyramus.domainmodel.projects.ProjectModule;
import fi.pyramus.domainmodel.projects.StudentProject;
import fi.pyramus.domainmodel.students.Student;
import fi.pyramus.domainmodel.users.User;
import fi.pyramus.framework.JSONRequestController;
import fi.pyramus.framework.UserRole;

public class CreateStudentProjectJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext jsonRequestContext) {
    UserDAO userDAO = DAOFactory.getInstance().getUserDAO();
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
