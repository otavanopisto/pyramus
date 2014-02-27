package fi.pyramus.views.system;

import java.io.IOException;
import java.io.InputStream;
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
    
    ClassLoader classLoader = getClass().getClassLoader();
    InputStream initialDataStream = classLoader.getResourceAsStream("initialdata.xml");
    try {
      String classes = requestContext.getRequest().getParameter("classes");
      if (StringUtils.isEmpty(classes)) {
        dataImporter.importDataFromStream(initialDataStream, null);
      } else {
        dataImporter.importDataFromStream(initialDataStream, Arrays.asList(classes.split(",")));
      }
    } finally {
      try {
        initialDataStream.close();
      } catch (IOException e) {
        throw new SmvcRuntimeException(e); 
      }
    }
    
    requestContext.setRedirectURL(requestContext.getRequest().getContextPath() + "/system/reindexhibernateobjects.page");
  }

  public UserRole[] getAllowedRoles() {
    return new UserRole[] { UserRole.EVERYONE };
  }

}