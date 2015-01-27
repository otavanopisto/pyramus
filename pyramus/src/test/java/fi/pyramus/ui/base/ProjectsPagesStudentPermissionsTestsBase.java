package fi.pyramus.ui.base;

import org.junit.Test;

import fi.pyramus.SqlAfter;
import fi.pyramus.SqlBefore;
import fi.pyramus.ui.AbstractUITest;

public class ProjectsPagesStudentPermissionsTestsBase extends AbstractUITest {

  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testCreateProjectPagePermission() throws InterruptedException{
    assertStudentDenied("/projects/createproject.page");
  }
  
  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testCreateStudentProjectPagePermission() throws InterruptedException{
    assertStudentDenied("/projects/createstudentproject.page");
  }
  
  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testEditProjectPagePermission() throws InterruptedException{
    assertStudentDenied("/projects/editproject.page");
  }
  
  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testEditStudentProjectPagePermission() throws InterruptedException{
    assertStudentDenied("/projects/editstudentproject.page");
  }
  
  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testSearchModulesDialogPagePermission() throws InterruptedException{
    assertStudentDenied("/projects/searchmodulesdialog.page");
  }
  
  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testSearchStudentProjectCoursesDialogPagePermission() throws InterruptedException{
    assertStudentDenied("/projects/searchstudentprojectcoursesdialog.page");
  }
  
  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testSearchProjectsPagePermission() throws InterruptedException{
    assertStudentDenied("/projects/searchprojects.page");
  }
  
  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testSearcStudentsProjectsPagePermission() throws InterruptedException{
    assertStudentDenied("/projects/searchstudentprojects.page");
  }
  
  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testSearchStudentProjectModuleCoursesDialogPagePermission() throws InterruptedException{
    assertStudentDenied("/projects/searchstudentprojectmodulecoursesdialog.page");
  }
  
  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testSelectProjectDialogPagePermission() throws InterruptedException{
    assertStudentDenied("/projects/selectprojectdialog.page");
  }
  
  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testViewProjectPagePermission() throws InterruptedException{
    assertStudentDenied("/projects/viewproject.page");
  }

}
