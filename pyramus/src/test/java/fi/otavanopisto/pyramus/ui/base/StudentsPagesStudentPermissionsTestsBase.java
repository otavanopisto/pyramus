package fi.otavanopisto.pyramus.ui.base;

import org.junit.Test;

import fi.otavanopisto.pyramus.ui.AbstractUITest;

public class StudentsPagesStudentPermissionsTestsBase extends AbstractUITest {

  @Test
  public void testCreateStudentPagePermission() throws InterruptedException{
    assertStudentDenied("/students/createstudent.page");
  }
  
  @Test
  public void testEditStudentPagePermission() throws InterruptedException{
    assertStudentDenied("/students/editstudent.page");
  }

  @Test
  public void testSearchStudentPagePermission() throws InterruptedException{
    assertStudentDenied("/students/searchstudents.page");
  }
  
  @Test
  public void testSearchStudentsDialogPagePermission() throws InterruptedException{
    assertStudentDenied("/students/searchstudentsdialog.page");
  }
  
  @Test
  public void testViewStudentPagePermission() throws InterruptedException{
    assertStudentDenied("/students/viewstudent.page");
  }
  
  @Test
  public void testStudentInfoPopupPagePermission() throws InterruptedException{
    assertStudentDenied("/students/studentinfopopup.page");
  }
  
  @Test
  public void testStudyProgrammeCopyDialogPagePermission() throws InterruptedException{
    assertStudentDenied("/students/studyprogrammecopydialog.page");
  }
  
  @Test
  public void testImportStudentCreditsPagePermission() throws InterruptedException{
    assertStudentDenied("/students/importstudentcredits.page");
  }
  
  @Test
  public void testCreateStudentGroupPagePermission() throws InterruptedException{
    assertStudentDenied("/students/createstudentgroup.page");
  }
  
  @Test
  public void testEditStudentGroupPagePermission() throws InterruptedException{
    assertStudentDenied("/students/editstudentgroup.page?studentgroup=1#at-basic");
  }
  
  @Test
  public void testSearchStudentGroupsPagePermission() throws InterruptedException{
    assertStudentDenied("/students/searchstudentgroups.page");
  }
  
  @Test
  public void testViewStudentGroupPagePermission() throws InterruptedException{
    assertStudentDenied("/students/viewstudentgroup.page?studentgroup=1#at-basic");
  }
  
  @Test
  public void testManagesStudentContactEntriesPagePermission() throws InterruptedException{
    assertStudentDenied("/students/managestudentcontactentries.page");
  }
  
  @Test
  public void testEditStudentimageDialogPagePermission() throws InterruptedException{
    assertStudentDenied("/students/editstudentimagedialog.page");
  }
  
  @Test
  public void testStudentCourseAssessmentRequestPopupPagePermission() throws InterruptedException{
    assertStudentDenied("/students/studentcourseassessmentrequestspopup.page");
  }
}
