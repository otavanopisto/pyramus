package fi.otavanopisto.pyramus.ui.base;

import org.junit.Test;

import fi.otavanopisto.pyramus.ui.AbstractUITest;

public class SettingsPagesStudentPermissionsTestsBase extends AbstractUITest {

  @Test
  public void testAcademicTermsPagePermission() throws InterruptedException {
    assertStudentDenied("/settings/academicterms.page");
  }

  @Test
  public void testCourseDescriptioCategoriesPagePermission() throws InterruptedException {
    assertStudentDenied("/settings/coursedescriptioncategories.page");
  }

  @Test
  public void testCreateGradingscalePagePermission() throws InterruptedException {
    assertStudentDenied("/settings/creategradingscale.page");
  }

  @Test
  public void testCreateSchoolPagePermission() throws InterruptedException {
    assertStudentDenied("/settings/createschool.page");
  }

  @Test
  public void testEditGradingscalePagePermission() throws InterruptedException {
    assertStudentDenied("/settings/editgradingscale.page");
  }

  @Test
  public void testEditSchoolPagePermission() throws InterruptedException {
    assertStudentDenied("/settings/editschool.page");
  }

  @Test
  public void testEducationSubTypesPagePermission() throws InterruptedException {
    assertStudentDenied("/settings/educationsubtypes.page");
  }

  @Test
  public void testEducationTypesPagePermission() throws InterruptedException {
    assertStudentDenied("/settings/educationtypes.page");
  }

  @Test
  public void testListGradingscalesPagePermission() throws InterruptedException {
    assertStudentDenied("/settings/listgradingscales.page");
  }

  @Test
  public void testMunicipalitiesPagePermission() throws InterruptedException {
    assertStudentDenied("/settings/municipalities.page");
  }

  @Test
  public void testReportCategoriesPagePermission() throws InterruptedException {
    assertStudentDenied("/settings/reportcategories.page");
  }

  @Test
  public void testSearchSchoolsPagePermission() throws InterruptedException {
    assertStudentDenied("/settings/searchschools.page");
  }

  @Test
  public void testSubjectsPagePermission() throws InterruptedException {
    assertStudentDenied("/settings/subjects.page");
  }

  @Test
  public void testStudyProgrammesPagePermission() throws InterruptedException {
    assertStudentDenied("/settings/studyprogrammes.page");
  }

  @Test
  public void testStudyProgrammeCategoriesPagePermission() throws InterruptedException {
    assertStudentDenied("/settings/studyprogrammecategories.page");
  }

  @Test
  public void testViewSchoolPagePermission() throws InterruptedException {
    assertStudentDenied("/settings/viewschool.page");
  }

  @Test
  public void testTimeUnitsPagePermission() throws InterruptedException {
    assertStudentDenied("/settings/timeunits.page");
  }

  @Test
  public void testCourseStatesPagePermission() throws InterruptedException {
    assertStudentDenied("/settings/coursestates.page");
  }

  @Test
  public void testCreateTransferCreditTemplatePagePermission() throws InterruptedException {
    assertStudentDenied("/settings/createtransfercredittemplate.page");
  }

  @Test
  public void testEditTransferCreditTemplatePagePermission() throws InterruptedException {
    assertStudentDenied("/settings/edittransfercredittemplate.page");
  }

  @Test
  public void testCourseParticipationTypesPagePermission() throws InterruptedException {
    assertStudentDenied("/settings/courseparticipationtypes.page");
  }

  @Test
  public void testManageTransferCreditTemplatesPagePermission() throws InterruptedException {
    assertStudentDenied("/settings/managetransfercredittemplates.page");
  }

  @Test
  public void testManageSchoolFieldsPagePermission() throws InterruptedException {
    assertStudentDenied("/settings/manageschoolfields.page");
  }

  @Test
  public void testManageChangeLogPagePermission() throws InterruptedException {
    assertStudentDenied("/settings/managechangelog.page");
  }

  @Test
  public void testManageFileTypesPagePermission() throws InterruptedException {
    assertStudentDenied("/settings/managefiletypes.page");
  }

  @Test
  public void testStudyEndReasonsPagePermission() throws InterruptedException {
    assertStudentDenied("/settings/studyendreasons.page");
  }

}
