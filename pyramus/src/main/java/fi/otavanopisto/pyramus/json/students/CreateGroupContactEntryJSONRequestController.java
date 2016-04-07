package fi.otavanopisto.pyramus.json.students;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.StatusCode;
import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.students.StudentGroupContactLogEntryDAO;
import fi.otavanopisto.pyramus.dao.students.StudentGroupDAO;
import fi.otavanopisto.pyramus.domainmodel.students.StudentContactLogEntryType;
import fi.otavanopisto.pyramus.domainmodel.students.StudentGroup;
import fi.otavanopisto.pyramus.domainmodel.students.StudentGroupContactLogEntry;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

/**
 * JSON request controller for creating new student group contact entry.
 */
public class CreateGroupContactEntryJSONRequestController extends JSONRequestController {

  /**
   * Method to process JSON requests.
   * 
   * In parameters
   * - studentId - Student id to identify the student who is receiving the entry
   * - entryText - Textual message or description about the contact
   * - entryCreatorName - Name of the person who made the contact
   * - entryDate - Date of the entry
   * - entryType - Type of the entry
   * 
   * Page parameters
   * - results Map including
   * * id - New entry id
   * * creatorName - New entry creator
   * * timestamp - New entry date
   * * text - New entry message
   * * type - New entry type
   * 
   * @param jsonRequestContext JSON request context
   */
  public void process(JSONRequestContext jsonRequestContext) {
    StudentGroupDAO studentGroupDAO = DAOFactory.getInstance().getStudentGroupDAO();
    StudentGroupContactLogEntryDAO contactLogEntryDAO = DAOFactory.getInstance().getStudentGroupContactLogEntryDAO();

    Long studentGroupId = jsonRequestContext.getLong("studentGroupId");
    if (studentGroupId == null)
      throw new SmvcRuntimeException(StatusCode.UNDEFINED, "StudentGroupId was not defined.");
    
    try {
      StudentGroup studentGroup = studentGroupDAO.findById(studentGroupId);
      
      String entryText = jsonRequestContext.getString("entryText");
      String entryCreator = jsonRequestContext.getString("entryCreatorName");
      Date entryDate = jsonRequestContext.getDate("entryDate"); 
      StudentContactLogEntryType entryType = StudentContactLogEntryType.valueOf(jsonRequestContext.getString("entryType"));
      
      StudentGroupContactLogEntry entry = contactLogEntryDAO.create(studentGroup, entryType, entryText, entryDate, entryCreator);

      Map<String, Object> info = new HashMap<>();
      info.put("id", entry.getId());
      info.put("creatorName", entry.getCreatorName());
      info.put("timestamp", entry.getEntryDate() != null ? entry.getEntryDate().getTime() : "");
      info.put("text", entry.getText());
      info.put("type", entry.getType());
      info.put("studentGroupId", studentGroupId);

      jsonRequestContext.addResponseParameter("results", info);
    } catch (Exception e) {
      throw new SmvcRuntimeException(e);
    }
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

}
