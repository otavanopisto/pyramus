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
import fi.pyramus.domainmodel.modules.Module;
import fi.pyramus.domainmodel.projects.Project;
import fi.pyramus.domainmodel.projects.ProjectModule;
import fi.pyramus.domainmodel.projects.ProjectModuleOptionality;
import fi.pyramus.rest.annotation.RESTPermit;
import fi.pyramus.rest.controller.CommonController;
import fi.pyramus.rest.controller.ModuleController;
import fi.pyramus.rest.controller.ProjectController;
import fi.pyramus.rest.controller.permissions.ProjectPermissions;
import fi.pyramus.security.impl.SessionController;

@Path("/projects")
@Produces("application/json")
@Consumes("application/json")
@Stateful
@RequestScoped
public class ProjectRESTService extends AbstractRESTService {

  @Inject
  private ProjectController projectController;
  
  @Inject
  private CommonController commonController;
  
  @Inject
  private ModuleController moduleController;

  @Inject
  private SessionController sessionController;

  @Inject
  private ObjectFactory objectFactory;
  
  @Path("/projects")
  @POST
  @RESTPermit (ProjectPermissions.CREATE_PROJECT)
  public Response createProject(fi.pyramus.rest.model.Project entity) {
    EducationalTimeUnit optionalStudiesLengthUnit = entity.getOptionalStudiesLengthUnitId() != null ? commonController.findEducationalTimeUnitById(entity.getOptionalStudiesLengthUnitId()) : null;
    Double optionalStudiesLength = entity.getOptionalStudiesLength();
    String name = entity.getName();
    String description = entity.getDescription();
    
    if (StringUtils.isBlank(name)) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    Project project = projectController.createProject(name, description, optionalStudiesLength, optionalStudiesLengthUnit, sessionController.getUser());
    
    if (entity.getTags() != null) {
      for (String tag : entity.getTags()) {
        projectController.createProjectTag(project, tag); 
      }
    }
    
    return Response.ok(objectFactory.createModel(project)).build();
  }

  @Path("/projects")
  @GET
  @RESTPermit (ProjectPermissions.LIST_PROJECTS)
  public Response listProjects(@DefaultValue("false") @QueryParam("filterArchived") boolean filterArchived) {
    List<Project> projects;
    if (filterArchived) {
      projects = projectController.listUnarchivedProjects();
    } else {
      projects = projectController.listProjects();
    }
    
    if (projects.isEmpty()) {
      return Response.noContent().build();
    }
    
    return Response.ok(objectFactory.createModel(projects)).build();
  }
  
