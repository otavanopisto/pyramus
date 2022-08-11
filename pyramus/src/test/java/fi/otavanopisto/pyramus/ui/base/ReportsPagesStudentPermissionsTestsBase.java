package fi.otavanopisto.pyramus.ui.base;

import org.junit.Test;

import fi.otavanopisto.pyramus.SqlAfter;
import fi.otavanopisto.pyramus.SqlBefore;
import fi.otavanopisto.pyramus.ui.AbstractUITest;

public class ReportsPagesStudentPermissionsTestsBase extends AbstractUITest {

  @Test
  public void testEditReportPagePermission() throws InterruptedException{
    assertStudentDenied("/reports/editreport.page");
  }
  
  @Test
  public void testListReportsPagePermission() throws InterruptedException{
    assertStudentDenied("/reports/listreports.page");
  }

  @Test
  public void testViewReportPagePermission() throws InterruptedException{
    assertStudentDenied("/reports/viewreport.page");
  }
  
  @Test
  public void testViewReportParametersPagePermission() throws InterruptedException{
    assertStudentDenied("/reports/viewreportparameters.page");
  }
  
  @Test
  public void testViewReportContentsPagePermission() throws InterruptedException{
    assertStudentDenied("/reports/viewreportcontents.page");
  }
}
