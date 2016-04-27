package fi.otavanopisto.pyramus.views.courses;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.accommodation.RoomDAO;
import fi.otavanopisto.pyramus.dao.accommodation.RoomTypeDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseStudentDAO;
import fi.otavanopisto.pyramus.domainmodel.accommodation.Room;
import fi.otavanopisto.pyramus.domainmodel.accommodation.RoomType;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseStudent;
import fi.otavanopisto.pyramus.framework.PyramusStatusCode;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class CourseStudentDetailsDialogViewController extends PyramusViewController {

  public void process(PageRequestContext pageRequestContext) {
    CourseStudentDAO courseStudentDAO = DAOFactory.getInstance().getCourseStudentDAO();
    RoomTypeDAO roomTypeDAO = DAOFactory.getInstance().getRoomTypeDAO();
    RoomDAO roomDAO = DAOFactory.getInstance().getRoomDAO();
    
    Long courseStudentId = pageRequestContext.getLong("courseStudentId");
    if (courseStudentId == null) {
      throw new SmvcRuntimeException(PyramusStatusCode.UNDEFINED, "Missing courseStudentId parameter");
    }
    
    CourseStudent courseStudent = courseStudentDAO.findById(courseStudentId);
    if (courseStudent == null) {
      throw new SmvcRuntimeException(PyramusStatusCode.UNDEFINED, "Invalid courseStudentId parameter");
    }
    
    Map<RoomType, List<Room>> rooms = new TreeMap<>(new Comparator<RoomType>() {
      @Override
      public int compare(RoomType o1, RoomType o2) {
        return o1.getName().compareTo(o2.getName());
      }
    });
    
    List<RoomType> roomTypes = roomTypeDAO.listAll();

    for (RoomType roomType : roomTypes) {
      List<Room> typeRooms = roomDAO.listByType(roomType);
      rooms.put(roomType, typeRooms);
    }
    
    pageRequestContext.getRequest().setAttribute("courseStudent", courseStudent);
    pageRequestContext.getRequest().setAttribute("rooms", rooms);
    
    pageRequestContext.setIncludeJSP("/templates/courses/studentdetailsdialog.jsp");
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

}
