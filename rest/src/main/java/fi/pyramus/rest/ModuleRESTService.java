package fi.pyramus.rest;

import java.util.ArrayList;
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
import fi.pyramus.rest.model.ObjectFactory;

@Path("/modules")
@Produces("application/json")
@Consumes("application/json")
@Stateful
@RequestScoped
public class ModuleRESTService extends AbstractRESTService{

  @Inject
  private ModuleController moduleController;
  
  @Inject
  private CommonController commonController;
  
  @Inject
  private ObjectFactory objectFactory;
  
  @Path("/modules")
  @POST
  public Response createModule(fi.pyramus.rest.model.Module entity) {
    if (entity == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    String name = entity.getName();
    if (StringUtils.isBlank(name)) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    Subject subject = entity.getSubjectId() != null ? commonController.findSubjectById(entity.getSubjectId()) : null;
    Integer courseNumber = entity.getCourseNumber();
    EducationalTimeUnit lengthUnit = entity.getLengthUnitId() != null ? commonController.findEducationalTimeUnitById(entity.getLengthUnitId()) : null;
    Double length = entity.getLength();
    String description = entity.getDescription();
    Long maxParticipantCount = entity.getMaxParticipantCount();
    Module module = moduleController.createModule(name, subject, courseNumber, length, lengthUnit, description, maxParticipantCount, getLoggedUser());
    
    if (entity.getTags() != null) {
      for (String tag : entity.getTags()) {
        moduleController.createModuleTag(module, tag); 
      }
    }
    
    return Response
        .ok(objectFactory.createModel(module))
        .build();
  }

  @Path("/modules")
  @GET
  public Response listModules(@DefaultValue("false") @QueryParam("filterArchived") boolean filterArchived) {
    List<Module> modules;
    if (filterArchived) {
      modules = moduleController.findUnarchivedModules();
    } else {
      modules = moduleController.findModules();
    }
    
    if (modules.isEmpty()) {
      return Response.noContent().build();
    }
    
    return Response.ok(objectFactory.createModel(modules)).build();
  }
  
  @Path("/modules/{ID:[0-9]*}")
  @GET
  public Response findModuleById(@PathParam("ID") Long id) {
    Module module = moduleController.findModuleById(id);
    if (module == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (module.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }

    return Response.ok()
        .entity(objectFactory.createModel(module))
        .build();
  }
  
  @Path("/modules/{ID:[0-9]*}")
  @PUT
  public Response updateModule(@PathParam("ID") Long id, fi.pyramus.rest.model.Module entity) {
    if (entity == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    String name = entity.getName();
    if (StringUtils.isBlank(name)) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    Module module = moduleController.findModuleById(id);
    if (module == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (module.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    Subject subject = entity.getSubjectId() != null ? commonController.findSubjectById(entity.getSubjectId()) : null;
    Integer courseNumber = entity.getCourseNumber();
    EducationalTimeUnit lengthUnit = entity.getLengthUnitId() != null ? commonController.findEducationalTimeUnitById(entity.getLengthUnitId()) : null;
    Double length = entity.getLength();
    String description = entity.getDescription();
    Long maxParticipantCount = entity.getMaxParticipantCount();
    
    module = moduleController.updateModuleTags(module, entity.getTags() == null ? new ArrayList<String>() : entity.getTags());
    module = moduleController.updateModule(module, name, subject, courseNumber, length, lengthUnit, description, maxParticipantCount, getLoggedUser());
    
    return Response.ok(objectFactory.createModel(module)).build();
  }
  
  @Path("/modules/{ID:[0-9]*}")
  @DELETE
  public Response deleteModule(@PathParam("ID") Long id, @DefaultValue ("false") @QueryParam ("permanent") Boolean permanent) {
    Module module = moduleController.findModuleById(id);
    if (module == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (permanent) {
      moduleController.deleteModule(module);
    } else {
      moduleController.archiveModule(module, getLoggedUser());
    }
    
    return Response.noContent().build();
  }

  
//@Path("/modules/{ID:[0-9]*}/components")
//@GET
//public Response findComponents(@PathParam("ID") Long id) {
//  Module module = moduleController.findModuleById(id);
//  if (module != null) {
//    return Response.ok()
//        .entity(tranqualise(moduleController.findComponents(module)))
//        .build();
//  } else {
//    return Response.status(Status.NOT_FOUND).build();
//  }
//}
//
//@Path("/modules/{ID:[0-9]*}/courses")
//@GET
//public Response findCourses(@PathParam("ID") Long id) {
//  Module module = moduleController.findModuleById(id);
//  if (module != null) {
//    return Response.ok()
//        .entity(tranqualise(moduleController.findCourses(module)))
//        .build();
//  } else {
//    return Response.status(Status.NOT_FOUND).build();
//  }
//}
//
//@Path("/modules/{ID:[0-9]*}/projects")
//@GET
//public Response findProjects(@PathParam("ID") Long id) {
//  Module module = moduleController.findModuleById(id);
//  if( module != null) {
//    return Response.ok()
//        .entity(tranqualise(moduleController.findProjects(module)))
//        .build();
//  } else {
//    return Response.status(Status.NOT_FOUND).build();
//  }
//}
//
//@Path("/modules/{ID:[0-9]*}/variables")
//@GET
//public Response findVariables(@PathParam("ID") Long id) {
//  Module module = moduleController.findModuleById(id);
//  if (module != null) {
//    return Response.ok()
//        .entity(tranqualise(moduleController.findVariables(module)))
//        .build();
//  } else {
//    return Response.status(Status.NOT_FOUND).build();
//  }
//}

}
