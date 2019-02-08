package fi.otavanopisto.pyramus.events;

public class StudentSubjectGradeEvent {

  public StudentSubjectGradeEvent(Long studentSubjectGradeId, Long studentId) {
    this.studentSubjectGradeId = studentSubjectGradeId;
    this.studentId = studentId;
  }

  public Long getStudentId() {
    return studentId;
  }

  public Long getStudentSubjectGradeId() {
    return studentSubjectGradeId;
  }

  private final Long studentSubjectGradeId;
  private final Long studentId;
}
