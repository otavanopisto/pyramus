package fi.pyramus.util.dataimport.scripting;

import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.github.javafaker.Faker;

import fi.pyramus.util.dataimport.scripting.api.StudentAPI;
import fi.pyramus.util.dataimport.scripting.api.SubjectAPI;

public class ImportAPI {
  
  public void log(String arg) {
    Logger.getLogger(getClass().getName()).log(Level.INFO, arg);
  }
  
  public Faker getFaker() {
    Faker faker = new Faker(Locale.forLanguageTag("fi"));
    return faker;
  }
  
  public StudentAPI getStudent() {
    return new StudentAPI();
  }
  
  public SubjectAPI getSubject() {
    return new SubjectAPI();
  }

}
