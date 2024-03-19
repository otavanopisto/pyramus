package fi.otavanopisto.pyramus.events;

public class StudentParentDeletedEvent {

  public StudentParentDeletedEvent(Long studentParentId) {
    super();
    this.studentParentId = studentParentId;
  }

  public Long getStudentParentId() {
    return studentParentId;
  }

  private final Long studentParentId;
}
