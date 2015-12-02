package fi.pyramus.json.students;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.StatusCode;
import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.students.StudentGroupContactLogEntryCommentDAO;
import fi.pyramus.domainmodel.students.StudentGroupContactLogEntryComment;
import fi.pyramus.framework.JSONRequestController;
import fi.pyramus.framework.UserRole;

public class ArchiveGroupContactEntryCommentJSONRequestController extends JSONRequestController {
  
  public void process(JSONRequestContext requestContext) {
    StudentGroupContactLogEntryCommentDAO entryCommentDAO = DAOFactory.getInstance().getStudentGroupContactLogEntryCommentDAO();
    Long commentId = requestContext.getLong("commentId");
    
    if (commentId == null)
      throw new SmvcRuntimeException(StatusCode.UNDEFINED, "CommentId was not defined.");
    
    StudentGroupContactLogEntryComment comment = entryCommentDAO.findById(commentId);
    
    entryCommentDAO.archive(comment);
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

}
