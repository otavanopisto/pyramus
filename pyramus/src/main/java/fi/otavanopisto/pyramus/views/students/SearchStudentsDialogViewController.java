package fi.otavanopisto.pyramus.views.students;

import java.util.Collections;
import java.util.List;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.StudyProgrammeDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.Archived;
import fi.otavanopisto.pyramus.domainmodel.base.StudyProgramme;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.framework.UserUtils;
import fi.otavanopisto.pyramus.util.StringAttributeComparator;

public class SearchStudentsDialogViewController extends PyramusViewController {

  public void process(PageRequestContext requestContext) {
    StudyProgrammeDAO studyProgrammeDAO = DAOFactory.getInstance().getStudyProgrammeDAO();
    StaffMemberDAO staffMemberDAO = DAOFactory.getInstance().getStaffMemberDAO();
    
    User loggedUser = staffMemberDAO.findById(requestContext.getLoggedUserId());

    List<StudyProgramme> studyProgrammes = UserUtils.canAccessAllOrganizations(loggedUser) ? 
        studyProgrammeDAO.listUnarchived() : studyProgrammeDAO.listByOrganization(loggedUser.getOrganization(), Archived.UNARCHIVED);
    Collections.sort(studyProgrammes, new StringAttributeComparator("getName"));
    
    requestContext.getRequest().setAttribute("studyProgrammes", studyProgrammes);
    requestContext.setIncludeJSP("/templates/students/searchstudentsdialog.jsp");
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.GUEST, UserRole.USER, UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }
  
}
