package fi.otavanopisto.pyramus.ytl;

import java.util.Objects;

public class YTLAineKoodiSuoritettuKurssi {

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof YTLAineKoodiSuoritettuKurssi) {
      YTLAineKoodiSuoritettuKurssi cmp = (YTLAineKoodiSuoritettuKurssi) obj;
      return 
          Objects.equals(subjectCode, cmp.subjectCode) && 
          Objects.equals(educationType, cmp.educationType);
    } else {
      return false;
    }
  }
  
  public Long getEducationType() {
    return educationType;
  }

  public void setEducationType(Long educationType) {
    this.educationType = educationType;
  }

  public String getSubjectCode() {
    return subjectCode;
  }

  public void setSubjectCode(String subjectCode) {
    this.subjectCode = subjectCode;
  }

  private Long educationType;
  private String subjectCode;
}
