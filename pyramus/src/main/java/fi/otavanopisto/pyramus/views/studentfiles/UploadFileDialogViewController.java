package fi.otavanopisto.pyramus.views.studentfiles;

import java.util.Collections;
import java.util.List;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.file.FileTypeDAO;
import fi.otavanopisto.pyramus.domainmodel.file.FileType;
import fi.otavanopisto.pyramus.domainmodel.users.Role;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;
import fi.otavanopisto.pyramus.util.StringAttributeComparator;

/**
 * The controller responsible of the Search Modules dialog of the application.
 */
public class UploadFileDialogViewController extends PyramusViewController {
  
  /**
   * Processes the page request by including the corresponding JSP page to the response.
   * 
   * @param pageRequestContext Page request context
   */
  public void process(PageRequestContext pageRequestContext) {
    Long studentId = pageRequestContext.getLong("studentId");

    FileTypeDAO fileTypeDAO = DAOFactory.getInstance().getFileTypeDAO();
    List<FileType> fileTypes = fileTypeDAO.listUnarchived();
    Collections.sort(fileTypes, new StringAttributeComparator("getName"));
    
    pageRequestContext.getRequest().setAttribute("studentId", studentId);
    pageRequestContext.getRequest().setAttribute("fileTypes", fileTypes);
    pageRequestContext.setIncludeJSP("/templates/studentfiles/uploadfile.jsp");
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

}
