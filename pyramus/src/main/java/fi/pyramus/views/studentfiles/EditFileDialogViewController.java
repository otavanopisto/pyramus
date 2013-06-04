package fi.pyramus.views.studentfiles;

import java.util.Collections;
import java.util.List;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.file.FileTypeDAO;
import fi.pyramus.dao.file.StudentFileDAO;
import fi.pyramus.domainmodel.file.FileType;
import fi.pyramus.domainmodel.users.Role;
import fi.pyramus.framework.PyramusViewController;
import fi.pyramus.framework.UserRole;
import fi.pyramus.util.StringAttributeComparator;

/**
 * The controller responsible of the Search Modules dialog of the application.
 */
public class EditFileDialogViewController extends PyramusViewController {
  
  /**
   * Processes the page request by including the corresponding JSP page to the response.
   * 
   * @param pageRequestContext Page request context
   */
  public void process(PageRequestContext pageRequestContext) {
    Long fileId = pageRequestContext.getLong("fileId");

    StudentFileDAO studentFileDAO = DAOFactory.getInstance().getStudentFileDAO();
    FileTypeDAO fileTypeDAO = DAOFactory.getInstance().getFileTypeDAO();
    List<FileType> fileTypes = fileTypeDAO.listUnarchived();
    Collections.sort(fileTypes, new StringAttributeComparator("getName"));
    
    pageRequestContext.getRequest().setAttribute("file", studentFileDAO.findById(fileId));
    pageRequestContext.getRequest().setAttribute("fileTypes", fileTypes);
    pageRequestContext.setIncludeJSP("/templates/studentfiles/editfile.jsp");
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
