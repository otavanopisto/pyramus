package fi.otavanopisto.pyramus.json.settings;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.courses.CourseParticipationTypeDAO;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;

/**
 * The controller responsible of archiving a course participation type. 
 */
public class ArchiveCourseParticipationTypeJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext jsonRequestContext) {
    CourseParticipationTypeDAO participationTypeDAO = DAOFactory.getInstance().getCourseParticipationTypeDAO();
    Long courseParticipationTypeId = jsonRequestContext.getLong("courseParticipationTypeId");
    participationTypeDAO.archive(participationTypeDAO.findById(courseParticipationTypeId));
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

}
