package fi.otavanopisto.pyramus.events;

public class StudentArchivedEvent {

  public StudentArchivedEvent(Long studentId) {
    super();
    this.studentId = studentId;
  }

  public Long getStudentId() {
    return studentId;
  }

  private Long studentId;
}
