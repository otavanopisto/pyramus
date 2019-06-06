package fi.otavanopisto.pyramus.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

import fi.otavanopisto.pyramus.dao.base.OrganizationDAO;
import fi.otavanopisto.pyramus.domainmodel.base.Organization;
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
  private OrganizationDAO organizationDAO;
  
  @Inject
  private SessionController sessionController;

  @Inject
  private ObjectFactory objectFactory;
  
  @Path("/")
  @POST
  @RESTPermit (OrganizationPermissions.CREATE_ORGANIZATION)
  public Response createOrganization(fi.otavanopisto.pyramus.rest.model.Organization entity) {
    if (entity == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    String name = entity.getName();
    
    if (StringUtils.isBlank(name)) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    Organization organization = organizationDAO.create(name);
    
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
  @RESTPermit (OrganizationPermissions.UPDATE_ORGANIZATION)
  public Response updateOrganization(@PathParam("ID") Long id, fi.otavanopisto.pyramus.rest.model.Organization entity) {
    if (entity == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    String name = entity.getName();
    if (StringUtils.isBlank(name)) {
      return Response.status(Status.BAD_REQUEST).build();
    }

    Organization organization = organizationDAO.findById(id);
    if (organization == null || organization.getArchived() || !UserUtils.canAccessOrganization(sessionController.getUser(), organization)) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    organizationDAO.update(organization, name);
    
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
  
}
