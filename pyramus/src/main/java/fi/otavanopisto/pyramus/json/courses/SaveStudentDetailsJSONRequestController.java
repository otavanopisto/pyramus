package fi.otavanopisto.pyramus.json.courses;

import java.math.BigDecimal;
import java.util.Currency;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.accommodation.RoomDAO;
import fi.otavanopisto.pyramus.dao.base.BillingDetailsDAO;
import fi.otavanopisto.pyramus.dao.courses.CourseStudentDAO;
import fi.otavanopisto.pyramus.domainmodel.accommodation.Room;
import fi.otavanopisto.pyramus.domainmodel.base.BillingDetails;
import fi.otavanopisto.pyramus.domainmodel.courses.CourseStudent;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.PyramusStatusCode;
import fi.otavanopisto.pyramus.framework.UserRole;

public class SaveStudentDetailsJSONRequestController extends JSONRequestController {

  public void process(JSONRequestContext requestContext) {
    CourseStudentDAO courseStudentDAO = DAOFactory.getInstance().getCourseStudentDAO();
    RoomDAO roomDAO = DAOFactory.getInstance().getRoomDAO();
    BillingDetailsDAO billingDetailsDAO = DAOFactory.getInstance().getBillingDetailsDAO();
    
    Long courseStudentId = requestContext.getLong("courseStudentId");
    String organization = requestContext.getString("organization");
    String additionalInfo = requestContext.getString("additionalInfo");
    Long roomId = requestContext.getLong("roomId");
    String roomAdditionalInfo = requestContext.getString("roomAdditionalInfo");
    BigDecimal lodgingFee = requestContext.getBigDecimal("lodgingFee");
    Currency lodgingFeeCurrency = requestContext.getCurrency("lodgingFeeCurrency");
    BigDecimal reservationFee = requestContext.getBigDecimal("reservationFee");
    Currency reservationFeeCurrency = requestContext.getCurrency("reservationFeeCurrency");
    
    String billingDetailsPersonName = requestContext.getString("billingDetailsPersonName"); 
    String billingDetailsCompanyName = requestContext.getString("billingDetailsCompanyName"); 
    String billingDetailsStreetAddress1 = requestContext.getString("billingDetailsStreetAddress1"); 
    String billingDetailsStreetAddress2 = requestContext.getString("billingDetailsStreetAddress2"); 
    String billingDetailsPostalCode = requestContext.getString("billingDetailsPostalCode"); 
    String billingDetailsCity = requestContext.getString("billingDetailsCity"); 
    String billingDetailsRegion = requestContext.getString("billingDetailsRegion"); 
    String billingDetailsCountry = requestContext.getString("billingDetailsCountry"); 
    String billingDetailsPhoneNumber = requestContext.getString("billingDetailsPhoneNumber"); 
    String billingDetailsEmailAddress = requestContext.getString("billingDetailsEmailAddress"); 
    String billingDetailsCompanyIdentifier = requestContext.getString("billingDetailsCompanyIdentifier"); 
    String billingDetailsReferenceNumber = requestContext.getString("billingDetailsReferenceNumber"); 
    String billingDetailsElectronicBillingAddress = requestContext.getString("billingDetailsElectronicBillingAddress"); 
    String billingDetailsElectronicBillingOperator = requestContext.getString("billingDetailsElectronicBillingOperator"); 
    String billingDetailsNotes = requestContext.getString("billingDetailsNotes");
    
    Long billingDetailsId = requestContext.getLong("billingDetailsId");
    if (billingDetailsId != null) {
      BillingDetails billingDetails = billingDetailsDAO.findById(billingDetailsId);
      if (billingDetails != null) {
        billingDetailsPersonName = billingDetails.getPersonName();
        billingDetailsCompanyName = billingDetails.getCompanyName();
        billingDetailsStreetAddress1 = billingDetails.getStreetAddress1();
        billingDetailsStreetAddress2 = billingDetails.getStreetAddress2();
        billingDetailsPostalCode = billingDetails.getPostalCode();
        billingDetailsCity = billingDetails.getCity();
        billingDetailsRegion = billingDetails.getRegion();
        billingDetailsCountry = billingDetails.getCountry();
        billingDetailsPhoneNumber = billingDetails.getPhoneNumber();
        billingDetailsEmailAddress = billingDetails.getEmailAddress();
        billingDetailsCompanyIdentifier = billingDetails.getCompanyIdentifier();
        billingDetailsReferenceNumber = billingDetails.getReferenceNumber();
        billingDetailsElectronicBillingAddress = billingDetails.getElectronicBillingAddress();
        billingDetailsElectronicBillingOperator = billingDetails.getElectronicBillingOperator();
        billingDetailsNotes = billingDetails.getNotes();
      }
    }
    
    boolean billingDetailsSet = !allNull(billingDetailsPersonName, billingDetailsCompanyName,
        billingDetailsStreetAddress1, billingDetailsStreetAddress2, billingDetailsPostalCode, billingDetailsCity,
        billingDetailsRegion, billingDetailsCountry, billingDetailsPhoneNumber, billingDetailsEmailAddress,
        billingDetailsCompanyIdentifier, billingDetailsReferenceNumber, billingDetailsElectronicBillingAddress,
        billingDetailsElectronicBillingOperator, billingDetailsNotes);
    
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

    BillingDetails billingDetails = courseStudent.getBillingDetails();

    if (billingDetailsSet) {
      if (billingDetails != null) {
        billingDetailsDAO.updatePersonName(billingDetails, billingDetailsPersonName);
        billingDetailsDAO.updateCompanyName(billingDetails, billingDetailsCompanyName);
        billingDetailsDAO.updateStreetAddress1(billingDetails, billingDetailsStreetAddress1);
        billingDetailsDAO.updateStreetAddress2(billingDetails, billingDetailsStreetAddress2);
        billingDetailsDAO.updatePostalCode(billingDetails, billingDetailsPostalCode);
        billingDetailsDAO.updateCity(billingDetails, billingDetailsCity);
        billingDetailsDAO.updateRegion(billingDetails, billingDetailsRegion);
        billingDetailsDAO.updateCountry(billingDetails, billingDetailsCountry);
        billingDetailsDAO.updatePhoneNumber(billingDetails, billingDetailsPhoneNumber);
        billingDetailsDAO.updateEmailAddress(billingDetails, billingDetailsEmailAddress);
        billingDetailsDAO.updateCompanyIdentifier(billingDetails, billingDetailsCompanyIdentifier);
        billingDetailsDAO.updateReferenceNumber(billingDetails, billingDetailsReferenceNumber);
        billingDetailsDAO.updateNotes(billingDetails, billingDetailsNotes);
        billingDetailsDAO.updateElectronicBillingAddress(billingDetails, billingDetailsElectronicBillingAddress);
        billingDetailsDAO.updateElectronicBillingOperator(billingDetails, billingDetailsElectronicBillingOperator);
      } else {
        courseStudentDAO.updateBillingDetails(courseStudent, billingDetailsDAO.create(billingDetailsPersonName, billingDetailsCompanyName, billingDetailsStreetAddress1,
            billingDetailsStreetAddress2, billingDetailsPostalCode, billingDetailsCity, billingDetailsRegion,
            billingDetailsCountry, billingDetailsPhoneNumber, billingDetailsEmailAddress,
            billingDetailsElectronicBillingAddress, billingDetailsElectronicBillingOperator, billingDetailsCompanyIdentifier, billingDetailsReferenceNumber,
            billingDetailsNotes));
      }
    } else {
      if (billingDetails != null) {
        courseStudentDAO.updateBillingDetails(courseStudent, null);
        billingDetailsDAO.delete(billingDetails);
      }
    }

    courseStudentDAO.updateOrganization(courseStudent, organization);
    courseStudentDAO.updateAdditionalInfo(courseStudent, additionalInfo);
    courseStudentDAO.updateRoom(courseStudent, room);
    courseStudentDAO.updateRoomAdditionalInfo(courseStudent, roomAdditionalInfo);
    courseStudentDAO.updateLodgingFee(courseStudent, lodgingFee, lodgingFeeCurrency);
    courseStudentDAO.updateReservationFee(courseStudent, reservationFee, reservationFeeCurrency);
  }

  private boolean allNull(Object... objs) {
    for (Object obj : objs) {
      if (obj != null) {
        return false;
      }
    }
    
    return true;
  }

  @Override
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }
  
}
