package fi.pyramus.util.dataimport.scripting;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.LocaleUtils;

import com.github.javafaker.Faker;

import fi.pyramus.util.dataimport.scripting.api.ActivityTypeAPI;
import fi.pyramus.util.dataimport.scripting.api.ClientApplicationAPI;
import fi.pyramus.util.dataimport.scripting.api.ClientApplicationAuthorizationCodeAPI;
import fi.pyramus.util.dataimport.scripting.api.ContactTypeAPI;
import fi.pyramus.util.dataimport.scripting.api.CourseAPI;
import fi.pyramus.util.dataimport.scripting.api.CourseStudentAPI;
import fi.pyramus.util.dataimport.scripting.api.EducationTypeAPI;
import fi.pyramus.util.dataimport.scripting.api.EducationalLevelAPI;
import fi.pyramus.util.dataimport.scripting.api.ExaminationTypeAPI;
import fi.pyramus.util.dataimport.scripting.api.LanguageAPI;
import fi.pyramus.util.dataimport.scripting.api.ModuleAPI;
import fi.pyramus.util.dataimport.scripting.api.MunicipalityAPI;
import fi.pyramus.util.dataimport.scripting.api.NationalityAPI;
import fi.pyramus.util.dataimport.scripting.api.PersonAPI;
import fi.pyramus.util.dataimport.scripting.api.SchoolAPI;
import fi.pyramus.util.dataimport.scripting.api.SchoolFieldAPI;
import fi.pyramus.util.dataimport.scripting.api.SettingAPI;
import fi.pyramus.util.dataimport.scripting.api.SettingKeyAPI;
import fi.pyramus.util.dataimport.scripting.api.StaffMemberAPI;
import fi.pyramus.util.dataimport.scripting.api.StudentAPI;
import fi.pyramus.util.dataimport.scripting.api.StudentGroupAPI;
import fi.pyramus.util.dataimport.scripting.api.StudentGroupStudentAPI;
import fi.pyramus.util.dataimport.scripting.api.StudyProgrammeAPI;
import fi.pyramus.util.dataimport.scripting.api.StudyProgrammeCategoryAPI;
import fi.pyramus.util.dataimport.scripting.api.SubjectAPI;
import fi.pyramus.util.dataimport.scripting.api.UserIdentificationAPI;
import fi.pyramus.util.dataimport.scripting.api.WebhookAPI;

public class ImportAPI {
  
  public ImportAPI(Long loggedUserId) {
    this.loggedUserId = loggedUserId;
  }
  
  public void log(String arg) {
    Logger.getLogger(getClass().getName()).log(Level.INFO, arg);
  }
  
  public Faker getFaker(String locale) {
    return new Faker(LocaleUtils.toLocale(locale));
  }
  
  public SubjectAPI getSubjects() {
    return new SubjectAPI(loggedUserId);
  }
  
  public EducationTypeAPI getEducationTypes() {
    return new EducationTypeAPI(loggedUserId);
  }
  
  public ModuleAPI getModules() {
    return new ModuleAPI(loggedUserId);
  }
  
  public CourseAPI getCourses() {
    return new CourseAPI(loggedUserId);
  }
  
  public StudyProgrammeCategoryAPI getStudyProgrammeCategories() {
    return new StudyProgrammeCategoryAPI(loggedUserId);
  }
  
  public StudyProgrammeAPI getStudyProgrammes() {
    return new StudyProgrammeAPI(loggedUserId);
  }
  
  public PersonAPI getPersons() {
    return new PersonAPI(loggedUserId);
  }
  
  public StudentAPI getStudents() {
    return new StudentAPI(loggedUserId);
  }
  
  public SettingKeyAPI getSettingKeys(){
    return new SettingKeyAPI(loggedUserId);
  }
  
  public SettingAPI getSettings(){
    return new SettingAPI(loggedUserId);
  }
  
  public ActivityTypeAPI getActivityTypes() {
    return new ActivityTypeAPI(loggedUserId);
  }
  
  public EducationalLevelAPI getEducationalLevels() {
    return new EducationalLevelAPI(loggedUserId);
  }
  
  public ExaminationTypeAPI getExaminationTypes() {
    return new ExaminationTypeAPI(loggedUserId);
  }
  
  public LanguageAPI getLanguages() {
    return new LanguageAPI(loggedUserId);
  }
  
  public MunicipalityAPI getMunicipalities() {
    return new MunicipalityAPI(loggedUserId);
  }
  
  public NationalityAPI getNationalities() {
    return new NationalityAPI(loggedUserId);
  }
  
  public SchoolAPI getSchools() {
    return new SchoolAPI(loggedUserId);
  }
  
  public SchoolFieldAPI getSchoolFields() {
    return new SchoolFieldAPI(loggedUserId);
  }
  
  public ContactTypeAPI getContactTypes() {
    return new ContactTypeAPI(loggedUserId);
  }
  
  public StudentGroupAPI getStudentGroups() {
    return new StudentGroupAPI(loggedUserId);
  }
  
  public StudentGroupStudentAPI getStudentGroupStudents() {
    return new StudentGroupStudentAPI(loggedUserId);
  }
  
  public CourseStudentAPI getCourseStudents() {
    return new CourseStudentAPI(loggedUserId);
  }
  
  public ClientApplicationAPI getClientApplications() {
    return new ClientApplicationAPI(loggedUserId);
  }
  
  public ClientApplicationAuthorizationCodeAPI getClientApplicationAuthorizationCodes() {
    return new ClientApplicationAuthorizationCodeAPI(loggedUserId);
  }
  
  public StaffMemberAPI getStaffMembers() {
    return new StaffMemberAPI();
  }
  public UserIdentificationAPI getUserIdentifications(){
    return new UserIdentificationAPI();
  }
  
  public WebhookAPI getWebhooks() {
    return new WebhookAPI();
  }
  
  private Long loggedUserId;
}
