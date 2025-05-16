package fi.otavanopisto.pyramus.ui.base;

import org.junit.Test;

import fi.otavanopisto.pyramus.ui.AbstractUITest;

public class StudentFilesPagesStudentPermissionsTestsBase extends AbstractUITest {

  @Test
  public void testEditFilePagePermission() throws InterruptedException{
    assertStudentDenied("/studentfiles/editfile.page");
  }
  
  @Test
  public void testUploadFilePagePermission() throws InterruptedException{
    assertStudentDenied("/studentfiles/uploadfile.page");
  }

  @Test
  public void testUploadReportPagePermission() throws InterruptedException{
    assertStudentDenied("/studentfiles/uploadreport.page");
  }
  
}
