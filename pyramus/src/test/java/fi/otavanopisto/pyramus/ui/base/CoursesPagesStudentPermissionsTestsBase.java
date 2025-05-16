package fi.otavanopisto.pyramus.ui.base;

import org.junit.Test;

import fi.otavanopisto.pyramus.ui.AbstractUITest;

public class CoursesPagesStudentPermissionsTestsBase extends AbstractUITest {

  @Test
  public void testCreateCoursePagePermission() throws InterruptedException{
    assertStudentDenied("/courses/createcourse.page");
  }
  
  @Test
  public void testCreateCourseWizardPagePermission() throws InterruptedException{
    assertStudentDenied("/courses/createcoursewizard.page");
  }
  
  @Test
  public void testSearchCoursesPagePermission() throws InterruptedException{
    assertStudentDenied("/courses/searchcourses.page");
  }
  
  @Test
  public void testEditCoursePagePermission() throws InterruptedException{
    assertStudentDenied("/courses/editcourse.page");
  }

  @Test
  public void testViewCoursePagePermission() throws InterruptedException{
    assertStudentDenied("/courses/viewcourse.page");
  }
  
  @Test
  public void testManageCourseAssessmentsPagePermission() throws InterruptedException{
    assertStudentDenied("/courses/managecourseassessments.page");
  }
  
  @Test
  public void testEditVerbalAssessmentDialogPagePermission() throws InterruptedException{
    assertStudentDenied("/courses/editverbalassessmentdialog.page");
  }
  
  @Test
  public void testCoursePlannerPagePermission() throws InterruptedException{
    assertStudentDenied("/courses/courseplanner.page");
  }
  
}
