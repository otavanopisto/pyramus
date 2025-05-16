package fi.otavanopisto.pyramus.ui.base;

import org.junit.Test;

import fi.otavanopisto.pyramus.ui.AbstractUITest;

public class ProjectsPagesStudentPermissionsTestsBase extends AbstractUITest {

  @Test
  public void testCreateProjectPagePermission() throws InterruptedException{
    assertStudentDenied("/projects/createproject.page");
  }
  
  @Test
  public void testCreateStudentProjectPagePermission() throws InterruptedException{
    assertStudentDenied("/projects/createstudentproject.page");
  }
  
  @Test
  public void testEditProjectPagePermission() throws InterruptedException{
    assertStudentDenied("/projects/editproject.page");
  }
  
  @Test
  public void testEditStudentProjectPagePermission() throws InterruptedException{
    assertStudentDenied("/projects/editstudentproject.page");
  }
  
  @Test
  public void testSearchModulesDialogPagePermission() throws InterruptedException{
    assertStudentDenied("/projects/searchmodulesdialog.page");
  }
  
  @Test
  public void testSearchStudentProjectCoursesDialogPagePermission() throws InterruptedException{
    assertStudentDenied("/projects/searchstudentprojectcoursesdialog.page");
  }
  
  @Test
  public void testSearchProjectsPagePermission() throws InterruptedException{
    assertStudentDenied("/projects/searchprojects.page");
  }
  
  @Test
  public void testSearcStudentsProjectsPagePermission() throws InterruptedException{
    assertStudentDenied("/projects/searchstudentprojects.page");
  }
  
  @Test
  public void testSearchStudentProjectModuleCoursesDialogPagePermission() throws InterruptedException{
    assertStudentDenied("/projects/searchstudentprojectmodulecoursesdialog.page");
  }
  
  @Test
  public void testSelectProjectDialogPagePermission() throws InterruptedException{
    assertStudentDenied("/projects/selectprojectdialog.page");
  }
  
  @Test
  public void testViewProjectPagePermission() throws InterruptedException{
    assertStudentDenied("/projects/viewproject.page");
  }

}
