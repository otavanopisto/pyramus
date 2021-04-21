package fi.otavanopisto.pyramus.events;

public class StudyProgrammeArchivedEvent {

  public StudyProgrammeArchivedEvent(Long studyProgrammeId) {
    this.studyProgrammeId = studyProgrammeId;
  }

  public Long getStudyProgrammeId() {
    return studyProgrammeId;
  }

  private final Long studyProgrammeId;
}
