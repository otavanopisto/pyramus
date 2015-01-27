package fi.pyramus.ui.base;

import org.junit.Test;

import fi.pyramus.SqlAfter;
import fi.pyramus.SqlBefore;
import fi.pyramus.ui.AbstractUITest;

public class ResourcesPagesStudentPermissionsTestsBase extends AbstractUITest {

  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testCreatematerialResourcePagePermission() throws InterruptedException{
    assertStudentDenied("/resources/creatematerialresource.page");
  }
  
  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testCreateWorkResourcePagePermission() throws InterruptedException{
    assertStudentDenied("/resources/createworkresource.page");
  }
  
  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testEditWorkResourcePagePermission() throws InterruptedException{
    assertStudentDenied("/resources/editworkresource.page");
  }
  
  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testEditMaterialResourcePagePermission() throws InterruptedException{
    assertStudentDenied("/resources/editmaterialresource.page");
  }
  
  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testResourceCategoriesPagePermission() throws InterruptedException{
    assertStudentDenied("/resources/resourcecategories.page");
  }
  
  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testSearchResourcesPagePermission() throws InterruptedException{
    assertStudentDenied("/resources/searchresources.page");
  }
  
  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testSearchResourcesDialogPagePermission() throws InterruptedException{
    assertStudentDenied("/resources/searchresourcesdialog.page");
  }

}
