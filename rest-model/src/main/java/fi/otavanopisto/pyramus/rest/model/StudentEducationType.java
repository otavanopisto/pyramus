package fi.otavanopisto.pyramus.rest.model;

public class StudentEducationType {
  
  public StudentEducationType() {
  }

  public StudentEducationType(String educationTypeCode, Long studentId) {
    this.educationTypeCode = educationTypeCode;
    this.studentId = studentId;
  }

  public String getEducationTypeCode() {
    return educationTypeCode;
  }

  public void setEducationTypeCode(String educationTypeCode) {
    this.educationTypeCode = educationTypeCode;
  }

  public Long getStudentId() {
    return studentId;
  }

  public void setStudentId(Long studentId) {
    this.studentId = studentId;
  }

  private String educationTypeCode;
  private Long studentId;

}
