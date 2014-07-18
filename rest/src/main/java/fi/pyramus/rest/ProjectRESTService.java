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
import fi.pyramus.domainmodel.projects.Project;
import fi.pyramus.rest.controller.CommonController;
import fi.pyramus.rest.controller.ProjectController;
import fi.pyramus.rest.model.ObjectFactory;

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
  private ObjectFactory objectFactory;
  
  @Path("/projects")
  @POST
  public Response createProject(fi.pyramus.rest.model.Project entity) {
    EducationalTimeUnit optionalStudiesLengthUnit = entity.getOptionalStudiesLengthUnitId() != null ? commonController.findEducationalTimeUnitById(entity.getOptionalStudiesLengthUnitId()) : null;
    Double optionalStudiesLength = entity.getOptionalStudiesLength();
    String name = entity.getName();
    String description = entity.getDescription();
    
    if (StringUtils.isBlank(name)) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    Project project = projectController.createProject(name, description, optionalStudiesLength, optionalStudiesLengthUnit, getLoggedUser());
    
    if (entity.getTags() != null) {
      for (String tag : entity.getTags()) {
        projectController.createProjectTag(project, tag); 
      }
    }
    
    return Response.ok(objectFactory.createModel(project)).build();
  }

  @Path("/projects")
  @GET
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
    
    return Response.ok(objectFactory.createModel(projectController.updateProject(project, name, description, optionalStudiesLength, optionalStudiesLengthUnit, getLoggedUser()))).build();
  }
  
    @Path("/projects/{ID:[0-9]*}")
    @DELETE
    public Response deleteProject(@PathParam("ID") Long id, @DefaultValue ("false") @QueryParam ("permanent") Boolean permanent) {
      Project project = projectController.findProjectById(id);
      if (project == null) {
        return Response.status(Status.NOT_FOUND).build();
      }    
      
      if (permanent) {
        projectController.deleteProject(project);
      } else {
        projectController.archiveProject(project, getLoggedUser());
      }
      
      return Response.noContent().build();
    }
//  
//  @Path("/projects/{PID:[0-9]*}/modules/{ID:[0-9]*}")
//  @DELETE
//  public Response removeProjectModule(@PathParam("ID") Long id) {
//    ProjectModule projectModule = projectController.findProjectModuleById(id);
//    if (projectModule != null) {
//      projectController.deleteProjectModule(projectModule);
//      return Response.status(200).build();
//    } else {
//      return Response.status(Status.NOT_FOUND).build();
//    }
//  }
//  
//  @Path("/projects/{PID:[0-9]*}/tags/{ID:[0-9]*}")
//  @DELETE
//  public Response removeTag(@PathParam("PID") Long projectId, @PathParam("ID") Long tagId) {
//    Project project = projectController.findProjectById(projectId);
//    Tag tag = projectController.findTagById(tagId);
//    if (project != null && tag != null) {
//      project.removeTag(tag);
//      return Response.ok()
//          .entity(tranqualise(project))
//          .build();
//    } else {
//        return Response.status(Status.NOT_FOUND).build();
//    }
//  }
  
//  @Path("/projects/{ID:[0-9]*}/modules")
//  @POST
//  public Response createModule(@PathParam("ID") Long id, ProjectModuleEntity moduleEntity) {
//    try {
//      Project project = projectController.findProjectById(id);
//      Module module = moduleController.findModuleById(moduleEntity.getModule_id());
//      ProjectModuleOptionality optionality = moduleEntity.getOptionality();
//      if (project != null && module != null && optionality != null) {
//        return Response.ok()
//            .entity(tranqualise(projectController.createProjectModule(project, module, optionality)))
//            .build();
//      } else {
//        return Response.status(500).build();
//      }
//    } catch (NullPointerException e) {
//      return Response.status(Status.NOT_FOUND).build();
//    }
//  }
//  
//  @Path("/projects/{ID:[0-9]*}/tags")
//  @POST
//  public Response createTag(@PathParam("ID") Long id, TagEntity tagEntity) {
//    Project project = projectController.findProjectById(id);
//    String text = tagEntity.getText();
//    if (project != null && !StringUtils.isBlank(text)) {
//      return Response.ok()
//          .entity(tranqualise(projectController.createTag(project, text)))
//          .build();
//    } else {
//      return Response.status(501).build();
//    }
//  }
//  

//  @Path("/projects/{ID:[0-9]*}/modules")
//  @GET
//  public Response findProjectModules(@PathParam("ID") Long id) {
//    Project project = projectController.findProjectById(id);
//    if (project != null) {
//      return Response.ok()
//          .entity(tranqualise(projectController.findProjectModules(project)))
//          .build();
//    } else {
//      return Response.status(Status.NOT_FOUND).build();
//    }
//  }
//  
//  @Path ("/projects/{ID:[0-9]*}/tags")
//  @GET
//  public Response findTags(@PathParam("ID") Long id) {
//    Project project = projectController.findProjectById(id);
//    if (project != null) {
//      Set<Tag> tags = project.getTags();
//      return Response.ok()
//          .entity(tranqualise(tags))
//          .build();
//    } else {
//      return Response.status(Status.NOT_FOUND).build();
//    }
//  }
//  
//  @Path("/projects/{PID:[0-9]*}/modules/{ID:[0-9]*}")
//  @PUT
//  public Response updateProjectModule(@PathParam("ID") Long id, ProjectModuleEntity projectModuleEntity) {
//    ProjectModule projectModule = projectController.findProjectModuleById(id);
//    ProjectModuleOptionality optionality = projectModuleEntity.getOptionality();
//    if (projectModule != null) {
//      return Response.ok()
//          .entity(tranqualise(projectController.updateProjectModule(projectModule, optionality)))
//          .build();
//    } else {
//      return Response.status(500).build();
//    }
//  }
//

}