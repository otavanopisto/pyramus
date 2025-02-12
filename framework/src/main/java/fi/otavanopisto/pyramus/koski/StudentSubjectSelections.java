package fi.otavanopisto.pyramus.koski;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import fi.otavanopisto.pyramus.koski.koodisto.KoskiOppiaineetYleissivistava;

public class StudentSubjectSelections {

  public StudentSubjectSelections() {
  }

  public KoskiOppiaineetYleissivistava koskiKoodi(OpiskelijanOPS ops, String languageCode) {
    if (ops == OpiskelijanOPS.ops2019) {
      return
          isALanguage(languageCode) ? KoskiOppiaineetYleissivistava.A :
            isA1Language(languageCode) ? KoskiOppiaineetYleissivistava.A :
              isA2Language(languageCode) ? KoskiOppiaineetYleissivistava.A :
                isB1Language(languageCode) ? KoskiOppiaineetYleissivistava.B1 :
                  isB2Language(languageCode) ? KoskiOppiaineetYleissivistava.B2 :
                    isB3Language(languageCode) ? KoskiOppiaineetYleissivistava.B3 : null;
    } else {
      return
          isALanguage(languageCode) ? KoskiOppiaineetYleissivistava.A1 :
            isA1Language(languageCode) ? KoskiOppiaineetYleissivistava.A1 :
              isA2Language(languageCode) ? KoskiOppiaineetYleissivistava.A2 :
                isB1Language(languageCode) ? KoskiOppiaineetYleissivistava.B1 :
                  isB2Language(languageCode) ? KoskiOppiaineetYleissivistava.B2 :
                    isB3Language(languageCode) ? KoskiOppiaineetYleissivistava.B3 : null;
    }
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
  
  public boolean isAccomplishment(Long subjectId) {
    return subjectId != null ? ArrayUtils.contains(split(accomplishments), String.valueOf(subjectId)) : false;
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
  
  public String getReligion() {
    return religion;
  }

  public void setReligion(String religion) {
    this.religion = religion;
  }

  /**
   * Returns true if the given subject is selected 
   * as the religion subject by the student.
   * 
   * @param subjectCode subject's code
   * @return true if given subject is selected as religion subject
   */
  public boolean isReligion(String subjectCode) {
    return StringUtils.equals(getReligion(), subjectCode);
  }
  
  public String getAccomplishments() {
    return accomplishments;
  }

  public void setAccomplishments(String accomplishments) {
    this.accomplishments = accomplishments;
  }

  private String math;
  private String primaryLanguage;
  private String aLanguages;
  private String a1Languages;
  private String a2Languages;
  private String b1Languages;
  private String b2Languages;
  private String b3Languages;
  private String religion;
  private String accomplishments;
}
