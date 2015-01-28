package fi.pyramus.ui.base;

import org.junit.Test;

import fi.pyramus.SqlAfter;
import fi.pyramus.SqlBefore;
import fi.pyramus.ui.AbstractUITest;

public class ModulesPagesStudentPermissionsTestsBase extends AbstractUITest {

  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testCreateModulePagePermission() throws InterruptedException{
    assertStudentDenied("/modules/createmodule.page");
  }
  
  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testEditModulePagePermission() throws InterruptedException{
    assertStudentDenied("/modules/editmodule.page");
  }
  
  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testViewModulePagePermission() throws InterruptedException{
    assertStudentDenied("/modules/viewmodule.page");
  }
  
  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testSearchModulePagePermission() throws InterruptedException{
    assertStudentDenied("/modules/searchmodules.page");
  }

}
