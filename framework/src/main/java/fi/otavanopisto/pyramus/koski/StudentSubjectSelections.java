package fi.otavanopisto.pyramus.koski;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public class StudentSubjectSelections {

  public StudentSubjectSelections() {
  }

  public boolean isALanguage(String languageCode) {
    return ArrayUtils.contains(split(getALanguages()), languageCode);
  }
  public boolean isA1Language(String languageCode) {
    return ArrayUtils.contains(split(getA1Languages()), languageCode);
  }
  public boolean isA2Language(String languageCode) {
    return ArrayUtils.contains(split(getA2Languages()), languageCode);
  }
  public boolean isB1Language(String languageCode) {
    return ArrayUtils.contains(split(getB1Languages()), languageCode);
  }
  public boolean isB2Language(String languageCode) {
    return ArrayUtils.contains(split(getB2Languages()), languageCode);
  }
  public boolean isB3Language(String languageCode) {
    return ArrayUtils.contains(split(getB3Languages()), languageCode);
  }
  public boolean isAdditionalLanguage(String languageCode) {
    return 
        isALanguage(languageCode) || isA1Language(languageCode) || isA2Language(languageCode) || 
        isB1Language(languageCode) || isB2Language(languageCode) || isB3Language(languageCode);
  }
  
  public String getMath() {
    return math;
  }
  
  public void setMath(String math) {
    this.math = math;
  }
  
  public String getPrimaryLanguage() {
    return primaryLanguage;
  }

  public void setPrimaryLanguage(String primaryLanguage) {
    this.primaryLanguage = primaryLanguage;
  }
  
  public String getALanguages() {
    return aLanguages;
  }

  public void setALanguages(String aLanguages) {
    this.aLanguages = aLanguages;
  }

  public String getA1Languages() {
    return a1Languages;
  }

  public void setA1Languages(String a1Languages) {
    this.a1Languages = a1Languages;
  }

  public String getA2Languages() {
    return a2Languages;
  }

  public void setA2Languages(String a2Languages) {
    this.a2Languages = a2Languages;
  }

  public String getB1Languages() {
    return b1Languages;
  }

  public void setB1Languages(String b1Languages) {
    this.b1Languages = b1Languages;
  }

  public String getB2Languages() {
    return b2Languages;
  }

  public void setB2Languages(String b2Languages) {
    this.b2Languages = b2Languages;
  }

  public String getB3Languages() {
    return b3Languages;
  }

  public void setB3Languages(String b3Languages) {
    this.b3Languages = b3Languages;
  }

  private String[] split(String csv) {
    if (StringUtils.isNotBlank(csv))
      return StringUtils.split(csv, ',');
    return new String[0];
  }
  
  private String math;
  private String primaryLanguage;
  private String aLanguages;
  private String a1Languages;
  private String a2Languages;
  private String b1Languages;
  private String b2Languages;
  private String b3Languages;
}
