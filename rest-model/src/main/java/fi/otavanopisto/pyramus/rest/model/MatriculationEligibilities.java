package fi.otavanopisto.pyramus.rest.model;

public class MatriculationEligibilities {

  public MatriculationEligibilities() {
  }
  
  public MatriculationEligibilities(boolean compulsoryEducation, boolean upperSecondarySchoolCurriculum) {
    this.compulsoryEducation = compulsoryEducation;
    this.upperSecondarySchoolCurriculum = upperSecondarySchoolCurriculum;
  }

  public boolean isUpperSecondarySchoolCurriculum() {
    return upperSecondarySchoolCurriculum;
  }

  public void setUpperSecondarySchoolCurriculum(boolean upperSecondarySchoolCurriculum) {
    this.upperSecondarySchoolCurriculum = upperSecondarySchoolCurriculum;
  }

  public boolean isCompulsoryEducation() {
    return compulsoryEducation;
  }

  public void setCompulsoryEducation(boolean compulsoryEducation) {
    this.compulsoryEducation = compulsoryEducation;
  }

  private boolean compulsoryEducation;
  private boolean upperSecondarySchoolCurriculum;
}
