package fi.pyramus.dao;

import java.util.List;

import org.hibernate.PropertyValueException;
import org.hibernate.validator.InvalidStateException;
import org.testng.Assert;
import org.testng.annotations.Test;

import fi.pyramus.dao.users.UserDAO;
import fi.pyramus.domainmodel.users.InternalAuth;
import fi.pyramus.domainmodel.users.User;
import fi.pyramus.persistence.usertypes.Role;
import fi.testutils.DatabaseDependingTest;
import fi.testutils.TestDataGenerator;

public class UserDAOTest extends DatabaseDependingTest {

  private static String FIRSTNAME = "やかん - Teakettle";
  private static String LASTNAME = "铲 - Shovel";
  private static String AUTHPROVIDER = "test";
  private static String EXTERNALID = "1";
  private static String USERNAME = "uname や";
  private static String PASSWORD = "pword か";
  private static String NEWPASSWORD = "newpword ん";
  private static Role ROLE = UserRole.USER;
  
  private final static String INTERNAL_AUTH_OLDUSER1 = "เก่า";
  private final static String INTERNAL_AUTH_NEWUSER1 = "ใหม่";
  private final static String INTERNAL_AUTH_OLDPASS1 = "รหัสผ่าน";
  private final static String INTERNAL_AUTH_OLDUSER2 = "ผู้ใช้";
  private final static String INTERNAL_AUTH_OLDPASS2 = "ลับ";
  private final static String INTERNAL_AUTH_NEWUSER2 = "ผู้ใช้ใหม่";
  private final static String INTERNAL_AUTH_NEWPASS2 = "เหลือร้ายลับ";
  
  @Test
  public void testGetUserLong() {
    beginTransaction();
    User user = userDAO.createUser(FIRSTNAME, LASTNAME, EXTERNALID, AUTHPROVIDER, ROLE);
    commit();
    
    Long userId = user.getId();
    
    /* basic user retrieval test */
    
    beginTransaction();
    user = userDAO.getUser(userId);
    Assert.assertEquals(user.getId(), userId);  
    userDAO.deleteUser(user);
    commit();
  }
  
  @Test
  public void testDeleteUser() {
    beginTransaction();
    User user = userDAO.createUser(FIRSTNAME, LASTNAME, EXTERNALID, AUTHPROVIDER, ROLE);
    commit();
    
    Long userId = user.getId();
    
    /* basic user removal test */
    
    beginTransaction();
    userDAO.deleteUser(userDAO.getUser(userId));
    commit();
  }

  @Test
  public void testCreateUser() {
    /* Null first name */ 
    beginTransaction();
    try {
      userDAO.createUser(null, LASTNAME, EXTERNALID, AUTHPROVIDER, ROLE);
      Assert.fail("Successfullycreated user with null firstname");
    } catch (PropertyValueException pve) {  
      rollback(); 
    }
    
    /* Empty first name */ 
    beginTransaction();
    try {
      userDAO.createUser("", LASTNAME, EXTERNALID, AUTHPROVIDER, ROLE);
      commit();
      Assert.fail("Successfully created user with empty first name");
    } catch (InvalidStateException ise) {
      rollback();
    }
  
    /* Null last name */ 
    beginTransaction();
    try {
      userDAO.createUser(FIRSTNAME, null, EXTERNALID, AUTHPROVIDER, ROLE);
      Assert.fail("Successfully nulled lastname");
    } catch (PropertyValueException pve) {  
      rollback(); 
    }
    
    /* Empty last name */  
    beginTransaction();
    try {
      userDAO.createUser(FIRSTNAME, "", EXTERNALID, AUTHPROVIDER, ROLE);
      commit();
      Assert.fail("Successfully created user with empty lastname");
    } catch (InvalidStateException ise) {
      rollback();
    }
    
    /* Null authprovider */ 
    beginTransaction();
    try {
      userDAO.createUser(FIRSTNAME, LASTNAME, null, AUTHPROVIDER, ROLE);
      Assert.fail("Successfully created user with null authprovider");
    } catch (PropertyValueException pve)  {  
      rollback(); 
    }
    
    /* Empty authprovider */ 
    beginTransaction();
    try {
      userDAO.createUser(FIRSTNAME, LASTNAME, "", AUTHPROVIDER, ROLE);
      commit();
      Assert.fail("Successfully created user with empty authprovider");
    } catch (InvalidStateException ise) {
      rollback();
    }
    
    /* Null externalId */
    beginTransaction();
    try {
      userDAO.createUser(FIRSTNAME, LASTNAME, EXTERNALID, null, ROLE);
      commit();
      Assert.fail("Successfully created user with null externalId");
    } catch (PropertyValueException pve)  {  
      rollback(); 
    }
    
    /* Empty externalId */ 
    beginTransaction();
    try {
      userDAO.createUser(FIRSTNAME, LASTNAME, EXTERNALID, "", ROLE);
      commit();
      Assert.fail("Successfully created user with empty externalId");
    } catch (InvalidStateException ise) {
      rollback();
    }
  }

