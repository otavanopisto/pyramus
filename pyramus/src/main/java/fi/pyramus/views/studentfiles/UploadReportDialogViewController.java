package fi.pyramus.views.studentfiles;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.file.FileTypeDAO;
import fi.pyramus.dao.reports.ReportDAO;
import fi.pyramus.dao.students.StudentDAO;
import fi.pyramus.domainmodel.file.FileType;
import fi.pyramus.domainmodel.users.Role;
import fi.pyramus.framework.PyramusViewController;
import fi.pyramus.framework.UserRole;
import fi.pyramus.util.StringAttributeComparator;

/**
 * The controller responsible of the Search Modules dialog of the application.
 */
public class UploadReportDialogViewController extends PyramusViewController {
  
  /**
   * Processes the page request by including the corresponding JSP page to the response.
   * 
   * @param pageRequestContext Page request context
   */
  public void process(PageRequestContext pageRequestContext) {
    FileTypeDAO fileTypeDAO = DAOFactory.getInstance().getFileTypeDAO();
    ReportDAO reportDAO = DAOFactory.getInstance().getReportDAO();
    StudentDAO studentDAO = DAOFactory.getInstance().getStudentDAO();

    Long studentId = pageRequestContext.getLong("studentId");
    Long reportId = pageRequestContext.getLong("reportId");

    List<FileType> fileTypes = fileTypeDAO.listUnarchived();
    Collections.sort(fileTypes, new StringAttributeComparator("getName"));

    StringBuffer reportParameters = new StringBuffer();
    
    Map<String, String[]> parameterMap = pageRequestContext.getRequest().getParameterMap();
    for (String parameterName : parameterMap.keySet()) {
      if (!reservedParameters.contains(parameterName)) {
        String[] values = parameterMap.get(parameterName);
        for (String value : values) {
          // TODO ISO-8859-1 should be UTF-8, once Birt's parameter dialog form has its accept-charset="UTF-8" set 
          try {
            reportParameters.append('&').append(parameterName).append('=').append(URLEncoder.encode(value, "ISO-8859-1"));
          }
          catch (UnsupportedEncodingException e) {
            throw new SmvcRuntimeException(e);
          }
        }
      }
    }
    
    pageRequestContext.getRequest().setAttribute("student", studentDAO.findById(studentId));
    pageRequestContext.getRequest().setAttribute("report", reportDAO.findById(reportId));

    pageRequestContext.getRequest().setAttribute("reportParameters", reportParameters);
    
    pageRequestContext.getRequest().setAttribute("fileTypes", fileTypes);
    pageRequestContext.setIncludeJSP("/templates/studentfiles/uploadreport.jsp");
  }

  /**
   * Returns the roles allowed to access this page. Creating projects is available for users with
   * {@link Role#MANAGER} or {@link Role#ADMINISTRATOR} privileges.
   * 
   * @return The roles allowed to access this page
   */
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.ADMINISTRATOR };
  }

  private static Set<String> reservedParameters = new HashSet<String>();
  
  static {
    reservedParameters.add("reportId");
    reservedParameters.add("magicKey");
    reservedParameters.add("format");
    reservedParameters.add("__format");
    reservedParameters.add("__report");
  }
  
  
}
