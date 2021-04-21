package fi.otavanopisto.pyramus.events;

public class StudyProgrammeCreatedEvent {

  public StudyProgrammeCreatedEvent(Long studyProgrammeId) {
    this.studyProgrammeId = studyProgrammeId;
  }

  public Long getStudyProgrammeId() {
    return studyProgrammeId;
  }

  private final Long studyProgrammeId;
}
