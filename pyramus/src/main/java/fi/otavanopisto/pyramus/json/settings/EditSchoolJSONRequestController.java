package fi.otavanopisto.pyramus.json.settings;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.BillingDetailsDAO;
import fi.otavanopisto.pyramus.dao.base.SchoolDAO;
import fi.otavanopisto.pyramus.dao.base.SchoolFieldDAO;
import fi.otavanopisto.pyramus.dao.base.SchoolVariableDAO;
import fi.otavanopisto.pyramus.dao.base.TagDAO;
import fi.otavanopisto.pyramus.dao.students.StudentGroupDAO;
import fi.otavanopisto.pyramus.domainmodel.base.BillingDetails;
import fi.otavanopisto.pyramus.domainmodel.base.School;
import fi.otavanopisto.pyramus.domainmodel.base.SchoolField;
import fi.otavanopisto.pyramus.domainmodel.base.Tag;
import fi.otavanopisto.pyramus.domainmodel.students.StudentGroup;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.util.ContactInfoUtils;

/**
 * The controller responsible of creating a new school. 
 * 
 * @see fi.otavanopisto.pyramus.views.settings.CreateSchoolViewController
 */
public class EditSchoolJSONRequestController extends JSONRequestController {

  /**
   * Processes the request to create a new grading scale.
   * 
   * @param requestContext The JSON request context
   */
  public void process(JSONRequestContext requestContext) {
    SchoolDAO schoolDAO = DAOFactory.getInstance().getSchoolDAO();
    SchoolFieldDAO schoolFieldDAO = DAOFactory.getInstance().getSchoolFieldDAO();
    SchoolVariableDAO schoolVariableDAO = DAOFactory.getInstance().getSchoolVariableDAO();
    TagDAO tagDAO = DAOFactory.getInstance().getTagDAO();
    BillingDetailsDAO billingDetailsDAO = DAOFactory.getInstance().getBillingDetailsDAO();
    StudentGroupDAO studentGroupDAO = DAOFactory.getInstance().getStudentGroupDAO();

    Long schoolId = NumberUtils.createLong(requestContext.getRequest().getParameter("schoolId"));
    School school = schoolDAO.findById(schoolId);

    Long schoolFieldId = requestContext.getLong("schoolFieldId");
    SchoolField schoolField = null;
    if ((schoolFieldId != null) && (schoolFieldId.intValue() >= 0))
      schoolField = schoolFieldDAO.findById(schoolFieldId);
    
    Long studentGroupId = requestContext.getLong("studentGroupId");
    StudentGroup studentGroup = null;
    if (studentGroupId != null && studentGroupId.intValue() >= 0) {
      studentGroup = studentGroupDAO.findById(studentGroupId);
    }
    
    String schoolCode = requestContext.getString("code");
    String schoolName = requestContext.getString("name");
    String tagsText = requestContext.getString("tags");
    
    Set<Tag> tagEntities = new HashSet<>();
    if (!StringUtils.isBlank(tagsText)) {
      List<String> tags = Arrays.asList(tagsText.split("[\\ ,]"));
      for (String tag : tags) {
        if (!StringUtils.isBlank(tag)) {
          Tag tagEntity = tagDAO.findByText(tag.trim());
          if (tagEntity == null)
            tagEntity = tagDAO.create(tag);
          tagEntities.add(tagEntity);
        }
      }
    }
    
    school = schoolDAO.update(school, schoolCode, schoolName, schoolField, studentGroup);

    // BillingDetails
    
    String billingPersonName = requestContext.getString("billingDetailsPersonName");
    String billingCompanyName = requestContext.getString("billingDetailsCompanyName");
    String billingStreetAddress1 = requestContext.getString("billingDetailsStreetAddress1");
    String billingStreetAddress2 = requestContext.getString("billingDetailsStreetAddress2");
    String billingPostalCode = requestContext.getString("billingDetailsPostalCode");
    String billingCity = requestContext.getString("billingDetailsCity");
    String billingRegion = requestContext.getString("billingDetailsRegion");
    String billingCountry = requestContext.getString("billingDetailsCountry");
    String billingPhoneNumber = requestContext.getString("billingDetailsPhoneNumber");
    String billingEmailAddress = requestContext.getString("billingDetailsEmailAddress");
    String billingElectronicBillingAddress = requestContext.getString("billingDetailsElectronicBillingAddress");
    String billingElectronicBillingOperator = requestContext.getString("billingDetailsElectronicBillingOperator");
    String billingCompanyIdentifier = requestContext.getString("billingDetailsCompanyIdentifier");
    String billingReferenceNumber = requestContext.getString("billingDetailsReferenceNumber");
    String billingNotes = requestContext.getString("billingDetailsNotes");

    if (school.getBillingDetails() == null) {
      BillingDetails billingDetails = billingDetailsDAO.create(
          billingPersonName, billingCompanyName, billingStreetAddress1, billingStreetAddress2, 
          billingPostalCode, billingCity, billingRegion, billingCountry, billingPhoneNumber, 
          billingEmailAddress, billingElectronicBillingAddress, billingElectronicBillingOperator, 
          billingCompanyIdentifier, billingReferenceNumber, billingNotes);
      
      school = schoolDAO.updateBillingDetails(school, billingDetails);
    } else {
      BillingDetails billingDetails = school.getBillingDetails();
      billingDetailsDAO.updatePersonName(billingDetails, billingPersonName);
      billingDetailsDAO.updateCompanyName(billingDetails, billingCompanyName);
      billingDetailsDAO.updateStreetAddress1(billingDetails, billingStreetAddress1);
      billingDetailsDAO.updateStreetAddress2(billingDetails, billingStreetAddress2);
      billingDetailsDAO.updatePostalCode(billingDetails, billingPostalCode);
      billingDetailsDAO.updateCity(billingDetails, billingCity);
      billingDetailsDAO.updateRegion(billingDetails, billingRegion);
      billingDetailsDAO.updateCountry(billingDetails, billingCountry);
      billingDetailsDAO.updatePhoneNumber(billingDetails, billingPhoneNumber);
      billingDetailsDAO.updateEmailAddress(billingDetails, billingEmailAddress);
      billingDetailsDAO.updateElectronicBillingAddress(billingDetails, billingElectronicBillingAddress);
      billingDetailsDAO.updateElectronicBillingOperator(billingDetails, billingElectronicBillingOperator);
      billingDetailsDAO.updateCompanyIdentifier(billingDetails, billingCompanyIdentifier);
      billingDetailsDAO.updateReferenceNumber(billingDetails, billingReferenceNumber);
      billingDetailsDAO.updateNotes(billingDetails, billingNotes);
    }
    
    // Tags

    school = schoolDAO.updateTags(school, tagEntities);

    
    // ContactInfos
    
    ContactInfoUtils.readAndUpdateTypedContactInfos(requestContext, "contactInfos", school.getContactInfos());

    // Variables

    Integer variableCount = requestContext.getInteger("variablesTable.rowCount");
    if (variableCount != null) {
      for (int i = 0; i < variableCount; i++) {
        String colPrefix = "variablesTable." + i;
        String key = requestContext.getRequest().getParameter(colPrefix + ".key");
        String value = requestContext.getRequest().getParameter(colPrefix + ".value");
        schoolVariableDAO.setVariable(school, key, value);
      }
    }
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

}
