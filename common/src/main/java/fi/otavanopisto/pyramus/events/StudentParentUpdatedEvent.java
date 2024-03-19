package fi.otavanopisto.pyramus.events;

public class StudentParentUpdatedEvent {

  public StudentParentUpdatedEvent(Long studentParentId) {
    super();
    this.studentParentId = studentParentId;
  }

  public Long getStudentParentId() {
    return studentParentId;
  }

  private final Long studentParentId;
}
