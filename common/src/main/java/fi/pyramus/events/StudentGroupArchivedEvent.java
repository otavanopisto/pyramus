package fi.pyramus.events;

public class StudentGroupArchivedEvent {

  public StudentGroupArchivedEvent(Long studentGroupId) {
    super();
    this.studentGroupId = studentGroupId;
  }

  public Long getStudentGroupId() {
    return studentGroupId;
  }

  private final Long studentGroupId;
}
