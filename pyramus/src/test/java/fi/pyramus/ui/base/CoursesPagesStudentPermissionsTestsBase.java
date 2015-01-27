package fi.pyramus.ui.base;

import org.junit.Test;

import fi.pyramus.SqlAfter;
import fi.pyramus.SqlBefore;
import fi.pyramus.ui.AbstractUITest;

public class CoursesPagesStudentPermissionsTestsBase extends AbstractUITest {

  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testCreateCoursePagePermission() throws InterruptedException{
    assertStudentDenied("/courses/createcourse.page");
  }
  
  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testCreateCourseWizardPagePermission() throws InterruptedException{
    assertStudentDenied("/courses/createcoursewizard.page");
  }
  
  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testSearchCoursesPagePermission() throws InterruptedException{
    assertStudentDenied("/courses/searchcourses.page");
  }
  
  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testEditCoursePagePermission() throws InterruptedException{
    assertStudentDenied("/courses/editcourse.page");
  }

  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testViewCoursePagePermission() throws InterruptedException{
    assertStudentDenied("/courses/viewcourse.page");
  }
  
  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testManageCourseAssessmentsPagePermission() throws InterruptedException{
    assertStudentDenied("/courses/managecourseassessments.page");
  }
  
  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testEditVerbalAssessmentDialogPagePermission() throws InterruptedException{
    assertStudentDenied("/courses/editverbalassessmentdialog.page");
  }
  
  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testCoursePlannerPagePermission() throws InterruptedException{
    assertStudentDenied("/courses/courseplanner.page");
  }
  
}
