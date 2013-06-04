package fi.pyramus.domainmodel.users;

import org.hibernate.PropertyValueException;
import org.hibernate.Session;
import org.hibernate.validator.InvalidStateException;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


import fi.pyramus.domainmodel.users.User;
import fi.pyramus.persistence.usertypes.Role;
import fi.testutils.DatabaseDependingTest;


public class UserServiceTest extends DatabaseDependingTest {

  private static String FIRSTNAME = "やかん - Teakettle";
  private static String LASTNAME = "铲 - Shovel";
  private static String AUTHPROVIDER = "test";
  private static String EXTERNALID = "1";
  private static Role ROLE = UserRole.USER;
  
  @BeforeMethod
  public void setUp() {
    beginTransaction();
    
    User user = new User();
    user.setAuthProvider(AUTHPROVIDER);
    user.setExternalId(EXTERNALID);
    user.setFirstName(FIRSTNAME);
    user.setLastName(LASTNAME);
    user.setRole(ROLE);
    
    getCurrentSession().save(user);
    
    commit();
    
    userId = user.getId();
  }
  
  @AfterMethod
  public void tearDown() { 
    beginTransaction();
    Session s = getCurrentSession();
    s.delete(s.load(User.class, userId));
    commit();
  }
  
  @Test
  public void testGetId() {
    beginTransaction();
    Session s = getCurrentSession();
    User user = (User) s.load(User.class, userId);
    Assert.assertEquals(user.getId(), userId);
    commit();
  }

  @Test
  public void testGetFirstName() {
    beginTransaction();
    Session s = getCurrentSession();
    User user = (User) s.load(User.class, userId);
    Assert.assertEquals(user.getFirstName(), FIRSTNAME);
    commit();
  }

  @Test
  public void testSetFirstName() {
    /* Null test */ 
    beginTransaction();
    try {
      User user = new User();
      user.setAuthProvider(AUTHPROVIDER);
      user.setExternalId(EXTERNALID);
      user.setFirstName(null);
      user.setLastName(LASTNAME);
      user.setRole(ROLE);
      getCurrentSession().save(user);
      commit();
      Assert.fail("Successfully created user with null firstname");
    } catch (PropertyValueException pve) {  
      rollback(); 
    }
    
    /* Empty test */ 
    beginTransaction();
    try {
      User user = new User();
      user.setAuthProvider(AUTHPROVIDER);
      user.setExternalId(EXTERNALID);
      user.setFirstName("");
      user.setLastName(LASTNAME);
      user.setRole(ROLE);
      getCurrentSession().save(user);
      commit();
      Assert.fail("Successfully create user with empty firstname");
    } catch (InvalidStateException ise) {  
      rollback(); 
    }
  }

  @Test
  public void testGetLastName() {
    beginTransaction();
    Session s = getCurrentSession();
    User user = (User) s.load(User.class, userId);
    Assert.assertEquals(user.getLastName(), LASTNAME);
    commit();
  }

  @Test
  public void testSetLastName() {
    /* Null test */ 
    beginTransaction();
    try {
      User user = new User();
      user.setAuthProvider(AUTHPROVIDER);
      user.setExternalId(EXTERNALID);
      user.setFirstName(FIRSTNAME);
      user.setLastName(null);
      user.setRole(ROLE);
      getCurrentSession().save(user);
      commit();
      Assert.fail("Successfully created user with null lastname");
    } catch (PropertyValueException pve) {  
      rollback(); 
    }
    
    /* Empty test */ 
    beginTransaction();
    try {
      User user = new User();
      user.setAuthProvider(AUTHPROVIDER);
      user.setExternalId(EXTERNALID);
      user.setFirstName(FIRSTNAME);
      user.setLastName("");
      user.setRole(ROLE);
      getCurrentSession().save(user);
      commit();
      Assert.fail("Successfully create user with empty lastname");
    } catch (InvalidStateException ise) {  
      rollback(); 
    }
  }

  @Test
  public void testGetAuthProvider() {
    beginTransaction();
    Session s = getCurrentSession();
    User user = (User) s.load(User.class, userId);
    Assert.assertEquals(user.getAuthProvider(), AUTHPROVIDER);
    commit();
  }

  @Test
  public void testSetAuthProvider() {
    /* Null test */ 
    beginTransaction();
    try {
      User user = new User();
      user.setAuthProvider(null);
      user.setExternalId(EXTERNALID);
      user.setFirstName(FIRSTNAME);
      user.setLastName(LASTNAME);
      user.setRole(ROLE);
      getCurrentSession().save(user);
      commit();
      Assert.fail("Successfully created user with null authprovider");
    } catch (PropertyValueException pve) {  
      rollback(); 
    }
    
    /* Empty test */ 
    beginTransaction();
    try {
      User user = new User();
      user.setAuthProvider("");
      user.setExternalId(EXTERNALID);
      user.setFirstName(FIRSTNAME);
      user.setLastName(LASTNAME);
      user.setRole(ROLE);
      getCurrentSession().save(user);
      commit();
      Assert.fail("Successfully create user with empty authprovider");
    } catch (InvalidStateException ise) {  
      rollback(); 
    }
  }

  @Test
  public void testGetExternalId() {
    beginTransaction();
    Session s = getCurrentSession();
    User user = (User) s.load(User.class, userId);
    Assert.assertEquals(user.getExternalId(), EXTERNALID);
    commit();
  }

  @Test
  public void testSetExternalId() {
    /* Null test */ 
    beginTransaction();
    try {
      User user = new User();
      user.setAuthProvider(AUTHPROVIDER);
      user.setExternalId(null);
      user.setFirstName(FIRSTNAME);
      user.setLastName(LASTNAME);
      user.setRole(ROLE);
      getCurrentSession().save(user);
      commit();
      Assert.fail("Successfully created user with null externalId");
    } catch (PropertyValueException pve) {  
      rollback(); 
    }
    
    /* Empty test */ 
    beginTransaction();
    try {
      User user = new User();
      user.setAuthProvider(AUTHPROVIDER);
      user.setExternalId("");
      user.setFirstName(FIRSTNAME);
      user.setLastName(LASTNAME);
      user.setRole(ROLE);
      getCurrentSession().save(user);
      commit();
      Assert.fail("Successfully create user with empty externalId");
    } catch (InvalidStateException ise) {  
      rollback(); 
    }
  }

  @Test
  public void testGetFullName() {
    beginTransaction();
    Session s = getCurrentSession();
    User user = (User) s.load(User.class, userId);
    Assert.assertEquals(user.getFullName(), FIRSTNAME + ' ' + LASTNAME);
    commit();
  }
  
  // TODO: ROLE TESTS

  private Long userId;
}
