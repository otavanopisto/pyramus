package fi.pyramus.events;

public class StudentCreatedEvent {

  public StudentCreatedEvent(Long studentId) {
    super();
    this.studentId = studentId;
  }

  public Long getStudentId() {
    return studentId;
  }

  private Long studentId;
}
