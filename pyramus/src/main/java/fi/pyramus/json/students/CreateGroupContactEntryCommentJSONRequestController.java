package fi.pyramus.json.students;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.StatusCode;
import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.students.StudentGroupContactLogEntryCommentDAO;
import fi.pyramus.dao.students.StudentGroupContactLogEntryDAO;
import fi.pyramus.domainmodel.students.StudentGroupContactLogEntry;
import fi.pyramus.domainmodel.students.StudentGroupContactLogEntryComment;
import fi.pyramus.framework.JSONRequestController;
import fi.pyramus.framework.UserRole;

/**
 * JSON request controller for creating new student group contact entry comment.
 */
public class CreateGroupContactEntryCommentJSONRequestController extends JSONRequestController {

  /**
   * Method to process JSON requests.
   * 
   * In parameters
   * - entryId - Id to identify the entry where the comment is being bind into
   * - commentText - Textual message or description about the contact
   * - commentCreatorName - Name of the person who made the contact
   * - commentDate - Date of the entry
   * 
   * Page parameters
   * - results Map including
   * * id - New comment id
   * * entryId - Entry id
   * * creatorName - Comment creator
   * * timestamp - Comment date
   * * text - Comment message
   * 
   * @param jsonRequestContext JSON request context
   */
  public void process(JSONRequestContext jsonRequestContext) {
    StudentGroupContactLogEntryDAO logEntryDAO = DAOFactory.getInstance().getStudentGroupContactLogEntryDAO();
    StudentGroupContactLogEntryCommentDAO entryCommentDAO = DAOFactory.getInstance().getStudentGroupContactLogEntryCommentDAO();

    Long entryId = jsonRequestContext.getLong("entryId");
    if (entryId == null)
      throw new SmvcRuntimeException(StatusCode.UNDEFINED, "EntryId was not defined.");
    
    try {
      StudentGroupContactLogEntry entry = logEntryDAO.findById(entryId);

      String commentText = jsonRequestContext.getString("commentText");
      String commentCreatorName = jsonRequestContext.getString("commentCreatorName");
      Date commentDate = jsonRequestContext.getDate("commentDate"); 
      
      StudentGroupContactLogEntryComment comment = entryCommentDAO.create(entry, commentText, commentDate, commentCreatorName);

      Map<String, Object> info = new HashMap<String, Object>();
      info.put("id", comment.getId());
      info.put("creatorName", comment.getCreatorName());
      info.put("timestamp", comment.getCommentDate() != null ? comment.getCommentDate().getTime() : "");
      info.put("text", comment.getText());
      info.put("entryId", entryId);

      jsonRequestContext.addResponseParameter("results", info);
    } catch (Exception e) {
      throw new SmvcRuntimeException(e);
    }
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

}
