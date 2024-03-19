package fi.otavanopisto.pyramus.events;

public class StudentParentCreatedEvent {

  public StudentParentCreatedEvent(Long studentParentId) {
    super();
    this.studentParentId = studentParentId;
  }

  public Long getStudentParentId() {
    return studentParentId;
  }

  private final Long studentParentId;
}