  @Test
  public void testGetUserStringString() {
    beginTransaction();
    User user = userDAO.createUser(FIRSTNAME, LASTNAME, EXTERNALID, AUTHPROVIDER, ROLE);
    commit();
    
    Long userId = user.getId();
    
    /* Test to load user by externalId & authProvider combination */
    
    beginTransaction();
    user = userDAO.getUser(EXTERNALID, AUTHPROVIDER);
    Assert.assertEquals(user.getId(), userId);  
    userDAO.deleteUser(user);
    commit();
  }

  @Test
  public void testCreateInternalAuth() {
    beginTransaction();
    try {
      userDAO.createInternalAuth(null, PASSWORD);
      commit();
      Assert.fail("Successfully internalAuth with null username");
    } catch (PropertyValueException pve)  {  
      rollback(); 
    }

    /* Empty username */
    beginTransaction();
    try {
      userDAO.createInternalAuth("", USERNAME);
      commit();
      Assert.fail("Successfully created user with empty username");
    } catch (InvalidStateException ise) {
      rollback();
    }
    
    /* Null username */
    beginTransaction();
    try {
      userDAO.createInternalAuth(USERNAME, null);
      commit();
      Assert.fail("Successfully internalAuth with null password");
    } catch (PropertyValueException pve)  {  
      rollback(); 
    }

    /* Empty username */
    beginTransaction();
    try {
      userDAO.createInternalAuth(USERNAME, "");
      commit();
      Assert.fail("Successfully created user with empty password");
    } catch (InvalidStateException ise) {
      rollback();
    }
    
    /* Dublicate auth */
    
    beginTransaction();
    Long internalAuthId = userDAO.createInternalAuth(USERNAME, PASSWORD).getId();
    commit();
    
    beginTransaction();
    try {
      userDAO.createInternalAuth(USERNAME, PASSWORD);
      commit();
      Assert.fail("Successfully created two equal internal auths");
    } catch (org.hibernate.exception.GenericJDBCException e) {
      rollback();
    }
    
    beginTransaction();
    userDAO.deleteInternalAuth(userDAO.getInternalAuth(internalAuthId));
    commit();
  }

  @Test
  public void testGetInternalAuth() {
    beginTransaction();
    InternalAuth internalAuth = userDAO.createInternalAuth(USERNAME, PASSWORD);
    commit();

    Long internalAuthId = internalAuth.getId();
    
    beginTransaction();
    internalAuth = userDAO.getInternalAuth(internalAuthId);
    Assert.assertEquals(internalAuth.getId(), internalAuthId);
    Assert.assertEquals(internalAuth.getPassword(), PASSWORD);
    Assert.assertEquals(internalAuth.getUsername(), USERNAME);
    commit();
    
    beginTransaction();
    userDAO.deleteInternalAuth(userDAO.getInternalAuth(internalAuthId));
    commit();
  }

  @Test
  public void testSetInternalAuthPassword() {
    beginTransaction();
    InternalAuth internalAuth = userDAO.createInternalAuth(USERNAME, PASSWORD);
    commit();

    Long internalAuthId = internalAuth.getId();
    
    beginTransaction();
    internalAuth = userDAO.getInternalAuth(internalAuthId);
    Assert.assertEquals(internalAuth.getPassword(), PASSWORD);
    commit();
    
    beginTransaction();
    internalAuth = userDAO.getInternalAuth(internalAuthId);
    userDAO.setInternalAuthPassword(internalAuth, NEWPASSWORD);
    commit();
    
    beginTransaction();
    internalAuth = userDAO.getInternalAuth(internalAuthId);
    Assert.assertEquals(internalAuth.getPassword(), NEWPASSWORD);
    commit();
    
    beginTransaction();
    userDAO.deleteInternalAuth(userDAO.getInternalAuth(internalAuthId));
    commit();
  }

