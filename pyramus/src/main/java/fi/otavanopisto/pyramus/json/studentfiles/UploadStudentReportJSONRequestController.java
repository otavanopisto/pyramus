package fi.otavanopisto.pyramus.json.studentfiles;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;

import fi.internetix.smvc.controllers.JSONRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.base.MagicKeyDAO;
import fi.otavanopisto.pyramus.dao.file.FileTypeDAO;
import fi.otavanopisto.pyramus.dao.file.StudentFileDAO;
import fi.otavanopisto.pyramus.dao.reports.ReportDAO;
import fi.otavanopisto.pyramus.dao.students.StudentDAO;
import fi.otavanopisto.pyramus.dao.users.StaffMemberDAO;
import fi.otavanopisto.pyramus.domainmodel.base.MagicKey;
import fi.otavanopisto.pyramus.domainmodel.file.FileType;
import fi.otavanopisto.pyramus.domainmodel.reports.Report;
import fi.otavanopisto.pyramus.domainmodel.students.Student;
import fi.otavanopisto.pyramus.domainmodel.users.User;
import fi.otavanopisto.pyramus.framework.JSONRequestController;
import fi.otavanopisto.pyramus.framework.PyramusFileUtils;
import fi.otavanopisto.pyramus.framework.UserRole;

/**
 * The controller responsible of modifying an existing student group.
 * 
 * @see fi.otavanopisto.pyramus.views.students.EditStudentGroupViewController
 */
public class UploadStudentReportJSONRequestController extends JSONRequestController {

  private static final Logger logger = Logger.getLogger(UploadStudentReportJSONRequestController.class.getName());

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
  
      String fileId = null;
      byte[] data = IOUtils.toByteArray(inputStream);

      if (PyramusFileUtils.isFileSystemStorageEnabled()) {
        try {
          fileId = PyramusFileUtils.generateFileId();
          PyramusFileUtils.storeFile(student, fileId, data);
          data = null;
        }
        catch (IOException e) {
          fileId = null;
          logger.log(Level.SEVERE, "Store user file to file system failed", e);
        }
      }
  
      String reportName = report.getName().toLowerCase().replaceAll("[^a-z0-9\\.]", "_");
      studentFileDAO.create(student, name, reportName + "." + format, fileId, fileType, contentType, data, loggedUser);
    } catch (MalformedURLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }
}
