package fi.otavanopisto.pyramus.rest.model;

import java.time.OffsetDateTime;

public class CourseAssessmentRequest {

  public CourseAssessmentRequest() {
    super();
  }

  public CourseAssessmentRequest(Long id, Long courseStudentId, OffsetDateTime created, String requestText, Boolean archived) {
    super();
    this.id = id;
    this.courseStudentId = courseStudentId;
    this.created = created;
    this.requestText = requestText;
    this.archived = archived;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getCourseStudentId() {
    return courseStudentId;
  }

  public void setCourseStudentId(Long courseStudentId) {
    this.courseStudentId = courseStudentId;
  }

  public OffsetDateTime getCreated() {
    return created;
  }

  public void setCreated(OffsetDateTime created) {
    this.created = created;
  }

  public String getRequestText() {
    return requestText;
  }

  public void setRequestText(String requestText) {
    this.requestText = requestText;
  }

  public Boolean getArchived() {
    return archived;
  }

  public void setArchived(Boolean archived) {
    this.archived = archived;
  }

  private Long id;
  private Long courseStudentId;
  private OffsetDateTime created;
  private String requestText;
  private Boolean archived;
}
