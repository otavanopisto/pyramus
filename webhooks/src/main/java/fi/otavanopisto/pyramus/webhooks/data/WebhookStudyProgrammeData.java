package fi.otavanopisto.pyramus.webhooks.data;

public class WebhookStudyProgrammeData {

  public WebhookStudyProgrammeData() {
    super();
  }

  public WebhookStudyProgrammeData(Long studyProgrammeId) {
    super();
    this.studyProgrammeId = studyProgrammeId;
  }

  public Long getStudyProgrammeId() {
    return studyProgrammeId;
  }

  public void setStudyProgrammeId(Long studyProgrammeId) {
    this.studyProgrammeId = studyProgrammeId;
  }

  private Long studyProgrammeId;
}
