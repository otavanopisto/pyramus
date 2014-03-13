package fi.pyramus.views.system.setupwizard;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import fi.internetix.smvc.controllers.PageRequestContext;
import fi.pyramus.dao.DAOFactory;
import fi.pyramus.dao.SystemDAO;
import fi.pyramus.dao.base.DefaultsDAO;
import fi.pyramus.util.DataImporter;

public class IndexSetupWizardViewController extends SetupWizardController {
  
  public IndexSetupWizardViewController() {
    super("index");
  }
  
  @Override
  public void setup(PageRequestContext requestContext) throws SetupWizardException {
    DefaultsDAO defaultsDAO = DAOFactory.getInstance().getDefaultsDAO();
    SystemDAO systemDAO = DAOFactory.getInstance().getSystemDAO();

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
      
      List<Class<?>> indexedEntities = systemDAO.getIndexedEntities();
      for (Class<?> indexedEntity : indexedEntities) {
        try {
          systemDAO.reindexHibernateSearchObjects(indexedEntity, 200);
        } catch (InterruptedException e) {
          throw new SetupWizardException("Error occurred while initializing search indices", e); 
        }
      }
      
    }
  }

  @Override
  public void save(PageRequestContext requestContext) throws SetupWizardException {
  }

  @Override
  public boolean isInitialized(PageRequestContext requestContext) throws SetupWizardException {
    return false;
  }
  
}
