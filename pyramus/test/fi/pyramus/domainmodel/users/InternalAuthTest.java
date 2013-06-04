package fi.pyramus.domainmodel.users;

import org.hibernate.PropertyValueException;
import org.hibernate.validator.InvalidStateException;
import org.testng.Assert;
import org.testng.annotations.Test;


import fi.pyramus.domainmodel.users.InternalAuth;
import fi.testutils.DatabaseDependingTest;


public class InternalAuthTest extends DatabaseDependingTest {

  private static String USERNAME = "uname や";
  private static String PASSWORD = "pword ん";
  
  @Test
  public void testGetId() {
    beginTransaction();
    InternalAuth internalAuth = new InternalAuth();
    internalAuth.setPassword(PASSWORD);
    internalAuth.setUsername(USERNAME);
    getCurrentSession().save(internalAuth);
    commit();
    
    Long internalAuthId = internalAuth.getId();
    
    beginTransaction();
    internalAuth = (InternalAuth) getCurrentSession().load(InternalAuth.class, internalAuthId);
    Assert.assertEquals(internalAuth.getId(), internalAuthId);
    commit();
    
    beginTransaction();
    getCurrentSession().delete(getCurrentSession().load(InternalAuth.class, internalAuthId));
    commit();
  }

  @Test
  public void testGetPassword() {
    beginTransaction();
    InternalAuth internalAuth = new InternalAuth();
    internalAuth.setPassword(PASSWORD);
    internalAuth.setUsername(USERNAME);
    getCurrentSession().save(internalAuth);
    commit();
    
    Long internalAuthId = internalAuth.getId();
    
    beginTransaction();
    internalAuth = (InternalAuth) getCurrentSession().load(InternalAuth.class, internalAuthId);
    Assert.assertEquals(internalAuth.getPassword(), PASSWORD);
    commit();
    
    beginTransaction();
    getCurrentSession().delete(getCurrentSession().load(InternalAuth.class, internalAuthId));
    commit();
  }

  @Test
  public void testSetPassword() {
    /* Null test */ 
    beginTransaction();
    try {
      InternalAuth internalAuth = new InternalAuth();
      internalAuth.setUsername(USERNAME);
      internalAuth.setPassword(null);
      getCurrentSession().save(internalAuth);
      commit();
      Assert.fail("Successfully created user with null password");
    } catch (PropertyValueException pve) {  
      rollback(); 
    }
    
    /* Empty test */ 
    beginTransaction();
    try {
      InternalAuth internalAuth = new InternalAuth();
      internalAuth.setUsername(USERNAME);
      internalAuth.setPassword("");
      getCurrentSession().save(internalAuth);
      commit();
      Assert.fail("Successfully create user with empty password");
    } catch (InvalidStateException ise) {  
      rollback(); 
    }
  }

  @Test
  public void testGetUsername() {
    beginTransaction();
    InternalAuth internalAuth = new InternalAuth();
    internalAuth.setPassword(PASSWORD);
    internalAuth.setUsername(USERNAME);
    getCurrentSession().save(internalAuth);
    commit();
    
    Long internalAuthId = internalAuth.getId();
    
    beginTransaction();
    internalAuth = (InternalAuth) getCurrentSession().load(InternalAuth.class, internalAuthId);
    Assert.assertEquals(internalAuth.getUsername(), USERNAME);
    commit();
    
    beginTransaction();
    getCurrentSession().delete(getCurrentSession().load(InternalAuth.class, internalAuthId));
    commit();
  }

  @Test
  public void testSetUsername() {
    /* Null test */ 
    beginTransaction();
    try {
      InternalAuth internalAuth = new InternalAuth();
      internalAuth.setPassword(PASSWORD);
      getCurrentSession().save(internalAuth);
      commit();
      Assert.fail("Successfully created user with null username");
    } catch (PropertyValueException pve) {  
      rollback(); 
    }
    
    /* Empty test */ 
    beginTransaction();
    try {
      InternalAuth internalAuth = new InternalAuth();
      internalAuth.setUsername("");
      internalAuth.setPassword(PASSWORD);
      getCurrentSession().save(internalAuth);
      commit();
      Assert.fail("Successfully create user with empty username");
    } catch (InvalidStateException ise) {  
      rollback(); 
    }
  }

}
