package fi.otavanopisto.pyramus.ui.base;

import org.junit.Test;

import fi.otavanopisto.pyramus.SqlAfter;
import fi.otavanopisto.pyramus.SqlBefore;
import fi.otavanopisto.pyramus.ui.AbstractUITest;

public class ResourcesPagesStudentPermissionsTestsBase extends AbstractUITest {

  @Test
  public void testCreatematerialResourcePagePermission() throws InterruptedException{
    assertStudentDenied("/resources/creatematerialresource.page");
  }
  
  @Test
  public void testCreateWorkResourcePagePermission() throws InterruptedException{
    assertStudentDenied("/resources/createworkresource.page");
  }
  
  @Test
  public void testEditWorkResourcePagePermission() throws InterruptedException{
    assertStudentDenied("/resources/editworkresource.page");
  }
  
  @Test
  public void testEditMaterialResourcePagePermission() throws InterruptedException{
    assertStudentDenied("/resources/editmaterialresource.page");
  }
  
  @Test
  public void testResourceCategoriesPagePermission() throws InterruptedException{
    assertStudentDenied("/resources/resourcecategories.page");
  }
  
  @Test
  public void testSearchResourcesPagePermission() throws InterruptedException{
    assertStudentDenied("/resources/searchresources.page");
  }
  
  @Test
  public void testSearchResourcesDialogPagePermission() throws InterruptedException{
    assertStudentDenied("/resources/searchresourcesdialog.page");
  }

}
