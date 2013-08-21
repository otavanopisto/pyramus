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

import org.apache.commons.lang.StringUtils;

import fi.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.pyramus.domainmodel.base.Tag;
import fi.pyramus.domainmodel.projects.Project;
import fi.pyramus.persistence.search.SearchResult;
import fi.pyramus.rest.controller.ProjectController;
import fi.pyramus.rest.tranquil.base.TagEntity;
import fi.pyramus.rest.tranquil.projects.ProjectEntity;
import fi.tranquil.TranquilityBuilderFactory;

@Path("/projects")
@Produces("application/json")
@Consumes("application/json")
@Stateful
@RequestScoped
public class ProjectRESTService extends AbstractRESTService {
  @Inject
  private TranquilityBuilderFactory tranquilityBuilderFactory;
  @Inject
  private ProjectController projectController;

  @Path("/projects")
  @POST
  public Response createProject(ProjectEntity projectEntity) {
    EducationalTimeUnit timeUnit = null;
    double optionalStudiesLength = 0;
    String name = projectEntity.getName();
    String description = projectEntity.getDescription();
    Long optionalStudiesLengthId = projectEntity.getOptionalStudiesLength_id();
    if(optionalStudiesLengthId != null) {
      timeUnit = projectController.findEducationalTimeUnitById(optionalStudiesLengthId);
      optionalStudiesLength = timeUnit.getBaseUnits();
    }
    if (!StringUtils.isBlank(name) && !StringUtils.isBlank(description) ) {
      return Response.ok()
          .entity(tranqualise(projectController.createProject(name, description, optionalStudiesLength, timeUnit, getUser())))
          .build();
    } else {
      return Response.status(501).build();
    }
  }
  
  @Path("/projects/{ID:[0-9]*}/tags")
  @POST
  public Response createTag(@PathParam("ID") Long id, TagEntity tagEntity) {
    Project project = projectController.findProjectById(id);
    String text = tagEntity.getText();
    if (project != null && !StringUtils.isBlank(text)) {
      return Response.ok()
          .entity(tranqualise(projectController.createTag(project, text)))
          .build();
    } else {
      return Response.status(501).build();
    }
  }
  
  @Path("/projects")
  @GET
  public Response findProjects(@QueryParam("name") String name,
                              @QueryParam("desciprtion") String description,
                              @QueryParam("tags") String tags,
                              @DefaultValue("false") @QueryParam("filterArchived") boolean filterArchived) {
    if (StringUtils.isBlank(name) && StringUtils.isBlank(description) && StringUtils.isBlank(tags)) {
      List<Project> projects;
      if (filterArchived) {
        projects = projectController.findUnarchivedProjects();
      } else {
        projects = projectController.findProjects();
      }
      if (!projects.isEmpty()){
        return Response.ok()
            .entity(tranqualise(projects))
            .build();
      } else {
        return Response.status(Status.NOT_FOUND).build();
      }
    } else {
      SearchResult<Project> projects = projectController.searchProjects(100,0,name,description,tags,filterArchived);
      if (!projects.getResults().isEmpty()) {
        return Response.ok()
          .entity(tranqualise(projects.getResults()))
          .build();
      } else {
        return Response.status(Status.NOT_FOUND).build();
      }
    }
  }

  @Path("/projects/{ID:[0-9]*}")
  @GET
  public Response findProjectById(@PathParam("ID") Long id) {
    Project project = projectController.findProjectById(id);
    if (project != null) {
      return Response.ok()
          .entity(tranqualise(project))
          .build();
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }
  
  @Path ("/projects/{ID:[0-9]*}/tags")
  @GET
  public Response findTags(@PathParam("ID") Long id) {
    Project project = projectController.findProjectById(id);
    if(project != null) {
      Set<Tag> tags = project.getTags();
      return Response.ok()
          .entity(tranqualise(tags))
          .build();
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }
  
  @Path("/projects/{ID:[0-9]*}")
  @PUT
  public Response updateProject(@PathParam("ID") Long id, ProjectEntity projectEntity) {
    Project project = projectController.findProjectById(id);
    String name = projectEntity.getName();
    String description = projectEntity.getDescription();
    double optionalStudiesLength = 0;
    EducationalTimeUnit timeUnit = null;
    if (project != null && !StringUtils.isBlank(name) && !StringUtils.isBlank(description)) {
      return Response.ok()
          .entity(tranqualise(projectController.updateProject(project, name, description, optionalStudiesLength, timeUnit, getUser())))
          .build();
    } else if (!projectEntity.getArchived()) {
        return Response.ok()
            .entity(tranqualise(projectController.unarchiveProject(project, getUser())))
            .build();
    } else {
      return Response.status(501).build();
    }
  }
  
  @Path("/projects/{PID:[0-9]*}/tags/{ID:[0-9]*}")
  @DELETE
  public Response removeTag(@PathParam("PID") Long projectId, @PathParam("ID") Long tagId) {
    Project project = projectController.findProjectById(projectId);
    Tag tag = projectController.findTagById(tagId);
    if(project != null && tag != null) {
      project.removeTag(tag);
      return Response.ok()
          .entity(tranqualise(project))
          .build();
    } else {
        return Response.status(Status.NOT_FOUND).build();
    }
  }

  @Path("/projects/{ID:[0-9]*}")
  @DELETE
  public Response archiveProject(@PathParam("ID") Long id) {
    Project project = projectController.findProjectById(id);
    if(project != null) {
      return Response.ok()
          .entity(tranqualise(projectController.archiveProject(project, getUser())))
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