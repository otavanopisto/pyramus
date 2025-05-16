package fi.otavanopisto.pyramus.ui.base;

import org.junit.Test;

import fi.otavanopisto.pyramus.ui.AbstractUITest;

public class GenericPagesStudentPermissionsTestsBase extends AbstractUITest {

  @Test
  public void testCourseAssessmentPagePermission() throws InterruptedException{
    assertStudentAllowed("/index.page");
  }
  
  @Test
  public void testAccessDeniedPagePermission() throws InterruptedException{
    assertStudentAllowed("/accessdenied.page");
  }

}
