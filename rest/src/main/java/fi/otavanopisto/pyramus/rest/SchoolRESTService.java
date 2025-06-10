package fi.otavanopisto.pyramus.rest;

import java.util.List;
import java.util.Set;

import jakarta.ejb.Stateful;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import org.apache.commons.lang3.StringUtils;

import fi.otavanopisto.pyramus.domainmodel.base.Address;
import fi.otavanopisto.pyramus.domainmodel.base.BillingDetails;
import fi.otavanopisto.pyramus.domainmodel.base.ContactType;
import fi.otavanopisto.pyramus.domainmodel.base.ContactURL;
import fi.otavanopisto.pyramus.domainmodel.base.ContactURLType;
import fi.otavanopisto.pyramus.domainmodel.base.Email;
import fi.otavanopisto.pyramus.domainmodel.base.PhoneNumber;
import fi.otavanopisto.pyramus.domainmodel.base.School;
import fi.otavanopisto.pyramus.domainmodel.base.SchoolField;
import fi.otavanopisto.pyramus.domainmodel.base.SchoolVariableKey;
import fi.otavanopisto.pyramus.domainmodel.base.VariableType;
import fi.otavanopisto.pyramus.domainmodel.students.StudentGroup;
import fi.otavanopisto.pyramus.persistence.search.SearchResult;
import fi.otavanopisto.pyramus.rest.annotation.RESTPermit;
import fi.otavanopisto.pyramus.rest.controller.CommonController;
import fi.otavanopisto.pyramus.rest.controller.SchoolController;
import fi.otavanopisto.pyramus.rest.controller.StudentGroupController;
import fi.otavanopisto.pyramus.rest.controller.permissions.SchoolPermissions;
import fi.otavanopisto.pyramus.security.impl.SessionController;

@Path("/schools")
@Produces("application/json")
@Consumes("application/json")
@Stateful
@RequestScoped
public class SchoolRESTService extends AbstractRESTService {

  @Inject
  private SchoolController schoolController;

  @Inject
  private StudentGroupController studentGroupController;

  @Inject
  private CommonController commonController;

  @Inject
  private SessionController sessionController;

  @Inject
  private ObjectFactory objectFactory;

