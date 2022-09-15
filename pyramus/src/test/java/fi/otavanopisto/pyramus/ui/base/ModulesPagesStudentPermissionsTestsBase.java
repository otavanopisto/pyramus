package fi.otavanopisto.pyramus.ui.base;

import org.junit.Test;

import fi.otavanopisto.pyramus.ui.AbstractUITest;

public class ModulesPagesStudentPermissionsTestsBase extends AbstractUITest {

  @Test
  public void testCreateModulePagePermission() throws InterruptedException{
    assertStudentDenied("/modules/createmodule.page");
  }
  
  @Test
  public void testEditModulePagePermission() throws InterruptedException{
    assertStudentDenied("/modules/editmodule.page");
  }
  
  @Test
  public void testViewModulePagePermission() throws InterruptedException{
    assertStudentDenied("/modules/viewmodule.page");
  }
  
  @Test
  public void testSearchModulePagePermission() throws InterruptedException{
    assertStudentDenied("/modules/searchmodules.page");
  }

}
