package fi.otavanopisto.pyramus.json.students;

import java.util.HashMap;
import java.util.Map;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.StatusCode;
import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.students.StudentGroupContactLogEntryCommentDAO;
import fi.otavanopisto.pyramus.domainmodel.students.StudentGroupContactLogEntryComment;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

/**
 * JSON request controller for reading a contact entry comment.
 */
public class GetGroupContactEntryCommentJSONRequestController extends JSONRequestController {

  /**
   * Method to process JSON requests.
   * 
   * In parameters
   * - commentId - Long parameter to identify the contact entry comment that is being read
   * 
   * Page parameters
   * - results Map including
   * * id - Comment id
   * * entryId - Entry id
   * * creatorName - Comment creator name
   * * timestamp - Comment date
   * * text - Comment message
   * 
   * @param jsonRequestContext JSON request context
   */
  public void process(JSONRequestContext jsonRequestContext) {
    StudentGroupContactLogEntryCommentDAO entryCommentDAO = DAOFactory.getInstance().getStudentGroupContactLogEntryCommentDAO();

    Long commentId = jsonRequestContext.getLong("commentId");

    if (commentId == null)
      throw new SmvcRuntimeException(StatusCode.UNDEFINED, "CommentId was not defined.");
    
    try {
      StudentGroupContactLogEntryComment comment = entryCommentDAO.findById(commentId);
      
      Map<String, Object> info = new HashMap<>();
      info.put("id", comment.getId());
      info.put("entryId", comment.getEntry().getId());
      info.put("creatorName", comment.getCreatorName());
      info.put("timestamp", comment.getCommentDate() != null ? comment.getCommentDate().getTime() : "");
      info.put("text", comment.getText());
      
      jsonRequestContext.addResponseParameter("results", info);
    } catch (Exception e) {
      throw new SmvcRuntimeException(e);
    }
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

}
