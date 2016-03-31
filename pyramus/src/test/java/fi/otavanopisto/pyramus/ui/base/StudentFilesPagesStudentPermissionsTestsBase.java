package fi.otavanopisto.pyramus.ui.base;

import org.junit.Test;

import fi.otavanopisto.pyramus.SqlAfter;
import fi.otavanopisto.pyramus.SqlBefore;
import fi.otavanopisto.pyramus.ui.AbstractUITest;

public class StudentFilesPagesStudentPermissionsTestsBase extends AbstractUITest {

  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testEditFilePagePermission() throws InterruptedException{
    assertStudentDenied("/studentfiles/editfile.page");
  }
  
  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testUploadFilePagePermission() throws InterruptedException{
    assertStudentDenied("/studentfiles/uploadfile.page");
  }

  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testUploadReportPagePermission() throws InterruptedException{
    assertStudentDenied("/studentfiles/uploadreport.page");
  }
  
}
