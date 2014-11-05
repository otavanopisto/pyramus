package fi.pyramus.json.studentfiles;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.io.IOUtils;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.MagicKeyDAO;
import fi.pyramus.dao.file.FileTypeDAO;
import fi.pyramus.dao.file.StudentFileDAO;
import fi.pyramus.dao.reports.ReportDAO;
import fi.pyramus.dao.students.StudentDAO;
import fi.pyramus.dao.users.StaffMemberDAO;
import fi.pyramus.domainmodel.base.MagicKey;
import fi.pyramus.domainmodel.file.FileType;
import fi.pyramus.domainmodel.reports.Report;
import fi.pyramus.domainmodel.students.Student;
import fi.pyramus.domainmodel.users.User;
import fi.pyramus.framework.JSONRequestController;
import fi.pyramus.framework.UserRole;

/**
 * The controller responsible of modifying an existing student group.
 * 
 * @see fi.pyramus.views.students.EditStudentGroupViewController
 */
public class UploadStudentReportJSONRequestController extends JSONRequestController {

  /**
   * Processes the request to edit a student group.
   * 
   * @param requestContext
   *          The JSON request context
   */
  public void process(JSONRequestContext requestContext) {
    StudentFileDAO studentFileDAO = DAOFactory.getInstance().getStudentFileDAO();
    FileTypeDAO fileTypeDAO = DAOFactory.getInstance().getFileTypeDAO();
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();
    StaffMemberDAO userDAO = DAOFactory.getInstance().getStaffMemberDAO();
    ReportDAO reportDAO = DAOFactory.getInstance().getReportDAO();
    
    Long studentId = requestContext.getLong("studentId");
    Long reportId = requestContext.getLong("reportId");
    String reportParameters = requestContext.getString("reportParameters");
    
    Long userId = requestContext.getLoggedUserId();
    User loggedUser = userDAO.findById(userId);
    String name = requestContext.getString("fileName");
    Long fileTypeId = requestContext.getLong("fileType");

    String contentType;
    String format = requestContext.getString("reportFormat");
    if ("doc".equals(format)) {
      contentType = "application/msword";
    } else {
      format = "pdf";
      contentType = "application/pdf";
    }  

    
    Student student = studentDAO.findById(studentId);
    FileType fileType = fileTypeId != null ? fileTypeDAO.findById(fileTypeId) : null;
    Report report = reportDAO.findById(reportId);
    
    MagicKeyDAO magicKeyDAO = DAOFactory.getInstance().getMagicKeyDAO();

    String reportsContextPath = System.getProperty("reports.contextPath");
    String reportsHost = System.getProperty("reports.host");
    String reportsProtocol = System.getProperty("reports.protocol");
    String reportsPort = System.getProperty("reports.port");

    String outputMethod = "preview"; // output or preview
    MagicKey magicKey = magicKeyDAO.findByApplicationScope();
    
    StringBuilder urlBuilder = new StringBuilder()
      .append(reportsProtocol)
      .append("://")
      .append(reportsHost)
      .append(":")
      .append(reportsPort)
      .append(reportsContextPath)
      .append("/")
      .append(outputMethod)
      .append("?magicKey=")
      .append(magicKey.getName())
      .append("&__report=reports/")
      .append(reportId)
      .append(".rptdesign");
    urlBuilder.append("&__format=").append(format);
    urlBuilder.append(reportParameters);

    try {
      URL url = new URL(urlBuilder.toString());

      URLConnection urlConn = url.openConnection();
      InputStream inputStream = urlConn.getInputStream();
  
      byte[] data = IOUtils.toByteArray(inputStream);
  
      String reportName = report.getName().toLowerCase().replaceAll("[^a-z0-9\\.]", "_");
      studentFileDAO.create(student, name, reportName + "." + format, fileType, contentType, data, loggedUser);
    } catch (MalformedURLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }
}
