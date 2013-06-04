package fi.testutils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.PropertyValueException;
import org.hibernate.Session;
import org.hibernate.metadata.ClassMetadata;
import org.hsqldb.Server;
import org.hsqldb.ServerConfiguration;
import org.hsqldb.persist.HsqlProperties;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;

import fi.pyramus.persistence.HibernateUtils;

public class DatabaseDependingTest extends UnitTest {
  
  private static final boolean useGarbageTesting = true;  
  
  // TODO: This is not a appropriate way to do unit testing with Hibernate
  // but i don't know a better way to do it. Problem is that in DAO tests 
  // HibernateUtils.getSessionFactory().getCurrentSession(); is used but after first 
  // commit the session is closed which causes all succeeding session operations to fail 
  @SuppressWarnings("deprecation")
  protected Session getCurrentSession() {
    Session s = HibernateUtils.getSessionFactory().getCurrentSession();
    if (!s.isOpen())
      s.reconnect();
    return s;
  }
  
  protected void beginTransaction() {
    if (getCurrentSession().getTransaction().isActive())
      throw new RuntimeException("Transaction is already active");
    
    getCurrentSession().beginTransaction();
  }
  
  protected void commit() {
    if (!getCurrentSession().getTransaction().isActive())
      throw new RuntimeException("Transaction is not active");
    
    getCurrentSession().getTransaction().commit();
  }
  
  protected void rollback() {
    if (!getCurrentSession().getTransaction().isActive())
      throw new RuntimeException("Transaction is not active");
    
    getCurrentSession().getTransaction().rollback();
  }
  
  @AfterMethod
  public void ensureTransactionNotActive() {
    if (getCurrentSession().getTransaction().isActive())
      throw new RuntimeException("Transaction is still active");
  }
  
  @AfterClass
  public void checkForGarbage() {
    if (useGarbageTesting)
      testForGarbage();
  }
  
  @SuppressWarnings("unchecked")
  private void testForGarbage() {
    Session s = HibernateUtils.getSessionFactory().getCurrentSession();
    
    beginTransaction();
    
    Map<String, ClassMetadata> allClassMetadata = HibernateUtils.getSessionFactory().getAllClassMetadata();
    for (String key : allClassMetadata.keySet()) {
      ClassMetadata classMetadata = allClassMetadata.get(key);
      String className = classMetadata.getEntityName();
      Class<?> pojoClass;
      try {
        pojoClass = Class.forName(className);
      } catch (ClassNotFoundException e) {
        throw new RuntimeException(e);
      }
      int pojos;
      if ((pojos = s.createCriteria(pojoClass).list().size()) > 0) {
        throw new RuntimeException("GARBAGE DETECTED :" + className + " found " + pojos + " instances");
      }
    }
    
    commit();
  }
  
  protected void checkProperty(String propertyName, PropertyCheckStrategy strategy ,TestCode code) {
    getPropertyChecks().get(strategy).checkProperty(propertyName, code);
  }
  
  public enum PropertyCheckStrategy {
    NotNull,
    NotEmpty,
    Valid
  }

  public interface TestCode {
    public void run();
  };
  
  private interface PropertyCheck {
    public void checkProperty(String propertyName, TestCode code);
  } 
  
  private class NotNullPropertyCheckStrategy implements PropertyCheck {
    public void checkProperty(String propertyName, TestCode code) {
      beginTransaction();
      try {
        code.run();
        commit();
        Assert.fail("null check failed for " + propertyName);
      } catch (PropertyValueException pve) {
        Assert.assertEquals(pve.getPropertyName(), propertyName);
        rollback();
      }
    }
  }
  
  private class NotEmptyPropertyCheckStrategy implements PropertyCheck {
    public void checkProperty(String propertyName, TestCode code) {
      beginTransaction();
      try {
        code.run();
        commit();
        Assert.fail("empty check failed for " + propertyName);
      } catch (InvalidStateException ise) {
        rollback();
      }
    }
  }
  
  private class ValidValuePropertyCheckStrategy implements PropertyCheck {
    public void checkProperty(String propertyName, TestCode code) {
      beginTransaction();
      try {
        code.run();
        commit();
        Assert.fail("valid check failed for " + propertyName);
      } catch (InvalidStateException ise) {
        rollback();
      }
    }
  }
  
  private synchronized Map<PropertyCheckStrategy, PropertyCheck> getPropertyChecks() {
    if (propertyChecks == null) {
      propertyChecks = new HashMap<PropertyCheckStrategy, PropertyCheck>();
      propertyChecks.put(PropertyCheckStrategy.NotEmpty, new NotEmptyPropertyCheckStrategy());
      propertyChecks.put(PropertyCheckStrategy.NotNull, new NotNullPropertyCheckStrategy());
      propertyChecks.put(PropertyCheckStrategy.Valid, new ValidValuePropertyCheckStrategy());
      
    }
    
    return propertyChecks;
  }
  
  private Map<PropertyCheckStrategy, PropertyCheck> propertyChecks;
  
  static {
    try {
      FileReader fileReader = new FileReader(new File("test/fi/hsqldb.script"));
      FileWriter fileWriter = new FileWriter(new File("test.script"));
      
      char[] cbuf = new char[256];
      int len;
      while ((len = fileReader.read(cbuf)) > 0){
        fileWriter.write(cbuf, 0, len);
      }
      
      fileReader.close();
      fileWriter.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    
    HsqlProperties props = new HsqlProperties();
    
    ServerConfiguration.translateDefaultDatabaseProperty(props);
    Server server = new Server();
    server.setProperties(props);
    server.start();
    
    System.getProperties().setProperty("hibernate.driver", "org.hsqldb.jdbcDriver");
    System.getProperties().setProperty("hibernate.url", "jdbc:hsqldb:hsql://localhost/");
    System.getProperties().setProperty("hibernate.username", "sa");
    System.getProperties().setProperty("hibernate.password", "");
    System.getProperties().setProperty("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
    System.getProperties().setProperty("hibernate.hbm2ddl", "create");
    
    System.getProperties().setProperty("hibernate.search.default.directory_provider", "org.hibernate.search.store.RAMDirectoryProvider");
  }
}