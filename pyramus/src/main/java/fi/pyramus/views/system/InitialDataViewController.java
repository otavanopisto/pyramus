package fi.pyramus.views.system;

import java.util.Arrays;

import org.apache.commons.lang.StringUtils;

import fi.internetix.smvc.SmvcRuntimeException;
import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.DefaultsDAO;
import fi.pyramus.framework.PyramusViewController;
import fi.pyramus.framework.UserRole;
import fi.pyramus.util.DataImporter;

public class InitialDataViewController extends PyramusViewController {

  public void process(PageRequestContext requestContext) {
    DefaultsDAO defaultsDAO = DAOFactory.getInstance().getDefaultsDAO();

    if (defaultsDAO.isPyramusInitialized()) 
      throw new SmvcRuntimeException(new Exception("Pyramus is already initialized."));
    
    DataImporter dataImporter = new DataImporter();
    String classes = requestContext.getRequest().getParameter("classes");
    if (StringUtils.isEmpty(classes)) {
      dataImporter.importDataFromFile(System.getProperty("appdirectory") + "initialdata.xml", null);
    } else {
      dataImporter.importDataFromFile(System.getProperty("appdirectory") + "initialdata.xml", Arrays.asList(classes.split(",")));
    }
    
    requestContext.setRedirectURL(requestContext.getRequest().getContextPath() + "/system/reindexhibernateobjects.page");
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.EVERYONE };
  }

}