package fi.pyramus.rest;

import java.util.Date;
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

import fi.pyramus.domainmodel.base.AcademicTerm;
import fi.pyramus.domainmodel.courses.Course;
import fi.pyramus.persistence.search.SearchResult;
import fi.pyramus.persistence.search.SearchTimeFilterMode;
import fi.pyramus.rest.controller.CalendarController;

@Path("/calendar")
@Produces("application/json")
@Consumes("application/json")
@Stateful
@RequestScoped
public class CalendarRESTService extends AbstractRESTService {
  
@Inject
  private CalendarController calendarController;
  
  @Path("/academicTerms")
  @POST
  public Response createAcademidTerm(AcademicTermEntity termEntity) {
    String name = termEntity.getName();
    Date startDate = termEntity.getStartDate();
    Date endDate = termEntity.getEndDate();
    if(!StringUtils.isBlank(name) && startDate != null && endDate != null) {
      return Response.ok()
          .entity(tranqualise(calendarController.createAcademicTerm(name, startDate, endDate)))
          .build();
    } else {
      return Response.status(500).build();
    }
  }
  
  @Path("/academicTerms")
  @GET
  public Response findAcademicTerms(@DefaultValue("false") @QueryParam("filterArchived") boolean filterArchived) {
    List<AcademicTerm> terms;
    if (filterArchived) {
      terms = calendarController.findUnarchivedAcademicTerms();
    } else {
      terms = calendarController.findAcademicTerms();
    } 
    if (!terms.isEmpty()) {
      return Response.ok()
          .entity(tranqualise(terms))
          .build();
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }
  
  @Path("/academicTerms/{ID:[0-9]*}")
  @GET
  public Response findAcademicTerms(@PathParam("ID") Long id) {
    AcademicTerm term = calendarController.findAcademicTermById(id);
    if (term != null) {
      return Response.ok()
          .entity(tranqualise(term))
          .build();
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }
  
  @Path("/academicTerms/{ID:[0-9]*}/courses")
  @GET
  public Response findCoursesByTerm(@PathParam("ID") Long id, @DefaultValue("false") @QueryParam("filterArchived") boolean filterArchived) {
    AcademicTerm term = calendarController.findAcademicTermById(id);
    if (term != null) {
      Date timeframeStart = term.getStartDate();
      Date timeframeEnd = term.getEndDate();
      SearchResult<Course> courses = calendarController.findCoursesByTerm(100, 0, SearchTimeFilterMode.getMode(1), timeframeStart, timeframeEnd, filterArchived);
      return Response.ok()
          .entity(tranqualise(courses.getResults()))
          .build();
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }
  
  @Path("/academicTerms/{ID:[0-9]*}")
  @PUT
  public Response updateAcademicTerms(@PathParam("ID") Long id, AcademicTermEntity termEntity) {
    AcademicTerm academicTerm = calendarController.findAcademicTermById(id);
    String name = termEntity.getName();
    Date startDate = termEntity.getStartDate();
    Date endDate = termEntity.getEndDate();
    if (academicTerm != null && !StringUtils.isBlank(name) && startDate != null && endDate != null) {
      return Response.ok()
          .entity(tranqualise(calendarController.updateAcademicTerm(academicTerm, name, startDate, endDate)))
          .build();
    } else if (!termEntity.getArchived()) {
      return Response.ok()
          .entity(tranqualise(calendarController.unarchiveAcademicTerm(academicTerm, getUser())))
          .build();
    } else {
      return Response.status(500).build();
    } 
  }
  
  @Path("/academicTerms/{ID:[0-9]*}")
  @DELETE
  public Response archiveAcademicTerm(@PathParam("ID") Long id) {
    AcademicTerm academicTerm = calendarController.findAcademicTermById(id);
    if (academicTerm != null) {
      return Response.ok()
          .entity(tranqualise(calendarController.archiveAcademicTerm(academicTerm, getUser())))
          .build();
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }

}
