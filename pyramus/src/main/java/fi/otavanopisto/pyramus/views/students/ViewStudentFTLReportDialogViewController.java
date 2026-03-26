package fi.otavanopisto.pyramus.views.students;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.reports.ReportDAO;
import fi.otavanopisto.pyramus.dao.students.StudentDAO;
import fi.otavanopisto.pyramus.domainmodel.reports.Report;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class ViewStudentFTLReportDialogViewController extends PyramusViewController {

  public void process(PageRequestContext requestContext) {
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    ReportDAO reportDAO = DAOFactory.getInstance().getReportDAO();

    Long studentId = requestContext.getLong("studentId");
    Long reportId = requestContext.getLong("reportId");
    
    Student student = studentDAO.findById(studentId);
    Report report = reportDAO.findById(reportId);
    
    requestContext.getRequest().setAttribute("student", student);
    requestContext.getRequest().setAttribute("report", report);
    
    requestContext.setIncludeJSP("/templates/students/viewstudentftlreportdialog.jsp");
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

}
