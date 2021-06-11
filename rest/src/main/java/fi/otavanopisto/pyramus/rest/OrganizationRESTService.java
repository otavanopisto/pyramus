package fi.otavanopisto.pyramus.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import fi.otavanopisto.pyramus.dao.base.BillingDetailsDAO;
import fi.otavanopisto.pyramus.dao.base.EducationTypeDAO;
import fi.otavanopisto.pyramus.dao.base.OrganizationContactPersonDAO;
import fi.otavanopisto.pyramus.dao.base.OrganizationContractPeriodDAO;
import fi.otavanopisto.pyramus.dao.base.OrganizationDAO;
import fi.otavanopisto.pyramus.domainmodel.base.BillingDetails;
import fi.otavanopisto.pyramus.domainmodel.base.EducationType;
import fi.otavanopisto.pyramus.domainmodel.base.Organization;
import fi.otavanopisto.pyramus.domainmodel.base.OrganizationContactPerson;
import fi.otavanopisto.pyramus.domainmodel.base.OrganizationContactPersonType;
import fi.otavanopisto.pyramus.domainmodel.base.OrganizationContractPeriod;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.framework.UserUtils;
import fi.otavanopisto.pyramus.rest.annotation.RESTPermit;
import fi.otavanopisto.pyramus.security.impl.SessionController;
import fi.otavanopisto.pyramus.security.impl.permissions.OrganizationPermissions;

@Path("/organizations")
@Produces("application/json")
@Consumes("application/json")
@Stateful
@RequestScoped
public class OrganizationRESTService extends AbstractRESTService {

  @Inject
  private BillingDetailsDAO billingDetailsDAO;
  
  @Inject
  private EducationTypeDAO educationTypeDAO;
  
  @Inject
  private OrganizationDAO organizationDAO;
  
  @Inject
  private OrganizationContactPersonDAO organizationContactPersonDAO;
  
  @Inject
  private OrganizationContractPeriodDAO organizationContractPeriodDAO;

  @Inject
  private SessionController sessionController;

  @Inject
  private ObjectFactory objectFactory;
  
  
  
  @Path("/")
  @POST
  @RESTPermit (OrganizationPermissions.CREATE_ORGANIZATION)
  public Response createOrganization(fi.otavanopisto.pyramus.rest.model.Organization entity) {
    if ((entity == null) || StringUtils.isBlank(entity.getName())) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    EducationType educationType = null;
    if ((entity.getEducationType() != null) && (entity.getEducationType().getId() != null)) {
      educationType = educationTypeDAO.findById(entity.getEducationType().getId());
    }
    
    Organization organization = organizationDAO.create(entity.getName(), educationType);

    if (entity.getBillingDetails() != null) {
      BillingDetails billingDetails = billingDetailsDAO.create(
          entity.getBillingDetails().getPersonName(), 
          entity.getBillingDetails().getCompanyName(), 
          entity.getBillingDetails().getStreetAddress1(), 
          entity.getBillingDetails().getStreetAddress2(), 
          entity.getBillingDetails().getPostalCode(), 
          entity.getBillingDetails().getCity(), 
          entity.getBillingDetails().getRegion(), 
          entity.getBillingDetails().getCountry(), 
          entity.getBillingDetails().getPhoneNumber(), 
          entity.getBillingDetails().getEmailAddress(), 
          entity.getBillingDetails().getElectronicBillingAddress(), 
          entity.getBillingDetails().getElectronicBillingOperator(), 
          entity.getBillingDetails().getCompanyIdentifier(), 
          entity.getBillingDetails().getReferenceNumber(), 
          entity.getBillingDetails().getNotes());
      organization = organizationDAO.updateBillingDetails(organization, billingDetails);
    }

    // Contract Periods
    
    List<fi.otavanopisto.pyramus.rest.model.OrganizationContractPeriod> updatedContractPeriods = CollectionUtils.isNotEmpty(entity.getContractPeriods()) ? entity.getContractPeriods() : Collections.emptyList();

    for (fi.otavanopisto.pyramus.rest.model.OrganizationContractPeriod updatedContractPeriod : updatedContractPeriods) {
      Date begin = updatedContractPeriod.getBegin() != null ? java.sql.Date.valueOf(updatedContractPeriod.getBegin()) : null;
      Date end = updatedContractPeriod.getEnd() != null ? java.sql.Date.valueOf(updatedContractPeriod.getEnd()) : null;
      organizationContractPeriodDAO.create(organization, begin, end);
    }
    
    // Contact Persons
    
    List<fi.otavanopisto.pyramus.rest.model.OrganizationContactPerson> contactPersons = CollectionUtils.isNotEmpty(entity.getContactPersons()) ? entity.getContactPersons() : Collections.emptyList();

    for (fi.otavanopisto.pyramus.rest.model.OrganizationContactPerson contactPerson : contactPersons) {
      fi.otavanopisto.pyramus.rest.model.OrganizationContactPersonType contactPersonType = contactPerson.getType();
      OrganizationContactPersonType type = contactPersonType != null ? OrganizationContactPersonType.valueOf(contactPersonType.name()) : null;
      organizationContactPersonDAO.create(organization, type, contactPerson.getName(), contactPerson.getEmail(), contactPerson.getPhone(), contactPerson.getTitle());
    }
    
    return Response.ok(objectFactory.createModel(organization)).build();
  }

