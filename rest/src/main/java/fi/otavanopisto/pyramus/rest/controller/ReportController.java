package fi.otavanopisto.pyramus.rest.controller;

import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import fi.otavanopisto.pyramus.dao.reports.ReportCategoryDAO;
import fi.otavanopisto.pyramus.dao.reports.ReportDAO;
import fi.otavanopisto.pyramus.domainmodel.reports.Report;
import fi.otavanopisto.pyramus.domainmodel.reports.ReportCategory;
import fi.otavanopisto.pyramus.domainmodel.users.User;

@Dependent
@Stateless
public class ReportController {
  @Inject
  private ReportDAO reportDAO;
  @Inject
  private ReportCategoryDAO reportCategoryDAO;

  public Report createReport(String name, String data, User user) {
    Report report = reportDAO.create(name, data, user);
    return report;
  }
  
  public ReportCategory createReportCategory(String name, Integer indexColumn) {
    ReportCategory reportCategory = reportCategoryDAO.create(name, indexColumn);
    return reportCategory;
  }

  public List<Report> findReports() {
    List<Report> reports = reportDAO.listAll();
    return reports;
  }
  
  public List<Report> findUnarchivedReports() {
    List<Report> reports = reportDAO.listUnarchived();
    return reports;
  }

  public Report findReportById(Long id) {
    Report report = reportDAO.findById(id);
    return report;
  }

  public List<ReportCategory> findReportCategories() {
    List<ReportCategory> reportCategories = reportCategoryDAO.listAll();
    return reportCategories;
  }
  
  public List<ReportCategory> findUnarchivedReportCategories() {
    List<ReportCategory> reportCategories = reportCategoryDAO.listUnarchived();
    return reportCategories;
  }
  
  public ReportCategory findReportCategoryById(Long id) {
    ReportCategory reportCategory = reportCategoryDAO.findById(id);
    return reportCategory;
  }
  
  public Report updateReport(Report report, String name, ReportCategory reportCategory) {
    reportDAO.update(report, name, reportCategory);
    return report;
  }
  
  public Report updateReportName(Report report, String name, User user) {
    reportDAO.updateName(report, name, user);
    return report;
  }
  
  public Report updateReportData(Report report, String data, User user) {
    reportDAO.updateData(report, data, user);
    return report;
  }
  
  public ReportCategory updateReportCategory(ReportCategory reportCategory, String name, Integer indexColumn) {
    reportCategoryDAO.update(reportCategory, name, indexColumn);
    return reportCategory;
  }
  
  public Report archiveReport(Report report, User user) {
    reportDAO.archive(report, user);
    return report;
  }
  
  public Report unarchiveReport(Report report, User user) {
    reportDAO.unarchive(report, user);
    return report;
  }
  
  public ReportCategory archiveReportCategory(ReportCategory reportCategory, User user) {
    reportCategoryDAO.archive(reportCategory, user);
    return reportCategory;
  }
  
  public ReportCategory unarchiveReportCategory(ReportCategory reportCategory, User user) {
    reportCategoryDAO.unarchive(reportCategory, user);
    return reportCategory;
  }
}
