package fi.otavanopisto.pyramus.events;

public class StudyProgrammeUpdatedEvent {

  public StudyProgrammeUpdatedEvent(Long studyProgrammeId) {
    this.studyProgrammeId = studyProgrammeId;
  }

  public Long getStudyProgrammeId() {
    return studyProgrammeId;
  }

  private final Long studyProgrammeId;
}
