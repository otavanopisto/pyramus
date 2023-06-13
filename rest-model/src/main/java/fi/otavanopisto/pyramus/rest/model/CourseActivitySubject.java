package fi.otavanopisto.pyramus.rest.model;

public class CourseActivitySubject {

  public String getSubjectCode() {
    return subjectCode;
  }

  public void setSubjectCode(String subjectCode) {
    this.subjectCode = subjectCode;
  }

  public String getSubjectName() {
    return subjectName;
  }

  public void setSubjectName(String subjectName) {
    this.subjectName = subjectName;
  }

  public Integer getCourseNumber() {
    return courseNumber;
  }

  public void setCourseNumber(Integer courseNumber) {
    this.courseNumber = courseNumber;
  }

  public Double getCourseLength() {
    return courseLength;
  }

  public void setCourseLength(Double courseLength) {
    this.courseLength = courseLength;
  }

  public String getCourseLengthSymbol() {
    return courseLengthSymbol;
  }

  public void setCourseLengthSymbol(String courseLengthSymbol) {
    this.courseLengthSymbol = courseLengthSymbol;
  }

  public Long getCourseModuleId() {
    return courseModuleId;
  }

  public void setCourseModuleId(Long courseModuleId) {
    this.courseModuleId = courseModuleId;
  }

  private Long courseModuleId;
  private String subjectCode;
  private String subjectName;
  private Integer courseNumber;
  private Double courseLength;
  private String courseLengthSymbol;

}
