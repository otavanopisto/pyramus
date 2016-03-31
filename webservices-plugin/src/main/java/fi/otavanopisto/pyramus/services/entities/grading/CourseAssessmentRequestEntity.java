package fi.otavanopisto.pyramus.services.entities.grading;

import java.util.Date;

public class CourseAssessmentRequestEntity {

  public CourseAssessmentRequestEntity() {
    
  }
  
  public CourseAssessmentRequestEntity(Long id, Long courseStudentId, Date created, String requestText) {
    this.setId(id);
    this.setCourseStudentId(courseStudentId);
    this.setCreated(created);
    this.setRequestText(requestText);
  }
  
  public Long getCourseStudentId() {
    return courseStudentId;
  }

  public Long getId() {
    return id;
  }

  public Date getCreated() {
    return created;
  }

  public String getRequestText() {
    return requestText;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setCourseStudentId(Long courseStudentId) {
    this.courseStudentId = courseStudentId;
  }

  public void setCreated(Date created) {
    this.created = created;
  }

  public void setRequestText(String requestText) {
    this.requestText = requestText;
  }

  private Long id;
  private Long courseStudentId;
  private Date created;
  private String requestText;
}
