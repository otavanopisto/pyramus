package fi.pyramus.json.students;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.students.StudentGroupContactLogEntryDAO;
import fi.pyramus.dao.students.StudentGroupDAO;
import fi.pyramus.domainmodel.students.StudentContactLogEntryType;
import fi.pyramus.domainmodel.students.StudentGroup;
import fi.pyramus.domainmodel.students.StudentGroupContactLogEntry;
import fi.pyramus.framework.JSONRequestController;
import fi.pyramus.framework.UserRole;

/**
 * JSON request controller for creating new contact entry.
 * 
 * @author antti.viljakainen
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

    try {
      Long studentGroupId = NumberUtils.createLong(jsonRequestContext.getRequest().getParameter("studentGroupId"));
    
      StudentGroup studentGroup = studentGroupDAO.findById(studentGroupId);
      
      String entryText = jsonRequestContext.getRequest().getParameter("entryText");
      String entryCreator = jsonRequestContext.getRequest().getParameter("entryCreatorName");
      Date entryDate = new Date(NumberUtils.createLong(jsonRequestContext.getRequest().getParameter("entryDate"))); 
      StudentContactLogEntryType entryType = StudentContactLogEntryType.valueOf(jsonRequestContext.getString("entryType"));
      
      StudentGroupContactLogEntry entry = contactLogEntryDAO.create(studentGroup, entryType, entryText, entryDate, entryCreator);

      Map<String, Object> info = new HashMap<String, Object>();
      info.put("id", entry.getId());
      info.put("creatorName", entry.getCreatorName());
      info.put("timestamp", entry.getEntryDate().getTime());
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