  @Path("/schools")
  @POST
  @RESTPermit(SchoolPermissions.CREATE_SCHOOL)
  public Response createSchool(fi.otavanopisto.pyramus.rest.model.School entity) {
    if (entity == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    if (entity.getFieldId() == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    String code = entity.getCode();
    String name = entity.getName();

    if (StringUtils.isBlank(code) || StringUtils.isBlank(name)) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    SchoolField schoolField = schoolController.findSchoolFieldById(entity.getFieldId());
    if (schoolField == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    StudentGroup studentGroup = null;
    if (entity.getStudentGroupId() != null) {
      studentGroup = studentGroupController.findStudentGroupById(entity.getStudentGroupId());
      if (studentGroup == null) {
        return Response.status(Status.BAD_REQUEST).build();
      }
    }

    BillingDetails billingDetails = null;
    School school = schoolController.createSchool(code, name, schoolField, studentGroup, billingDetails);
    if (entity.getTags() != null) {
      schoolController.updateSchoolTags(school, entity.getTags());
    }

    if (entity.getVariables() != null) {
      Set<String> variableKeys = entity.getVariables().keySet();
      for (String variableKey : variableKeys) {
        SchoolVariableKey schoolVariableKey = schoolController.findSchoolVariableKeyByVariableKey(variableKey);
        if (schoolVariableKey == null) {
          return Response.status(Status.BAD_REQUEST).build();
        }

        schoolController.createSchoolVariable(school, schoolVariableKey, entity.getVariables().get(variableKey));
      }
    }

    schoolController.updateSchoolAdditionalContactInfo(school, entity.getAdditionalContactInfo());

    return Response.ok(objectFactory.createModel(school)).build();
  }

  @Path("/schools")
  @GET
  @RESTPermit (SchoolPermissions.LIST_SCHOOLS)
  public Response listSchools(@DefaultValue("") @QueryParam("code") String code, 
      @DefaultValue("false") @QueryParam("filterArchived") boolean filterArchived,
      @DefaultValue("10") @QueryParam("resultsPerPage") int resultsPerPage,
      @DefaultValue("0") @QueryParam("page") int page) {
    SearchResult<School> searchSchools = schoolController.searchSchools(resultsPerPage, page, code, null, null, filterArchived);
    return Response.ok(objectFactory.createModel(searchSchools.getResults())).build();
  }

  @Path("/schools/{ID:[0-9]*}")
  @GET
  @RESTPermit(SchoolPermissions.FIND_SCHOOL)
  public Response findSchool(@PathParam("ID") Long id) {
    School school = schoolController.findSchoolById(id);
    if (school == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (school.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }

    return Response.ok(objectFactory.createModel(school)).build();
  }

  @Path("/schools/{ID:[0-9]*}")
  @PUT
  @RESTPermit(SchoolPermissions.UPDATE_SCHOOL)
  public Response updateSchool(@PathParam("ID") Long id, fi.otavanopisto.pyramus.rest.model.School entity) {
    if (entity == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    School school = schoolController.findSchoolById(id);
    if (school == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (school.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (entity.getFieldId() == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    String code = entity.getCode();
    String name = entity.getName();

    if (StringUtils.isBlank(code) || StringUtils.isBlank(name)) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    SchoolField schoolField = schoolController.findSchoolFieldById(entity.getFieldId());
    if (schoolField == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    StudentGroup studentGroup = null;
    if (entity.getStudentGroupId() != null) {
      studentGroup = studentGroupController.findStudentGroupById(entity.getStudentGroupId());
      if (studentGroup == null) {
        return Response.status(Status.BAD_REQUEST).build();
      }
    }

    schoolController.updateSchool(school, code, name, schoolField, studentGroup);
    schoolController.updateSchoolTags(school, entity.getTags());
    schoolController.updateSchoolVariables(school, entity.getVariables());
    schoolController.updateSchoolAdditionalContactInfo(school, entity.getAdditionalContactInfo());

    return Response.ok(objectFactory.createModel(school)).build();
  }

  @Path("/schools/{ID:[0-9]*}")
  @DELETE
  @RESTPermit(SchoolPermissions.DELETE_SCHOOL)
  public Response deleteSchool(@PathParam("ID") Long id,
      @DefaultValue("false") @QueryParam("permanent") Boolean permanent) {
    School school = schoolController.findSchoolById(id);
    if (school == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (permanent) {
      schoolController.deleteSchool(school);
    } else {
      schoolController.archiveSchool(school, sessionController.getUser());
    }

    return Response.noContent().build();
  }

  @Path("/schools/{SCHOOLID:[0-9]*}/emails")
  @GET
  @RESTPermit(SchoolPermissions.LIST_SCHOOLEMAILS)
  public Response listSchoolEmails(@PathParam("SCHOOLID") Long schoolId) {
    School school = schoolController.findSchoolById(schoolId);
    if (school == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (school.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }

    List<Email> emails = school.getContactInfo().getEmails();
    if (emails.isEmpty()) {
      return Response.noContent().build();
    }

    return Response.ok(objectFactory.createModel(emails)).build();
  }

  @Path("/schools/{SCHOOLID:[0-9]*}/emails")
  @POST
  @RESTPermit(SchoolPermissions.CREATE_SCHOOLEMAIL)
  public Response createSchoolEmail(@PathParam("SCHOOLID") Long schoolId,
      fi.otavanopisto.pyramus.rest.model.Email email) {
    if (email == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    Long contactTypeId = email.getContactTypeId();
    Boolean defaultAddress = email.getDefaultAddress();
    String address = email.getAddress();

    if (contactTypeId == null || defaultAddress == null || StringUtils.isBlank(address)) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    School school = schoolController.findSchoolById(schoolId);
    if (school == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (school.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }

    ContactType contactType = commonController.findContactTypeById(contactTypeId);
    if (contactType == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    return Response
        .ok(objectFactory.createModel(schoolController.addSchoolEmail(school, contactType, address, defaultAddress)))
        .build();
  }

  @Path("/schools/{SCHOOLID:[0-9]*}/emails/{ID:[0-9]*}")
  @GET
  @RESTPermit(SchoolPermissions.FIND_SCHOOLEMAIL)
  public Response findSchoolEmail(@PathParam("SCHOOLID") Long schoolId, @PathParam("ID") Long id) {
    School school = schoolController.findSchoolById(schoolId);
    if (school == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (school.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }

    Email email = commonController.findEmailById(id);
    if (email == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (!email.getContactInfo().getId().equals(school.getContactInfo().getId())) {
      return Response.status(Status.NOT_FOUND).build();
    }

    return Response.ok(objectFactory.createModel(email)).build();
  }

  @Path("/schools/{SCHOOLID:[0-9]*}/emails/{ID:[0-9]*}")
  @DELETE
  @RESTPermit(SchoolPermissions.DELETE_SCHOOLEMAIL)
  public Response deleteSchoolEmail(@PathParam("SCHOOLID") Long schoolId, @PathParam("ID") Long id) {
    School school = schoolController.findSchoolById(schoolId);
    if (school == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (school.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }

    Email email = commonController.findEmailById(id);
    if (email == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (!email.getContactInfo().getId().equals(school.getContactInfo().getId())) {
      return Response.status(Status.NOT_FOUND).build();
    }

    commonController.deleteEmail(email);

    return Response.noContent().build();
  }

  @Path("/schools/{SCHOOLID:[0-9]*}/addresses")
  @GET
  @RESTPermit(SchoolPermissions.LIST_SCHOOLADDRESSS)
  public Response listSchoolAddresses(@PathParam("SCHOOLID") Long schoolId) {
    School school = schoolController.findSchoolById(schoolId);
    if (school == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (school.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }

    List<Address> addresses = school.getContactInfo().getAddresses();
    if (addresses.isEmpty()) {
      return Response.noContent().build();
    }

    return Response.ok(objectFactory.createModel(addresses)).build();
  }

  @Path("/schools/{SCHOOLID:[0-9]*}/addresses")
  @POST
  @RESTPermit(SchoolPermissions.CREATE_SCHOOLADDRESS)
  public Response createSchoolAddress(@PathParam("SCHOOLID") Long schoolId,
      fi.otavanopisto.pyramus.rest.model.Address address) {
    if (address == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    Long contactTypeId = address.getContactTypeId();
    Boolean defaultAddress = address.getDefaultAddress();
    String name = address.getName();
    String streetAddress = address.getStreetAddress();
    String postalCode = address.getPostalCode();
    String country = address.getCountry();
    String city = address.getCity();

    if (contactTypeId == null || defaultAddress == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    School school = schoolController.findSchoolById(schoolId);
    if (school == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (school.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }

    ContactType contactType = commonController.findContactTypeById(contactTypeId);
    if (contactType == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    return Response.ok(objectFactory.createModel(schoolController.addSchoolAddress(school, contactType, defaultAddress,
        name, streetAddress, postalCode, city, country))).build();
  }

  @Path("/schools/{SCHOOLID:[0-9]*}/addresses/{ID:[0-9]*}")
  @GET
  @RESTPermit(SchoolPermissions.FIND_SCHOOLADDRESS)
  public Response findSchoolAddress(@PathParam("SCHOOLID") Long schoolId, @PathParam("ID") Long id) {
    School school = schoolController.findSchoolById(schoolId);
    if (school == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (school.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }

    Address address = commonController.findAddressById(id);
    if (address == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (!address.getContactInfo().getId().equals(school.getContactInfo().getId())) {
      return Response.status(Status.NOT_FOUND).build();
    }

    return Response.ok(objectFactory.createModel(address)).build();
  }

  @Path("/schools/{SCHOOLID:[0-9]*}/addresses/{ID:[0-9]*}")
  @DELETE
  @RESTPermit(SchoolPermissions.DELETE_SCHOOLADDRESS)
  public Response deleteSchoolAddress(@PathParam("SCHOOLID") Long schoolId, @PathParam("ID") Long id) {
    School school = schoolController.findSchoolById(schoolId);
    if (school == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (school.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }

    Address address = commonController.findAddressById(id);
    if (address == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (!address.getContactInfo().getId().equals(school.getContactInfo().getId())) {
      return Response.status(Status.NOT_FOUND).build();
    }

    commonController.deleteAddress(address);

    return Response.noContent().build();
  }

  @Path("/schools/{SCHOOLID:[0-9]*}/phoneNumbers")
  @GET
  @RESTPermit(SchoolPermissions.LIST_SCHOOLPHONENUMBERS)
  public Response listSchoolPhoneNumbers(@PathParam("SCHOOLID") Long schoolId) {
    School school = schoolController.findSchoolById(schoolId);
    if (school == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (school.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }

    List<PhoneNumber> phoneNumbers = school.getContactInfo().getPhoneNumbers();
    if (phoneNumbers.isEmpty()) {
      return Response.noContent().build();
    }

    return Response.ok(objectFactory.createModel(phoneNumbers)).build();
  }

  @Path("/schools/{SCHOOLID:[0-9]*}/phoneNumbers")
  @POST
  @RESTPermit(SchoolPermissions.CREATE_SCHOOLPHONENUMBER)
  public Response createSchoolPhoneNumber(@PathParam("SCHOOLID") Long schoolId,
      fi.otavanopisto.pyramus.rest.model.PhoneNumber phoneNumber) {
    if (phoneNumber == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    Long contactTypeId = phoneNumber.getContactTypeId();
    Boolean defaultNumber = phoneNumber.getDefaultNumber();
    String number = phoneNumber.getNumber();

    if (contactTypeId == null || defaultNumber == null || StringUtils.isBlank(number)) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    School school = schoolController.findSchoolById(schoolId);
    if (school == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (school.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }

    ContactType contactType = commonController.findContactTypeById(contactTypeId);
    if (contactType == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    return Response.ok(
        objectFactory.createModel(schoolController.addSchoolPhoneNumber(school, contactType, number, defaultNumber)))
        .build();
  }

  @Path("/schools/{SCHOOLID:[0-9]*}/phoneNumbers/{ID:[0-9]*}")
  @GET
  @RESTPermit(SchoolPermissions.FIND_SCHOOLPHONENUMBER)
  public Response findSchoolPhoneNumber(@PathParam("SCHOOLID") Long schoolId, @PathParam("ID") Long id) {
    School school = schoolController.findSchoolById(schoolId);
    if (school == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (school.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }

    PhoneNumber phoneNumber = commonController.findPhoneNumberById(id);
    if (phoneNumber == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (!phoneNumber.getContactInfo().getId().equals(school.getContactInfo().getId())) {
      return Response.status(Status.NOT_FOUND).build();
    }

    return Response.ok(objectFactory.createModel(phoneNumber)).build();
  }

  @Path("/schools/{SCHOOLID:[0-9]*}/phoneNumbers/{ID:[0-9]*}")
  @DELETE
  @RESTPermit(SchoolPermissions.DELETE_SCHOOLPHONENUMBER)
  public Response deleteSchoolPhoneNumber(@PathParam("SCHOOLID") Long schoolId, @PathParam("ID") Long id) {
    School school = schoolController.findSchoolById(schoolId);
    if (school == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (school.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }

    PhoneNumber phoneNumber = commonController.findPhoneNumberById(id);
    if (phoneNumber == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (!phoneNumber.getContactInfo().getId().equals(school.getContactInfo().getId())) {
      return Response.status(Status.NOT_FOUND).build();
    }

    commonController.deletePhoneNumber(phoneNumber);

    return Response.noContent().build();
  }

  @Path("/schools/{SCHOOLID:[0-9]*}/contactURLs")
  @GET
  @RESTPermit(SchoolPermissions.LIST_SCHOOLCONTACTURLS)
  public Response listSchoolContactURLs(@PathParam("SCHOOLID") Long schoolId) {
    School school = schoolController.findSchoolById(schoolId);
    if (school == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (school.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }

    List<ContactURL> contactUrls = school.getContactInfo().getContactURLs();
    if (contactUrls.isEmpty()) {
      return Response.noContent().build();
    }

    return Response.ok(objectFactory.createModel(contactUrls)).build();
  }

  @Path("/schools/{SCHOOLID:[0-9]*}/contactURLs")
  @POST
  @RESTPermit(SchoolPermissions.CREATE_SCHOOLCONTACTURL)
  public Response createSchoolContactURL(@PathParam("SCHOOLID") Long schoolId,
      fi.otavanopisto.pyramus.rest.model.ContactURL contactURL) {
    if (contactURL == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    Long contactURLTypeId = contactURL.getContactURLTypeId();
    String url = contactURL.getUrl();

    if (contactURLTypeId == null || StringUtils.isBlank(url)) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    School school = schoolController.findSchoolById(schoolId);
    if (school == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (school.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }

    ContactURLType contactURLType = commonController.findContactURLTypeById(contactURLTypeId);
    if (contactURLType == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    return Response.ok(objectFactory.createModel(schoolController.addSchoolContactURL(school, contactURLType, url)))
        .build();
  }

  @Path("/schools/{SCHOOLID:[0-9]*}/contactURLs/{ID:[0-9]*}")
  @GET
  @RESTPermit(SchoolPermissions.FIND_SCHOOLCONTACTURL)
  public Response findSchoolContactURL(@PathParam("SCHOOLID") Long schoolId, @PathParam("ID") Long id) {
    School school = schoolController.findSchoolById(schoolId);
    if (school == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (school.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }

    ContactURL contactURL = commonController.findContactURLById(id);
    if (contactURL == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (!contactURL.getContactInfo().getId().equals(school.getContactInfo().getId())) {
      return Response.status(Status.NOT_FOUND).build();
    }

    return Response.ok(objectFactory.createModel(contactURL)).build();
  }

  @Path("/schools/{SCHOOLID:[0-9]*}/contactURLs/{ID:[0-9]*}")
  @DELETE
  @RESTPermit(SchoolPermissions.DELETE_SCHOOLCONTACTURL)
  public Response deleteSchoolContactURL(@PathParam("SCHOOLID") Long schoolId, @PathParam("ID") Long id) {
    School school = schoolController.findSchoolById(schoolId);
    if (school == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (school.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }

    ContactURL contactURL = commonController.findContactURLById(id);
    if (contactURL == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (!contactURL.getContactInfo().getId().equals(school.getContactInfo().getId())) {
      return Response.status(Status.NOT_FOUND).build();
    }

    commonController.deleteContactURL(contactURL);

    return Response.noContent().build();
  }

  @Path("/schoolFields")
  @POST
  @RESTPermit(SchoolPermissions.CREATE_SCHOOLFIELD)
  public Response createSchoolField(fi.otavanopisto.pyramus.rest.model.SchoolField entity) {
    String name = entity.getName();
    if (StringUtils.isBlank(name)) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    return Response.ok(objectFactory.createModel(schoolController.createSchoolField(name))).build();
  }

  @Path("/schoolFields")
  @GET
  @RESTPermit(SchoolPermissions.LIST_SCHOOLFIELDS)
  public Response listSchoolFields() {
    List<SchoolField> schoolFields = schoolController.listSchoolFields();
    if (schoolFields.isEmpty()) {
      return Response.noContent().build();
    }

    return Response.ok(objectFactory.createModel(schoolFields)).build();
  }

  @Path("/schoolFields/{ID:[0-9]*}")
  @GET
  @RESTPermit(SchoolPermissions.FIND_SCHOOLFIELD)
  public Response findSchoolFieldByID(@PathParam("ID") Long id) {
    SchoolField schoolField = schoolController.findSchoolFieldById(id);
    if (schoolField == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (schoolField.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }

    return Response.ok(objectFactory.createModel(schoolField)).build();
  }

  @Path("/schoolFields/{ID:[0-9]*}")
  @PUT
  @RESTPermit(SchoolPermissions.UPDATE_SCHOOLFIELD)
  public Response updateSchoolField(@PathParam("ID") Long id, fi.otavanopisto.pyramus.rest.model.SchoolField entity) {
    if (entity == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    SchoolField schoolField = schoolController.findSchoolFieldById(id);
    if (schoolField == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    String name = entity.getName();

    if (StringUtils.isBlank(name)) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    return Response.ok(objectFactory.createModel(schoolController.updateSchoolField(schoolField, name))).build();
  }

  @Path("/schoolFields/{ID:[0-9]*}")
  @DELETE
  @RESTPermit(SchoolPermissions.DELETE_SCHOOLFIELD)
  public Response deleteSchoolField(@PathParam("ID") Long id,
      @DefaultValue("false") @QueryParam("permanent") Boolean permanent) {
    SchoolField schoolField = schoolController.findSchoolFieldById(id);
    if (schoolField == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (permanent) {
      schoolController.deleteSchoolField(schoolField);
    } else {
      schoolController.archiveSchoolField(schoolField, sessionController.getUser());
    }

    return Response.noContent().build();
  }

  @Path("/variables")
  @POST
  @RESTPermit(SchoolPermissions.CREATE_SCHOOLVARIABLEKEY)
  public Response createVariable(fi.otavanopisto.pyramus.rest.model.VariableKey entity) {
    if (entity == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    if (StringUtils.isBlank(entity.getKey()) || StringUtils.isBlank(entity.getName()) || entity.getType() == null
        || entity.getUserEditable() == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    VariableType variableType = null;
    switch (entity.getType()) {
      case BOOLEAN:
        variableType = VariableType.BOOLEAN;
      break;
      case DATE:
        variableType = VariableType.DATE;
      break;
      case NUMBER:
        variableType = VariableType.NUMBER;
      break;
      case TEXT:
        variableType = VariableType.TEXT;
      break;
    }

    SchoolVariableKey schoolVariableKey = schoolController.createSchoolVariableKey(entity.getKey(), entity.getName(),
        variableType, entity.getUserEditable());
    return Response.ok(objectFactory.createModel(schoolVariableKey)).build();
  }

  @Path("/variables")
  @GET
  @RESTPermit(SchoolPermissions.LIST_SCHOOLVARIABLEKEYS)
  public Response listVariables() {
    List<SchoolVariableKey> variableKeys = schoolController.listSchoolVariableKeys();
    if (variableKeys.isEmpty()) {
      return Response.noContent().build();
    }

    return Response.ok(objectFactory.createModel(variableKeys)).build();
  }

  @Path("/variables/{KEY}")
  @GET
  @RESTPermit(SchoolPermissions.FIND_SCHOOLVARIABLEKEY)
  public Response findVariable(@PathParam("KEY") String key) {
    SchoolVariableKey schoolVariableKey = schoolController.findSchoolVariableKeyByVariableKey(key);
    if (schoolVariableKey == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    return Response.ok(objectFactory.createModel(schoolVariableKey)).build();
  }

  @Path("/variables/{KEY}")
  @PUT
  @RESTPermit(SchoolPermissions.UPDATE_SCHOOLVARIABLEKEY)
  public Response updateVariable(@PathParam("KEY") String key, fi.otavanopisto.pyramus.rest.model.VariableKey entity) {
    if (entity == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    if (StringUtils.isBlank(entity.getName()) || entity.getType() == null || entity.getUserEditable() == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    SchoolVariableKey schoolVariableKey = schoolController.findSchoolVariableKeyByVariableKey(key);
    if (schoolVariableKey == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    VariableType variableType = null;
    switch (entity.getType()) {
      case BOOLEAN:
        variableType = VariableType.BOOLEAN;
      break;
      case DATE:
        variableType = VariableType.DATE;
      break;
      case NUMBER:
        variableType = VariableType.NUMBER;
      break;
      case TEXT:
        variableType = VariableType.TEXT;
      break;
    }

    schoolController.updateSchoolVariableKey(schoolVariableKey, entity.getName(), variableType,
        entity.getUserEditable());

    return Response.ok(objectFactory.createModel(schoolVariableKey)).build();
  }

  @Path("/variables/{KEY}")
  @DELETE
  @RESTPermit(SchoolPermissions.DELETE_SCHOOLVARIABLEKEY)
  public Response deleteVariable(@PathParam("KEY") String key) {
    SchoolVariableKey schoolVariableKey = schoolController.findSchoolVariableKeyByVariableKey(key);
    if (schoolVariableKey == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    schoolController.deleteSchoolVariableKey(schoolVariableKey);

    return Response.noContent().build();
  }

}
