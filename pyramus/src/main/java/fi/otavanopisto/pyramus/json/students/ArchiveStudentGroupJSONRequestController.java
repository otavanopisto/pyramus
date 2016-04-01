package fi.otavanopisto.pyramus.json.students;

import org.apache.commons.lang.math.NumberUtils;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.students.StudentGroupDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.students.StudentGroup;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class ArchiveStudentGroupJSONRequestController extends JSONRequestController {
  
  public void process(JSONRequestContext requestContext) {
    StudentGroupDAO studentDAO = DAOFactory.getInstance().getStudentGroupDAO();
    StaffMemberDAO userDAO = DAOFactory.getInstance().getStaffMemberDAO();
    
    Long studentGroupId = NumberUtils.createLong(requestContext.getRequest().getParameter("studentGroupId"));
    User loggedUser = userDAO.findById(requestContext.getLoggedUserId());

    StudentGroup studentGroup = studentDAO.findById(studentGroupId);    
    studentDAO.archive(studentGroup, loggedUser);
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }
  
}
