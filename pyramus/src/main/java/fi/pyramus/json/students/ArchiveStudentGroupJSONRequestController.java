package fi.pyramus.json.students;

import org.apache.commons.lang.math.NumberUtils;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.users.StaffMemberDAO;
import fi.pyramus.dao.students.StudentGroupDAO;
import fi.pyramus.domainmodel.students.StudentGroup;
import fi.pyramus.domainmodel.users.User;
import fi.pyramus.framework.JSONRequestController;
import fi.pyramus.framework.UserRole;

public class ArchiveStudentGroupJSONRequestController extends JSONRequestController {
  
  public void process(JSONRequestContext requestContext) {
    StudentGroupDAO studentDAO = DAOFactory.getInstance().getStudentGroupDAO();
    StaffMemberDAO userDAO = DAOFactory.getInstance().getStaffDAO();
    
    Long studentGroupId = NumberUtils.createLong(requestContext.getRequest().getParameter("studentGroupId"));
    User loggedUser = userDAO.findById(requestContext.getLoggedUserId());

    StudentGroup studentGroup = studentDAO.findById(studentGroupId);    
    studentDAO.archive(studentGroup, loggedUser);
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }
  
}
