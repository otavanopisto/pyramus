package fi.otavanopisto.pyramus.rest.model;

public class StudentGroupStudent {

  public StudentGroupStudent() {
    super();
  }

  public StudentGroupStudent(Long id, Long studentId) {
    this();
    this.id = id;
    this.studentId = studentId;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
  
  public Long getStudentId() {
    return studentId;
  }
  
  public void setStudentId(Long studentId) {
    this.studentId = studentId;
  }

  private Long id;
  private Long studentId;
}
