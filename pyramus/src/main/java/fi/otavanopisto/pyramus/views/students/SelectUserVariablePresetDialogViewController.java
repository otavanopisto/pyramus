package fi.otavanopisto.pyramus.views.students;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;

public class SelectUserVariablePresetDialogViewController extends PyramusViewController {

  public void process(PageRequestContext requestContext) {
    readUserVariablePresets(requestContext);
    
    requestContext.getRequest().setAttribute("variableKey", requestContext.getString("variableKey"));
    requestContext.setIncludeJSP("/templates/students/selectuservariablepresetdialog.jsp");
  }

  /**
   * Reads a json from file system and delivers it to frontend. The file is of format
   * 
   * {
   *   "variableKey (from UserVariable.variableKey)": {
   *     "presets": [
   *       {
   *         label: "Lorem ipsum",
   *         value: "dolor sit amet"
   *       }
   *     ]
   *   }
   * }
   *
   */
  private void readUserVariablePresets(PageRequestContext pageRequestContext) {
    String json = null;

    String fileName = System.getProperty("jboss.server.config.dir") + "/pyramus-uservariable-presets.json";
    File file = new File(fileName);
    if (file.exists()) {
      try {
        json = FileUtils.readFileToString(file);
      } catch (IOException e) {
      }
    }
    
    if (StringUtils.isBlank(json)) {
      json = "{}";
    }
    
    setJsDataVariable(pageRequestContext, "userVariablePresets", json);
  }
  
  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.MANAGER, UserRole.STUDY_PROGRAMME_LEADER, UserRole.ADMINISTRATOR };
  }

}
