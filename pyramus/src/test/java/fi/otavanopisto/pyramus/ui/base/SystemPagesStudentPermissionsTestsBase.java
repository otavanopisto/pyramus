package fi.otavanopisto.pyramus.ui.base;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openqa.selenium.By;

import fi.otavanopisto.pyramus.SqlAfter;
import fi.otavanopisto.pyramus.SqlBefore;
import fi.otavanopisto.pyramus.ui.AbstractUITest;

public class SystemPagesStudentPermissionsTestsBase extends AbstractUITest {

  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testPluginsPagePermission() throws InterruptedException{
    assertStudentDenied("/system/plugins.page");
  }

  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testReindexHibernateObjectsPagePermission() throws InterruptedException{
    assertStudentDenied("/system/reindexhibernateobjects.page");
  }

  // TODO: Wot m8?
  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testInitialDataPagePermission() throws InterruptedException{
    assertStudentDenied("/system/initialdata.page");
  }
  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testImportCsvPagePermission() throws InterruptedException{
    assertStudentDenied("/system/importcsv.page");
  }
  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testImportDataPagePermission() throws InterruptedException{
    assertStudentDenied("/system/importdata.page");
  } 
  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testImportScriptedDataPagePermission() throws InterruptedException{
    assertStudentDenied("/system/importscripteddata.page");
  } 
  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testImportReportPagePermission() throws InterruptedException{
    assertStudentDenied("/system/importreport.page");
  } 
  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testSetupWizardAdminPasswordPagePermission() throws InterruptedException{
    assertStudentDenied("/system/setupwizard/adminpassword.page");
  } 
  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testSetupWizardAcademictermsPagePermission() throws InterruptedException{
    assertStudentDenied("/system/setupwizard/academicterms.page");
  } 
  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testSetupWizardIndexPagePermission() throws InterruptedException{
    assertStudentDenied("/system/setupwizard/index.page");
  } 
  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testSetupWizardContactTypesPagePermission() throws InterruptedException{
    assertStudentDenied("/system/setupwizard/contacttypes.page");
  } 
  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testSetupWizardExaminationTypesPagePermission() throws InterruptedException{
    assertStudentDenied("/system/setupwizard/examinationtypes.page");
  } 
  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testSetupWizardCourseParticipationTypesPagePermission() throws InterruptedException{
    assertStudentDenied("/system/setupwizard/courseparticipationtypes.page");
  } 
  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testSetupWizardCourseStatesPagePermission() throws InterruptedException{
    assertStudentDenied("/system/setupwizard/coursesstate.page");
  } 
  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testSetupWizardEducationSubTypesPagePermission() throws InterruptedException{
    assertStudentDenied("/system/setupwizard/educationsubtypes.page");
  } 
  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testSetupWizardEducationTypesPagePermission() throws InterruptedException{
    assertStudentDenied("/system/setupwizard/educationtypes.page");
  } 
  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testSetupWizardLanguagesPagePermission() throws InterruptedException{
    assertStudentDenied("/system/setupwizard/languages.page");
  } 
  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testSetupWizardMunicipalitiesPagePermission() throws InterruptedException{
    assertStudentDenied("/system/setupwizard/municipalities.page");
  } 
  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testSetupWizardNationalitiesPagePermission() throws InterruptedException{
    assertStudentDenied("/system/setupwizard/nationalities.page");
  }
  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testSetupWizardSchoolFieldsPagePermission() throws InterruptedException{
    assertStudentDenied("/system/setupwizard/schoolfields.page");
  } 
  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testSetupWizardSchoolsPagePermission() throws InterruptedException{
    assertStudentDenied("/system/setupwizard/schools.page");
  } 
  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testSetupWizardStudentActivityTypesPagePermission() throws InterruptedException{
    assertStudentDenied("/system/setupwizard/studentactivity.page");
  } 
  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testSetupWizardStudentEducationalLevelsPagePermission() throws InterruptedException{
    assertStudentDenied("/system/setupwizard/studenteducationallevels.page");
  } 
  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testSetupWizardStudyEndReasonsPagePermission() throws InterruptedException{
    assertStudentDenied("/system/setupwizard/studyendreasons.page");
  } 
  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testSetupWizardStudyProgrammeCategoriesPagePermission() throws InterruptedException{
    assertStudentDenied("system/setupwizard/studyprogrammecategories.page");
  } 
  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testSetupWizardStudyProgrammesPagePermission() throws InterruptedException{
    assertStudentDenied("/system/setupwizard/studyprogrammes.page");
  } 
  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testSetupWizardSubjectsPagePermission() throws InterruptedException{
    assertStudentDenied("/system/setupwizard/subjects.page");
  } 
  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testSetupWizardTimeUnitsPagePermission() throws InterruptedException{
    assertStudentDenied("/system/setupwizard/timeunits.page");
  } 
  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testSetupWizardFinalPagePermission() throws InterruptedException{
    assertStudentDenied("/system/setupwizard/final.page");
  } 
  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testElementCheatSheetPagePermission() throws InterruptedException{
    assertStudentDenied("/system/elementcheatsheet.page");
  } 
  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testDebugDataPagePermission() throws InterruptedException{
    assertStudentDenied("/system/debugdata.page");
  } 

  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testSettingsPagePermission() throws InterruptedException{
    assertStudentDenied("/system/systemsettings.page");
  } 
  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testInfoPagePermission() throws InterruptedException{
    assertStudentDenied("/system/systeminfo.page");
  } 
  @Test
  @SqlBefore ("sql/basic-before.sql")
  @SqlAfter ("sql/basic-after.sql")
  public void testClientapplicationsPagePermission() throws InterruptedException{
    assertStudentDenied("/system/clientapplications.page");
  } 
  
}
