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

import fi.pyramus.domainmodel.reports.Report;
import fi.pyramus.domainmodel.reports.ReportCategory;
import fi.pyramus.rest.controller.ReportController;

@Path("/reports")
@Produces("application/json")
@Consumes("application/json")
@Stateful
@RequestScoped
public class ReportRESTService extends AbstractRESTService{
//
//  @Inject
//  private ReportController reportController;
//
//  @Path("/reports")
//  @POST
//  public Response createReport(ReportEntity reportEntity) {
//    String name = reportEntity.getName();
//    String data = reportEntity.getData();
//    if (!StringUtils.isBlank(name) && !StringUtils.isBlank(data) ) {
//      return Response.ok()
//          .entity(tranqualise(reportController.createReport(name, data, getUser())))
//          .build();
//    } else {
//      return Response.status(501).build();
//    }
//  }
//  
//  @Path("/categories")
//  @POST
//  public Response createReportCategory(ReportCategoryEntity reportCategoryEntity) {
//    String name = reportCategoryEntity.getName();
//    Integer indexColumn = reportCategoryEntity.getIndexColumn();
//    if(!StringUtils.isBlank(name) && indexColumn != null) {
//      return Response.ok()
//      .entity(tranqualise(reportController.createReportCategory(name, indexColumn)))
//      .build();
//    } else {
//      return Response.status(501).build();
//    }
//  }
//  
//  @Path("/reports")
//  @GET
//  public Response findReports(@DefaultValue("false") @QueryParam("filterArchived") boolean filterArchived) {
//      List<Report> reports;
//      if (filterArchived) {
//        reports = reportController.findUnarchivedReports();
//      } else {
//        reports = reportController.findReports();
//      }
//      if (!reports.isEmpty()){
//        return Response.ok()
//            .entity(tranqualise(reports))
//            .build();
//      } else {
//        return Response.status(Status.NOT_FOUND).build();
//      }
//  }
//  
//  @Path("/reports/{ID:[0-9]*}")
//  @GET
//  public Response findReportById(@PathParam("ID") Long id) {
//    Report report = reportController.findReportById(id);
//    if (report != null) {
//      return Response.ok()
//          .entity(tranqualise(report))
//          .build();
//    } else {
//      return Response.status(Status.NOT_FOUND).build();
//    }
//  }
//  
//  @Path("/categories")
//  @GET
//  public Response findReportCategories(@DefaultValue("false") @QueryParam("filterArchived") boolean filterArchived) {
//      List<ReportCategory> reportCategories;
//      if (filterArchived) {
//        reportCategories = reportController.findUnarchivedReportCategories();
//      } else {
//        reportCategories = reportController.findReportCategories();
//      }
//      if (!reportCategories.isEmpty()){
//        return Response.ok()
//            .entity(tranqualise(reportCategories))
//            .build();
//      } else {
//        return Response.status(Status.NOT_FOUND).build();
//      }
//  }
//  
//  @Path("/categories/{ID:[0-9]*}")
//  @GET
//  public Response findReportCategoryById(@PathParam("ID") Long id) {
//    ReportCategory reportCategories = reportController.findReportCategoryById(id);
//    if (reportCategories != null) {
//      return Response.ok()
//          .entity(tranqualise(reportCategories))
//          .build();
//    } else {
//      return Response.status(Status.NOT_FOUND).build();
//    }
//  }
//  
//  @Path("/reports/{ID:[0-9]*}")
//  @PUT
//  public Response updateReport(@PathParam("ID") Long id, ReportEntity reportEntity) {
//    Report report = reportController.findReportById(id);
//    String name = reportEntity.getName();
//    String data = reportEntity.getData();
//    if (report != null && !StringUtils.isBlank(name)) {
//      Long categoryId = reportEntity.getCategory_id();
//      if (categoryId != null) {
//        ReportCategory reportCategory = reportController.findReportCategoryById(categoryId);
//        return Response.ok()
//            .entity(tranqualise(reportController.updateReport(report, name, reportCategory)))
//            .build();
//      } else {
//        return Response.ok()
//            .entity(tranqualise(reportController.updateReportName(report, name, getUser())))
//            .build(); 
//      }
//    } else if (!StringUtils.isBlank(data)) {
//      return Response.ok()
//          .entity(tranqualise(reportController.updateReportData(report,data,getUser())))
//          .build();
//    } else if (!reportEntity.getArchived()) {
//        return Response.ok()
//            .entity(tranqualise(reportController.unarchiveReport(report, getUser())))
//            .build();
//    } else {
//      return Response.status(501).build();
//    }
//  }
//  
//  @Path("/categories/{ID:[0-9]*}")
//  @PUT
//  public Response updateReportCategory(@PathParam("ID") Long id, ReportCategoryEntity reportCategoryEntity) {
//    ReportCategory reportCategory = reportController.findReportCategoryById(id);
//    String name = reportCategoryEntity.getName();
//    Integer indexColumn = reportCategoryEntity.getIndexColumn();
//    if(reportCategory != null && !StringUtils.isBlank(name) && indexColumn != null) {
//      return Response.ok()
//          .entity(tranqualise(reportController.updateReportCategory(reportCategory, name, indexColumn)))
//          .build();
//    } else if (!reportCategoryEntity.getArchived()) {
//        return Response.ok()
//            .entity(tranqualise(reportController.unarchiveReportCategory(reportCategory, getUser())))
//            .build();
//    } else {
//      return Response.status(501).build();
//    }
//  }
//  
//  @Path("/reports/{ID:[0-9]*}")
//  @DELETE
//  public Response archiveReport(@PathParam("ID") Long id) {
//    Report report = reportController.findReportById(id);
//    if(report != null) {
//      return Response.ok()
//          .entity(tranqualise(reportController.archiveReport(report, getUser())))
//          .build();
//    } else {
//      return Response.status(Status.NOT_FOUND).build();
//    }
//  }
//  
//  @Path("/categories/{ID:[0-9]*}")
//  @DELETE
//  public Response archiveReportCategory(@PathParam("ID") Long id) {
//    ReportCategory reportCategory = reportController.findReportCategoryById(id);
//    if(reportCategory != null) {
//      return Response.ok()
//          .entity(tranqualise(reportController.archiveReportCategory(reportCategory, getUser())))
//          .build();
//    } else {
//      return Response.status(Status.NOT_FOUND).build();
//    }
//  }
//  
}
