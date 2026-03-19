package fi.otavanopisto.pyramus.koski;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
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

  /**
   * Returns list of subject codes for which the subject
   * selection is required for the subject to be considered
   * as part of the Student's studies.
   * 
   * @return
   */
  public Set<String> getSubjectSelectionRequired() {
    return subjectSelectionRequired;
  }

  /**
   * Sets the list of subject codes for which the subject
   * selection is required for the subject to be considered
   * as part of the Student's studies.
   * 
   * @param subjectSelectionRequired
   */
  public void setSubjectSelectionRequired(Set<String> subjectSelectionRequired) {
    this.subjectSelectionRequired = subjectSelectionRequired;
  }

  /**
   * Returns true if the given subject code is any of the
   * subject selections.
   * 
   * Note that this method returning false does not mean
   * that the subject is not valid for student's studies,
   * only that there is no subject selection done on the
   * subject.
   * 
   * @param subjectCode
   * @return
   */
  public boolean isSelectedSubject(String subjectCode) {
    return
        StringUtils.equals(math, subjectCode) ||
        StringUtils.equals(primaryLanguage, subjectCode) ||
        StringUtils.equals(religion, subjectCode) ||
        isALanguage(subjectCode) ||
        isA1Language(subjectCode) ||
        isA2Language(subjectCode) ||
        isB1Language(subjectCode) ||
        isB2Language(subjectCode) ||
        isB3Language(subjectCode);
  }

  /**
   * Returns true if the subject is considered included in the studies.
   * 
   * Returns true when either
   *  a. the subject is in the list of subject for which selection is required
   *     and it is a selected subject (determined by isSelectedSubject)
   *  b. the subject does not require subject selection or there are no subjects 
   *     defined that require subject selection
   *     
   * 
   * @param subjectCode
   * @return
   */
  public boolean isIncludedInStudies(String subjectCode) {
    // If there's no selection requirements, there's nothing to check 
    // so assume the subject is included in studies
    if (CollectionUtils.isEmpty(subjectSelectionRequired)) {
      return true;
    }
    
    if (subjectSelectionRequired.contains(subjectCode)) {
      // The subject does require selection so check that it is
      return isSelectedSubject(subjectCode);
    }
    else {
      return true;
    }
  }
  
  /**
   * Sets a fields value by the uservariable name.
   * 
   * Maps the uservariable to a field in this object and sets that 
   * field's value to fieldValue.
   * 
   * @param userVariableName name of the uservariable
   * @param fieldValue value for the field
   */
  public void setFieldValueByUserVariableName(String userVariableName, String fieldValue) {
    switch (userVariableName) {
      case KoskiConsts.SubjectSelections.MATH:
        setMath(fieldValue);
      break;
      case KoskiConsts.SubjectSelections.NATIVE_LANGUAGE:
        setPrimaryLanguage(fieldValue);
      break;
      case KoskiConsts.SubjectSelections.LANG_A:
        setALanguages(fieldValue);
      break;
      case KoskiConsts.SubjectSelections.LANG_A1:
        setA1Languages(fieldValue);
      break;
      case KoskiConsts.SubjectSelections.LANG_A2:
        setA2Languages(fieldValue);
      break;
      case KoskiConsts.SubjectSelections.LANG_B1:
        setB1Languages(fieldValue);
      break;
      case KoskiConsts.SubjectSelections.LANG_B2:
        setB2Languages(fieldValue);
      break;
      case KoskiConsts.SubjectSelections.LANG_B3:
        setB3Languages(fieldValue);
      break;
      case KoskiConsts.SubjectSelections.RELIGION:
        setReligion(fieldValue);
      break;
      
      default:
        throw new RuntimeException(String.format("Unknown or not supported variable name %s", userVariableName));
    }
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
  private Set<String> subjectSelectionRequired = new HashSet<>();
}
