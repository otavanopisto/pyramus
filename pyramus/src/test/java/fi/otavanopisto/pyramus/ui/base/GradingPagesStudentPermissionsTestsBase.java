package fi.otavanopisto.pyramus.ui.base;

import org.junit.Test;

import fi.otavanopisto.pyramus.SqlAfter;
import fi.otavanopisto.pyramus.SqlBefore;
import fi.otavanopisto.pyramus.ui.AbstractUITest;

public class GradingPagesStudentPermissionsTestsBase extends AbstractUITest {

  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testCourseAssessmentPagePermission() throws InterruptedException{
    assertStudentDenied("/grading/courseassessment.page");
  }
  
  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testManageTransferCreditsPagePermission() throws InterruptedException{
    assertStudentDenied("/grading/managetransfercredits.page");
  }

}
