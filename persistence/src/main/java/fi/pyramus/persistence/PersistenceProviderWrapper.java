package fi.pyramus.persistence;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.persistence.EntityManagerFactory;
import javax.persistence.SharedCacheMode;
import javax.persistence.ValidationMode;
import javax.persistence.spi.ClassTransformer;
import javax.persistence.spi.PersistenceUnitInfo;
import javax.persistence.spi.PersistenceUnitTransactionType;
import javax.sql.DataSource;

import org.hibernate.ejb.HibernatePersistence;


public class PersistenceProviderWrapper extends org.hibernate.ejb.HibernatePersistence {
 
  public PersistenceProviderWrapper() {
    super();
  }
  
  @Override
  @SuppressWarnings("rawtypes")
  public EntityManagerFactory createContainerEntityManagerFactory(PersistenceUnitInfo info, Map properties) {
    PersistenceUnitInfo infoDelegate = new PersistenceUnitInfoDelegate(info, DomainModelExtensionVault.getInstance().getEntityNames());
    return super.createContainerEntityManagerFactory(infoDelegate, properties);
  }
  
  @Override
  @SuppressWarnings({ "deprecation", "rawtypes" })
  public EntityManagerFactory createEntityManagerFactory(Map properties) {
    return super.createEntityManagerFactory(properties);
  }
  
  @Override
  @SuppressWarnings("rawtypes")
  public EntityManagerFactory createEntityManagerFactory(String persistenceUnitName, Map properties) {
    return super.createEntityManagerFactory(persistenceUnitName, properties);
  }
  
  private class PersistenceUnitInfoDelegate implements PersistenceUnitInfo {

    public PersistenceUnitInfoDelegate(PersistenceUnitInfo original, Set<String> domainModelExtensions) {
      this.original = original;
      this.domainModelExtensions = domainModelExtensions;
    }
    
    public void addTransformer(ClassTransformer arg0) {
      original.addTransformer(arg0);
    }

    public boolean excludeUnlistedClasses() {
      return original.excludeUnlistedClasses();
    }

    public ClassLoader getClassLoader() {
      return original.getClassLoader();
    }

    public List<URL> getJarFileUrls() {
      return original.getJarFileUrls();
    }

    public DataSource getJtaDataSource() {
      return original.getJtaDataSource();
    }

    public List<String> getManagedClassNames() {
      List<String> managedClassNames = new ArrayList<String>(original.getManagedClassNames());
      managedClassNames.addAll(domainModelExtensions);
      return managedClassNames;
    }

    public List<String> getMappingFileNames() {
      return original.getMappingFileNames();
    }

    public ClassLoader getNewTempClassLoader() {
      return original.getNewTempClassLoader();
    }

    public DataSource getNonJtaDataSource() {
      return original.getNonJtaDataSource();
    }

    public String getPersistenceProviderClassName() {
      return HibernatePersistence.class.getName();
    }

    public String getPersistenceUnitName() {
      return original.getPersistenceUnitName();
    }

    public URL getPersistenceUnitRootUrl() {
      return original.getPersistenceUnitRootUrl();
    }

    public String getPersistenceXMLSchemaVersion() {
      return original.getPersistenceXMLSchemaVersion();
    }

    public Properties getProperties() {
      return original.getProperties();
    }

    public SharedCacheMode getSharedCacheMode() {
      return original.getSharedCacheMode();
    }

    public PersistenceUnitTransactionType getTransactionType() {
      return original.getTransactionType();
    }

    public ValidationMode getValidationMode() {
      return original.getValidationMode();
    }
   
    private PersistenceUnitInfo original;
    private Set<String> domainModelExtensions;
  }
}
