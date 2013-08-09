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

import fi.pyramus.domainmodel.base.EducationalTimeUnit;
import fi.pyramus.domainmodel.projects.Project;
import fi.pyramus.rest.controller.ProjectController;
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
    String name = projectEntity.getName();
    String description = projectEntity.getDescription();
    double optionalStudiesLength = 0;
    EducationalTimeUnit timeUnit = null;
    if (!StringUtils.isBlank(name) && !StringUtils.isBlank(description) ) {
      return Response.ok()
          .entity(tranqualise(projectController.createProject(name, description, optionalStudiesLength, timeUnit, getUser())))
          .build();
    } else {
      return Response.status(501).build();
    }
  }
  
  @Path("/projects")
  @GET
  public Response findProjects(@DefaultValue("false") @QueryParam("filterArchived") boolean filterArchived) {
    List<Project> schools;
    if (filterArchived) {
      schools = projectController.findUnarchivedProjects();
    } else {
      schools = projectController.findProjects();
    }
    if (!schools.isEmpty()){
      return Response.ok()
          .entity(tranqualise(schools))
          .build();
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }

  @Path("/projects/{ID:[0-9]*}")
  @GET
  public Response findProjectById(@PathParam("ID") Long id) {
    Project project = projectController.findProjectById(id);
    if (!project.equals(null)) {
      return Response.ok()
          .entity(tranqualise(project))
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
    if (!project.equals(null) && !StringUtils.isBlank(name) && !StringUtils.isBlank(description)) {
      return Response.ok()
          .entity(tranqualise(projectController.updateProject(project, name, description, optionalStudiesLength, timeUnit, getUser())))
          .build();
    } else if (projectEntity.getArchived()) {
        return Response.ok()
            .entity(tranqualise(projectController.unarchiveProject(project, getUser())))
            .build();
    } else {
      return Response.status(501).build();
    }
  }

  @Path("/projects/{ID:[0-9]*}")
  @DELETE
  public Response archiveProject(@PathParam("ID") Long id) {
    Project project = projectController.findProjectById(id);
    if (!project.equals(null)) {
      return Response.ok()
          .entity(tranqualise(projectController.archiveProject(project)))
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