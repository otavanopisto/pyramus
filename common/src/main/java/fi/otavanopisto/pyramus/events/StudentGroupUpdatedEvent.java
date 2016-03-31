package fi.otavanopisto.pyramus.events;

public class StudentGroupUpdatedEvent {

  public StudentGroupUpdatedEvent(Long studentGroupId) {
    super();
    this.studentGroupId = studentGroupId;
  }

  public Long getStudentGroupId() {
    return studentGroupId;
  }

  private final Long studentGroupId;
}
