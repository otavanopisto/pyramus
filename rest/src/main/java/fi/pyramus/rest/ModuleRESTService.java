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

import org.apache.commons.lang3.StringUtils;

import fi.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.pyramus.domainmodel.base.Subject;
import fi.pyramus.domainmodel.modules.Module;
import fi.pyramus.rest.controller.CommonController;
import fi.pyramus.rest.controller.ModuleController;
import fi.pyramus.rest.tranquil.modules.ModuleEntity;
import fi.tranquil.TranquilityBuilderFactory;

@Path("/modules")
@Produces("application/json")
@Consumes("application/json")
@Stateful
@RequestScoped
public class ModuleRESTService extends AbstractRESTService{
  @Inject
  TranquilityBuilderFactory tranquilityBuilderFactory;
  @Inject
  ModuleController moduleController;
  @Inject
  CommonController commonController;
  
  @Path("/modules")
  @POST
  public Response createModule(ModuleEntity moduleEntity) {
    try {
      String name = moduleEntity.getName();
      Subject subject = commonController.findSubjectById(moduleEntity.getSubject_id());
      Integer courseNumber = moduleEntity.getCourseNumber();
      EducationalTimeUnit moduleLengthTimeUnit = commonController.findEducationalTimeUnitById(moduleEntity.getCourseLength_id());
      Double moduleLength = moduleLengthTimeUnit.getBaseUnits();
      String description = moduleEntity.getDescription();
      Long maxParticipantCount = moduleEntity.getMaxParticipantCount();
      if (!StringUtils.isBlank(name) && courseNumber != null && !StringUtils.isBlank(description) && maxParticipantCount != null){
        return Response.ok()
            .entity(tranqualise(moduleController.createModule(name, subject, courseNumber, moduleLength, moduleLengthTimeUnit, description, maxParticipantCount, getUser())))
            .build();
      } else {
        return Response.status(500).build();
      }
    } catch (NullPointerException e) {
      return Response.status(Status.NOT_FOUND).build();
    }
  }
  
  @Path("/modules")
  @GET
  public Response findModules(@DefaultValue("false") @QueryParam("filterArchived") boolean filterArchived) {
    List<Module> modules;
    if (filterArchived) {
      modules = moduleController.findUnarchivedModules();
    } else {
      modules = moduleController.findModules();
    }
    if (!modules.isEmpty()) {
      return Response.ok()
          .entity(tranqualise(modules))
          .build();
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }
  
  @Path("/modules/{ID:[0-9]*}")
  @GET
  public Response findModuleById(@PathParam("ID") Long id) {
    Module module = moduleController.findModuleById(id);
    if (module != null) {
      return Response.ok()
          .entity(tranqualise(module))
          .build();
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }
  
  @Path("/modules/{ID:[0-9]*}")
  @PUT
  public Response updateModule(@PathParam("ID") Long id, ModuleEntity moduleEntity) {
    Module module = moduleController.findModuleById(id);
    try {
      String name = moduleEntity.getName();
      Integer courseNumber = moduleEntity.getCourseNumber();
      String description = moduleEntity.getDescription();
      Long maxParticipantCount = moduleEntity.getMaxParticipantCount();
      if (module != null && !StringUtils.isBlank(name) && courseNumber != null && !StringUtils.isBlank(description) && maxParticipantCount != null ){
        Subject subject = commonController.findSubjectById(moduleEntity.getSubject_id());
        EducationalTimeUnit lengthTimeUnit = commonController.findEducationalTimeUnitById(moduleEntity.getCourseLength_id());
        Double length = lengthTimeUnit.getBaseUnits();
        return Response.ok()
            .entity(tranqualise(moduleController.updateModule(module, name, subject, courseNumber, length, lengthTimeUnit, description, maxParticipantCount, getUser())))
            .build();
      } else if (!moduleEntity.getArchived() && module != null) {
        return Response.ok()
            .entity(tranqualise(moduleController.unarchiveModule(module, getUser())))
            .build();
      } else {
        return Response.status(500).build();
      }
    } catch (NullPointerException e) {
      return Response.status(Status.NOT_FOUND).build();
    }
  }
  
  @Path("/modules/{ID:[0-9]*}")
  @DELETE
  public Response archiveModule(@PathParam("ID") Long id) {
    Module module = moduleController.findModuleById(id);
    if (module != null){
      return Response.ok()
          .entity(tranqualise(moduleController.archiveModule(module, getUser())))
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
