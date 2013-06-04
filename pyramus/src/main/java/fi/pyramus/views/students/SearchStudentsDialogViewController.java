package fi.pyramus.views.students;

import java.util.Collections;
import java.util.List;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.StudyProgrammeDAO;
import fi.pyramus.domainmodel.base.StudyProgramme;
import fi.pyramus.framework.PyramusViewController;
import fi.pyramus.framework.UserRole;
import fi.pyramus.util.StringAttributeComparator;

public class SearchStudentsDialogViewController extends PyramusViewController {

  public void process(PageRequestContext requestContext) {
    StudyProgrammeDAO studyProgrammeDAO = DAOFactory.getInstance().getStudyProgrammeDAO();
    List<StudyProgramme> studyProgrammes = studyProgrammeDAO.listUnarchived();
    Collections.sort(studyProgrammes, new StringAttributeComparator("getName"));
    
    requestContext.getRequest().setAttribute("studyProgrammes", studyProgrammes);
    requestContext.setIncludeJSP("/templates/students/searchstudentsdialog.jsp");
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.GUEST, UserRole.USER, UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }
  
}
