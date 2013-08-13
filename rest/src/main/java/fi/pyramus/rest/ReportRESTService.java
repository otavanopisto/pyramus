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
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang.StringUtils;

import fi.pyramus.domainmodel.reports.Report;
import fi.pyramus.rest.controller.ReportController;
import fi.pyramus.rest.tranquil.reports.ReportEntity;
import fi.tranquil.TranquilityBuilderFactory;

@Path("/reports")
@Produces("application/json")
@Consumes("application/json")
@Stateful
@RequestScoped
public class ReportRESTService extends AbstractRESTService{
  @Inject
  private TranquilityBuilderFactory tranquilityBuilderFactory;
  @Inject
  private ReportController reportController;

  @Path("/reports")
  @POST
  public Response createReport(ReportEntity reportEntity) {
    String name = reportEntity.getName();
    String data = reportEntity.getData();
    if (!StringUtils.isBlank(name) && !StringUtils.isBlank(data) ) {
      return Response.ok()
          .entity(tranqualise(reportController.createReport(name, data, getUser())))
          .build();
    } else {
      return Response.status(501).build();
    }
  }
  
  @Path("/reports")
  @GET
  public Response findReports(@DefaultValue("false") @QueryParam("filterArchived") boolean filterArchived) {
      List<Report> schools;
      if (filterArchived) {
        schools = reportController.findUnarchivedReports();
      } else {
        schools = reportController.findReports();
      }
      if (!schools.isEmpty()){
        return Response.ok()
            .entity(tranqualise(schools))
            .build();
      } else {
        return Response.status(Status.NOT_FOUND).build();
      }
  }
  
  @Path("/reports/{ID:[0-9]*}")
  @GET
  public Response findReportById(@PathParam("ID") Long id) {
    Report report = reportController.findReportById(id);
    if (report != null) {
      return Response.ok()
          .entity(tranqualise(report))
          .build();
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }
  
  @Path("/reports/{ID:[0-9]*}")
  @DELETE
  public Response archiveReport(@PathParam("ID") Long id) {
    Report report = reportController.findReportById(id);
    if(report != null) {
      return Response.ok()
          .entity(tranqualise(reportController.archiveReport(report, getUser())))
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
