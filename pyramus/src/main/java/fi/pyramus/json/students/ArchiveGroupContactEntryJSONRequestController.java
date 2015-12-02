package fi.pyramus.json.students;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.StatusCode;
import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.students.StudentGroupContactLogEntryDAO;
import fi.pyramus.domainmodel.students.StudentGroupContactLogEntry;
import fi.pyramus.framework.JSONRequestController;
import fi.pyramus.framework.UserRole;

public class ArchiveGroupContactEntryJSONRequestController extends JSONRequestController {
  
  public void process(JSONRequestContext requestContext) {
    StudentGroupContactLogEntryDAO entryDAO = DAOFactory.getInstance().getStudentGroupContactLogEntryDAO();
    Long entryId = requestContext.getLong("entryId");
    
    if (entryId == null)
      throw new SmvcRuntimeException(StatusCode.UNDEFINED, "EntryId was not defined.");
    
    StudentGroupContactLogEntry entry = entryDAO.findById(entryId);
    
    entryDAO.archive(entry);
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

}
