package fi.otavanopisto.pyramus.ui.base;

import org.junit.Test;

import fi.otavanopisto.pyramus.SqlAfter;
import fi.otavanopisto.pyramus.SqlBefore;
import fi.otavanopisto.pyramus.ui.AbstractUITest;

public class StudentsPagesStudentPermissionsTestsBase extends AbstractUITest {

  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testCreateStudentPagePermission() throws InterruptedException{
    assertStudentDenied("/students/createstudent.page");
  }
  
  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testEditStudentPagePermission() throws InterruptedException{
    assertStudentDenied("/students/editstudent.page");
  }

  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testSearchStudentPagePermission() throws InterruptedException{
    assertStudentDenied("/students/searchstudents.page");
  }
  
  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testSearchStudentsDialogPagePermission() throws InterruptedException{
    assertStudentDenied("/students/searchstudentsdialog.page");
  }
  
  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testViewStudentPagePermission() throws InterruptedException{
    assertStudentDenied("/students/viewstudent.page");
  }
  
  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testStudentInfoPopupPagePermission() throws InterruptedException{
    assertStudentDenied("/students/studentinfopopup.page");
  }
  
  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testStudyProgrammeCopyDialogPagePermission() throws InterruptedException{
    assertStudentDenied("/students/studyprogrammecopydialog.page");
  }
  
  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testImportStudentCreditsPagePermission() throws InterruptedException{
    assertStudentDenied("/students/importstudentcredits.page");
  }
  
  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testCreateStudentGroupPagePermission() throws InterruptedException{
    assertStudentDenied("/students/createstudentgroup.page");
  }
  
  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testEditStudentGroupPagePermission() throws InterruptedException{
    assertStudentDenied("/students/editstudentgroup.page");
  }
  
  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testSearchStudentGroupsPagePermission() throws InterruptedException{
    assertStudentDenied("/students/searchstudentgroups.page");
  }
  
  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testViewStudentGroupPagePermission() throws InterruptedException{
    assertStudentDenied("/students/viewstudentgroup.page");
  }
  
  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testManagesStudentContactEntriesPagePermission() throws InterruptedException{
    assertStudentDenied("/students/managestudentcontactentries.page");
  }
  
  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testEditStudentimageDialogPagePermission() throws InterruptedException{
    assertStudentDenied("/students/editstudentimagedialog.page");
  }
  
  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testStudentCourseAssessmentRequestPopupPagePermission() throws InterruptedException{
    assertStudentDenied("/students/studentcourseassessmentrequestspopup.page");
  }
}
