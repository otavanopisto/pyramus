package fi.otavanopisto.pyramus.ui.base;

import org.junit.Test;

import fi.otavanopisto.pyramus.SqlAfter;
import fi.otavanopisto.pyramus.SqlBefore;
import fi.otavanopisto.pyramus.ui.AbstractUITest;

public class SettingsPagesStudentPermissionsTestsBase extends AbstractUITest {

  @Test
  @SqlBefore("sql/basic-before.sql")
  @SqlAfter("sql/basic-after.sql")
  public void testAcademicTermsPagePermission() throws InterruptedException {
    assertStudentDenied("/settings/academicterms.page");
  }

  @Test
  @SqlBefore("sql/basic-before.sql")
  @SqlAfter("sql/basic-after.sql")
  public void testCourseDescriptioCategoriesPagePermission() throws InterruptedException {
    assertStudentDenied("/settings/coursedescriptioncategories.page");
  }

  @Test
  @SqlBefore("sql/basic-before.sql")
  @SqlAfter("sql/basic-after.sql")
  public void testCreateGradingscalePagePermission() throws InterruptedException {
    assertStudentDenied("/settings/creategradingscale.page");
  }

  @Test
  @SqlBefore("sql/basic-before.sql")
  @SqlAfter("sql/basic-after.sql")
  public void testCreateSchoolPagePermission() throws InterruptedException {
    assertStudentDenied("/settings/createschool.page");
  }

  @Test
  @SqlBefore("sql/basic-before.sql")
  @SqlAfter("sql/basic-after.sql")
  public void testEditGradingscalePagePermission() throws InterruptedException {
    assertStudentDenied("/settings/editgradingscale.page");
  }

  @Test
  @SqlBefore("sql/basic-before.sql")
  @SqlAfter("sql/basic-after.sql")
  public void testEditSchoolPagePermission() throws InterruptedException {
    assertStudentDenied("/settings/editschool.page");
  }

  @Test
  @SqlBefore("sql/basic-before.sql")
  @SqlAfter("sql/basic-after.sql")
  public void testEducationSubTypesPagePermission() throws InterruptedException {
    assertStudentDenied("/settings/educationsubtypes.page");
  }

  @Test
  @SqlBefore("sql/basic-before.sql")
  @SqlAfter("sql/basic-after.sql")
  public void testEducationTypesPagePermission() throws InterruptedException {
    assertStudentDenied("/settings/educationtypes.page");
  }

  @Test
  @SqlBefore("sql/basic-before.sql")
  @SqlAfter("sql/basic-after.sql")
  public void testListGradingscalesPagePermission() throws InterruptedException {
    assertStudentDenied("/settings/listgradingscales.page");
  }

  @Test
  @SqlBefore("sql/basic-before.sql")
  @SqlAfter("sql/basic-after.sql")
  public void testMunicipalitiesPagePermission() throws InterruptedException {
    assertStudentDenied("/settings/municipalities.page");
  }

  @Test
  @SqlBefore("sql/basic-before.sql")
  @SqlAfter("sql/basic-after.sql")
  public void testReportCategoriesPagePermission() throws InterruptedException {
    assertStudentDenied("/settings/reportcategories.page");
  }

  @Test
  @SqlBefore("sql/basic-before.sql")
  @SqlAfter("sql/basic-after.sql")
  public void testSearchSchoolsPagePermission() throws InterruptedException {
    assertStudentDenied("/settings/searchschools.page");
  }

  @Test
  @SqlBefore("sql/basic-before.sql")
  @SqlAfter("sql/basic-after.sql")
  public void testSubjectsPagePermission() throws InterruptedException {
    assertStudentDenied("/settings/subjects.page");
  }

  @Test
  @SqlBefore("sql/basic-before.sql")
  @SqlAfter("sql/basic-after.sql")
  public void testStudyProgrammesPagePermission() throws InterruptedException {
    assertStudentDenied("/settings/studyprogrammes.page");
  }

  @Test
  @SqlBefore("sql/basic-before.sql")
  @SqlAfter("sql/basic-after.sql")
  public void testStudyProgrammeCategoriesPagePermission() throws InterruptedException {
    assertStudentDenied("/settings/studyprogrammecategories.page");
  }

  @Test
  @SqlBefore("sql/basic-before.sql")
  @SqlAfter("sql/basic-after.sql")
  public void testViewSchoolPagePermission() throws InterruptedException {
    assertStudentDenied("/settings/viewschool.page");
  }

  @Test
  @SqlBefore("sql/basic-before.sql")
  @SqlAfter("sql/basic-after.sql")
  public void testTimeUnitsPagePermission() throws InterruptedException {
    assertStudentDenied("/settings/timeunits.page");
  }

  @Test
  @SqlBefore("sql/basic-before.sql")
  @SqlAfter("sql/basic-after.sql")
  public void testCourseStatesPagePermission() throws InterruptedException {
    assertStudentDenied("/settings/coursestates.page");
  }

  @Test
  @SqlBefore("sql/basic-before.sql")
  @SqlAfter("sql/basic-after.sql")
  public void testCreateTransferCreditTemplatePagePermission() throws InterruptedException {
    assertStudentDenied("/settings/createtransfercredittemplate.page");
  }

  @Test
  @SqlBefore("sql/basic-before.sql")
  @SqlAfter("sql/basic-after.sql")
  public void testEditTransferCreditTemplatePagePermission() throws InterruptedException {
    assertStudentDenied("/settings/edittransfercredittemplate.page");
  }

  @Test
  @SqlBefore("sql/basic-before.sql")
  @SqlAfter("sql/basic-after.sql")
  public void testCourseParticipationTypesPagePermission() throws InterruptedException {
    assertStudentDenied("/settings/courseparticipationtypes.page");
  }

  @Test
  @SqlBefore("sql/basic-before.sql")
  @SqlAfter("sql/basic-after.sql")
  public void testManageTransferCreditTemplatesPagePermission() throws InterruptedException {
    assertStudentDenied("/settings/managetransfercredittemplates.page");
  }

  @Test
  @SqlBefore("sql/basic-before.sql")
  @SqlAfter("sql/basic-after.sql")
  public void testManageSchoolFieldsPagePermission() throws InterruptedException {
    assertStudentDenied("/settings/manageschoolfields.page");
  }

  @Test
  @SqlBefore("sql/basic-before.sql")
  @SqlAfter("sql/basic-after.sql")
  public void testManageChangeLogPagePermission() throws InterruptedException {
    assertStudentDenied("/settings/managechangelog.page");
  }

  @Test
  @SqlBefore("sql/basic-before.sql")
  @SqlAfter("sql/basic-after.sql")
  public void testManageFileTypesPagePermission() throws InterruptedException {
    assertStudentDenied("/settings/managefiletypes.page");
  }

  @Test
  @SqlBefore("sql/basic-before.sql")
  @SqlAfter("sql/basic-after.sql")
  public void testStudyEndReasonsPagePermission() throws InterruptedException {
    assertStudentDenied("/settings/studyendreasons.page");
  }

}
