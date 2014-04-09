package fi.pyramus.views.system;

import java.io.IOException;
import java.io.InputStreamReader;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.framework.PyramusFormViewController;
import fi.pyramus.framework.UserRole;
import fi.pyramus.util.DataImporter;
import fi.pyramus.util.dataimport.scripting.ScriptedImporter;

public class ImportScriptedDataViewController extends PyramusFormViewController {

  @Override
  public void processForm(PageRequestContext requestContext) {
    requestContext.setIncludeJSP("/templates/system/importscripteddata.jsp");
  }
  
  @Override
  public void processSend(PageRequestContext requestContext) {
    ScriptedImporter dataImporter = new ScriptedImporter();
    try {
      dataImporter.importDataFromReader(new InputStreamReader(requestContext.getFile("file").getInputStream()));
    } catch (Exception e) {
      throw new SmvcRuntimeException(e);
    }
    
    requestContext.setRedirectURL(requestContext.getRequest().getContextPath() + "/index.page");
  }
  
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR };
  }
 
  
}