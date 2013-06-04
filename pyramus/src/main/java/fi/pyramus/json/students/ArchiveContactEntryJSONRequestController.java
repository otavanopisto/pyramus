package fi.pyramus.json.students;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.students.StudentContactLogEntryDAO;
import fi.pyramus.domainmodel.students.StudentContactLogEntry;
import fi.pyramus.framework.JSONRequestController;
import fi.pyramus.framework.UserRole;

public class ArchiveContactEntryJSONRequestController extends JSONRequestController {
  
  public void process(JSONRequestContext requestContext) {
    StudentContactLogEntryDAO entryDAO = DAOFactory.getInstance().getStudentContactLogEntryDAO();
    Long entryId = requestContext.getLong("entryId");
    
    StudentContactLogEntry entry = entryDAO.findById(entryId);
    
    entryDAO.archive(entry);
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

}
