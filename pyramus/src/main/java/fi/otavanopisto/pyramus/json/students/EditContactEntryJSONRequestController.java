package fi.otavanopisto.pyramus.json.students;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.students.StudentContactLogEntryDAO;
import fi.otavanopisto.pyramus.domainmodel.students.StudentContactLogEntry;
import fi.otavanopisto.pyramus.domainmodel.students.StudentContactLogEntryType;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

/**
 * JSON request controller for editing a contact entry.
 * 
 * @author antti.viljakainen
 */
public class EditContactEntryJSONRequestController extends JSONRequestController {

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
    StudentContactLogEntryDAO logEntryDAO = DAOFactory.getInstance().getStudentContactLogEntryDAO();

    try {
      Long entryId = NumberUtils.createLong(jsonRequestContext.getRequest().getParameter("entryId"));
      
      StudentContactLogEntry entry = logEntryDAO.findById(entryId);
      
      String entryText = jsonRequestContext.getRequest().getParameter("entryText");
      Date entryDate = new Date(NumberUtils.createLong(jsonRequestContext.getRequest().getParameter("entryDate"))); 
      StudentContactLogEntryType entryType = StudentContactLogEntryType.valueOf(jsonRequestContext.getString("entryType"));
      
      logEntryDAO.update(entry, entryType, entryText, entryDate);

      Map<String, Object> info = new HashMap<>();
      info.put("id", entry.getId());
      info.put("timestamp", entry.getEntryDate().getTime());
      info.put("creatorName", entry.getCreatorName());
      info.put("creatorId", entry.getCreator().getId());
      info.put("text", entry.getText());
      info.put("type", entry.getType());
      info.put("studentId", entry.getStudent().getId());

      jsonRequestContext.addResponseParameter("results", info);
    } catch (Exception e) {
      throw new SmvcRuntimeException(e);
    }
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

}
