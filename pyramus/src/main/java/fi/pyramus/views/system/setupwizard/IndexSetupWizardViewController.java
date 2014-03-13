package fi.pyramus.views.system.setupwizard;

import java.io.IOException;
import java.io.InputStream;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.base.DefaultsDAO;
import fi.pyramus.util.DataImporter;

public class IndexSetupWizardViewController extends SetupWizardController {
  
  public IndexSetupWizardViewController() {
    super("index");
  }
  
  @Override
  public void setup(PageRequestContext requestContext) throws SetupWizardException {
    DefaultsDAO defaultsDAO = DAOFactory.getInstance().getDefaultsDAO();

    if (!defaultsDAO.isPyramusInitialized()) {
      DataImporter dataImporter = new DataImporter();
      
      ClassLoader classLoader = getClass().getClassLoader();
      InputStream initialDataStream = classLoader.getResourceAsStream("initialdata.xml");
      try {
        dataImporter.importDataFromStream(initialDataStream, null);
      } finally {
        try {
          initialDataStream.close();
        } catch (IOException e) {
          throw new SetupWizardException("Error occurred while importing initial data", e); 
        }
      }
    }
  }

  @Override
  public void save(PageRequestContext requestContext) throws SetupWizardException {
  }
  
}