  @Path("/")
  @GET
  @RESTPermit (OrganizationPermissions.LIST_ORGANIZATIONS)
  public Response listOrganizations(@DefaultValue("false") @QueryParam("includeArchived") boolean showArchived) {
    List<Organization> organizations;
    
    if (UserUtils.canAccessAllOrganizations(sessionController.getUser())) {
      if (showArchived) {
        organizations = organizationDAO.listAll();
      } else {
        organizations = organizationDAO.listUnarchived();
      }
    } else {
      User user = sessionController.getUser();
      organizations = (user != null && user.getOrganization() != null) ? Arrays.asList(user.getOrganization()) : new ArrayList<>();
    }
    
    return Response.ok(objectFactory.createModel(organizations)).build();
  }
  
  @Path("/{ID:[0-9]*}")
  @GET
  @RESTPermit (OrganizationPermissions.FIND_ORGANIZATION)
  public Response findOrganization(@PathParam("ID") Long id) {
    Organization organization = organizationDAO.findById(id);
    if (organization == null || organization.getArchived() || !UserUtils.canAccessOrganization(sessionController.getUser(), organization)) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    return Response.ok(objectFactory.createModel(organization)).build();
  }

  @Path("/{ID:[0-9]*}")
  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @RESTPermit (OrganizationPermissions.UPDATE_ORGANIZATION)
  public Response updateOrganization(@PathParam("ID") Long id, fi.otavanopisto.pyramus.rest.model.Organization entity) {
    if (entity == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    String name = entity.getName();
    if (StringUtils.isBlank(name) || entity.getBillingDetails() == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    Organization organization = organizationDAO.findById(id);
    if (organization == null || organization.getArchived() || !UserUtils.canAccessOrganization(sessionController.getUser(), organization)) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    EducationType educationType = organization.getEducationType();
    if ((entity.getEducationType() != null) && (entity.getEducationType().getId() != null)) {
      educationType = educationTypeDAO.findById(entity.getEducationType().getId());
    }
    
    if (organization.getBillingDetails() == null) {
      BillingDetails billingDetails = billingDetailsDAO.create(
          entity.getBillingDetails().getPersonName(), 
          entity.getBillingDetails().getCompanyName(), 
          entity.getBillingDetails().getStreetAddress1(), 
          entity.getBillingDetails().getStreetAddress2(), 
          entity.getBillingDetails().getPostalCode(), 
          entity.getBillingDetails().getCity(), 
          entity.getBillingDetails().getRegion(), 
          entity.getBillingDetails().getCountry(), 
          entity.getBillingDetails().getPhoneNumber(), 
          entity.getBillingDetails().getEmailAddress(), 
          entity.getBillingDetails().getElectronicBillingAddress(), 
          entity.getBillingDetails().getElectronicBillingOperator(), 
          entity.getBillingDetails().getCompanyIdentifier(), 
          entity.getBillingDetails().getReferenceNumber(), 
          entity.getBillingDetails().getNotes());
      organization = organizationDAO.updateBillingDetails(organization, billingDetails);
    } else {
      BillingDetails billingDetails = organization.getBillingDetails();
      billingDetails.setPersonName(entity.getBillingDetails().getPersonName()); 
      billingDetails.setCompanyName(entity.getBillingDetails().getCompanyName());
      billingDetails.setStreetAddress1(entity.getBillingDetails().getStreetAddress1()); 
      billingDetails.setStreetAddress2(entity.getBillingDetails().getStreetAddress2()); 
      billingDetails.setPostalCode(entity.getBillingDetails().getPostalCode()); 
      billingDetails.setCity(entity.getBillingDetails().getCity()); 
      billingDetails.setRegion(entity.getBillingDetails().getRegion()); 
      billingDetails.setCountry(entity.getBillingDetails().getCountry()); 
      billingDetails.setPhoneNumber(entity.getBillingDetails().getPhoneNumber()); 
      billingDetails.setEmailAddress(entity.getBillingDetails().getEmailAddress()); 
      billingDetails.setElectronicBillingAddress(entity.getBillingDetails().getElectronicBillingAddress()); 
      billingDetails.setElectronicBillingOperator(entity.getBillingDetails().getElectronicBillingOperator()); 
      billingDetails.setCompanyIdentifier(entity.getBillingDetails().getCompanyIdentifier()); 
      billingDetails.setReferenceNumber(entity.getBillingDetails().getReferenceNumber()); 
      billingDetails.setNotes(entity.getBillingDetails().getNotes());
    }
    
    organization = organizationDAO.update(organization, name, educationType);

    // Update Contract Periods
    
    List<OrganizationContractPeriod> existingContractPeriods = organizationContractPeriodDAO.listBy(organization);
    List<fi.otavanopisto.pyramus.rest.model.OrganizationContractPeriod> updatedContractPeriods = CollectionUtils.isNotEmpty(entity.getContractPeriods()) ? entity.getContractPeriods() : Collections.emptyList();

    for (fi.otavanopisto.pyramus.rest.model.OrganizationContractPeriod updatedContractPeriod : updatedContractPeriods) {
      Date begin = updatedContractPeriod.getBegin() != null ? java.sql.Date.valueOf(updatedContractPeriod.getBegin()) : null;
      Date end = updatedContractPeriod.getEnd() != null ? java.sql.Date.valueOf(updatedContractPeriod.getEnd()) : null;
      
      if ((updatedContractPeriod.getId() == null) || NumberUtils.LONG_MINUS_ONE.equals(updatedContractPeriod.getId())) {
        organizationContractPeriodDAO.create(organization, begin, end);
      } else {
        OrganizationContractPeriod contractPeriod = organizationContractPeriodDAO.findById(updatedContractPeriod.getId());
        organizationContractPeriodDAO.update(contractPeriod, begin, end);
      }
    }
    
    Set<Long> remainingContractPeriodIds = updatedContractPeriods.stream().map(contractPeriod -> contractPeriod.getId()).collect(Collectors.toSet());
    
    existingContractPeriods.forEach(contractPeriod -> {
      if (!remainingContractPeriodIds.contains(contractPeriod.getId())) {
        organizationContractPeriodDAO.delete(contractPeriod);
      }
    });
    
    // Update Contact Persons
    
    List<OrganizationContactPerson> existingContactPersons = organizationContactPersonDAO.listBy(organization);
    List<fi.otavanopisto.pyramus.rest.model.OrganizationContactPerson> updatedContactPersons = CollectionUtils.isNotEmpty(entity.getContactPersons()) ? entity.getContactPersons() : Collections.emptyList();

    for (fi.otavanopisto.pyramus.rest.model.OrganizationContactPerson updatedContactPerson : updatedContactPersons) {
      fi.otavanopisto.pyramus.rest.model.OrganizationContactPersonType contactPersonType = updatedContactPerson.getType();
      OrganizationContactPersonType type = contactPersonType != null ? OrganizationContactPersonType.valueOf(contactPersonType.name()) : null;
      
      if ((updatedContactPerson.getId() == null) || NumberUtils.LONG_MINUS_ONE.equals(updatedContactPerson.getId())) {
        organizationContactPersonDAO.create(organization, type, updatedContactPerson.getName(), updatedContactPerson.getEmail(), updatedContactPerson.getPhone(), updatedContactPerson.getTitle());
      } else {
        OrganizationContactPerson contactPerson = organizationContactPersonDAO.findById(updatedContactPerson.getId());
        organizationContactPersonDAO.update(contactPerson, type, updatedContactPerson.getName(), updatedContactPerson.getEmail(), updatedContactPerson.getPhone(), updatedContactPerson.getTitle());
      }
    }
    
    Set<Long> remainingIds = updatedContactPersons.stream().map(contactPerson -> contactPerson.getId()).collect(Collectors.toSet());
    
    existingContactPersons.forEach(contactPerson -> {
      if (!remainingIds.contains(contactPerson.getId())) {
        organizationContactPersonDAO.delete(contactPerson);
      }
    });
    
    return Response.ok(objectFactory.createModel(organization)).build();
  }
  
  @Path("/{ID:[0-9]*}")
  @DELETE
  @RESTPermit (OrganizationPermissions.DELETE_ORGANIZATION)
  public Response deleteOrganization(@PathParam("ID") Long id, @DefaultValue ("false") @QueryParam ("permanent") Boolean permanent) {
    Organization organization = organizationDAO.findById(id);
    if (organization == null || !UserUtils.canAccessOrganization(sessionController.getUser(), organization)) {
      return Response.status(Status.NOT_FOUND).build();
    }
  
    if (permanent) {
      organizationDAO.delete(organization);
    } else {
      organizationDAO.archive(organization);
    }
    
    return Response.noContent().build();
  }
  
  @Path("/{ID:[0-9]*}/contactPersons")
  @GET
  @RESTPermit (OrganizationPermissions.LIST_ORGANIZATION_CONTACT_PERSONS)
  public Response listOrganizationContactPersons(@PathParam("ID") Long organizationId) {
    Organization organization = organizationDAO.findById(organizationId);
    if (organization == null || organization.getArchived() || !UserUtils.canAccessOrganization(sessionController.getUser(), organization)) {
      return Response.status(Status.NOT_FOUND).build();
    }
    List<OrganizationContactPerson> contactPersons = organizationContactPersonDAO.listBy(organization);
    
    List<fi.otavanopisto.pyramus.rest.model.OrganizationContactPerson> restContactPersons = new ArrayList<>();
    for (OrganizationContactPerson contactPerson : contactPersons) {
      restContactPersons.add(createRestModel(contactPerson));
    }
    return Response.ok(restContactPersons).build();
  }
  
  private fi.otavanopisto.pyramus.rest.model.OrganizationContactPerson createRestModel(OrganizationContactPerson contactPerson) {
    fi.otavanopisto.pyramus.rest.model.OrganizationContactPerson restModel = new fi.otavanopisto.pyramus.rest.model.OrganizationContactPerson();
    fi.otavanopisto.pyramus.rest.model.OrganizationContactPersonType type = contactPerson.getType() != null ? fi.otavanopisto.pyramus.rest.model.OrganizationContactPersonType.valueOf(contactPerson.getType().name()) : null;
    restModel.setEmail(contactPerson.getEmail());
    restModel.setName(contactPerson.getName());
    restModel.setPhone(contactPerson.getPhone());
    restModel.setTitle(contactPerson.getTitle());
    restModel.setType(type);
    return restModel;
  }
  
}
