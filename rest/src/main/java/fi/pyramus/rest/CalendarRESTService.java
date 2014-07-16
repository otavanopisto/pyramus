package fi.pyramus.rest;

import java.io.IOException;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.JAXBException;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import fi.pyramus.domainmodel.base.AcademicTerm;
import fi.pyramus.domainmodel.courses.Course;
import fi.pyramus.persistence.search.SearchResult;
import fi.pyramus.persistence.search.SearchTimeFilterMode;
import fi.pyramus.rest.controller.CalendarController;
import fi.pyramus.rest.model.ObjectFactory;

@Path("/calendar")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Stateful
@RequestScoped
public class CalendarRESTService extends AbstractRESTService {
  
  @Inject
  private CalendarController calendarController;

  @Inject
  private ObjectFactory objectFactory;
  
  @Path("/academicTerms")
  @POST
  public Response createAcademidTerm(fi.pyramus.rest.model.AcademicTerm entity) {
    String name = entity.getName();
    DateTime startDate = entity.getStartDate();
    DateTime endDate = entity.getEndDate();
    
    if (StringUtils.isBlank(name)) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    if ((startDate == null) || (endDate == null)) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    return Response.ok()
        .entity(objectFactory.createModel(calendarController.createAcademicTerm(name, toDate(startDate), toDate(endDate))))
        .build();
  }
  
  @Path("/academicTerms")
  @GET
  public Response listAcademicTerms(@DefaultValue("false") @QueryParam("filterArchived") boolean filterArchived) {
    List<AcademicTerm> academicTerms;
    if (filterArchived) {
      academicTerms = calendarController.listUnarchivedAcademicTerms();
    } else {
      academicTerms = calendarController.listAcademicTerms();
    }
    
    if (academicTerms.isEmpty()) {
      return Response.noContent().build();
    }
    
    return Response.ok(objectFactory.createModel(academicTerms)).build();
  }
  
  @Path("/academicTerms/{ID:[0-9]*}")
  @GET
  public Response findAcademicTerms(@PathParam("ID") Long id) throws JAXBException, IOException {
    AcademicTerm academicTerm = calendarController.findAcademicTermById(id);
    if (academicTerm == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (academicTerm.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }

    return Response.ok(objectFactory.createModel(academicTerm)).build();
  }
  
  @Path("/academicTerms/{ID:[0-9]*}")
  @PUT
  public Response updateAcademicTerm(@PathParam("ID") Long id, fi.pyramus.rest.model.AcademicTerm entity) {
    AcademicTerm academicTerm = calendarController.findAcademicTermById(id);
    String name = entity.getName();
    DateTime startDate = entity.getStartDate();
    DateTime endDate = entity.getEndDate();
    
    if (StringUtils.isBlank(name)) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    if ((startDate == null) || (endDate == null)) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    return Response
        .ok(objectFactory.createModel(calendarController.updateAcademicTerm(academicTerm, name, startDate.toDate(), endDate.toDate())))
        .build();
  }
  
  @Path("/academicTerms/{ID:[0-9]*}")
  @DELETE
  public Response deleteAcademicTerm(@PathParam("ID") Long id, @DefaultValue ("false") @QueryParam ("permanent") Boolean permanent) {
    AcademicTerm academicTerm = calendarController.findAcademicTermById(id);
    if (academicTerm == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (permanent) {
      calendarController.deleteAcademicTerm(academicTerm);
    } else {
      calendarController.archiveAcademicTerm(academicTerm, getLoggedUser());
    }
    
    return Response.noContent().build();
  }
  
  @Path("/academicTerms/{ID:[0-9]*}/courses")
  @GET
  public Response findCoursesByTerm(@PathParam("ID") Long id, @DefaultValue("false") @QueryParam("filterArchived") boolean filterArchived) {
    AcademicTerm academicTerm = calendarController.findAcademicTermById(id);
    if (academicTerm == null) {
      return Response.status(Status.NOT_FOUND).build();
    }

    if (academicTerm.getArchived()) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    Date timeframeStart = academicTerm.getStartDate();
    Date timeframeEnd = academicTerm.getEndDate();
    
    SearchResult<Course> courses = calendarController.findCoursesByTerm(100, 0, SearchTimeFilterMode.INCLUSIVE, timeframeStart, timeframeEnd, filterArchived);
    
    return Response.ok()
        .entity(objectFactory.createModel(courses.getResults()))
        .build();
  }

}