  @Test
  public void testListUsers() {
    beginTransaction();
    
    TestDataGenerator.createTestUsers(10, 0, "Test", "User", "test.user", "fakemail.com", "internal", UserRole.ADMINISTRATOR);
    
    commit();
    
    beginTransaction();
    
    List<User> users = userDAO.listUsers();
    Assert.assertEquals(users.size(), 10);
  
    for (int i = 0; i < users.size(); i++) 
      userDAO.deleteUser(userDAO.getUser(users.get(i).getId()));
    
    commit();
  }

  @Test
  public void testUpdateUser() {
    beginTransaction();
    
    User user = TestDataGenerator.createTestUsers(1, 0, "Test", "User", "test.user", "fakemail.com", "internal", UserRole.ADMINISTRATOR).get(0);
    final Long userId = user.getId();
    commit();
    
    /* null & empty firstName tests */
    
    checkProperty("firstName", PropertyCheckStrategy.NotNull, new TestCode() {
      public void run() {
        userDAO.updateUser(userDAO.getUser(userId), null, LASTNAME, UserRole.GUEST);
      }
    });
    checkProperty("firstName", PropertyCheckStrategy.NotEmpty, new TestCode() {
      public void run() {
        userDAO.updateUser(userDAO.getUser(userId), "", LASTNAME, UserRole.GUEST);
      }
    });
    
    /* null & empty lastName tests */
    
    checkProperty("lastName", PropertyCheckStrategy.NotNull, new TestCode() {
      public void run() {
        userDAO.updateUser(userDAO.getUser(userId), FIRSTNAME, null, UserRole.GUEST);
      }
    });
    checkProperty("lastName", PropertyCheckStrategy.NotEmpty, new TestCode() {
      public void run() {
        userDAO.updateUser(userDAO.getUser(userId), FIRSTNAME, "", UserRole.GUEST);
      }
    });
    
    /* cleanup */
    
    beginTransaction();
    
    userDAO.deleteUser(userDAO.getUser(userId));
    
    commit();
  }

  @Test
  public void testUpdateInternalAuth() {
    /* Create 2 test users */
    
    beginTransaction();
    
    InternalAuth internalAuth1 = userDAO.createInternalAuth(INTERNAL_AUTH_OLDUSER1, INTERNAL_AUTH_OLDPASS1);
    InternalAuth internalAuth2 = userDAO.createInternalAuth(INTERNAL_AUTH_OLDUSER2, INTERNAL_AUTH_OLDPASS2);
    
    Long auth1Id = internalAuth1.getId();
    Long auth2Id = internalAuth2.getId();
    
    User user1 = userDAO.createUser("Test 1", "User 1", String.valueOf(auth1Id), "internal", UserRole.USER);
    User user2 = userDAO.createUser("Test 2", "User 2", String.valueOf(auth2Id), "internal", UserRole.USER);
    
    Long user1Id = user1.getId();
    Long user2Id = user2.getId();
    
    commit();
    
    /* change first one's username and second one's username and password  */ 
    
    beginTransaction();
    user1 = userDAO.getUser(user1Id);
    user2 = userDAO.getUser(user2Id);
    
    userDAO.updateInternalAuth(userDAO.getInternalAuth(auth1Id), INTERNAL_AUTH_NEWUSER1);
    userDAO.updateInternalAuth(userDAO.getInternalAuth(auth2Id), INTERNAL_AUTH_NEWUSER2, INTERNAL_AUTH_NEWPASS2);
    commit();
    
    /* and the we check if both credentials can still be retrieved back from the database */
    
    beginTransaction(); 
    
    internalAuth1 = userDAO.getInternalAuth(auth1Id);
    internalAuth2 = userDAO.getInternalAuth(auth2Id);
    
    Assert.assertEquals(internalAuth1.getUsername(), INTERNAL_AUTH_NEWUSER1);
    Assert.assertEquals(internalAuth1.getPassword(), INTERNAL_AUTH_OLDPASS1);
    
    Assert.assertEquals(internalAuth2.getUsername(), INTERNAL_AUTH_NEWUSER2);
    Assert.assertEquals(internalAuth2.getPassword(), INTERNAL_AUTH_NEWPASS2);
    commit();
    
    /* and cleanup */
    
    beginTransaction(); 
    
    userDAO.deleteInternalAuth(userDAO.getInternalAuth(auth1Id));
    userDAO.deleteInternalAuth(userDAO.getInternalAuth(auth2Id));
    userDAO.deleteUser(userDAO.getUser(user1Id));
    userDAO.deleteUser(userDAO.getUser(user2Id));
    
    commit();
  }
  
  private UserDAO userDAO = DAOFactory.getInstance().getUserDAO();
}
