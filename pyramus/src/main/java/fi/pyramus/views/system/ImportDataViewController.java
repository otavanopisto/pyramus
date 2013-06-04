package fi.pyramus.views.system;

import java.io.IOException;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.framework.PyramusFormViewController;
import fi.pyramus.framework.UserRole;
import fi.pyramus.util.DataImporter;

public class ImportDataViewController extends PyramusFormViewController {

  @Override
  public void processForm(PageRequestContext requestContext) {
    requestContext.setIncludeJSP("/templates/system/importdata.jsp");
  }
  
  @Override
  public void processSend(PageRequestContext requestContext) {
    DataImporter dataImporter = new DataImporter();
    try {
      dataImporter.importDataFromStream(requestContext.getFile("file").getInputStream(), null);
    } catch (IOException e) {
      throw new SmvcRuntimeException(e);
    }
    
    requestContext.setRedirectURL(requestContext.getRequest().getContextPath() + "/index.page");
  }
  
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR };
  }
 
  
}