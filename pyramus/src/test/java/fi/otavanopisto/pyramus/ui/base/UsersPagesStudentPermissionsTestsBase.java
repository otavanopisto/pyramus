package fi.otavanopisto.pyramus.ui.base;

import org.junit.Test;

import fi.otavanopisto.pyramus.SqlAfter;
import fi.otavanopisto.pyramus.SqlBefore;
import fi.otavanopisto.pyramus.ui.AbstractUITest;

public class UsersPagesStudentPermissionsTestsBase extends AbstractUITest {

  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testLoginPagePermission() throws InterruptedException{
    getWebDriver().get(getAppUrl(true) + "/users/login.page");
    testPageElementsByName("username");
    testPageElementsByName("password");
  }
  
//  @Test
//  @SqlBefore ("sql/basic-before.sql")
//  @SqlAfter ("sql/basic-after.sql")
//  public void testLogoutPagePermission() throws InterruptedException{
//    assertStudentDenied("/users/logout.page");
//  }
  
//   Tests allready exists for this?
//  @Test
//  @SqlBefore ("sql/basic-before.sql")
//  @SqlAfter ("sql/basic-after.sql")
//  public void testAuthorizePagePermission() throws InterruptedException{
//    assertStudentDenied("/users/authorize.page");
//  }
  
  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testCreateUserPagePermission() throws InterruptedException{
    assertStudentDenied("/users/createuser.page");
  }

  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testEditUserPagePermission() throws InterruptedException{
    assertStudentDenied("/users/edituser.page");
  }
//  ?
//  @Test
//  @SqlBefore ("sql/basic-before.sql")
//  @SqlAfter ("sql/basic-after.sql")
//  public void testExternalLoginPagePermission() throws InterruptedException{
//    assertStudentDenied("/users/externallogin.page");
//  }
  
  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testSearchUsersPagePermission() throws InterruptedException{
    assertStudentDenied("/users/searchusers.page");
  }
  
  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testSearchUserDialogPagePermission() throws InterruptedException{
    assertStudentDenied("/users/searchuserdialog.page");
  }
  
  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testSearchUsersDialogPagePermission() throws InterruptedException{
    assertStudentDenied("/users/searchusersdialog.page");
  }
  
}
