package fi.pyramus.ui.base;

import org.junit.Test;

import fi.pyramus.SqlAfter;
import fi.pyramus.SqlBefore;
import fi.pyramus.ui.AbstractUITest;

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
