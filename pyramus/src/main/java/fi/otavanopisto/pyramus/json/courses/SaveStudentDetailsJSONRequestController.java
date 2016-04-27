package fi.otavanopisto.pyramus.json.courses;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.accommodation.RoomDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseStudentDAO;
import fi.otavanopisto.pyramus.domainmodel.accommodation.Room;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseStudent;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.PyramusStatusCode;
import fi.otavanopisto.pyramus.framework.UserRole;

public class SaveStudentDetailsJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext requestContext) {
    CourseStudentDAO courseStudentDAO = DAOFactory.getInstance().getCourseStudentDAO();
    RoomDAO roomDAO = DAOFactory.getInstance().getRoomDAO();
    
    Long courseStudentId = requestContext.getLong("courseStudentId");
    Long roomId = requestContext.getLong("roomId");
    
    if (courseStudentId == null) {
      throw new SmvcRuntimeException(PyramusStatusCode.UNDEFINED, "Missing courseStudentId parameter");
    }
    
    CourseStudent courseStudent = courseStudentDAO.findById(courseStudentId);
    if (courseStudent == null) {
      throw new SmvcRuntimeException(PyramusStatusCode.UNDEFINED, "Invalid courseStudentId parameter");
    }
    
    Room room = null;
    if (roomId != null) {
      room = roomDAO.findById(roomId);
      if (room == null) {
        throw new SmvcRuntimeException(PyramusStatusCode.UNDEFINED, "Invalid roomId");
      }
    }
    
    courseStudentDAO.updateRoom(courseStudent, room);
  }

  @Override
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }
  
}
