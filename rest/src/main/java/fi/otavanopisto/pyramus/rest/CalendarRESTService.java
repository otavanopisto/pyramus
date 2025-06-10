package fi.otavanopisto.pyramus.rest;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import jakarta.ejb.Stateful;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.xml.bind.JAXBException;

import org.apache.commons.lang3.StringUtils;

import java.time.OffsetDateTime;
import fi.otavanopisto.pyramus.domainmodel.base.AcademicTerm;
import fi.otavanopisto.pyramus.domainmodel.courses.Course;
import fi.otavanopisto.pyramus.persistence.search.SearchResult;
import fi.otavanopisto.pyramus.persistence.search.SearchTimeFilterMode;
import fi.otavanopisto.pyramus.rest.annotation.RESTPermit;
import fi.otavanopisto.pyramus.rest.controller.CalendarController;
import fi.otavanopisto.pyramus.rest.controller.permissions.CalendarPermissions;
import fi.otavanopisto.pyramus.security.impl.SessionController;

@Path("/calendar")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Stateful
@RequestScoped
public class CalendarRESTService extends AbstractRESTService {
  
  @Inject
  private CalendarController calendarController;

  @Inject
  private SessionController sessionController;

  @Inject
  private ObjectFactory objectFactory;
  
  @Path("/academicTerms")
  @POST
  @RESTPermit (CalendarPermissions.CREATE_ACADEMICTERM)
  public Response createAcademidTerm(fi.otavanopisto.pyramus.rest.model.AcademicTerm entity) {
    String name = entity.getName();
    OffsetDateTime startDate = entity.getStartDate();
    OffsetDateTime endDate = entity.getEndDate();
    
    if (StringUtils.isBlank(name)) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    if (startDate == null || endDate == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    return Response.ok()
        .entity(objectFactory.createModel(calendarController.createAcademicTerm(name, toDate(startDate), toDate(endDate))))
        .build();
  }
  
  @Path("/academicTerms")
  @GET
  @RESTPermit (CalendarPermissions.LIST_ACADEMICTERMS)
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
  @RESTPermit (CalendarPermissions.FIND_ACADEMICTERM)
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
  @RESTPermit (CalendarPermissions.UPDATE_ACADEMICTERM)
  public Response updateAcademicTerm(@PathParam("ID") Long id, fi.otavanopisto.pyramus.rest.model.AcademicTerm entity) {
    AcademicTerm academicTerm = calendarController.findAcademicTermById(id);
    String name = entity.getName();
    OffsetDateTime startDate = entity.getStartDate();
    OffsetDateTime endDate = entity.getEndDate();
    
    if (StringUtils.isBlank(name)) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    if (startDate == null || endDate == null) {
      return Response.status(Status.BAD_REQUEST).build();
    }
    
    return Response
        .ok(objectFactory.createModel(calendarController.updateAcademicTerm(academicTerm, name, Date.from(startDate.toInstant()), Date.from(endDate.toInstant()))))
        .build();
  }
  
  @Path("/academicTerms/{ID:[0-9]*}")
  @DELETE
  @RESTPermit (CalendarPermissions.DELETE_ACADEMICTERM)
  public Response deleteAcademicTerm(@PathParam("ID") Long id, @DefaultValue ("false") @QueryParam ("permanent") Boolean permanent) {
    AcademicTerm academicTerm = calendarController.findAcademicTermById(id);
    if (academicTerm == null) {
      return Response.status(Status.NOT_FOUND).build();
    }
    
    if (permanent) {
      calendarController.deleteAcademicTerm(academicTerm);
    } else {
      calendarController.archiveAcademicTerm(academicTerm, sessionController.getUser());
    }
    
    return Response.noContent().build();
  }
  
  @Path("/academicTerms/{ID:[0-9]*}/courses")
  @GET
  @RESTPermit (CalendarPermissions.FIND_COURSESBYACADEMICTERM)
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
