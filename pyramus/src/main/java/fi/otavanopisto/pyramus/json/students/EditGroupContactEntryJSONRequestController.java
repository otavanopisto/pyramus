package fi.otavanopisto.pyramus.json.students;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.StatusCode;
import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.students.StudentGroupContactLogEntryDAO;
import fi.otavanopisto.pyramus.domainmodel.students.StudentContactLogEntryType;
import fi.otavanopisto.pyramus.domainmodel.students.StudentGroupContactLogEntry;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

/**
 * JSON request controller for editing a contact entry.
 */
public class EditGroupContactEntryJSONRequestController extends JSONRequestController {

  /**
   * Method to process JSON requests.
   * 
   * In parameters
   * - entryId - Long parameter to identify the contact entry that is being modified
   * - entryText - Textual message or description about the contact
   * - entryCreatorName - Name of the person who made the contact
   * - entryDate - Date of the entry
   * - entryType - Type of the entry
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
      
      String entryText = jsonRequestContext.getString("entryText");
      String entryCreator = jsonRequestContext.getString("entryCreatorName");
      Date entryDate = jsonRequestContext.getDate("entryDate"); 
      StudentContactLogEntryType entryType = StudentContactLogEntryType.valueOf(jsonRequestContext.getString("entryType"));
      
      logEntryDAO.update(entry, entryType, entryText, entryDate, entryCreator);

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
