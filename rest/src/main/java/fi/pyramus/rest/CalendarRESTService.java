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

import org.apache.commons.lang3.StringUtils;

import fi.pyramus.domainmodel.base.AcademicTerm;
import fi.pyramus.rest.controller.CalendarController;
import fi.pyramus.rest.tranquil.base.AcademicTermEntity;
import fi.tranquil.TranquilityBuilderFactory;

@Path("/calendar")
@Produces("application/json")
@Consumes("application/json")
@Stateful
@RequestScoped
public class CalendarRESTService extends AbstractRESTService {
  @Inject
  TranquilityBuilderFactory tranquilityBuilderFactory;
  @Inject
  CalendarController calendarController;
  
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
    } else if (termEntity.getArchived()) {
      calendarController.unarchiveAcademicTerm(academicTerm, getUser());
      return Response.ok(200).build();
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
  
  @Override
  protected TranquilityBuilderFactory getTranquilityBuilderFactory() {
    return tranquilityBuilderFactory;
  }

}