  @Path("/projects/{ID:[0-9]*}")
  @GET
  @RESTPermit (ProjectPermissions.FIND_PROJECT)
  public Response findProjectById(@PathParam("ID") Long id) {
    Project project = projectController.findProjectById(id);
    if (project == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (project.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    return Response.ok(objectFactory.createModel(project)).build();
  }

  @Path("/projects/{ID:[0-9]*}")
  @PUT
  @RESTPermit (ProjectPermissions.UPDATE_PROJECT)
  public Response updateProject(@PathParam("ID") Long id, fi.pyramus.rest.model.Project entity) {
    Project project = projectController.findProjectById(id);
    if (project == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (project.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    EducationalTimeUnit optionalStudiesLengthUnit = entity.getOptionalStudiesLengthUnitId() != null ? commonController.findEducationalTimeUnitById(entity.getOptionalStudiesLengthUnitId()) : null;
    Double optionalStudiesLength = entity.getOptionalStudiesLength();
    String name = entity.getName();
    String description = entity.getDescription();
    
    if (StringUtils.isBlank(name)) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    project = projectController.updateProjectTags(project, entity.getTags() == null ? new ArrayList<String>() : entity.getTags());
    
    return Response.ok(objectFactory.createModel(projectController.updateProject(project, name, description, optionalStudiesLength, optionalStudiesLengthUnit, sessionController.getUser()))).build();
  }
  
  @Path("/projects/{ID:[0-9]*}")
  @DELETE
  @RESTPermit (ProjectPermissions.DELETE_PROJECT)
  public Response deleteProject(@PathParam("ID") Long id, @DefaultValue ("false") @QueryParam ("permanent") Boolean permanent) {
    Project project = projectController.findProjectById(id);
    if (project == null) {
      return Response.status(Status.NOT_FOUND).build();
    }    
    
    if (permanent) {
      projectController.deleteProject(project);
    } else {
      projectController.archiveProject(project, sessionController.getUser());
    }
    
    return Response.noContent().build();
  }
    
  @Path("/projects/{ID:[0-9]*}/modules")
  @POST
  @RESTPermit (ProjectPermissions.CREATE_PROJECTMODULE)
  public Response createProjectModule(@PathParam("ID") Long id, fi.pyramus.rest.model.ProjectModule moduleEntity) {
    Project project = projectController.findProjectById(id);
    if (project == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (project.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if ((moduleEntity.getModuleId() == null) || (moduleEntity.getOptionality() == null)) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    Module module = moduleController.findModuleById(moduleEntity.getModuleId());
    if (module == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    ProjectModuleOptionality optionality = null;
    switch (moduleEntity.getOptionality()) {
      case MANDATORY:
        optionality = ProjectModuleOptionality.MANDATORY;
      break;
      case OPTIONAL:
        optionality = ProjectModuleOptionality.OPTIONAL;
      break;
    }
    
    ProjectModule projectModule = projectController.createProjectModule(project, module, optionality);
    
    return Response.ok(objectFactory.createModel(projectModule)).build();
  }
  
  @Path("/projects/{PROJECTID:[0-9]*}/modules")
  @GET
  @RESTPermit (ProjectPermissions.LIST_PROJECTMODULES)
  public Response listProjectModules(@PathParam("PROJECTID") Long projectId) {
    Project project = projectController.findProjectById(projectId);
    if (project == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (project.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    List<ProjectModule> projectModules = projectController.listProjectModules(project);
    if (projectModules.isEmpty()) {
      return Response.noContent().build();
    }
    
    return Response.ok(objectFactory.createModel(projectModules)).build();
  }
  
  @Path("/projects/{PROJECTID:[0-9]*}/modules/{ID:[0-9]*}")
  @GET
  @RESTPermit (ProjectPermissions.FIND_PROJECTMODULE)
  public Response listProjectModules(@PathParam("PROJECTID") Long projectId, @PathParam("ID") Long id) {
    Project project = projectController.findProjectById(projectId);
    if (project == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (project.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    ProjectModule projectModule = projectController.findProjectModuleById(id);
    if (projectModule == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (!projectModule.getProject().getId().equals(projectId)) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (projectModule.getProject().getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    return Response.ok(objectFactory.createModel(projectModule)).build();
  }
  
  @Path("/projects/{PROJECTID:[0-9]*}/modules/{ID:[0-9]*}")
  @PUT
  @RESTPermit (ProjectPermissions.UPDATE_PROJECTMODULE)
  public Response updateProjectModule(@PathParam("PROJECTID") Long projectId, @PathParam("ID") Long id, fi.pyramus.rest.model.ProjectModule entity) {
    Project project = projectController.findProjectById(projectId);
    if (project == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (project.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    ProjectModule projectModule = projectController.findProjectModuleById(id);
    if (projectModule == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (!projectModule.getProject().getId().equals(projectId)) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (projectModule.getProject().getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    ProjectModuleOptionality optionality = null;
    switch (entity.getOptionality()) {
      case MANDATORY:
        optionality = ProjectModuleOptionality.MANDATORY;
      break;
      case OPTIONAL:
        optionality = ProjectModuleOptionality.OPTIONAL;
      break;
    }
    
    return Response.ok(objectFactory.createModel(projectController.updateProjectModule(projectModule, optionality))).build();
  }

  @Path("/projects/{PROJECTID:[0-9]*}/modules/{ID:[0-9]*}")
  @DELETE
  @RESTPermit (ProjectPermissions.DELETE_PROJECTMODULE)
  public Response deleteProjectModule(@PathParam("PROJECTID") Long projectId, @PathParam("ID") Long id) {
    Project project = projectController.findProjectById(projectId);
    if (project == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    ProjectModule projectModule = projectController.findProjectModuleById(id);
    if (projectModule == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (!projectModule.getProject().getId().equals(projectId)) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    projectController.deleteProjectModule(projectModule);
    
    return Response.noContent().build();
  }
  
}