package fi.otavanopisto.pyramus.events;

public class StudentGroupCreatedEvent {

  public StudentGroupCreatedEvent(Long studentGroupId) {
    super();
    this.studentGroupId = studentGroupId;
  }

  public Long getStudentGroupId() {
    return studentGroupId;
  }

  private final Long studentGroupId;
}
