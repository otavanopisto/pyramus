package fi.pyramus.ui.base;

import org.junit.Test;

import fi.pyramus.SqlAfter;
import fi.pyramus.SqlBefore;
import fi.pyramus.ui.AbstractUITest;

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
