package fi.pyramus.json.students;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.students.StudentContactLogEntryCommentDAO;
import fi.pyramus.domainmodel.students.StudentContactLogEntryComment;
import fi.pyramus.framework.JSONRequestController;
import fi.pyramus.framework.UserRole;

public class ArchiveContactEntryCommentJSONRequestController extends JSONRequestController {
  
  public void process(JSONRequestContext requestContext) {
    StudentContactLogEntryCommentDAO entryCommentDAO = DAOFactory.getInstance().getStudentContactLogEntryCommentDAO();
    Long commentId = requestContext.getLong("commentId");
    
    StudentContactLogEntryComment comment = entryCommentDAO.findById(commentId);
    
    entryCommentDAO.archive(comment);
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

}
