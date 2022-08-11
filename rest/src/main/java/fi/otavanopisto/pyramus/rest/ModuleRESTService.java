package fi.otavanopisto.pyramus.rest;

import java.util.ArrayList;
import java.util.HashSet;
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
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import fi.otavanopisto.pyramus.domainmodel.base.CourseBaseVariableKey;
import fi.otavanopisto.pyramus.domainmodel.base.Curriculum;
import fi.otavanopisto.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.otavanopisto.pyramus.domainmodel.base.Subject;
import fi.otavanopisto.pyramus.domainmodel.base.VariableType;
import fi.otavanopisto.pyramus.domainmodel.courses.Course;
import fi.otavanopisto.pyramus.domainmodel.modules.Module;
import fi.otavanopisto.pyramus.domainmodel.modules.ModuleComponent;
import fi.otavanopisto.pyramus.domainmodel.projects.Project;
import fi.otavanopisto.pyramus.rest.annotation.RESTPermit;
import fi.otavanopisto.pyramus.rest.controller.CommonController;
import fi.otavanopisto.pyramus.rest.controller.CourseController;
import fi.otavanopisto.pyramus.rest.controller.CurriculumController;
import fi.otavanopisto.pyramus.rest.controller.ModuleController;
import fi.otavanopisto.pyramus.rest.controller.permissions.CommonPermissions;
import fi.otavanopisto.pyramus.rest.controller.permissions.ModulePermissions;
import fi.otavanopisto.pyramus.rest.model.CourseLength;
import fi.otavanopisto.pyramus.rest.model.CourseModule;
import fi.otavanopisto.pyramus.security.impl.SessionController;

@Path("/modules")
@Produces("application/json")
@Consumes("application/json")
@Stateful
@RequestScoped
public class ModuleRESTService extends AbstractRESTService{

  @Inject
  private ModuleController moduleController;
  
  @Inject
  private CourseController courseController;
  
  @Inject
  private CommonController commonController;

  @Inject
  private CurriculumController curriculumController;
  
  @Inject
  private SessionController sessionController;
  
  @Inject
  private ObjectFactory objectFactory;
  
