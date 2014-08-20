package fi.pyramus.util.dataimport.scripting;

import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang3.LocaleUtils;

import com.github.javafaker.Faker;

import fi.pyramus.util.dataimport.scripting.api.StudentAPI;
import fi.pyramus.util.dataimport.scripting.api.SubjectAPI;

public class ImportAPI {
  
  public void log(String arg) {
    Logger.getLogger(getClass().getName()).log(Level.INFO, arg);
  }
  
  public Faker getFaker() {
    return new Faker(LocaleUtils.toLocale("fi"));
  }
  
  public Faker getFakerEn() {
    return new Faker(Locale.ENGLISH);
  }
  
  public StudentAPI getStudent() {
    return new StudentAPI();
  }
  
  public SubjectAPI getSubject() {
    return new SubjectAPI();
  }

}
