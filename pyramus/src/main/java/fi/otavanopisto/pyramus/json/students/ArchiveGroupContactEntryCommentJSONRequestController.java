package fi.otavanopisto.pyramus.json.students;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.StatusCode;
import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.students.StudentGroupContactLogEntryCommentDAO;
import fi.otavanopisto.pyramus.domainmodel.students.StudentGroupContactLogEntryComment;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

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
