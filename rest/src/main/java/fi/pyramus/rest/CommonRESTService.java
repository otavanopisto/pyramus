package fi.pyramus.rest;

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

import org.apache.commons.lang.StringUtils;

import fi.pyramus.domainmodel.base.EducationType;
import fi.pyramus.rest.controller.CommonController;
import fi.pyramus.rest.tranquil.base.EducationTypeEntity;
import fi.tranquil.TranquilityBuilderFactory;

@Path("/common")
@Produces("application/json")
@Consumes("application/json")
@Stateful
@RequestScoped
public class CommonRESTService extends AbstractRESTService {
  @Inject
  private TranquilityBuilderFactory tranquilityBuilderFactory;
  @Inject
  private CommonController commonController;
  
  @Path("/educationTypes")
  @POST
  public Response createEducationType(EducationTypeEntity educationTypeEntity) {
    String name = educationTypeEntity.getName();
    String code = educationTypeEntity.getCode();
    if(!StringUtils.isBlank(name) && !StringUtils.isBlank(code)) {
      return Response.ok()
          .entity(tranqualise(commonController.createEducationType(name, code)))
          .build();
    } else {
      return Response.status(500).build();
    }
  }
  
  @Path("/educationTypes")
  @GET
  public Response findEducationTypes(@DefaultValue("false") @QueryParam("filterArchived") boolean filterArchived) {
    List<EducationType> educationTypes;
    if(filterArchived) {
      educationTypes = commonController.findUnarchivedEducationTypes();
    } else {
      educationTypes = commonController.findEducationTypes();
    }
    if (!educationTypes.isEmpty()) {
      return Response.ok()
          .entity(tranqualise(educationTypes))
          .build();
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }
  
  @Path("/educationTypes/{ID:[0-9]*}")
  @GET
  public Response findEducationTypeById(@PathParam("ID") Long id) {
    EducationType educationType = commonController.findEducationTypeById(id);
    if (educationType != null) {
      return Response.ok()
          .entity(tranqualise(educationType))
          .build();
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }
  
  @Path("/educationTypes/{ID:[0-9]*}")
  @PUT
  public Response updateEducationType(@PathParam("ID") Long id, EducationTypeEntity educationTypeEntity) {
    EducationType educationType = commonController.findEducationTypeById(id);
    if (educationType != null) {
      String name = educationTypeEntity.getName();
      String code = educationTypeEntity.getCode();
      if(!StringUtils.isBlank(name) && !StringUtils.isBlank(code)) {
        return Response.ok()
            .entity(tranqualise(commonController.updateEducationType(educationType, name, code)))
            .build();
      }
      if (!educationTypeEntity.getArchived()) {
        return Response.ok()
            .entity(tranqualise(commonController.unarchiveEducationType(educationType, getUser())))
            .build();
      } else {
        return Response.status(500).build();
      }
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }
  
  
  @Path("/educationTypes/{ID:[0-9]*}")
  @DELETE
  public Response archiveEducationType(@PathParam("ID") Long id) {
    EducationType educationType = commonController.findEducationTypeById(id);
    if (educationType != null) {
      return Response.ok()
          .entity(tranqualise(commonController.archiveEducationType(educationType, getUser())))
          .build();
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }
  
  @Override
  protected TranquilityBuilderFactory getTranquilityBuilderFactory() {
    return tranquilityBuilderFactory;
  }

}
