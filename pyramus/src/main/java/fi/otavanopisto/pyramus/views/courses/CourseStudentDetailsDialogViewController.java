package fi.otavanopisto.pyramus.views.courses;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.accommodation.RoomDAO;
import fi.otavanopisto.pyramus.dao.accommodation.RoomTypeDAO;
import fi.otavanopisto.pyramus.dao.base.BillingDetailsDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseStudentDAO;
import fi.otavanopisto.pyramus.domainmodel.accommodation.Room;
import fi.otavanopisto.pyramus.domainmodel.accommodation.RoomType;
import fi.otavanopisto.pyramus.domainmodel.base.BillingDetails;
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
    
    // Support other currencies
    List<Currency> currencies = Arrays.asList(Currency.getInstance("EUR"));
    
    List<BillingDetails> existingBillingDetails = getExistingBillingDetails(courseStudent);
    
    pageRequestContext.getRequest().setAttribute("courseStudent", courseStudent);
    pageRequestContext.getRequest().setAttribute("rooms", rooms);
    pageRequestContext.getRequest().setAttribute("currencies", currencies);
    pageRequestContext.getRequest().setAttribute("existingBillingDetails", existingBillingDetails);
    
    pageRequestContext.setIncludeJSP("/templates/courses/studentdetailsdialog.jsp");
  }

  private List<BillingDetails> getExistingBillingDetails(CourseStudent courseStudent) {
    Map<Integer, BillingDetails> result = new HashMap<>();
    
    BillingDetailsDAO billingDetailsDAO = DAOFactory.getInstance().getBillingDetailsDAO();
    
    List<BillingDetails> billingDetails = billingDetailsDAO.listByStudent(courseStudent.getStudent());
    for (BillingDetails studentBillingDetails : billingDetails) {
      result.put(getBillingDetailsHash(studentBillingDetails), studentBillingDetails);
    }
    
    if (courseStudent.getBillingDetails() != null) {
      result.remove(getBillingDetailsHash(courseStudent.getBillingDetails()));
    }
    
    return new ArrayList<>(result.values());
  }
  
  private int getBillingDetailsHash(BillingDetails studentBillingDetails) {
    HashCodeBuilder builder = new HashCodeBuilder(43, 83);
    builder.append(studentBillingDetails.getCity());
    builder.append(studentBillingDetails.getCompanyIdentifier());
    builder.append(studentBillingDetails.getCompanyName());
    builder.append(studentBillingDetails.getCountry());
    builder.append(studentBillingDetails.getElectronicBillingAddress());
    builder.append(studentBillingDetails.getEmailAddress());
    builder.append(studentBillingDetails.getNotes());
    builder.append(studentBillingDetails.getPersonName());
    builder.append(studentBillingDetails.getPhoneNumber());
    builder.append(studentBillingDetails.getPostalCode());
    builder.append(studentBillingDetails.getReferenceNumber());
    builder.append(studentBillingDetails.getRegion());
    builder.append(studentBillingDetails.getStreetAddress1());
    builder.append(studentBillingDetails.getStreetAddress2());
    return builder.toHashCode();
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

}
