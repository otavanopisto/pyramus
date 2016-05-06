package fi.otavanopisto.pyramus.json.students;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.students.StudentContactLogEntryDAO;
import fi.otavanopisto.pyramus.domainmodel.students.StudentContactLogEntry;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class ArchiveContactEntryJSONRequestController extends JSONRequestController {
  
  public void process(JSONRequestContext requestContext) {
    StudentContactLogEntryDAO entryDAO = DAOFactory.getInstance().getStudentContactLogEntryDAO();
    Long entryId = requestContext.getLong("entryId");
    
    StudentContactLogEntry entry = entryDAO.findById(entryId);
    
    entryDAO.archive(entry);
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

}
