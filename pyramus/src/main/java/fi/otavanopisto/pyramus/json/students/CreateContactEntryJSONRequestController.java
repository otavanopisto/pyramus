package fi.otavanopisto.pyramus.json.students;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.students.StudentContactLogEntryDAO;
import fi.otavanopisto.pyramus.dao.students.StudentDAO;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.students.StudentContactLogEntry;
import fi.otavanopisto.pyramus.domainmodel.students.StudentContactLogEntryType;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

/**
 * JSON request controller for creating new contact entry.
 * 
 * @author antti.viljakainen
 */
public class CreateContactEntryJSONRequestController extends JSONRequestController {

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
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    StudentContactLogEntryDAO contactLogEntryDAO = DAOFactory.getInstance().getStudentContactLogEntryDAO();

    try {
      Long studentId = NumberUtils.createLong(jsonRequestContext.getRequest().getParameter("studentId"));
      
      Student student = studentDAO.findById(studentId);
      
      String entryText = jsonRequestContext.getRequest().getParameter("entryText");
      String entryCreator = jsonRequestContext.getRequest().getParameter("entryCreatorName");
      Date entryDate = new Date(NumberUtils.createLong(jsonRequestContext.getRequest().getParameter("entryDate"))); 
      StudentContactLogEntryType entryType = StudentContactLogEntryType.valueOf(jsonRequestContext.getString("entryType"));
      
      StudentContactLogEntry entry = contactLogEntryDAO.create(student, entryType, entryText, entryDate, entryCreator);

      Map<String, Object> info = new HashMap<String, Object>();
      info.put("id", entry.getId());
      info.put("creatorName", entry.getCreatorName());
      info.put("timestamp", entry.getEntryDate().getTime());
      info.put("text", entry.getText());
      info.put("type", entry.getType());
      info.put("studentId", studentId);

      jsonRequestContext.addResponseParameter("results", info);
    } catch (Exception e) {
      throw new SmvcRuntimeException(e);
    }
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

}
