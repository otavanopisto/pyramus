package fi.otavanopisto.pyramus.ui.base;

import org.junit.Test;

import fi.otavanopisto.pyramus.ui.AbstractUITest;

public class GradingPagesStudentPermissionsTestsBase extends AbstractUITest {

  @Test
  public void testCourseAssessmentPagePermission() throws InterruptedException{
    assertStudentDenied("/grading/courseassessment.page");
  }
  
  @Test
  public void testManageTransferCreditsPagePermission() throws InterruptedException{
    assertStudentDenied("/grading/managetransfercredits.page");
  }

}
