package fi.otavanopisto.pyramus.rest.model;

public class MatriculationEligibilities {

  public MatriculationEligibilities(boolean upperSecondarySchoolCurriculum) {
    this.upperSecondarySchoolCurriculum = upperSecondarySchoolCurriculum;
  }

  public boolean isUpperSecondarySchoolCurriculum() {
    return upperSecondarySchoolCurriculum;
  }

  public void setUpperSecondarySchoolCurriculum(boolean upperSecondarySchoolCurriculum) {
    this.upperSecondarySchoolCurriculum = upperSecondarySchoolCurriculum;
  }

  private boolean upperSecondarySchoolCurriculum;
}
