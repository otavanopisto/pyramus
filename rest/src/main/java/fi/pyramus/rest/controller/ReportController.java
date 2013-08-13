package fi.pyramus.rest.controller;

import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import fi.pyramus.dao.reports.ReportDAO;
import fi.pyramus.domainmodel.reports.Report;
import fi.pyramus.domainmodel.users.User;

@Dependent
@Stateless
public class ReportController {
  @Inject
  private ReportDAO reportDAO;

  public Report createReport(String name, String data, User user) {
    Report report = reportDAO.create(name, data, user);
    return report;
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

  public Report archiveReport(Report report, User user) {
    reportDAO.archive(report, user);
    return report;
  }
}
