package fi.otavanopisto.pyramus.ui.base;

import org.junit.Test;
import fi.otavanopisto.pyramus.ui.AbstractUITest;

public class SystemPagesStudentPermissionsTestsBase extends AbstractUITest {

  @Test
  public void testPluginsPagePermission() throws InterruptedException{
    assertStudentDenied("/system/plugins.page");
  }

  @Test
  public void testReindexHibernateObjectsPagePermission() throws InterruptedException{
    assertStudentDenied("/system/reindexhibernateobjects.page");
  }

  // TODO: Just does redirect?
//  @Test
//  public void testInitialDataPagePermission() throws InterruptedException{
//    assertStudentDenied("/system/initialdata.page");
//  }
  @Test
  public void testImportCsvPagePermission() throws InterruptedException{
    assertStudentDenied("/system/importcsv.page");
  }
  @Test
  public void testImportDataPagePermission() throws InterruptedException{
    assertStudentDenied("/system/importdata.page");
  } 
  @Test
  public void testImportScriptedDataPagePermission() throws InterruptedException{
    assertStudentDenied("/system/importscripteddata.page");
  } 
  @Test
  public void testImportReportPagePermission() throws InterruptedException{
    assertStudentDenied("/system/importreport.page");
  } 
  @Test
  public void testSetupWizardAdminPasswordPagePermission() throws InterruptedException{
    assertStudentDenied("/system/setupwizard/adminpassword.page");
  } 
  @Test
  public void testSetupWizardAcademictermsPagePermission() throws InterruptedException{
    assertStudentDenied("/system/setupwizard/academicterms.page");
  } 
//  TODO: Visible for students. Should it be?
//  @Test
//  public void testSetupWizardIndexPagePermission() throws InterruptedException{
//    assertStudentDenied("/system/setupwizard/index.page");
//  } 
  @Test
  public void testSetupWizardContactTypesPagePermission() throws InterruptedException{
    assertStudentDenied("/system/setupwizard/contacttypes.page");
  } 
  @Test
  public void testSetupWizardExaminationTypesPagePermission() throws InterruptedException{
    assertStudentDenied("/system/setupwizard/examinationtypes.page");
  } 
  @Test
  public void testSetupWizardCourseParticipationTypesPagePermission() throws InterruptedException{
    assertStudentDenied("/system/setupwizard/courseparticipationtypes.page");
  }
//  This throws an exception and 404 rather than an actual access denied page.
//  @Test
//  public void testSetupWizardCourseStatesPagePermission() throws InterruptedException{
//    assertStudentDenied("/system/setupwizard/coursesstate.page");
//  } 
  @Test
  public void testSetupWizardEducationSubTypesPagePermission() throws InterruptedException{
    assertStudentDenied("/system/setupwizard/educationsubtypes.page");
  } 
  @Test
  public void testSetupWizardEducationTypesPagePermission() throws InterruptedException{
    assertStudentDenied("/system/setupwizard/educationtypes.page");
  } 
  @Test
  public void testSetupWizardLanguagesPagePermission() throws InterruptedException{
    assertStudentDenied("/system/setupwizard/languages.page");
  } 
  @Test
  public void testSetupWizardMunicipalitiesPagePermission() throws InterruptedException{
    assertStudentDenied("/system/setupwizard/municipalities.page");
  } 
  @Test
  public void testSetupWizardNationalitiesPagePermission() throws InterruptedException{
    assertStudentDenied("/system/setupwizard/nationalities.page");
  }
  @Test
  public void testSetupWizardSchoolFieldsPagePermission() throws InterruptedException{
    assertStudentDenied("/system/setupwizard/schoolfields.page");
  } 
  @Test
  public void testSetupWizardSchoolsPagePermission() throws InterruptedException{
    assertStudentDenied("/system/setupwizard/schools.page");
  } 
//  TODO: error instead of access denied.
//  @Test
//  public void testSetupWizardStudentActivityTypesPagePermission() throws InterruptedException{
//    assertStudentDenied("/system/setupwizard/studentactivity.page");
//  } 
  @Test
  public void testSetupWizardStudentEducationalLevelsPagePermission() throws InterruptedException{
    assertStudentDenied("/system/setupwizard/studenteducationallevels.page");
  } 
  @Test
  public void testSetupWizardStudyEndReasonsPagePermission() throws InterruptedException{
    assertStudentDenied("/system/setupwizard/studyendreasons.page");
  } 
  @Test
  public void testSetupWizardStudyProgrammeCategoriesPagePermission() throws InterruptedException{
    assertStudentDenied("/system/setupwizard/studyprogrammecategories.page");
  } 
  @Test
  public void testSetupWizardStudyProgrammesPagePermission() throws InterruptedException{
    assertStudentDenied("/system/setupwizard/studyprogrammes.page");
  } 
  @Test
  public void testSetupWizardSubjectsPagePermission() throws InterruptedException{
    assertStudentDenied("/system/setupwizard/subjects.page");
  } 
  @Test
  public void testSetupWizardTimeUnitsPagePermission() throws InterruptedException{
    assertStudentDenied("/system/setupwizard/timeunits.page");
  } 
//  TODO: This actually shows "Setup complete!" even for students, could be cleaned away.
//  @Test
//  public void testSetupWizardFinalPagePermission() throws InterruptedException{
//    assertStudentDenied("/system/setupwizard/final.page");
//  } 
  @Test
  public void testElementCheatSheetPagePermission() throws InterruptedException{
    assertStudentDenied("/system/elementcheatsheet.page");
  } 
  @Test
  public void testDebugDataPagePermission() throws InterruptedException{
    assertStudentDenied("/system/debugdata.page");
  } 

  @Test
  public void testSettingsPagePermission() throws InterruptedException{
    assertStudentDenied("/system/systemsettings.page");
  } 
  @Test
  public void testInfoPagePermission() throws InterruptedException{
    assertStudentDenied("/system/systeminfo.page");
  } 
  @Test
  public void testClientapplicationsPagePermission() throws InterruptedException{
    assertStudentDenied("/system/clientapplications.page");
  } 
  
}
