package fi.otavanopisto.pyramus.views.applications;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.otavanopisto.pyramus.dao.DAOFactory;
import fi.otavanopisto.pyramus.dao.system.ConfigurationDAO;
import fi.otavanopisto.pyramus.domainmodel.system.Configuration;
import fi.otavanopisto.pyramus.framework.PyramusViewController;
import fi.otavanopisto.pyramus.framework.UserRole;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

/**
  Example:

  {"schoolStudentGroups": [
    {"school": "school name", "studentGroup": "student group name"},
    {"school": "school name", "studentGroup": "student group name"},
    {"school": "school name", "studentGroup": "student group name"}
  ]}

 */

public class ConfigurationViewController extends PyramusViewController {
  
  public void process(PageRequestContext pageRequestContext) {
    switch (pageRequestContext.getRequest().getMethod()) {
    case "GET":
      doGet(pageRequestContext);
      break;
    case "POST":
      doPost(pageRequestContext);
      break;
    }
  }
  
  private void doGet(PageRequestContext pageRequestContext) {
    ConfigurationDAO configurationDAO = DAOFactory.getInstance().getConfigurationDAO();
    Configuration configuration = configurationDAO.findByName("application");
    if (configuration != null) {
      pageRequestContext.getRequest().setAttribute("configuration", configuration.getValue());      
    }
    pageRequestContext.setIncludeJSP("/templates/applications/configuration.jsp");
  }

  private void doPost(PageRequestContext pageRequestContext) {
    String configurationDocument = pageRequestContext.getString("configuration");
    if (StringUtils.isBlank(configurationDocument)) {
      configurationDocument = null;
    }
    else {
      try {
        // Validate JSON syntax (could also validate that each school and student group currently
        // point to one entity but since they can be modified or deleted later on...)
        JSONObject.fromObject(configurationDocument);
      }
      catch (JSONException e) {
        try {
          pageRequestContext.getResponse().sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
        catch (IOException e1) {
        }
        return;
      }
    }
    ConfigurationDAO configurationDAO = DAOFactory.getInstance().getConfigurationDAO();
    Configuration configuration = configurationDAO.findByName("application");
    if (configuration == null) {
      configurationDAO.create("application", configurationDocument);
    }
    else {
      configurationDAO.update(configuration, configurationDocument);
    }
    pageRequestContext.setRedirectURL(pageRequestContext.getReferer(true));
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.ADMINISTRATOR };
  }

}