  @Path("/modules")
  @POST
  @RESTPermit (ModulePermissions.CREATE_MODULE)
  public Response createModule(fi.otavanopisto.pyramus.rest.model.Module entity) {
    if (entity == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    String name = entity.getName();
    if (StringUtils.isBlank(name)) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    String description = entity.getDescription();
    Long maxParticipantCount = entity.getMaxParticipantCount();
    Module module = moduleController.createModule(name, description, maxParticipantCount, sessionController.getUser());
    
    if (CollectionUtils.isNotEmpty(entity.getCourseModules())) {
      for (CourseModule courseModuleEntity : entity.getCourseModules()) {
        Subject subject = null;
        if (courseModuleEntity.getSubject() != null) {
          subject = commonController.findSubjectById(courseModuleEntity.getSubject().getId());
          if (subject == null) {
            return Response.status(Status.NOT_FOUND).entity("specified subject does not exist").build();
          }
        }
        
        Integer courseNumber = courseModuleEntity.getCourseNumber();
        
        CourseLength courseLength = courseModuleEntity.getCourseLength();
        Double courseLengthUnits = courseLength != null ? courseLength.getUnits() : null;
        EducationalTimeUnit courseLengthTimeUnit = null;
        if (courseLength != null) {
          if (courseLength.getUnit() == null) {
            return Response.status(Status.BAD_REQUEST).entity("length unit is missing").build();
          }
          
          courseLengthTimeUnit = commonController.findEducationalTimeUnitById(courseLength.getUnit().getId());
          if (courseLengthTimeUnit == null) {
            return Response.status(Status.BAD_REQUEST).entity("length unit is invalid").build();
          }
        }
        
        courseController.createCourseModule(module, subject, courseNumber, courseLengthUnits, courseLengthTimeUnit);
      }
    }
    
    if (entity.getTags() != null) {
      for (String tag : entity.getTags()) {
        moduleController.createModuleTag(module, tag); 
      }
    }
    
    module = moduleController.updateModuleCurriculums(module, getCurriculumsFromIds(entity.getCurriculumIds()));
    
    return Response
        .ok(objectFactory.createModel(module))
        .build();
  }

  @Path("/modules")
  @GET
  @RESTPermit (ModulePermissions.LIST_MODULES)
  public Response listModules(@DefaultValue("false") @QueryParam("filterArchived") boolean filterArchived) {
    List<Module> modules;
    if (filterArchived) {
      modules = moduleController.listUnarchivedModules();
    } else {
      modules = moduleController.listModules();
    }
    
    if (modules.isEmpty()) {
      return Response.noContent().build();
    }
    
    return Response.ok(objectFactory.createModel(modules)).build();
  }
  
  @Path("/modules/{ID:[0-9]*}")
  @GET
  @RESTPermit (ModulePermissions.FIND_MODULE)
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
  @RESTPermit (ModulePermissions.UPDATE_MODULE)
  public Response updateModule(@PathParam("ID") Long id, fi.otavanopisto.pyramus.rest.model.Module entity) {
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
    
    Set<Long> existingCourseModuleIds = module.getCourseModules().stream()
        .map(fi.otavanopisto.pyramus.domainmodel.base.CourseModule::getId)
        .collect(Collectors.toSet());
    
    if (entity.getCourseModules() == null) {
      return Response.status(Status.BAD_REQUEST).entity("courseModules is required").build();
    } else {
      for (CourseModule courseModuleEntity : entity.getCourseModules()) {
        // null or -1 are considered as new
        if ((courseModuleEntity.getId() != null) && (courseModuleEntity.getId().intValue() != -1) && !existingCourseModuleIds.contains(courseModuleEntity.getId())) {
          return Response.status(Status.BAD_REQUEST).entity("no such course module in this course").build();
        }
      }
    }
    
    String description = entity.getDescription();
    Long maxParticipantCount = entity.getMaxParticipantCount();
    
    module = moduleController.updateModuleTags(module, entity.getTags() == null ? new ArrayList<String>() : entity.getTags());
    module = moduleController.updateModule(module, name, description, maxParticipantCount, sessionController.getUser());
    module = moduleController.updateModuleCurriculums(module, getCurriculumsFromIds(entity.getCurriculumIds()));
    
    for (CourseModule courseModuleEntity : entity.getCourseModules()) {
      Subject subject = null;
      if (courseModuleEntity.getSubject() != null) {
        subject = commonController.findSubjectById(courseModuleEntity.getSubject().getId());
        if (subject == null) {
          return Response.status(Status.NOT_FOUND).entity("specified subject does not exist").build();
        }
      }
      
      Integer courseNumber = courseModuleEntity.getCourseNumber();
      
      CourseLength courseLength = courseModuleEntity.getCourseLength();
      Double courseLengthUnits = courseLength != null ? courseLength.getUnits() : null;
      EducationalTimeUnit courseLengthTimeUnit = null;
      if (courseLength != null) {
        if (courseLength.getUnit() == null) {
          return Response.status(Status.BAD_REQUEST).entity("length unit is missing").build();
        }
        
        courseLengthTimeUnit = commonController.findEducationalTimeUnitById(courseLength.getUnit().getId());
        if (courseLengthTimeUnit == null) {
          return Response.status(Status.BAD_REQUEST).entity("length unit is invalid").build();
        }
      }
      
      if (courseModuleEntity.getId() == null || courseModuleEntity.getId().intValue() == -1) {
        courseController.createCourseModule(module, subject, courseNumber, courseLengthUnits, courseLengthTimeUnit);
      } else {
        // id is set, update
        fi.otavanopisto.pyramus.domainmodel.base.CourseModule courseModule = module.getCourseModules().stream()
            .filter(courseModuleInner -> courseModuleInner.getId().equals(courseModuleEntity.getId()))
            .findFirst()
            .orElse(null);
        existingCourseModuleIds.remove(courseModule.getId());
        
        courseController.updateCourseModule(courseModule, subject, courseNumber, courseLengthUnits, courseLengthTimeUnit);
      }
    }
    
    // Remove courseModules that weren't updated
    module.getCourseModules().stream()
      .filter(courseModule -> existingCourseModuleIds.contains(courseModule.getId()))
      .forEach(courseModule -> courseController.removeCourseModule(courseModule));
    
    return Response.ok(objectFactory.createModel(module)).build();
  }
  
  @Path("/modules/{ID:[0-9]*}")
  @DELETE
  @RESTPermit (ModulePermissions.DELETE_MODULE)
  public Response deleteModule(@PathParam("ID") Long id, @DefaultValue ("false") @QueryParam ("permanent") Boolean permanent) {
    Module module = moduleController.findModuleById(id);
    if (module == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (permanent) {
      moduleController.deleteModule(module);
    } else {
      moduleController.archiveModule(module, sessionController.getUser());
    }
    
    return Response.noContent().build();
  }
  
  @Path("/modules/{MODULEID:[0-9]*}/components")
  @POST
  @RESTPermit (ModulePermissions.CREATE_MODULECOMPONENT)
  public Response createModuleComponent(@PathParam("MODULEID") Long moduleId, fi.otavanopisto.pyramus.rest.model.ModuleComponent entity) {
    Module module = moduleController.findModuleById(moduleId);
    if (module == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (module.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (StringUtils.isBlank(entity.getName())) {
      return Response.status(Status.BAD_REQUEST).entity("name is required").build();
    }
    
    EducationalTimeUnit componentLengthTimeUnit = null;
      
    if (entity.getLength() != null) {
      if (entity.getLengthUnitId() == null) {
        return Response.status(Status.BAD_REQUEST).entity("lengthUnitId is required when length is defined").build();
      }
      
      componentLengthTimeUnit = commonController.findEducationalTimeUnitById(entity.getLengthUnitId());
      if (componentLengthTimeUnit == null) {
        return Response.status(Status.BAD_REQUEST).entity("lengthUnitId is required when length is defined").build();
      }
    }
    
    return Response
      .status(Status.OK)
      .entity(objectFactory.createModel(moduleController.createModuleComponent(module, entity.getLength(), componentLengthTimeUnit, entity.getName(), entity.getDescription())))
      .build();
  }

  @Path("/modules/{ID:[0-9]*}/components")
  @GET
  @RESTPermit (ModulePermissions.LIST_MODULECOMPONENTS)
  public Response listModuleComponents(@PathParam("ID") Long moduleId) {
    Module module = moduleController.findModuleById(moduleId);
    if (module == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (module.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    List<ModuleComponent> components = module.getModuleComponents();
    if (components.isEmpty()) {
      return Response.status(Status.NO_CONTENT).build();
    }
    
    return Response.status(Status.OK).entity(objectFactory.createModel(components)).build();
  }

  @Path("/modules/{MODULEID:[0-9]*}/components/{ID:[0-9]*}")
  @GET
  @RESTPermit (ModulePermissions.FIND_MODULECOMPONENT)
  public Response findModuleComponentById(@PathParam("MODULEID") Long moduleId, @PathParam("ID") Long componentId) {
    Module module = moduleController.findModuleById(moduleId);
    if (module == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (module.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    ModuleComponent component = moduleController.findModuleComponentById(componentId);
    if (component == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (component.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (!component.getModule().getId().equals(moduleId)) {
      return Response.status(Status.NOT_FOUND).build();
    }

    return Response.status(Status.OK).entity(objectFactory.createModel(component)).build();
  }
  
  @Path("/modules/{MODULEID:[0-9]*}/components/{COMPONENTID:[0-9]*}")
  @PUT
  @RESTPermit (ModulePermissions.UPDATE_MODULECOMPONENT)
  public Response updateModuleComponent(@PathParam("MODULEID") Long moduleId, @PathParam("COMPONENTID") Long courseComponentId, fi.otavanopisto.pyramus.rest.model.ModuleComponent entity) {
    if (entity == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    ModuleComponent moduleComponent = moduleController.findModuleComponentById(courseComponentId);
    if (moduleComponent == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    Module module = moduleController.findModuleById(moduleId);
    if (module == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (module.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (StringUtils.isBlank(moduleComponent.getName())) {
      return Response.status(Status.BAD_REQUEST).entity("name is required").build();
    }
    
    EducationalTimeUnit componentLengthTimeUnit = null;
      
    if (entity.getLength() != null) {
      if (entity.getLengthUnitId() == null) {
        return Response.status(Status.BAD_REQUEST).entity("lengthUnitId is required when length is defined").build();
      }
      
      componentLengthTimeUnit = commonController.findEducationalTimeUnitById(entity.getLengthUnitId());
      if (componentLengthTimeUnit == null) {
        return Response.status(Status.BAD_REQUEST).entity("lengthUnitId is required when length is defined").build();
      }
    }
    
    return Response
      .status(Status.OK)
      .entity(objectFactory.createModel(moduleController.updateModuleComponent(moduleComponent, entity.getLength(), componentLengthTimeUnit, entity.getName(), entity.getDescription())))
      .build();
  }
  
  @Path("/modules/{MODULEID:[0-9]*}/components/{COMPONENTID:[0-9]*}")
  @DELETE
  @RESTPermit (ModulePermissions.DELETE_MODULECOMPONENT)
  public Response deleteModuleComponent(@PathParam("MODULEID") Long moduleId, @PathParam("COMPONENTID") Long componentId, @DefaultValue ("false") @QueryParam ("permanent") Boolean permanent) {
    if (moduleId == null || componentId == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    Module module = moduleController.findModuleById(moduleId);
    if (module == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
   
    ModuleComponent moduleComponent = moduleController.findModuleComponentById(componentId);
    if (moduleComponent == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (!moduleComponent.getModule().getId().equals(module.getId())) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (permanent) {
      moduleController.deleteModuleComponent(moduleComponent);
    } else {
      moduleController.archiveModuleComponent(moduleComponent, sessionController.getUser());
    }
    
    return Response.status(Status.NO_CONTENT).build();
  }
  
  @Path("/modules/{ID:[0-9]*}/courses")
  @GET
  @RESTPermit (ModulePermissions.LIST_COURSESBYMODULE)
  public Response listCourses(@PathParam("ID") Long id) {
    Module module = moduleController.findModuleById(id);
    if (module == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (module.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    List<Course> courses = moduleController.listCoursesByModule(module);
    if (courses.isEmpty()) {
      return Response.noContent().build();
    }
    
    return Response.ok(objectFactory.createModel(courses)).build();
  }
  
  @Path("/modules/{ID:[0-9]*}/projects")
  @GET
  @RESTPermit (ModulePermissions.LIST_PROJECTSBYMODULE)
  public Response listProjects(@PathParam("ID") Long id) {
    Module module = moduleController.findModuleById(id);
    if (module == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (module.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    List<Project> projects = moduleController.listProjectsByModule(module);
    if (projects.isEmpty()) {
      return Response.noContent().build();
    }
    
    return Response.ok(objectFactory.createModel(projects)).build();
  }

  @Path("/variables")
  @POST
  @RESTPermit (CommonPermissions.CREATE_COURSEBASEVARIABLEKEY)
  public Response createVariable(fi.otavanopisto.pyramus.rest.model.VariableKey entity) {
    if (entity == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    if (StringUtils.isBlank(entity.getKey())||StringUtils.isBlank(entity.getName())|| entity.getType() == null || entity.getUserEditable() == null) {
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
    
    CourseBaseVariableKey courseBaseVariableKey = commonController.createCourseBaseVariableKey(entity.getKey(), entity.getName(), variableType, entity.getUserEditable());
    return Response.ok(objectFactory.createModel(courseBaseVariableKey)).build();
  }
  
  @Path("/variables")
  @GET
  @RESTPermit (CommonPermissions.LIST_COURSEBASEVARIABLEKEYS)
  public Response listVariables() {
    List<CourseBaseVariableKey> variableKeys = commonController.listCourseBaseVariableKeys();
    if (variableKeys.isEmpty()) {
      return Response.noContent().build();
    }
    
    return Response.ok(objectFactory.createModel(variableKeys)).build();
  }
  
  @Path("/variables/{KEY}")
  @GET
  @RESTPermit (CommonPermissions.FIND_COURSEBASEVARIABLEKEY)
  public Response findVariable(@PathParam ("KEY") String key) {
    CourseBaseVariableKey courseBaseVariableKey = commonController.findCourseBaseVariableKeyByVariableKey(key);
    if (courseBaseVariableKey == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    return Response.ok(objectFactory.createModel(courseBaseVariableKey)).build();
  }
  
  @Path("/variables/{KEY}")
  @PUT
  @RESTPermit (CommonPermissions.UPDATE_COURSEBASEVARIABLEKEY)
  public Response updateVariable(@PathParam ("KEY") String key, fi.otavanopisto.pyramus.rest.model.VariableKey entity) {
    if (entity == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    if (StringUtils.isBlank(entity.getName())|| entity.getType() == null || entity.getUserEditable() == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    CourseBaseVariableKey courseBaseVariableKey = commonController.findCourseBaseVariableKeyByVariableKey(key);
    if (courseBaseVariableKey == null) {
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
    
    commonController.updateCourseBaseVariableKey(courseBaseVariableKey, entity.getName(), variableType, entity.getUserEditable());
    
    return Response.ok(objectFactory.createModel(courseBaseVariableKey)).build();
  }
  
  @Path("/variables/{KEY}")
  @DELETE
  @RESTPermit (CommonPermissions.DELETE_COURSEBASEVARIABLEKEY)
  public Response deleteVariable(@PathParam ("KEY") String key) {
    CourseBaseVariableKey courseBaseVariableKey = commonController.findCourseBaseVariableKeyByVariableKey(key);
    if (courseBaseVariableKey == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    commonController.deleteCourseBaseVariableKey(courseBaseVariableKey);
    
    return Response.noContent().build();
  }

  private Set<Curriculum> getCurriculumsFromIds(Set<Long> curriculumIds) {
    Set<Curriculum> curriculums = new HashSet<>();
    if (CollectionUtils.isNotEmpty(curriculumIds)) {
      for (Long curriculumId : curriculumIds) {
        Curriculum curriculum = curriculumId != null ? curriculumController.findCurriculumById(curriculumId) : null;
        if (curriculum != null)
          curriculums.add(curriculum);
      }
    }
    return curriculums;
  }
  
}
