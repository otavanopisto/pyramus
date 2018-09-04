package fi.otavanopisto.pyramus.rest.model;

public class CreditLinkCourseAssessment {
    
  public CreditLinkCourseAssessment() {
  }

  public CreditLinkCourseAssessment(Long id, Long studentId, CourseAssessment credit) {
    super();
    this.id = id;
    this.studentId = studentId;
    this.credit = credit;
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

  public CourseAssessment getCredit() {
    return credit;
  }

  public void setCredit(CourseAssessment credit) {
    this.credit = credit;
  }

  private Long id;
  private Long studentId;
  private CourseAssessment credit;
}
