package fi.pyramus.util.dataimport.scripting;

import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.LocaleUtils;

import com.github.javafaker.Faker;

import fi.pyramus.util.dataimport.scripting.api.CourseAPI;
import fi.pyramus.util.dataimport.scripting.api.EducationTypeAPI;
import fi.pyramus.util.dataimport.scripting.api.ModuleAPI;
import fi.pyramus.util.dataimport.scripting.api.StudyProgrammeAPI;
import fi.pyramus.util.dataimport.scripting.api.StudyProgrammeCategoryAPI;
import fi.pyramus.util.dataimport.scripting.api.SubjectAPI;

public class ImportAPI {
  
  public ImportAPI(Long loggedUserId) {
    this.loggedUserId = loggedUserId;
  }
  
  public void log(String arg) {
    Logger.getLogger(getClass().getName()).log(Level.INFO, arg);
  }
  
  public Faker getFaker() {
    return new Faker(LocaleUtils.toLocale("fi"));
  }
  
  public Faker getFakerEn() {
    return new Faker(Locale.ENGLISH);
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

  private Long loggedUserId;
}
