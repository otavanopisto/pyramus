package fi.otavanopisto.pyramus.views.students;

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.StudyProgrammeDAO;
import fi.otavanopisto.pyramus.dao.students.StudentDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.Archived;
import fi.otavanopisto.pyramus.domainmodel.base.StudyProgramme;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.users.StaffMember;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.framework.UserUtils;
import fi.otavanopisto.pyramus.util.StringAttributeComparator;

public class ChangeStudentStudyProgammeDialogViewController extends PyramusViewController {

  private static final Logger logger = Logger.getLogger(ChangeStudentStudyProgammeDialogViewController.class.getName());

  public void process(PageRequestContext requestContext) {
    StaffMemberDAO staffMemberDAO = DAOFactory.getInstance().getStaffMemberDAO();
    StudyProgrammeDAO studyProgrammeDAO = DAOFactory.getInstance().getStudyProgrammeDAO();
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();

    Long studentId = requestContext.getLong("studentId");
    
    if (studentId == null) {
      logger.log(Level.WARNING, "Unable to load dialog with missing studentId.");
      
      try {
        requestContext.getResponse().sendError(HttpServletResponse.SC_BAD_REQUEST);
      } catch (Exception e) {
        logger.log(Level.SEVERE, "Error sending response", e);
      }
      return;
    }

    StaffMember loggedUser = staffMemberDAO.findById(requestContext.getLoggedUserId());

    Student student = studentDAO.findById(studentId);

    List<StudyProgramme> studyProgrammes = UserUtils.canAccessAllOrganizations(loggedUser) ? 
        studyProgrammeDAO.listUnarchived() : studyProgrammeDAO.listByOrganization(loggedUser.getOrganization(), Archived.UNARCHIVED);
    Collections.sort(studyProgrammes, new StringAttributeComparator("getName"));

    requestContext.getRequest().setAttribute("student", student);
    requestContext.getRequest().setAttribute("studyProgrammes", studyProgrammes);
    
    requestContext.setIncludeJSP("/templates/students/changestudentstudyprogrammedialog.jsp");
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

}
