package fi.pyramus.json.students;

import java.util.HashMap;
import java.util.Map;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.StatusCode;
import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.students.StudentGroupContactLogEntryDAO;
import fi.pyramus.domainmodel.students.StudentGroupContactLogEntry;
import fi.pyramus.framework.JSONRequestController;
import fi.pyramus.framework.UserRole;

/**
 * JSON request controller for reading a contact entry.
 */
public class GetGroupContactEntryJSONRequestController extends JSONRequestController {

  /**
   * Method to process JSON requests.
   * 
   * In parameters
   * - entryId - Long parameter to identify the contact entry that is being modified
   * 
   * Page parameters
   * - results Map including
   * * id - Entry id
   * * creatorName - Entry creator name
   * * date - Entry date
   * * text - Entry message
   * * type - Entry type
   * 
   * @param jsonRequestContext JSON request context
   */
  public void process(JSONRequestContext jsonRequestContext) {
    StudentGroupContactLogEntryDAO logEntryDAO = DAOFactory.getInstance().getStudentGroupContactLogEntryDAO();

    Long entryId = jsonRequestContext.getLong("entryId");
    
    if (entryId == null)
      throw new SmvcRuntimeException(StatusCode.UNDEFINED, "EntryId was not defined.");
    
    try {
      StudentGroupContactLogEntry entry = logEntryDAO.findById(entryId);
      
      Map<String, Object> info = new HashMap<String, Object>();
      info.put("id", entry.getId());
      info.put("creatorName", entry.getCreatorName());
      info.put("timestamp", entry.getEntryDate() != null ? entry.getEntryDate().getTime() : "");
      info.put("text", entry.getText());
      info.put("type", entry.getType());
      info.put("studentGroupId", entry.getStudentGroup().getId());
      
      jsonRequestContext.addResponseParameter("results", info);
    } catch (Exception e) {
      throw new SmvcRuntimeException(e);
    }
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

}
