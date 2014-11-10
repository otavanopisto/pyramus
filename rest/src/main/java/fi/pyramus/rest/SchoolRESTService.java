package fi.pyramus.rest;

import java.util.List;
import java.util.Set;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang3.StringUtils;

import fi.pyramus.domainmodel.base.Address;
import fi.pyramus.domainmodel.base.ContactType;
import fi.pyramus.domainmodel.base.ContactURL;
import fi.pyramus.domainmodel.base.ContactURLType;
import fi.pyramus.domainmodel.base.Email;
import fi.pyramus.domainmodel.base.PhoneNumber;
import fi.pyramus.domainmodel.base.School;
import fi.pyramus.domainmodel.base.SchoolField;
import fi.pyramus.domainmodel.base.SchoolVariableKey;
import fi.pyramus.domainmodel.base.VariableType;
import fi.pyramus.rest.controller.CommonController;
import fi.pyramus.rest.controller.SchoolController;

@Path("/schools")
@Produces("application/json")
@Consumes("application/json")
@Stateful
@RequestScoped
public class SchoolRESTService extends AbstractRESTService {
  
  @Inject
  private SchoolController schoolController;
  
  @Inject
  private CommonController commonController;

  @Inject
  private ObjectFactory objectFactory;
  
  @Path("/schools")
  @POST
  public Response createSchool(fi.pyramus.rest.model.School entity) {
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
    
    School school = schoolController.createSchool(code, name, schoolField);
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
  public Response listSchools(@DefaultValue("false") @QueryParam("filterArchived") boolean filterArchived) {
    List<School> schools;
    
    if (filterArchived) {
      schools = schoolController.listUnarchivedSchools();
    } else {
      schools = schoolController.listSchools();
    }
    
    if (schools.isEmpty()) {
      return Response.noContent().build();
    }
    
    return Response.ok(objectFactory.createModel(schools)).build();
  }
  
  @Path("/schools/{ID:[0-9]*}")
  @GET
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
  public Response updateSchool(@PathParam("ID") Long id, fi.pyramus.rest.model.School entity) {
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
    
    schoolController.updateSchool(school, code, name, schoolField);
    schoolController.updateSchoolTags(school, entity.getTags());
    schoolController.updateSchoolVariables(school, entity.getVariables());
    schoolController.updateSchoolAdditionalContactInfo(school, entity.getAdditionalContactInfo());
    
    return Response.ok(objectFactory.createModel(school)).build();
  }
  
  @Path("/schools/{ID:[0-9]*}")
  @DELETE
  public Response deleteSchool(@PathParam("ID") Long id, @DefaultValue ("false") @QueryParam ("permanent") Boolean permanent) {
    School school = schoolController.findSchoolById(id);
    if (school == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
  
    if (permanent) {
      schoolController.deleteSchool(school);
    } else {
      schoolController.archiveSchool(school, getLoggedUser());
    }
    
    return Response.noContent().build();
  }
  
  @Path("/schools/{SCHOOLID:[0-9]*}/emails")
  @GET
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
  public Response createSchoolEmail(@PathParam("SCHOOLID") Long schoolId, fi.pyramus.rest.model.Email email) {
    if (email == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    Long contactTypeId = email.getContactTypeId();
    Boolean defaultAddress = email.getDefaultAddress();
    String address = email.getAddress();
    
    if ((contactTypeId == null) || (defaultAddress == null) || StringUtils.isBlank(address)) {
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
    
    return Response.ok(objectFactory.createModel(schoolController.addSchoolEmail(school, contactType, address, defaultAddress))).build();
  }
  
  @Path("/schools/{SCHOOLID:[0-9]*}/emails/{ID:[0-9]*}")
  @GET
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
  public Response createSchoolAddress(@PathParam("SCHOOLID") Long schoolId, fi.pyramus.rest.model.Address address) {
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
    
    if ((contactTypeId == null) || (defaultAddress == null)) {
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
    
    return Response.ok(objectFactory.createModel(schoolController.addSchoolAddress(school, contactType, defaultAddress, name, streetAddress, postalCode, city, country))).build();
  }
  
  @Path("/schools/{SCHOOLID:[0-9]*}/addresses/{ID:[0-9]*}")
  @GET
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
  public Response createSchoolPhoneNumber(@PathParam("SCHOOLID") Long schoolId, fi.pyramus.rest.model.PhoneNumber phoneNumber) {
    if (phoneNumber == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    Long contactTypeId = phoneNumber.getContactTypeId();
    Boolean defaultNumber = phoneNumber.getDefaultNumber();
    String number = phoneNumber.getNumber();
    
    if ((contactTypeId == null) || (defaultNumber == null) || StringUtils.isBlank(number)) {
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
    
    return Response.ok(objectFactory.createModel(schoolController.addSchoolPhoneNumber(school, contactType, number, defaultNumber))).build();
  }
  
  @Path("/schools/{SCHOOLID:[0-9]*}/phoneNumbers/{ID:[0-9]*}")
  @GET
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
  public Response createSchoolContactURL(@PathParam("SCHOOLID") Long schoolId, fi.pyramus.rest.model.ContactURL contactURL) {
    if (contactURL == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    Long contactURLTypeId = contactURL.getContactURLTypeId();
    String url = contactURL.getUrl();
    
    if ((contactURLTypeId == null) || StringUtils.isBlank(url)) {
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
    
    return Response.ok(objectFactory.createModel(schoolController.addSchoolContactURL(school, contactURLType, url))).build();
  }
  
  @Path("/schools/{SCHOOLID:[0-9]*}/contactURLs/{ID:[0-9]*}")
  @GET
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
  public Response createSchoolField(fi.pyramus.rest.model.SchoolField entity) {
    String name = entity.getName();
    if (StringUtils.isBlank(name)) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    return Response.ok(objectFactory.createModel(schoolController.createSchoolField(name))).build();
  }
  
  @Path("/schoolFields")
  @GET
  public Response listSchoolFields() {
    List<SchoolField> schoolFields = schoolController.listSchoolFields();
    if (schoolFields.isEmpty()) {
      return Response.noContent().build();
    }
    
    return Response.ok(objectFactory.createModel(schoolFields)).build();
  }
  
  @Path("/schoolFields/{ID:[0-9]*}")
  @GET
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
  public Response updateSchoolField(@PathParam("ID") Long id, fi.pyramus.rest.model.SchoolField entity) {
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
  public Response deleteSchoolField(@PathParam("ID") Long id, @DefaultValue ("false") @QueryParam ("permanent") Boolean permanent) {
    SchoolField schoolField = schoolController.findSchoolFieldById(id);
    if (schoolField == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (permanent) {
      schoolController.deleteSchoolField(schoolField);
    } else {
      schoolController.archiveSchoolField(schoolField, getLoggedUser());
    }
    
    return Response.noContent().build();
  }
  
  @Path("/variables")
  @POST
  public Response createVariable(fi.pyramus.rest.model.VariableKey entity) {
    if (entity == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    if (StringUtils.isBlank(entity.getKey())||StringUtils.isBlank(entity.getName())||(entity.getType() == null)||(entity.getUserEditable() == null)) {
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
    
    SchoolVariableKey schoolVariableKey = schoolController.createSchoolVariableKey(entity.getKey(), entity.getName(), variableType, entity.getUserEditable());
    return Response.ok(objectFactory.createModel(schoolVariableKey)).build();
  }
  
  @Path("/variables")
  @GET
  public Response listVariables() {
    List<SchoolVariableKey> variableKeys = schoolController.listSchoolVariableKeys();
    if (variableKeys.isEmpty()) {
      return Response.noContent().build();
    }
    
    return Response.ok(objectFactory.createModel(variableKeys)).build();
  }
  
  @Path("/variables/{KEY}")
  @GET
  public Response findVariable(@PathParam ("KEY") String key) {
    SchoolVariableKey schoolVariableKey = schoolController.findSchoolVariableKeyByVariableKey(key);
    if (schoolVariableKey == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    return Response.ok(objectFactory.createModel(schoolVariableKey)).build();
  }
  
  @Path("/variables/{KEY}")
  @PUT
  public Response updateVariable(@PathParam ("KEY") String key, fi.pyramus.rest.model.VariableKey entity) {
    if (entity == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    if (StringUtils.isBlank(entity.getName())||(entity.getType() == null)||(entity.getUserEditable() == null)) {
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
    
    schoolController.updateSchoolVariableKey(schoolVariableKey, entity.getName(), variableType, entity.getUserEditable());
    
    return Response.ok(objectFactory.createModel(schoolVariableKey)).build();
  }
  
  @Path("/variables/{KEY}")
  @DELETE
  public Response deleteVariable(@PathParam ("KEY") String key) {
    SchoolVariableKey schoolVariableKey = schoolController.findSchoolVariableKeyByVariableKey(key);
    if (schoolVariableKey == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    schoolController.deleteSchoolVariableKey(schoolVariableKey);
    
    return Response.noContent().build();
  }
  
}
